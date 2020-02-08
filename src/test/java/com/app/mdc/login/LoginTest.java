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
        param.put("loginName","1-j");
        param.put("userName","1-j");
        param.put("roleId","10");
        param.put("password","123456");
        param.put("email","mdc@qq.com");
        param.put("sendCode","8888");
        String s = HttpUtil.doPost(HOST + "/admin/users/add", param,null);
        System.out.println(s);
    }

    @Test
    public void login(){
        Map<String,String> param  = new HashMap<>();
        param.put("loginName","2-cc");
        param.put("password","123456");
        String s = HttpUtil.doPost(HOST + "/doLogin", param,null);
        System.out.println(s);
    }

    @Test
    public void loginOut(){
        Map<String,String> param  = new HashMap<>();
        param.put("userId","102");
        String s = HttpUtil.doPost(HOST + "/doLoginOut", param,null);
        System.out.println(s);
    }

    @Test
    public void updatePwd(){
        Map<String,String> param  = new HashMap<>();
        param.put("type","1");
        param.put("id","105");
        param.put("oldPassword","423456");
        param.put("newPassword","423456");
        param.put("verId","7");
        param.put("verCode","IPGICp");
        String s = HttpUtil.doPost(HOST + "/admin/users/updatePwd", param,"2cb5cc2a-05ea-4849-b9aa-13f49dc41c4d");
        System.out.println(s);
    }

    @Test
    public void getVerificationCode(){
        Map<String,String> param  = new HashMap<>();
        param.put("userId","105");
        String s = HttpUtil.doPost(HOST + "/verificationCode/getVerificationCode", param,"2cb5cc2a-05ea-4849-b9aa-13f49dc41c4d");
        System.out.println(s);
    }

    @Test
    public void updateGestureSwitch(){
        Map<String,String> param  = new HashMap<>();
        param.put("userId","105");
        param.put("gestureSwitch","0");
        String s = HttpUtil.doPost(HOST + "/admin/users/updateGestureSwitch", param,"2cb5cc2a-05ea-4849-b9aa-13f49dc41c4d");
        System.out.println(s);
    }

}
