package com.app.mdc.service.system;

import com.app.mdc.exception.BusinessException;
import com.baomidou.mybatisplus.service.IService;
import com.app.mdc.model.system.User;
import com.app.mdc.utils.viewbean.ResponseResult;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    /**
     * 获取用户的信息
     * @param id 用户id
     * @return
     */
	ResponseResult getOne(String id);

    /**
     * 新增
     * @param map   id   username password    name    telephone   position    remark  status  roleId  companyid   rank    code
     * @return
     */
    ResponseResult add(Map<String,Object> map);

    /**
     *  修改
     * @return
     */
    ResponseResult update(Map<String,Object> map);

    /**
     * 删除
     * @param id 用户id
     * @return
     */
    ResponseResult delete(String id);


    /**
     * 分页查询用户列表
     * @return User集合
     */
    List<User> findUserByPage(String name);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param httpSession httpsession，用来保存用户登录信息
     * @param loginType 登录类型，pc,app
     */
    Map<String, Object> doUserLogin(String username, String password, HttpSession httpSession, String loginType) throws BusinessException;

    /**
     * 根据用户token获取User；
     * @param userToken token
     * @return user
     */
    User findUserByToken(String userToken) throws BusinessException;

    /**
     * 修改密码
     * @param id    id
     * @param newPassword   新密码
     * @param oldPassword   老密码
     * @return  int
     * @throws BusinessException    抛出错误
     */
    Integer updatePwd(String id,String newPassword,String oldPassword) throws BusinessException;

    /**
     * 手机端通讯录
     * @param map   角色
     * @param userId   当前用户id
     * @return  用户list
     */
    List<Map<String,Object>> getAddressBook(Map<String,Object> map,String userId);

	/**
	 * 更新cid
	 * @param id	用户id
	 * @param cid	用户cid
	 * @return	0，500
	 */
    ResponseResult updateCid(String id,String cid);

	/**
	 * pc端管理员聊天，所有人
	 * @param userId 当前用户id
	 * @return
	 */
	List<Map<String,Object>> pcAddressBook(String userId);

    /**
     * 删除用户对应的token
     * @param userId
     */
    void removeTokenByUserId(Integer userId);
}
