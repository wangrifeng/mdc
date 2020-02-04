package com.app.mdc;

import com.app.mdc.utils.encryptdecrypt.DES;
import com.app.mdc.utils.jvm.JvmUtils;
import com.app.mdc.utils.jvm.LinceseUtils;
import org.junit.Test;

public class jvmtest {

    /**
     * 校验lincese是否有效
     */
    @Test
    public void linceseTest(){
        try {
            boolean ischecked = LinceseUtils.getInstance("b266593f-21d6-4772-9ef9-795a5faffc78").isCheckLincese();
            System.out.println(ischecked);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成lincese
     * @throws Exception exception
     */
    @Test
    public void linceseGeneratorTest() throws Exception {
        String mac = JvmUtils.getWindowsMac() + "_" + JvmUtils.getCPUIDWindows() + "_1593660187000" ;
        System.out.println(mac);

//        String mac = "6C-2B-59-CD-EC-5E_3SV8YH2_1627660800000" ;
//        System.out.println(mac);
        //默认加密校准
        System.out.println("加密后字符串：" + encrypt(mac));
        System.out.println("解密后字符串：" + decrypt(encrypt(mac)));

        String key = "b266593f-21d6-4772-9ef9-795a5faffc78";
        //加key校验校准
        System.out.println("加密后字符串：" + encrypt(mac, key));
        System.out.println("解密后字符串：" + decrypt(encrypt(mac, key), key));

        //是否是windows
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");
        System.out.println(isWindows);
    }

    @Test
    public void getCurrentTime(){
        long time = System.currentTimeMillis();
        System.out.println(time);
    }

    /**
     * 使用默认密钥进行DES加密
     */
    private static String encrypt(String plainText) {
        try {
            return new DES().encrypt(plainText);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 使用指定密钥进行DES解密
     */
    private static String encrypt(String plainText, String key) {
        try {
            return new DES(key).encrypt(plainText);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 使用默认密钥进行DES解密
     */
    private static String decrypt(String plainText) {
        try {
            return new DES().decrypt(plainText);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 使用指定密钥进行DES解密
     */
    private static String decrypt(String plainText, String key) {
        try {
            return new DES(key).decrypt(plainText);
        } catch (Exception e) {
            return null;
        }
    }


}
