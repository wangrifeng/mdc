package com.app.mdc.userlevel;

import com.app.mdc.utils.httpclient.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录测试用例
 */
public class UserLeveltTest {

    private final String HOST = "http://localhost:8081";

    @Test
    public void list(){
        Map<String,String> param  = new HashMap<>();
        param.put("userId","105");
        param.put("pageNumber","1");
        param.put("pageSize","10");
        String s = HttpUtil.doPost(HOST + "/userLevel/list", param,"0121188e-f37b-4789-b7ae-95fb93633862");
        System.out.println(s);
    }



}
