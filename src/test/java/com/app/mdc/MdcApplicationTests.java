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
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.app.mdc.schedule.service.ScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	public void testMap(){
		Map map1 = new HashMap<>();
		map1.put("code","1");
		Map map2 = new HashMap<>();
		map2.put("code","2");
		List<Map> list = new ArrayList<>();
		list.add(map1);
		list.add(map2);
		Map map3 = new HashMap<>();
		map3.put("1","ese");
		map3.put("2","dad");
		map3.put("3","dasda");
		for(Map map : list){
			map3.remove(map.get("code"));
		}
		System.out.println(map3);
	}

}
