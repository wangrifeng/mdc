package com.app.mdc.mapper.system;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.app.mdc.model.system.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM sys_user")
    List<Map> getUserList();
    
    @Select("select count(1) from sys_user where login_name=#{0}")
    Integer user(String username);

    @Select("select count(1) from sys_user where user_name=#{0}")
    Integer isRepeat(String name);

    @Select("select user_id as userId,up_user_id as upUserId ,up_user_ids as upUserIds from sys_user where send_code=#{0}")
    Map<String,Object> getUserBySendCode(int sendCode);

    List<Map<String,Object>> getOperaterBook(@Param(value = "userId") String userId);

    List<Map<String,Object>> getCompanyUserBook(@Param(value = "userId") String userId);

    List<Map<String,Object>> getPcAddressBook(@Param(value = "userId") String userId);

    @Select("SELECT user_id FROM sys_user where del_flag = 0")
    List<Integer> findAllUserIds();
}
