package com.app.mdc.notice;

import com.app.mdc.utils.httpclient.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录测试用例
 */
public class NoticeTest {

    private final String HOST = "http://localhost:8081";

    @Test
    public void list(){
        Map<String,String> param  = new HashMap<>();
        String s = HttpUtil.doPost(HOST + "/notice/list", param,null);
        System.out.println(s);
    }



}
