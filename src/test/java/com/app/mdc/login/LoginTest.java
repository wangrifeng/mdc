package com.app.mdc.login;

import com.app.mdc.utils.httpclient.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录测试用例
 */
public class LoginTest {

//    private final String HOST = "http://124.156.171.72:8081";
    private final String HOST = "http://localhost:8081";

    @Test
    public void register() {
        String prefix = "tes2131t@qq.com";
//        for(int i=1;i<=10;i++){
        Map<String, String> param = new HashMap<>();
        param.put("loginName", prefix);
        param.put("userName", prefix);
        param.put("roleId", "10");
        param.put("password", "123456");
        param.put("sendCode", "888888");
        param.put("walletPassword", "11232123456");
        param.put("verCode", "835062");
        param.put("verId", "47");
        param.put("registerType", "0");
        param.put("email", prefix);
        String s = HttpUtil.doPost(HOST + "/admin/users/add", param, null);
        System.out.println(s);
//        }
    }

    @Test
    public void getOne() {
        Map<String, String> param = new HashMap<>();
        param.put("id", "283");
        String s = HttpUtil.doPost(HOST + "/admin/users/getOne", param, "1d49e5b1-c211-430d-9502-52d78adec4d8");
        System.out.println(s);
    }

    @Test
    public void updateUserName() {
        Map<String, String> param = new HashMap<>();
        param.put("userId", "278");
        param.put("userName", "test222");
        String s = HttpUtil.doPost(HOST + "/admin/users/updateUserName", param, "31ddc0a8-2d72-48a2-9621-bb286a8617ea");
        System.out.println(s);
    }


    @Test
    public void login() {
        Map<String, String> param = new HashMap<>();
        param.put("loginName", "admin");
        param.put("password", "123456");
        String s = HttpUtil.doPost(HOST + "/doLogin", param, null);
        System.out.println(s);
    }

    @Test
    public void resetPassword() {
        Map<String, String> param = new HashMap<>();
        param.put("verCode", "835062");
        param.put("verId", "47");
        param.put("loginName", "1424547204@qq.com");
        param.put("password", "123456");
        param.put("payPassword", "123456");
        String s = HttpUtil.doPost(HOST + "/admin/users/resetPassword", param, null);
        System.out.println(s);
    }


    @Test
    public void loginOut() {
        Map<String, String> param = new HashMap<>();
        param.put("userId", "102");
        String s = HttpUtil.doPost(HOST + "/doLoginOut", param, null);
        System.out.println(s);
    }

    @Test
    public void updatePwd() {
        Map<String, String> param = new HashMap<>();
        param.put("type", "0");
        param.put("id", "278");
        param.put("loginName", "1424547204@qq.com");
        param.put("newPassword", "223456");
        param.put("verId", "48");
        param.put("verCode", "793879");
        String s = HttpUtil.doPost(HOST + "/admin/users/updatePwd", param, "7d0d120f-c6aa-49b3-9b0b-30b15e13d749");
        System.out.println(s);
    }

    @Test
    public void getVerificationCode() {
        Map<String, String> param = new HashMap<>();
        param.put("type", "0");
        param.put("email", "1424547204@qq.com");
        String s = HttpUtil.doPost(HOST + "/verificationCode/getVerificationCode", param, null);
        System.out.println(s);
    }

    @Test
    public void getVerificationCodeByUserId() {
        Map<String, String> param = new HashMap<>();
        param.put("userId", "278");
        String s = HttpUtil.doPost(HOST + "/verificationCode/getVerificationCodeByUserId", param, "7d0d120f-c6aa-49b3-9b0b-30b15e13d749");
        System.out.println(s);
    }

    @Test
    public void updateGestureSwitch() {
        Map<String, String> param = new HashMap<>();
        param.put("userId", "105");
        param.put("gestureSwitch", "0");
        String s = HttpUtil.doPost(HOST + "/admin/users/updateGestureSwitch", param, "2cb5cc2a-05ea-4849-b9aa-13f49dc41c4d");
        System.out.println(s);
    }

}
