package com.app.mdc.model.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@TableName("sys_user")
@Data
public class User {

	@TableId(value = "user_id", type = IdType.AUTO)
	private String id;

	@TableField("dept_id")
	private String deptId;

	@TableField("login_name")
	private String loginName;

	@TableField("user_name")
	private String userName;

	@TableField("user_type")
	private String userType;

	@TableField("email")
	private String email;

	@TableField("phonenumber")
	private String phoneNumber;

	@TableField("sex")
	private String sex;

	@TableField("avatar")
	private String avatar;

	@TableField("password")
	private String password;

	@TableField("salt")
	private String salt;

	@TableField("status")
	private String status;

	@TableField("del_flag")
	private Integer delFlag;

	@TableField("login_ip")
	private String loginIp;

	@TableField("login_date")
	private String loginDate;

	@TableField("create_by")
	private String createBy;

	@TableField("create_time")
	private Date createTime;

	@TableField("update_by")
	private String updateBy;

	@TableField("update_time")
	private Date updateTime;

	@TableField("remark")
	private String remark;

	@TableField("up_user_id")
	private String upUserId;

	@TableField("pay_password")
	private String payPassword;

	@TableField("level")
	private Integer level;
	@TableField("send_code")
	private int sendCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpUserId() {
		return upUserId;
	}

	public void setUpUserId(String upUserId) {
		this.upUserId = upUserId;
	}

	public int getSendCode() {
		return sendCode;
	}

	public void setSendCode(int sendCode) {
		this.sendCode = sendCode;
	}

	public void fromMap(Map<String, Object> map) {
		if (map.get("id") != null) {
			this.id = (String) map.get("id");
		}
		if (map.get("loginName") != null) {
			this.loginName = (String) map.get("loginName");
		}

		if (map.get("userName") != null) {
			this.userName = (String) map.get("userName");
		}

		if (map.get("password") != null) {
			this.password = (String) map.get("password");
		}
//		if (map.get("name") != null) {
//			this.name = (String) map.get("name");
//		}
//		if (map.get("telephone") != null) {
//			this.telephone = (String) map.get("telephone");
//		}
//		if (map.get("position") != null) {
//			this.position = (String) map.get("position");
//		}
		if (map.get("remark") != null) {
			this.remark = (String) map.get("remark");
		}
//		if (map.get("companyid") != null) {
//			this.companyid = (String) map.get("companyid");
//		}
//		if (map.get("rank") != null) {
//			this.rank = Integer.parseInt((String) map.get("rank"));
//		}
//		if (map.get("code") != null) {
//			this.code = (String) map.get("code");
//		}
//		if (map.get("districtId") != null) {
//			this.districtId = (String) map.get("districtId");
//		}
//		if (map.get("cid") != null) {
//			this.cid = (String) map.get("cid");
//		}
	}

}
