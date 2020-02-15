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
//    private final String HOST = "http://124.156.171.72:8081";

    @Test
    public void add() {
        Map<String, String> param = new HashMap<>();
        param.put("userId", "278");
        param.put("contractId", "1");
        param.put("number", "1");
        param.put("payPassword", "123456");
        param.put("verCode", "835062");
        param.put("verId", "47");
        String s = HttpUtil.doPost(HOST + "/userContract/add", param, "31ddc0a8-2d72-48a2-9621-bb286a8617ea");
        System.out.println(s);
    }


    @Test
    public void getUpgradePriceDifference() {
        Map<String, String> param = new HashMap<>();
        param.put("ucId", "50");
        param.put("upgradeId", "4");
        String s = HttpUtil.doPost(HOST + "/userContract/getUpgradePriceDifference", param, "fb900165-9342-4772-bc45-2b29b84584c7");
        System.out.println(s);
    }

    @Test
    public void getRescindMoney() {
        Map<String, String> param = new HashMap<>();
        param.put("contractId", "4");
        String s = HttpUtil.doPost(HOST + "/userContract/getRescindMoney", param, "1d49e5b1-c211-430d-9502-52d78adec4d8");
        System.out.println(s);
    }

    @Test
    public void upgrade() {
        Map<String, String> param = new HashMap<>();
        param.put("userId", "180");
        param.put("ucId", "50");
        param.put("upgradeId", "4");
        param.put("verCode", "8ILD8s");
        param.put("verId", "10");
        param.put("payPassword", "123456");
        String s = HttpUtil.doPost(HOST + "/userContract/upgrade", param, "fb900165-9342-4772-bc45-2b29b84584c7");
        System.out.println(s);
    }

    @Test
    public void rescind() {
        Map<String, String> param = new HashMap<>();
        param.put("userId", "180");
        param.put("ucId", "50");
        param.put("verCode", "8ILD8s");
        param.put("verId", "10");
        param.put("payPassword", "123456");
        String s = HttpUtil.doPost(HOST + "/userContract/rescind", param, "fb900165-9342-4772-bc45-2b29b84584c7");
        System.out.println(s);
    }


    @Test
    public void list() {
        Map<String, String> param = new HashMap<>();
        String s = HttpUtil.doPost(HOST + "/contract/list", param, "31ddc0a8-2d72-48a2-9621-bb286a8617ea");
        System.out.println(s);
    }

    @Test
    public void feedback() {
        Map<String, String> param = new HashMap<>();
        param.put("userId", "180");
        param.put("message", "180");
        String s = HttpUtil.doPost(HOST + "/feedback/add", param, "fb900165-9342-4772-bc45-2b29b84584c7");
        System.out.println(s);
    }

    @Test
    public void getHigherContract() {
        Map<String, String> param = new HashMap<>();
        param.put("contractId", "1");
        String s = HttpUtil.doPost(HOST + "/userContract/getHigherContract", param, "1d49e5b1-c211-430d-9502-52d78adec4d8");
        System.out.println(s);
    }


}
