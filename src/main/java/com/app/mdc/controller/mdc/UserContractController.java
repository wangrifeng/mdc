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
}
