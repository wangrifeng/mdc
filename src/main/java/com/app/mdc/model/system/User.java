package com.app.mdc.model.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Map;

@TableName("sys_users")
public class User {

	@TableId(value = "id", type = IdType.UUID)
	private String id;

	private String status;

	private Integer deleted;

	private Date createtime;

	private Date updatetime;
	private String username;//用户名
	private String password;//密码
	private String name;//姓名
	private String telephone;//联系方式
	private String position;//职位
	private String remark;//备注
	private String companyid;//公司id
	private Integer rank;//排序
	private String code;//用户编号

	@TableField("district_id")
	private String districtId;//行政区id

	private String cid;//人员手机id


	public String getUsername() {
		return username;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getName() {
		return name;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	public String getId() {
		return id;
	}

	public User setId(String id) {
		this.id = id;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public User setStatus(String status) {
		this.status = status;
		return this;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public User setCreatetime(Date createtime) {
		this.createtime = createtime;
		return this;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public User setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
		return this;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", status='" + status + '\'' +
				", deleted=" + deleted +
				", createtime=" + createtime +
				", updatetime=" + updatetime +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", name='" + name + '\'' +
				", telephone='" + telephone + '\'' +
				", position='" + position + '\'' +
				", remark='" + remark + '\'' +
				", companyid='" + companyid + '\'' +
				", rank=" + rank +
				", code='" + code + '\'' +
				", districtId='" + districtId + '\'' +
				", cid='" + cid + '\'' +
				'}';
	}

	public void fromMap(Map<String, Object> map) {
		if (map.get("id") != null) {
			this.id = (String) map.get("id");
		}
		if (map.get("username") != null) {
			this.username = (String) map.get("username");
		}
		if (map.get("password") != null) {
			this.password = (String) map.get("password");
		}
		if (map.get("name") != null) {
			this.name = (String) map.get("name");
		}
		if (map.get("telephone") != null) {
			this.telephone = (String) map.get("telephone");
		}
		if (map.get("position") != null) {
			this.position = (String) map.get("position");
		}
		if (map.get("remark") != null) {
			this.remark = (String) map.get("remark");
		}
		if (map.get("companyid") != null) {
			this.companyid = (String) map.get("companyid");
		}
		if (map.get("rank") != null) {
			this.rank = Integer.parseInt((String) map.get("rank"));
		}
		if (map.get("code") != null) {
			this.code = (String) map.get("code");
		}
		if (map.get("districtId") != null) {
			this.districtId = (String) map.get("districtId");
		}
		if (map.get("cid") != null) {
			this.cid = (String) map.get("cid");
		}
	}

}
