package com.app.mdc.serviceImpl.mdc;

import com.app.mdc.enums.ApiErrEnum;
import com.app.mdc.enums.InfuraInfo;
import com.app.mdc.exception.BusinessException;
import com.app.mdc.mapper.mdc.WalletMapper;
import com.app.mdc.mapper.system.UserMapper;
import com.app.mdc.model.mdc.Transaction;
import com.app.mdc.mapper.mdc.TransactionMapper;
import com.app.mdc.model.mdc.Wallet;
import com.app.mdc.model.system.User;
import com.app.mdc.service.mdc.TransactionService;
import com.app.mdc.utils.Md5Utils;
import com.app.mdc.utils.date.DateUtil;
import com.app.mdc.utils.viewbean.Page;
import com.app.mdc.utils.viewbean.ResponseResult;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2020-02-05
 */
@Service("transactionService")
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {

    private final WalletMapper walletMapper;
    private final UserMapper userMapper;
    private final TransactionMapper transactionMapper;
    private final Web3j web3j;

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper,WalletMapper walletMapper,UserMapper userMapper){
        this.walletMapper = walletMapper;
        this.userMapper = userMapper;
        this.transactionMapper = transactionMapper;
        this.web3j = Web3j.build(new HttpService(InfuraInfo.INFURA_ADDRESS.getDesc()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult transETH(String fromWalletId,String toWalletId,String transferNumber,String payPassword,String userId,String toUserId,String walletType) {
        User u = userMapper.selectById(userId);
        //验证支付密码
        if (StringUtils.isNotEmpty(u.getPayPassword()) && !Md5Utils.hash(u.getLoginName(), payPassword).equals(u.getPayPassword())) {
            return ResponseResult.fail(ApiErrEnum.ERR202);
        }
        Wallet fromWallet = walletMapper.selectById(fromWalletId);
        Wallet toWallet = walletMapper.selectById(toWalletId);

        BigDecimal trans = new BigDecimal(transferNumber);
        Transaction transaction = new Transaction();
        BigInteger gasPrice = Convert.toWei(new BigDecimal(InfuraInfo.GAS_PRICE.getDesc()), Convert.Unit.GWEI).toBigInteger();
        BigDecimal fee = new BigDecimal(gasPrice.toString()).divide(new BigDecimal(InfuraInfo.MDC_ETH.getDesc()));
        transaction.setFeeAmount(fee);
        transaction.setCreateTime(new Date());
        transaction.setFromAmount(trans.add(fee));
        transaction.setFromUserId(Integer.parseInt(userId));
        transaction.setFromWalletAddress(fromWallet.getAddress());
        transaction.setFromWalletType(walletType);
        //0-usdt
        transaction.setFromWalletType("0");
        transaction.setToAmount(trans);
        transaction.setToUserId(Integer.parseInt(toUserId));
        transaction.setToWalletAddress(toWallet.getAddress());
        transaction.setToWalletType(walletType);
        //1-交易完成
        transaction.setTransactionStatus("1");
        //1-提现
        transaction.setTransactionType("2");
        transactionMapper.insert(transaction);
        if("0".equals(walletType)){
            BigDecimal usdtFrom = fromWallet.getUstdBlance();
            BigDecimal usdtTo = toWallet.getUstdBlance();
            fromWallet.setUstdBlance(usdtFrom.subtract(trans.add(fee)));
            toWallet.setUstdBlance(usdtTo.add(trans));
        }else if("1".equals(walletType)){
            BigDecimal usdtFrom = fromWallet.getMdcBlance();
            BigDecimal usdtTo = toWallet.getMdcBlance();
            fromWallet.setMdcBlance(usdtFrom.subtract(trans.add(fee)));
            toWallet.setMdcBlance(usdtTo.add(trans));
        }
        walletMapper.updateById(fromWallet);
        walletMapper.updateById(toWallet);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getETHBlance(Page page, Map<String,Object> params) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<Transaction> transactionList = transactionMapper.selectByMap(params);
        return ResponseResult.success().setData(new PageInfo<>(transactionList));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult investUSDT(String userId,String toAddress, String investMoney) {
        BigDecimal invest = new BigDecimal(investMoney);
        Transaction transaction = new Transaction();
        transaction.setCreateTime(new Date());
        transaction.setToAmount(invest);
        transaction.setToUserId(Integer.parseInt(userId));
        transaction.setToWalletAddress(toAddress);
        //0-usdt
        transaction.setToWalletType("0");
        //0-待交易
        transaction.setTransactionStatus("0");
        //0-充值
        transaction.setTransactionType("0");
        transactionMapper.insert(transaction);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BigDecimal balance = getBalance(toAddress,InfuraInfo.USDT_CONTRACT_ADDRESS.getDesc());
        if(balance != null && balance.doubleValue()>= new Double(investMoney)){
            transaction.setTransactionStatus("1");
            transactionMapper.updateById(transaction);
        }else{
            confirm(new Date(),toAddress,InfuraInfo.USDT_CONTRACT_ADDRESS.getDesc(),investMoney,userId,transaction.getTransactionId().toString());
        }
        return ResponseResult.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult cashOutUSDT(String userId,String payPassword, String toAddress, String cashOutMoney) throws InterruptedException {
        BigDecimal cashOut = new BigDecimal(cashOutMoney);
        BigDecimal toBalance = getBalance(toAddress,InfuraInfo.USDT_CONTRACT_ADDRESS.getDesc());
        Transaction transaction = new Transaction();
        try {
            String transactionHash = transfer(userId,payPassword,cashOutMoney,InfuraInfo.WALLET_PATH.getDesc(),InfuraInfo.WALLET_ADDRESS.getDesc(),toAddress,"0");
            transaction.setTransactionHash(transactionHash);
        }catch (Exception e){
            return ResponseResult.fail("-999",e.getMessage());
        }
        BigInteger gasPrice = Convert.toWei(new BigDecimal(InfuraInfo.GAS_PRICE.getDesc()), Convert.Unit.GWEI).toBigInteger();
        BigDecimal fee = new BigDecimal(gasPrice.toString()).divide(new BigDecimal(InfuraInfo.MDC_ETH.getDesc()));
        transaction.setFeeAmount(fee);
        transaction.setCreateTime(new Date());
        transaction.setFromAmount(cashOut);
        transaction.setFromUserId(Integer.parseInt(userId));
        transaction.setFromWalletAddress(InfuraInfo.WALLET_ADDRESS.getDesc());
        //0-usdt
        transaction.setFromWalletType("0");
        transaction.setToAmount(cashOut);
        transaction.setToWalletAddress(toAddress);
        //0-usdt
        transaction.setToWalletType("0");
        //0-交易进行中
        transaction.setTransactionStatus("0");
        //1-提现
        transaction.setTransactionType("1");
        transactionMapper.insert(transaction);
        EntityWrapper<Wallet> walletEntityWrapper = new EntityWrapper<>();
        walletEntityWrapper.eq("user_id",userId);
        List<Wallet> wallets = walletMapper.selectList(walletEntityWrapper);
        Wallet wallet = wallets.get(0);
        BigDecimal balance = wallet.getUstdBlance();
        wallet.setUstdBlance(balance.subtract(cashOut));
        walletMapper.updateById(wallet);

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BigDecimal b = getBalance(toAddress,InfuraInfo.USDT_CONTRACT_ADDRESS.getDesc());
        if(b != null && b.doubleValue()>= toBalance.doubleValue()){
            transaction.setTransactionStatus("1");
            transactionMapper.updateById(transaction);
        }else{
            confirm(new Date(),toAddress,InfuraInfo.USDT_CONTRACT_ADDRESS.getDesc(),cashOutMoney,userId,transaction.getTransactionId().toString());
        }
        return ResponseResult.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public String transfer(String userId,String payPassword,String transferNumber,String fromPath,String fromAddress,String toAddress,String walletType) throws IOException, CipherException, ExecutionException, InterruptedException, BusinessException {
        User u = userMapper.selectById(userId);
        //验证支付密码
        if (StringUtils.isNotEmpty(u.getPayPassword()) && !Md5Utils.hash(u.getLoginName(), payPassword).equals(u.getPayPassword())) {
            throw new BusinessException(ApiErrEnum.ERR202);
        }
        BigDecimal trans = new BigDecimal(transferNumber);
        EntityWrapper<Wallet> walletEntityWrapper = new EntityWrapper<>();
        walletEntityWrapper.eq("user_id",userId);
        List<Wallet> wallets = walletMapper.selectList(walletEntityWrapper);
        //判断钱包是否存在
        if(wallets.size() > 0){
            Wallet wallet = wallets.get(0);
            //判断USTD余额和MDC余额 0-USDT 1-MDC
            if("0".equals(walletType) && wallet.getUstdBlance().doubleValue() < trans.doubleValue()){
                throw new BusinessException(ApiErrEnum.ERR205);
            }else if("1".equals(walletType) && wallet.getMdcBlance().doubleValue() < trans.doubleValue()){
                throw new BusinessException(ApiErrEnum.ERR206);
            }
        }else{
            throw new BusinessException(ApiErrEnum.ERR204);
        }
        //判断转出地址
        if(!toAddress.startsWith("0x")){
            throw new BusinessException(ApiErrEnum.ERR208);
        }
        Credentials credentials = WalletUtils.loadCredentials(payPassword, fromPath);
        /*Web3j web3j = Web3j.build(new HttpService(InfuraInfo.INFURA_ADDRESS.getDesc()));*/

        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        System.out.println("version=" + clientVersion);
        String transactionHash;

        BigDecimal eth;
        BigDecimal fee = new BigDecimal(0);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        Address transferAddress = new Address(toAddress);
        String contractAddress = "";
        if("0".equals(walletType)){
            eth = new BigDecimal(InfuraInfo.USDT_ETH.getDesc());
            contractAddress = InfuraInfo.USDT_CONTRACT_ADDRESS.getDesc();
        }else if("1".equals(walletType)){
            eth = new BigDecimal(InfuraInfo.MDC_ETH.getDesc());
            contractAddress = InfuraInfo.MDC_CONTRACT_ADDRESS.getDesc();
        }else{
            throw new BusinessException("交易失败");
        }
        Uint256 value = new Uint256(new BigInteger(trans.multiply(eth).stripTrailingZeros().toPlainString()));
        List<Type> parametersList = new ArrayList<>();
        parametersList.add(transferAddress);
        parametersList.add(value);
        List<TypeReference<?>> outList = new ArrayList<>();
        Function transfer = new Function("transfer", parametersList, outList);
        String encodedFunction = FunctionEncoder.encode(transfer);
        BigInteger gasPrice = Convert.toWei(new BigDecimal(InfuraInfo.GAS_PRICE.getDesc()), Convert.Unit.GWEI).toBigInteger();

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice,
                new BigInteger(InfuraInfo.GAS_SIZE.getDesc()),contractAddress, encodedFunction);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        transactionHash = ethSendTransaction.getTransactionHash();
        System.out.println(transactionHash);

        return transactionHash;
    }

    private BigDecimal getETHBalance(String address) throws IOException {
        EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send();
        //格式转化 wei-ether
        String blanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");
        return new BigDecimal(blanceETH);
    }

    private BigDecimal getBalance(String fromAddress,String contractAddress){
        //查询余额变化
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
        org.web3j.protocol.core.methods.request.Transaction transactions = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

        EthCall ethCall;
        BigDecimal balanceValue = new BigDecimal(0);
        try {
            ethCall = web3j.ethCall(transactions, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            System.out.println(results);
            balanceValue = (BigDecimal) results.get(0).getValue();
            System.out.println(balanceValue);
            return balanceValue;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balanceValue;
    }

    private void confirm(Date time,String fromAddress,String contractAddress,String money,String userId,String transactionId){
        //定时第一次15分钟后执行
        System.out.println(DateUtil.getDate("yyyy-MM-dd HH:mm:ss",0,time));
        System.out.println(DateUtil.getDate("yyyy-MM-dd HH:mm:ss",Calendar.MINUTE,15,time));
        User u = userMapper.selectById(userId);
        EntityWrapper<Wallet> walletEntityWrapper = new EntityWrapper<>();
        walletEntityWrapper.eq("user_id",userId);
        Wallet wallet = walletMapper.selectList(walletEntityWrapper).get(0);
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                System.out.println(DateUtil.getDate("yyyy-MM-dd HH:mm:ss",0,date));
                BigDecimal balanceValue = getBalance(fromAddress,contractAddress);
                if(balanceValue == null || balanceValue.doubleValue() <= new Double(money)){
                    //钱未到账继续第二次定时
                    System.out.println(DateUtil.getDate("yyyy-MM-dd HH:mm:ss",Calendar.HOUR_OF_DAY,1,date));
                    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
                    scheduler.initialize();
                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            Date lastDate = new Date();
                            System.out.println(DateUtil.getDate("yyyy-MM-dd HH:mm:ss",0,lastDate));
                            BigDecimal balance = getBalance(fromAddress,contractAddress);
                            if(balance == null || balance.doubleValue() <= new Double(money)){
                                //钱未到账继续第三次定时
                                System.out.println(DateUtil.getDate("yyyy-MM-dd HH:mm:ss",Calendar.HOUR_OF_DAY,2,lastDate));
                                ThreadPoolTaskScheduler lastScheduler = new ThreadPoolTaskScheduler();
                                lastScheduler.initialize();
                                lastScheduler.schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        BigDecimal lastBalance = getBalance(fromAddress,contractAddress);
                                        if(lastBalance == null || lastBalance.doubleValue() <= new Double(money)){
                                            //钱未到账，交易失败
                                            updateTransaction(transactionId,"-1");
                                        }else{
                                            //钱已到账
                                            transact(userId,u.getPayPassword(),money,wallet.getWalletPath(),wallet.getAddress(),InfuraInfo.WALLET_ADDRESS.getDesc(),"0",transactionId,"1");
                                        }
                                    }
                                },DateUtil.getDate(Calendar.HOUR_OF_DAY,2,lastDate));
                            }else{
                                //钱已到账
                                transact(userId,u.getPayPassword(),money,wallet.getWalletPath(),wallet.getAddress(),InfuraInfo.WALLET_ADDRESS.getDesc(),"0",transactionId,"1");
                            }
                        }
                    },DateUtil.getDate(Calendar.HOUR_OF_DAY,1,date));
                }else{
                    //钱已到账
                    transact(userId,u.getPayPassword(),money,wallet.getWalletPath(),wallet.getAddress(),InfuraInfo.WALLET_ADDRESS.getDesc(),"0",transactionId,"1");
                }
            }
        }, DateUtil.getDate(Calendar.MINUTE,15,time));
    }

    private void transact(String userId,String payPassword,String balance,String walletPath,String walletAddress,String toAddress,String walletType,String transactionId,String transactionStatus){
        try {
            transfer(userId,payPassword,balance,walletPath,walletAddress,toAddress,walletType);
            updateTransaction(transactionId,transactionStatus);
        } catch (IOException | CipherException | ExecutionException | InterruptedException | BusinessException e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTransaction(String transactionId,String transactionStatus){
        Transaction transaction = transactionMapper.selectById(transactionId);
        transaction.setTransactionStatus(transactionStatus);
        transactionMapper.updateById(transaction);

    }


}
