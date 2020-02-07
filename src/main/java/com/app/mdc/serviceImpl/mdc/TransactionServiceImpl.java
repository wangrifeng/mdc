package com.app.mdc.serviceImpl.mdc;

import com.app.mdc.enums.ApiErrEnum;
import com.app.mdc.enums.InfuraInfo;
import com.app.mdc.mapper.mdc.WalletMapper;
import com.app.mdc.mapper.system.UserMapper;
import com.app.mdc.model.mdc.Transaction;
import com.app.mdc.mapper.mdc.TransactionMapper;
import com.app.mdc.model.mdc.Wallet;
import com.app.mdc.model.system.User;
import com.app.mdc.service.mdc.TransactionService;
import com.app.mdc.utils.Md5Utils;
import com.app.mdc.utils.viewbean.Page;
import com.app.mdc.utils.viewbean.ResponseResult;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper,WalletMapper walletMapper,UserMapper userMapper){
        this.walletMapper = walletMapper;
        this.userMapper = userMapper;
        this.transactionMapper = transactionMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult transETH(String fromWalletId,String toWalletId,String transferNumber,String payPassword,String userId,String walletType,String transactionType) throws Exception {
        User u = userMapper.selectById(userId);
        //验证支付密码
        if (StringUtils.isNotEmpty(u.getPayPassword()) && !Md5Utils.hash(u.getLoginName(), payPassword).equals(u.getPayPassword())) {
            return ResponseResult.fail(ApiErrEnum.ERR202);
        }
        //开始转账
        Wallet fromWallet = walletMapper.selectById(fromWalletId);
        Wallet toWallet = walletMapper.selectById(toWalletId);
        Credentials credentials = WalletUtils.loadCredentials(payPassword, fromWallet.getWalletPath());
        Web3j web3j = Web3j.build(new HttpService(InfuraInfo.INFURA_ADDRESS.getDesc()));

        Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        System.out.println("version=" + clientVersion);
        String transactionHash;
        BigDecimal trans = new BigDecimal(transferNumber);
        BigDecimal mdcETH = new BigDecimal(InfuraInfo.MDC_ETH.getDesc());
        BigDecimal fee = new BigDecimal(0);
        if("3".equals(walletType)){
            //ETH交易
            BigDecimal changeNumber = new BigDecimal(InfuraInfo.ETH_FINNEY.getDesc());
            //开始转账
            TransactionReceipt send = Transfer.sendFunds(web3j, credentials, toWallet.getAddress(), trans.multiply(changeNumber), Convert.Unit.FINNEY).send();
            transactionHash = send.getTransactionHash();
            Thread.sleep(60000);
            EthGetBalance balance = web3j.ethGetBalance(fromWallet.getAddress(), DefaultBlockParameter.valueOf("latest")).send();
            //格式转化 wei-ether
            String blanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");
            BigDecimal blance = new BigDecimal(blanceETH);
            fee = fromWallet.getEthBlance().subtract(blance).subtract(trans);
            fromWallet.setEthBlance(blance);
        }else{
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    fromWallet.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            Address toAddress = new Address(toWallet.getAddress());

            Uint256 value = new Uint256(new BigInteger(trans.multiply(mdcETH).stripTrailingZeros().toPlainString()));
            List<Type> parametersList = new ArrayList<>();
            parametersList.add(toAddress);
            parametersList.add(value);
            List<TypeReference<?>> outList = new ArrayList<>();
            Function transfer = new Function("transfer", parametersList, outList);
            String encodedFunction = FunctionEncoder.encode(transfer);
            BigInteger gasPrice = Convert.toWei(new BigDecimal(InfuraInfo.GAS_PRICE.getDesc()), Convert.Unit.GWEI).toBigInteger();
            String contractAddress = "";
            if("0".equals(walletType)){
                contractAddress = InfuraInfo.USDT_CONTRACT_ADDRESS.getDesc();
            }else if("1".equals(walletType)){
                contractAddress = InfuraInfo.MDC_CONTRACT_ADDRESS.getDesc();
            }
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice,
                    new BigInteger(InfuraInfo.GAS_SIZE.getDesc()),contractAddress, encodedFunction);
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
            transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println(transactionHash);

            Thread.sleep(60000);

            //查询余额变化
            String methodName = "balanceOf";
            List<Type> inputParameters = new ArrayList<>();
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            Address address = new Address(fromWallet.getAddress());
            inputParameters.add(address);

            TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
            };
            outputParameters.add(typeReference);
            Function function = new Function(methodName, inputParameters, outputParameters);
            String data = FunctionEncoder.encode(function);
            org.web3j.protocol.core.methods.request.Transaction transactions = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(fromWallet.getAddress(), contractAddress, data);

            EthCall ethCall;
            BigDecimal balanceValue = new BigDecimal(0);
            try {
                ethCall = web3j.ethCall(transactions, DefaultBlockParameterName.LATEST).send();
                List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
                System.out.println(results);
                balanceValue = (BigDecimal) results.get(0).getValue();
                System.out.println(balanceValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if("0".equals(walletType)){
                fromWallet.setUstdBlance(balanceValue);
            }else if("1".equals(walletType)){
                fromWallet.setMdcBlance(balanceValue);
            }

        }
        if(transactionHash != null&&!"".equals(transactionHash)){
            //添加交易记录
            Transaction transaction = new Transaction();
            transaction.setCreateTime(new Date());
            transaction.setFromAmount(trans);
            transaction.setFromUserId(Integer.parseInt(u.getId()));
            transaction.setFromWalletId(fromWallet.getWalletId());
            transaction.setFromWalletType(walletType);
            transaction.setToAmount(trans);
            transaction.setToUserId(toWallet.getUserId());
            transaction.setToWalletId(toWallet.getWalletId());
            transaction.setToWalletType(walletType);
            transaction.setTransactionStatus("1");
            transaction.setTransactionType(transactionType);
            transaction.setTransactionHash(transactionHash);
            transaction.setFeeAmount(fee);
            transactionMapper.insert(transaction);
            //修改钱包余额
            walletMapper.updateById(fromWallet);
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getETHBlance(Page page, Map<String,Object> params) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<Transaction> transactionList = transactionMapper.selectByMap(params);
        return ResponseResult.success().setData(new PageInfo<>(transactionList));
    }

}
