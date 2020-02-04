package com.app.mdc.controller.system;

import com.app.mdc.annotation.anno.SystemLogAnno;
import com.app.mdc.controller.BaseController;
import com.app.mdc.enums.ApiErrEnum;
import com.app.mdc.exception.BusinessException;
import com.app.mdc.model.system.User;
import com.app.mdc.service.system.UserService;
import com.app.mdc.utils.viewbean.Page;
import com.app.mdc.utils.viewbean.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 用户管理controller
 */
@Controller
	@RequestMapping("/admin/users")
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查找所有的user并分页
     *
     * @param page 分页插件
     * @param name 用户姓名
     * @return 所有user的分页list
     */
    @RequestMapping("/selectPage")
    @ResponseBody
    public ResponseResult selectPage(Page page, String name) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<User> userList = userService.findUserByPage(name);
        return ResponseResult.success().add(new PageInfo<>(userList));
    }

    /**
     * 获取用户的信息
     * @param id 用户id
     * @return 某一个具体的用户
     */
    @RequestMapping("/getOne")
    @ResponseBody
    public ResponseResult getOne(String id) {
        return userService.getOne(id);
    }

    /**
     * 新增
     * @param map   id   username password    name    telephone   position    remark  status  roleId  companyid   rank    code
     * @return 返回的结果，checkTable
     */
    @PostMapping("/add")
    @SystemLogAnno(module = "用户管理", operation = "新增用户")
    @ResponseBody
    public ResponseResult add(@RequestParam Map<String,Object> map) {
        return userService.add(map);
    }

    /**
     * 修改
     * @param map   id   username password    name    telephone   position    remark  status  roleId  companyid   rank    code
     * @return 返回的结果，checkTable
     */
    @RequestMapping("/update")
	@SystemLogAnno(module = "用户管理", operation = "修改用户信息")
    @ResponseBody
    public ResponseResult update(@RequestParam Map<String,Object> map) {
        return userService.update(map);
    }

    /**
     * 删除
     * @param id 用户id
     * @return 返回的结果，checkTable
     */
    @RequestMapping("/delete")
	@SystemLogAnno(module = "用户管理", operation = "删除用户")
    @ResponseBody
    public ResponseResult delete(String id) {
        return userService.delete(id);
    }

    /**
     * 修改密码
     * @param id          id
     * @param newPassword 新密码
     * @param oldPassword 老密码
     * @throws BusinessException 抛出错误
     */
    @RequestMapping("/updatePwd")
	@SystemLogAnno(module = "用户管理", operation = "app端修改用户密码")
    @ResponseBody
    public ResponseResult updatePwd(@RequestParam String id,
                                    @RequestParam String newPassword,
                                    @RequestParam String oldPassword) {
        ResponseResult responseResult = new ResponseResult();
        try {
            userService.updatePwd(id,newPassword,oldPassword);
        } catch (BusinessException e) {
            responseResult.setErrMsg(ApiErrEnum.ERR500.toString(), e.getMessage());
        }
        return responseResult;
    }

	/**
	 * 手机端通讯录
	 * @param map   角色
	 * @return  用户list
	 */
	@RequestMapping("/getAddressBook")
	@ResponseBody
	public ResponseResult getAddressBook(@RequestParam Map<String,Object> map, HttpSession httpSession,Page page){
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
    	String userId=currentUser(httpSession).getId();
    	List<Map<String,Object>> mapList=userService.getAddressBook(map,userId);
    	return ResponseResult.success().add(new PageInfo<>(mapList));
	}

	/**
	 * 更新cid
	 * @param id	用户id
	 * @param cid	用户cid
	 * @return	0，500
	 */
	@RequestMapping("/updateCid")
	@SystemLogAnno(module = "用户管理", operation = "app端修改用户手机cid")
	@ResponseBody
	public ResponseResult updateCid(@RequestParam String id,@RequestParam String cid){
		return userService.updateCid(id,cid);
	}

	/**
	 * pc查找所有的user
	 * @return 所有userlist
	 */
	@RequestMapping("/pcAddressBook")
	@ResponseBody
	public ResponseResult pcAddressBook(HttpSession httpSession) {
		List<Map<String,Object>> mapList=userService.pcAddressBook(currentUser(httpSession).getId());
		return ResponseResult.success().add(mapList);
	}
}
