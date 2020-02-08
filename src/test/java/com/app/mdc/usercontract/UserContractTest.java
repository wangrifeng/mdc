package com.app.mdc.usercontract;

import com.app.mdc.utils.httpclient.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录测试用例
 */
public class UserContractTest {

    private final String HOST = "http://localhost:8081";

    @Test
    public void add(){
        Map<String,String> param  = new HashMap<>();
        param.put("userId","155");
        param.put("contractId","4");
        param.put("number","1");
        String s = HttpUtil.doPost(HOST + "/userContract/add", param,"0121188e-f37b-4789-b7ae-95fb93633862");
        System.out.println(s);
    }


}
