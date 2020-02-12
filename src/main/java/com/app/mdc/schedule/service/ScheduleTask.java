package com.app.mdc.schedule.service;

import com.alibaba.fastjson.JSON;
import com.app.mdc.enums.InfuraInfo;
import com.app.mdc.mapper.mdc.TransactionMapper;
import com.app.mdc.mapper.mdc.WalletMapper;
import com.app.mdc.model.mdc.Transaction;
import com.app.mdc.model.mdc.Wallet;
import com.app.mdc.model.system.Config;
import com.app.mdc.service.system.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Configuration
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private ConfigService configService;
    @Scheduled(cron = "0 */15 * * * ?")
    public void invest() throws ExecutionException, InterruptedException {


        String contractAddress = InfuraInfo.INFURA_ADDRESS.getDesc();
        List<Wallet> wallets = walletMapper.selectByMap(new HashMap<>());
        Config config = configService.getByKey("INFURA_ADDRESS");
        Config walletAddress = configService.getByKey("WALLET_ADDRESS");
        Web3j web3j = Web3j.build(new HttpService(config.getConfigValue()));
        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        System.out.println("version=" + clientVersion);
        for(Wallet wallet : wallets){
            try {
                BigDecimal balance = getBalance(web3j,wallet.getAddress(),contractAddress);
                if(balance.doubleValue()> (double) 0){
                    //充值
                    boolean flag = transfer(web3j,wallet.getPassword(),wallet.getWalletPath(),wallet.getAddress(),walletAddress.getConfigValue(),contractAddress,balance);
                    if(flag){
                        Transaction transaction = new Transaction();
                        transaction.setCreateTime(new Date());
                        transaction.setToAmount(balance);
                        transaction.setToUserId(wallet.getUserId());
                        transaction.setToWalletAddress(wallet.getAddress());
                        //0-usdt
                        transaction.setToWalletType("0");
                        //0-待交易
                        transaction.setTransactionStatus("1");
                        //0-充值
                        transaction.setTransactionType("0");
                        transactionMapper.insert(transaction);
                        BigDecimal oldBalance = wallet.getUstdBlance();
                        wallet.setUstdBlance(oldBalance.add(balance));
                        walletMapper.updateById(wallet);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

        }
    }

    private BigDecimal getBalance(Web3j web3j, String fromAddress, String contractAddress) throws IOException {
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
        BigDecimal balanceValue;
        ethCall = web3j.ethCall(transactions, DefaultBlockParameterName.LATEST).send();
        List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        System.out.println(JSON.toJSON(results));
        balanceValue = new BigDecimal((BigInteger) results.get(0).getValue()).divide(new BigDecimal("1000000"));
        System.out.println(balanceValue);
        return balanceValue;
    }

    private boolean transfer(Web3j web3j,String payPassword,String fromPath,String fromAddress,String toAddress,String contractAddress,BigDecimal trans) throws IOException, CipherException, ExecutionException, InterruptedException {
        Credentials credentials = WalletUtils.loadCredentials(payPassword, fromPath);
        /*Web3j web3j = Web3j.build(new HttpService(InfuraInfo.INFURA_ADDRESS.getDesc()));*/


        String transactionHash;

        BigDecimal eth = new BigDecimal(InfuraInfo.USDT_ETH.getDesc());
        BigDecimal fee = new BigDecimal(0);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        Address transferAddress = new Address(toAddress);
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
        if(transactionHash !=null && !"".equals(transactionHash)){
            return true;
        }else{
            return false;
        }
    }



}
