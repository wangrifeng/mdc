package com.app.mdc.enums;

public enum ApiErrEnum {

    //校验失败err
    ERR100("请入参usertoken！"),
    ERR101("access failed！"),
    ERR201("该用户不存在，请先注册用户!"),
    ERR202("该用户已经是运维人员，无需在新增!"),
    ERR203("无运维人员该角色，请先添加运维人员角色!"),
    ERR301("该文件已被删除，请选择其他文件"),
    ERR302("该文件已不存在，请选择其他文件"),
    ERR500( "数据处理失败！"),
    ERR501("编码重复"),
    ERR502("参数错误"),
    ERR503("申请数量大于库存量"),
    ERR504("车辆已被借出"),
    ERR505("车牌重复"),
    ERR506("不存在该用户负责的公司"),
    ERR507("归还数量必须等于借出数量"),
    ERR508("请先选择物资"),
    ERR509("请勿重复申请归还"),
    ERR510("损坏数量不得小于0"),
    ERR511("未使用数量不得小于0"),


    ERR600("该用户名已注册，请确认"),
    ERR601("该字典编号已存在，请重新输入"),
    ERR602("该用户姓名已存在，请重新输入姓名以便区分"),
    ERR701("该用户未有企业绑定，无法进行异常上报!"),
    ERR702("该用户不是企业用户，无法进行异常上报!"),
    ERR801("任务添加失败，请选择日期在本周或者在本周之后进行任务添加!"),
    ERR802("任务添加失败，该人员在选择周已有任务!"),
    ERR803("该运维人员在选择时间段内已存在该公司任务，请选择其他时间分配任务!"),
    ERR804("当前时间无法撤销任务!"),
    ERR805("当前任务无法撤销,只能撤销待处理任务!"),
    ERR806("当前任务已完成,无需在填报!");

	



    private String desc;
    ApiErrEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public ApiErrEnum setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
