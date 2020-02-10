package com.app.mdc.controller.system;

import com.app.mdc.exception.BusinessException;
import com.app.mdc.service.system.VerificationCodeService;
import com.app.mdc.utils.viewbean.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * 验证码管理
 */
@RestController
@RequestMapping("/verificationCode")
@Api("验证码管理")
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @RequestMapping("/getVerificationCode")
    @ApiOperation("/获取验证码")
    public ResponseResult getVerificationCode(String email) throws BusinessException {
        Map<String,Object> result = new HashMap<>();
        result.put("verId",verificationCodeService.getVerificationCode(email));
        return ResponseResult.success().setData(result);
    }

}
