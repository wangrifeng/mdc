package com.app.mdc;

import com.alibaba.fastjson.JSON;
import com.app.mdc.model.socket.Message;
import org.junit.Test;

import java.util.Date;

public class UtilsTest {

    @Test
    public void testTime(){
        Message message = new Message();
        message.setCreatetime(new Date());
        System.out.println(JSON.toJSONStringWithDateFormat(message, "yyyy-MM-dd HH:mm:ss"));
    }
}
