package com.app.mdc.income;

import com.app.mdc.utils.httpclient.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录测试用例
 */
public class InComeTest {

    private final String HOST = "http://localhost:8081";

    @Test
    public void list(){
        Map<String,String> param  = new HashMap<>();
        param.put("userId","181");
        param.put("pageNumber","1");
        param.put("pageSize","10");
        String s = HttpUtil.doPost(HOST + "/income/list", param,"fb900165-9342-4772-bc45-2b29b84584c7");
        System.out.println(s);
    }



}
