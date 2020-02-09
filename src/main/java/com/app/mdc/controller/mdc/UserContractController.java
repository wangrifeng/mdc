package com.app.mdc.controller.mdc;

import com.app.mdc.exception.BusinessException;
import com.app.mdc.service.mdc.UserContractService;
import com.app.mdc.utils.viewbean.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 用户合同接口
 */
@RestController
@RequestMapping("/userContract")
@Api("用户合同")
public class UserContractController {

    @Autowired
    private UserContractService userContractService;

    @RequestMapping(value = "/add",method = POST)
    @ApiOperation("新增用户合同")
    public ResponseResult add(@RequestParam Integer userId,@RequestParam Integer contractId,@RequestParam Integer number) throws BusinessException {
        userContractService.add(userId,contractId,number);
        return ResponseResult.success();
    }

    @RequestMapping(value = "/getUpgradePriceDifference",method = POST)
    @ApiOperation("获取合约升级差价")
    public ResponseResult getUpgradePriceDifference(@RequestParam Integer ucId,@RequestParam Integer upgradeId) throws BusinessException {
        BigDecimal price = userContractService.getUpgradePriceDifference(ucId,upgradeId);
        Map<String,Object> result = new HashMap<>();
        result.put("priceDifference",price);
        return ResponseResult.success().setData(result);
    }

    @RequestMapping(value = "/upgrade",method = POST)
    @ApiOperation("合约升级")
    public ResponseResult upgrade(@RequestParam Integer userId,@RequestParam Integer ucId,@RequestParam Integer upgradeId,@RequestParam String payToken) throws BusinessException {
        userContractService.upgrade(userId,ucId,payToken,upgradeId);
        return ResponseResult.success();
    }

    @RequestMapping(value = "/rescind",method = POST)
    @ApiOperation("合约解约")
    public ResponseResult rescind(@RequestParam Integer userId,@RequestParam Integer ucId) throws BusinessException {
        userContractService.rescind(userId,ucId);
        return ResponseResult.success();
    }

}
