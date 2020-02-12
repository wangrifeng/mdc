package com.app.mdc.controller.mdc;


import com.app.mdc.annotation.anno.SystemLogAnno;
import com.app.mdc.model.system.Dict;
import com.app.mdc.service.mdc.TransactionService;
import com.app.mdc.utils.viewbean.Page;
import com.app.mdc.utils.viewbean.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2019-06-12
 */
@Api(value = "TransactionController", description = "交易接口-充值，提现，转账，闪兑及其记录")
@Controller
@RequestMapping("/mdc/transaction")
public class TransactionController {

	private final TransactionService transactionService;

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService=transactionService;
	}
	
	/**
	 * 获取交易记录
	 * @param map
	 * @return 返回的结果，0正确ERR500错误
	 */
	@ApiOperation(value="交易记录", notes="获取充值，提现，转账，闪兑记录")
	@PostMapping("/getTransaction")
	@ResponseBody
	public ResponseResult getTransaction(@RequestParam Map<String, Object> map, Page page) {
		return transactionService.getETHBlance(page,map);
	}

	/**
	 * 交易转账
	 */
	@PostMapping("/transfer")
	@SystemLogAnno(module = "交易管理", operation = "交易转账")
	@ResponseBody
	public ResponseResult transfer(@RequestParam(required = true)String fromWalletId,
									 @RequestParam(required = true)String toWalletId,
									 @RequestParam(required = true)String transferNumber,
									 @RequestParam(required = true)String payPassword,
									 @RequestParam(required = true)String userId,
									 @RequestParam(required = true)String toUserId,
									 @RequestParam(required = true)String walletType) {
		return transactionService.transETH(fromWalletId,toWalletId,transferNumber,payPassword,userId,toUserId,walletType);
	}

	/**
	 * 充值
	 */
	@PostMapping("/invest")
	@SystemLogAnno(module = "交易管理", operation = "交易充值")
	@ResponseBody
	public ResponseResult invest(@RequestParam String userId,@RequestParam String toAddress,@RequestParam String investMoney) {
		return transactionService.investUSDT(userId,toAddress,investMoney);
	}

	/**
	 * 提现
	 */
	@PostMapping("/cashOut")
	@SystemLogAnno(module = "交易管理", operation = "交易提现")
	@ResponseBody
	public ResponseResult cashOut(@RequestParam String userId,@RequestParam String payPassword,@RequestParam String toAddress,@RequestParam String cashOutMoney) {
		try {
			return transactionService.cashOutUSDT(userId, payPassword, toAddress, cashOutMoney);
		}catch (Exception e){
			return ResponseResult.fail("-999",e.getMessage());
		}

	}

	@PostMapping("/convertMDC")
	@SystemLogAnno(module = "交易管理", operation = "mdc闪兑")
	@ResponseBody
	public ResponseResult convertMDC(@RequestParam String userId,@RequestParam String convertMoney,@RequestParam String payPassword) {
		try {
			return transactionService.convertMDC(userId, convertMoney, payPassword);
		}catch (Exception e){
			return ResponseResult.fail("-999",e.getMessage());
		}

	}

	@PostMapping("/handlerInvest")
	@SystemLogAnno(module = "充值", operation = "手动充值")
	@ResponseBody
	public ResponseResult handlerInvest() {
		try {
			return transactionService.handleInvest();
		}catch (Exception e){
			return ResponseResult.fail("-999",e.getMessage());
		}

	}

}

