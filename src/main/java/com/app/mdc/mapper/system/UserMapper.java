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

    @Select("SELECT * FROM sys_users")
    List<Map> getUserList();
    
    @Select("select count(id) from sys_users where username=#{0}")
    Integer user(String username);

    @Select("select count(id) from sys_users where name=#{0}")
    Integer isRepeat(String name);

    List<Map<String,Object>> getOperaterBook(@Param(value = "userId") String userId);

    List<Map<String,Object>> getCompanyUserBook(@Param(value = "userId") String userId);

    List<Map<String,Object>> getPcAddressBook(@Param(value = "userId") String userId);
}
