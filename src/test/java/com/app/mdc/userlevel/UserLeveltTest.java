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
        param.put("userId","1");
        param.put("pageNumber","1");
        param.put("pageSize","10");
        String s = HttpUtil.doPost(HOST + "/userLevel/list", param,"1d49e5b1-c211-430d-9502-52d78adec4d8");
        System.out.println(s);
    }



}
