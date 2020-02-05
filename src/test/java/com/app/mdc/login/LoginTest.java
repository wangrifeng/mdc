package com.app.mdc.login;

import com.app.mdc.utils.httpclient.HttpUtil;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录测试用例
 */
public class LoginTest {

    private final String HOST = "http://localhost:8081";

    @Test
    public void register(){
        Map<String,String> param  = new HashMap<>();
        param.put("loginName","admin3");
        param.put("userName","admin—3");
        param.put("roleId","10");
        param.put("password","123456");
        String s = HttpUtil.doPost(HOST + "/admin/users/add", param);
        System.out.println(s);
    }

    @Test
    public void login(){
        Map<String,String> param  = new HashMap<>();
        param.put("loginName","admin3");
        param.put("password","223456");
        String s = HttpUtil.doPost(HOST + "/doLogin", param);
        System.out.println(s);
    }

    @Test
    public void loginOut(){
        Map<String,String> param  = new HashMap<>();
        param.put("userId","102");
        String s = HttpUtil.doPost(HOST + "/doLoginOut", param);
        System.out.println(s);
    }

    @Test
    public void updatePwd(){
        Map<String,String> param  = new HashMap<>();
        param.put("type","1");
        param.put("id","104");
        param.put("oldPassword","223456");
        param.put("newPassword","323456");
        String s = HttpUtil.doPost(HOST + "/updatePwd", param);
        System.out.println(s);
    }

}
