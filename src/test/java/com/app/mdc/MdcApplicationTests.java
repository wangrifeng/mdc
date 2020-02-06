package com.app.mdc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.mdc.controller.socket.MessageSocket;
import com.app.mdc.model.system.User;
import com.app.mdc.service.system.FileService;
import com.app.mdc.service.system.RoleService;
import com.app.mdc.utils.date.DateUtil;
import com.app.mdc.utils.encryptdecrypt.DES;
import com.app.mdc.utils.encryptdecrypt.MD5EncryptDecrypt;
import com.app.mdc.utils.file.FileUtil;
import com.app.mdc.utils.httpclient.HttpUtil;
import com.app.mdc.utils.jvm.JvmUtils;
import com.app.mdc.utils.viewbean.Page;
import com.app.mdc.utils.viewbean.ResponseResult;
import com.app.mdc.utils.wallet.MonitorTransfer;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.app.mdc.schedule.service.ScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MdcApplicationTests {



    @Test
	public void testFile(){

	}

	@Test
	public void testQuartz() throws Exception {
		/*Map map = new HashMap();
		map.put("url","/mdc/devopsManage/devops/checkDevops");*/
		//quartzService.addJob(DevopsJob.class, "D001", "devopsQuartz", "0 0 1 ? * *",map);
		/*Map m = new HashMap();
		m.put("first",3);
		m.put("second",4);
		quartzService.addJob(DevopsJob.class, "job2", "test2", "0 30 10 * * ?",m);
		*/
		/*quartzService.deleteJob("T001","taskQuartz");
		quartzService.deleteJob("test","testName");
		quartzService.deleteJob("job","test");*/
		Object target = MessageSocket.getApplicationContext().getBean("taskService");
		//判断quartz中params参数（有参，无参调用），以及methodname获取调用的方法
		Method method;
		//result为该方法返回值，如果为void则为null；
		Object result;
		method = target.getClass().getDeclaredMethod("getScheduleJobs");
		ReflectionUtils.makeAccessible(method);
		result = method.invoke(target);
		System.out.println(result);
	}


	@Test
	public void FileChange(){
		FileUtil.xDocServiceChangeFile("C:\\mdcupload\\devops_wo\\de199c8e74234abebd37239fa2ed3539.docx","C:\\fileChange\\");
	}

	@Test
	public void md5Test(){
		System.out.println(MD5EncryptDecrypt.normalMd5(MD5EncryptDecrypt.normalMd5("zznb2019@")));
	}

	@Test
	public void testSql() throws Exception{
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec("cmd.exe /c start D:\\mysqlCheck");
		proc.getOutputStream().close();
		InputStream stderr = proc.getErrorStream();
		InputStreamReader isr = new InputStreamReader(stderr);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		System.out.println("<ERROR>");
		while ( (line = br.readLine()) != null)
		System.out.println(line);
		System.out.println("</ERROR>");
		String message = null;
		System.out.println("<message>");
		while ( (message = (new BufferedReader(new InputStreamReader(proc.getInputStream()))).readLine()) != null)
		System.out.println(message);
		System.out.println("</message>");
		int exitVal = proc.waitFor();
		System.out.println("Process exitValue: " + exitVal);
		proc.destroy();
	}


	@Test
	public void testExport(){
		User user = new User();
		user.setId("5b0a968caa814a39bb81e346a8272890");
		Map map = new HashMap();
		String[] taskIds = {"4695f4c44b854a70869a4cba1deaa627"};
		//map.put("taskIds",taskIds);
		String export = "exportTaskName=2-任务内容-task,exportCompany=1-企业名称-task,exportReportUser=3-填报人员-taskHistory,exportTodayFinish=4-今日已完成-taskHistory";
		String s = "";
		map.put("export",export);
		map.put("flag","open");
		map.put("taskType","2");
		map.forEach((k,v)->{
		    System.out.println(k+":"+v);
        });
		//taskService.exportTask(map,user);
	}

	@Test
	public  void test(){
		try {
			//创建SocketChannel，连接192.168.6.42服务器的8080端口
			SocketChannel sc8080 = createSocketChannel("192.168.0.128",8010);

			//创建SocketChannel，连接192.168.6.42服务器的8090端口
			SocketChannel sc8090 = createSocketChannel("192.168.0.128",8090);

			//创建selector
			Selector selector = Selector.open();
			//向通道注册选择器，并设置selector监听Channel时对读操作感兴趣
			sc8080.register(selector, SelectionKey.OP_READ);
			sc8090.register(selector, SelectionKey.OP_READ);
			//启动线程，监听是否从服务器端有数据传过来
			Thread thread = new Thread(new MyRunnable(selector));
			thread.start();
			//分别向服务器的8080和8090端口发送数据
			sendString(sc8080,"This message is going to send to server 8080 port");
			sendString(sc8090,"This message is going to send to server 8090 port");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static SocketChannel createSocketChannel(String ip, int port){
		SocketChannel socketChannel = null;
		try {
			//创建SocketChannel
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);  //设置为非阻塞模式
			//连接到某台服务器的某个端口
			socketChannel.connect(new InetSocketAddress(ip,port));
			//判断是否连接完成，若未完成则等待连接
			while(!socketChannel.finishConnect()){
				System.out.println("It is connecting>>>>>>>>>>>>>>>>>>>>>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//连接完成返回该SocketChannel
		return socketChannel;
	}
	private static void sendString(SocketChannel sc, String str){
		ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
		try {
			//将buffer中的数据写入sc通道
			sc.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class MyRunnable implements Runnable{
		private Selector selector;
		public MyRunnable(Selector s){
			this.selector =s;
		}
		@Override
		public void run() {
			try {
				while(true){
					//阻塞2000ms，判断是否有通道在注册的事件上就绪了，如果有则该返回值就绪通道的个数
					if(selector.select(2000) == 0){
						System.out.println("please waiting.....");
						continue;
					}else{
						//当有通道就绪时，获取SelectionKey，并遍历
						Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
						while(keys.hasNext()){
							SelectionKey key = keys.next();
							//判断通道中是否可读事件就绪了，如果是则isReadable()方法返回TRUE
							if(key.isReadable()){
								SocketChannel socketChannel = (SocketChannel) key.channel();
								//默认服务器端发送的数据都小于1024byte，因此一次可以读完
								ByteBuffer buffer = ByteBuffer.allocate(1024);
								socketChannel.read(buffer);  //利用通道将数据读入buffer中
								buffer.flip();   //将buffer切换为读模式
								String receiveString = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
								System.out.println(receiveString);
								buffer.clear();  //清空缓冲区buffer
							}
							//设置通道对什么时间感兴趣，该设置是对“读”和“写”感兴趣
							key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
							//移除当前已经处理过的SelectionKey
							keys.remove();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void Testss(){
    	/*System.out.println(BigDecimal.ONE);
		System.out.println(new BigDecimal(3));*/
    	Long l = 1000000000000000000L;
		BigDecimal b = new BigDecimal(String.valueOf(l));
    	BigDecimal s = new BigDecimal("0.3");
    	//BigInteger s = new BigInteger("1000");

    	System.out.println(b.multiply(s).stripTrailingZeros().toPlainString());
    	System.out.println(new Uint256(new BigInteger(b.multiply(s).stripTrailingZeros().toPlainString())).getValue());
	}

	@Test
	public void testMap() throws Exception {
		/*Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/50d1c4c05dbd472b85e4acf1cf58b01b"));

		Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
		String clientVersion = web3ClientVersion.getWeb3ClientVersion();
		System.out.println(clientVersion);*/

		String keyStoreDir = "D:/walletTest";
		System.out.println("生成keyStore文件的默认目录：" + keyStoreDir);
		String passWord="123456";
//		String name = WalletUtils.generateNewWalletFile(passWord, new File(keyStoreDir), true);
//		System.out.println(name);
		String walleFilePath=keyStoreDir+"/UTC--2020-02-06T02-46-47.330000000Z--5065d510249259532225db0e979368ee084e7c5f.json";

		Credentials credentials = WalletUtils.loadCredentials(passWord, walleFilePath);
		String address = credentials.getAddress();
		BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
		BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();

		System.out.println("address="+address);
		System.out.println("public key="+publicKey);
		System.out.println("private key="+privateKey);

		Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/4098a0ceccd5421fa162fb549adea10a"));

		Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
		String clientVersion = web3ClientVersion.getWeb3ClientVersion();
		System.out.println("version=" + clientVersion);

		if (web3j == null) return;
		//String address = "0x5065d510249259532225db0e979368ee084e7c5f";//等待查询余额的地址

		//通过密码和keystore文件获得钱包控制权
		//转账交易

//		List<EthBlock.TransactionResult> txs = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send().getBlock().getTransactions();
//		txs.forEach(tx -> {
//			EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) tx.get();
//
//			System.out.println(transaction.getFrom());
//		});

		/*String address_to = "0xa029180EF446cB6e64933848791539b7eeb12B35";
		TransactionReceipt send = Transfer.sendFunds(web3j, credentials, address_to, new BigDecimal(1), Convert.Unit.FINNEY).send();
		System.out.println("Transaction complete:");
		System.out.println("trans hash=" + send.getTransactionHash());
		System.out.println("from :" + send.getFrom());
		System.out.println("to:" + send.getTo());
		System.out.println("gas used=" + send.getGasUsed());
		System.out.println("status: " + send.getStatus());*/
		//第二个参数：区块的参数，建议选最新区块
		EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send();
		//格式转化 wei-ether
		String blanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");
		System.out.println(blanceETH);


//		String methodName = "balanceOf";
//		List inputParameters = new ArrayList<>();
//		List<TypeReference<?>> outputParameters = new ArrayList<>();
//		Address fromAddress = new Address(address);
//		inputParameters.add(fromAddress);
//
//		TypeReference<Uint256> typeReference = new TypeReference() {};
//		outputParameters.add(typeReference);
//		Function function = new Function(methodName, inputParameters, outputParameters);
//		String data = FunctionEncoder.encode(function);
//		Transaction transaction = Transaction.createEthCallTransaction(address, "0xdac17f958d2ee523a2206206994597c13d831ec7", data);
//
//		EthCall ethCall;
//		BigInteger balanceValue = BigInteger.ZERO;
//		try {
//			ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
//			List results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//			System.out.println(results);
//			//balanceValue = (BigInteger) results.get(0).getValue();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	@Test
	public void testMDC() throws ExecutionException, InterruptedException, IOException, CipherException {
    	//BigInteger p = BigInteger.valueOf(1000000000000000000L);
    	String p = "000000000000000000";
    	String fromAddress = "0x5065d510249259532225db0e979368ee084e7c5f";
    	String contractAddress = "0x53509548c0ce0be4bb88b85f4d2c37b2c5cd1546";
		String methodName = "balanceOf";
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();
		Address address = new Address(fromAddress);
		inputParameters.add(address);

		TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
		};
		outputParameters.add(typeReference);
		Function function = new Function(methodName, inputParameters, outputParameters);
		String data = FunctionEncoder.encode(function);
		Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

		EthCall ethCall;
		BigInteger balanceValue = BigInteger.ZERO;
		Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/4098a0ceccd5421fa162fb549adea10a"));

		Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
		String clientVersion = web3ClientVersion.getWeb3ClientVersion();
		System.out.println("version=" + clientVersion);


		String passWord="123456";
//		String name = WalletUtils.generateNewWalletFile(passWord, new File(keyStoreDir), true);
//		System.out.println(name);
		String walleFilePath="D:/walletTest/UTC--2020-02-06T02-46-47.330000000Z--5065d510249259532225db0e979368ee084e7c5f.json";

		Credentials credentials = WalletUtils.loadCredentials(passWord, walleFilePath);
		//String fromAddress = credentials.getAddress();



		EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
				fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
		BigInteger nonce = ethGetTransactionCount.getTransactionCount();
		Address toAddress = new Address("0xa029180EF446cB6e64933848791539b7eeb12B35");
		BigInteger b = new BigInteger(1000+p);
		Uint256 value = new Uint256(b);
		List<Type> parametersList = new ArrayList<>();
		parametersList.add(toAddress);
		parametersList.add(value);
		List<TypeReference<?>> outList = new ArrayList<>();
		/*Function transfer = new Function("transfer", parametersList, outList);
		String encodedFunction = FunctionEncoder.encode(transfer);
		BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(18), Convert.Unit.GWEI).toBigInteger();
		RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice,
				BigInteger.valueOf(100000), contractAddress, encodedFunction);
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		String hexValue = Numeric.toHexString(signedMessage);

		EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
		String transactionHash = ethSendTransaction.getTransactionHash();
		System.out.println(transactionHash);*/
		try {
			ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			System.out.println(results);
			balanceValue = (BigInteger) results.get(0).getValue();
			System.out.println(balanceValue);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testqq() throws ExecutionException, InterruptedException {
		String methodName = "decimals";
		String fromAddr = "0x5065d510249259532225db0e979368ee084e7c5f";
		String contractAddress = "0x53509548c0ce0be4bb88b85f4d2c37b2c5cd1546";
		int decimal = 0;
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();

		TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {};
		outputParameters.add(typeReference);

		Function function = new Function(methodName, inputParameters, outputParameters);

		String data = FunctionEncoder.encode(function);
		Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

		EthCall ethCall;
		Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/4098a0ceccd5421fa162fb549adea10a"));

		Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
		String clientVersion = web3ClientVersion.getWeb3ClientVersion();
		System.out.println("version=" + clientVersion);
		try {
			ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			decimal = Integer.parseInt(results.get(0).getValue().toString());
			System.out.println(decimal);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testssss() throws IOException {
		Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/4098a0ceccd5421fa162fb549adea10a"));
		/*List<EthBlock.TransactionResult> txs = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send().getBlock().getTransactions();
		txs.forEach(tx -> {
			EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) tx.get();

			System.out.println(transaction.getFrom());
		});*/
		ClientTransactionManager transactionManager = new ClientTransactionManager(web3j,
				"0x5065d510249259532225db0e979368ee084e7c5f");
		MonitorTransfer token = MonitorTransfer.load("0x53509548c0ce0be4bb88b85f4d2c37b2c5cd1546", web3j, transactionManager,
				Contract.GAS_PRICE, Contract.GAS_LIMIT);
		token.transferEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST)
				.subscribe(tx -> {
							String toAddress = tx._to.getValue();
							String fromAddress = tx._from.getValue();
							String txHash = tx._transactionHash.toString();
							System.out.println("toAddress:"+toAddress+"--------txHash:"+txHash);
						});
	}
}
