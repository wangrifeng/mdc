#2019-08-05
alter table tb_replacement_apply
  add equipment_id varchar(36)  comment '设备id';

alter table tb_replacement_record
  add equipment_id varchar(36)  comment '设备id';
#############################以发布

#2019-08-05
create table sys_api_log
(
  id          varchar(36)      not null
    primary key,
  createtime  datetime         null,
  ip          varchar(28)      null comment 'ip',
  request_url    text   null comment '请求链接',
  request_type    varchar(28)   null comment '请求类型',
  remarks     varchar(128)     null comment '请求说明',
  status      int(4)       null comment '状态',
  updatetime  datetime         null,
  username    varchar(16)      null comment '用户名',
  deleted     int(2) default 0 null comment '是否删除',
  request_tx  text             null comment 'request请求参数',
  response_tx text             null
)
  charset = utf8;

#2019-08-06
-- auto-generated definition
create table sys_quartz
(
  job_id          varchar(36)      not null
    primary key,
  create_time      datetime   null comment '创建时间',
  update_time  datetime         null comment '修改时间',
  job_name          varchar(28)      null comment '任务名称',
  job_group      varchar(28)      null comment '任务分组',
  job_status   varchar(4)      null comment '任务状态 是否启动任务',
  cron_expression     varchar(64)     null comment 'cron表达式',
  description      varchar(255)       null comment '描述',
  bean_class  varchar(28)       null comment '调用类',
  is_concurrent    varchar(4)    null comment '任务是否有状态',
  spring_id     varchar(28)  null comment 'spring bean',
  method_name  varchar(28)             null comment '调用方法',
  params text             null comment '调用参数'
)
  charset = utf8;
INSERT INTO mdc.sys_quartz (job_id, create_time, update_time, job_name, job_group, job_status, cron_expression, description, bean_class, is_concurrent, spring_id, method_name, params) VALUES ('1274fad9-861e-4263-8118-06b336751c46', null, null, null, null, null, '0 0 1 * * ?', null, 'devopsService', '0', null, 'checkDevops', '');
INSERT INTO mdc.sys_quartz (job_id, create_time, update_time, job_name, job_group, job_status, cron_expression, description, bean_class, is_concurrent, spring_id, method_name, params) VALUES ('95cb3b07-048e-47d8-8c08-602e05b3ab57', null, null, null, null, null, '0 0 1 ? * MON', null, 'taskService', '0', null, 'quartzTask', 'quartz');
INSERT INTO mdc.sys_quartz (job_id, create_time, update_time, job_name, job_group, job_status, cron_expression, description, bean_class, is_concurrent, spring_id, method_name, params) VALUES ('05b4c68d-9ce4-4f65-9c63-e94e8fe6122a', null, null, null, null, null, '0 0 2 * * ?', null, 'monitorEquipmentService', '0', null, 'updateStandard', '');
#############################已发布

#20190814
ALTER TABLE socket_message ADD readstatus int DEFAULT 0 NULL comment '是否已读';

CREATE TABLE socket_team_message_user
(
  user_id varchar(36) COMMENT '用户id',
  message_id varchar(36) COMMENT '消息Id',
  team_id varchar(36) COMMENT '群聊消息id'
);
ALTER TABLE socket_team_message_user COMMENT = '群聊消息用户已读未读中间表';

ALTER TABLE socket_message CHANGE send_user_id receive_user_id varchar(36) COMMENT '传送的用户id，如果type=2，该字段为空';
ALTER TABLE socket_message CHANGE user_id send_user_id varchar(36) COMMENT '发送用户的id';

#20190816
CREATE TABLE `mdc`.`sys_app_version_log`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `version_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `version_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本信息',
  `updatetime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

#20190821
ALTER TABLE tb_monitor_equipment add column company_name varchar(256) COMMENT '公司名称';
ALTER TABLE tb_monitor_equipment add column company_id varchar(36) COMMENT '公司id';
#############################已发布
#20190827
ALTER TABLE sys_app_version_log add column createtime datetime COMMENT '创建时间';
ALTER TABLE sys_app_version_log add column status int(4) COMMENT '状态';
ALTER TABLE sys_app_version_log add column deleted int(4) COMMENT '是否删除';

ALTER TABLE tb_devops add column description text COMMENT '描述和反馈';
#############################已发布

#20191213
create table tb_task_cancel
(
  id                 varchar(36)      not null comment 'uuid'
    primary key,
  task_id            varchar(36)      not null comment '撤销任务id',
  createtime         datetime         null comment '撤销时间',
  creator            varchar(36)         null comment '撤销人员'
)
  comment '撤销任务表';
ALTER TABLE tb_task_cancel add column cancel_reason text COMMENT '撤销原因';
#############################已发布

#20191219
-- auto-generated definition
create table tb_task_history
(
  id             varchar(36)      not null comment 'UUID'
    primary key,
  deleted        int(2) default 0 null comment '删除标识',
  status         varchar(4)       null,
  create_time    datetime         null comment '创建时间',
  update_time    datetime         null comment '修改时间',
  today_finish   text             null comment '今日完成内容',
  today_unfinish text             null comment '今日未完成内容',
  need_help      text             null comment '需要协调的工作',
  remark         text             null comment '备注',
  task_id        varchar(36)      null comment '任务id',
  report_user    varchar(36)      null comment '记录人员',
  sign_address       varchar(255) null comment '签到地址',
  longitude_latitude varchar(124) null comment '签到坐标'
)
  comment '任务记录';

alter table tb_task_history change create_time createtime datetime null comment '创建时间';
alter table tb_task_history change update_time updatetime datetime null comment '修改时间';

#20191231
alter table sys_question add column question_type varchar(100) comment '问题设备类型';
insert  into sys_dict(id,`status`,deleted,createtime,updatetime,`name`,pid,type,number,`desc`,rank) values ('a4819dc2880245e6a481206b85dde1fe','A',0,now(),now(),'其他','e65910c7464f42fe84861d7fed9b6531','','issueOther','',5);
insert  into sys_dict(id,`status`,deleted,createtime,updatetime,`name`,pid,type,number,`desc`,rank) values ('fe0f32f649b34a1d8103e1d27b4ee690','A',0,now(),now(),'水在线监测','e65910c7464f42fe84861d7fed9b6531','','issueWater','',4);
insert  into sys_dict(id,`status`,deleted,createtime,updatetime,`name`,pid,type,number,`desc`,rank) values ('92fffa76934242de83dd067e41a63b56','A',0,now(),now(),'总量柜','e65910c7464f42fe84861d7fed9b6531','','issueTotal','',3);
insert  into sys_dict(id,`status`,deleted,createtime,updatetime,`name`,pid,type,number,`desc`,rank) values ('bf03c9fad18e4c3a8cfb6918b4f2be8e','A',0,now(),now(),'数采仪','e65910c7464f42fe84861d7fed9b6531','','issueMn','',2);
insert  into sys_dict(id,`status`,deleted,createtime,updatetime,`name`,pid,type,number,`desc`,rank) values ('845fac1bed0c452ca19b61aad4bcabff','A',0,now(),now(),'VOC','e65910c7464f42fe84861d7fed9b6531','','issueVOC','',1);
insert  into sys_dict(id,`status`,deleted,createtime,updatetime,`name`,pid,type,number,`desc`,rank) values ('e65910c7464f42fe84861d7fed9b6531','A',0,now(),now(),'常见问题设备类型','e63083e3e07746259a1c471bb2942b1c','','issueEqipmentType','',0);

#20200102
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('5ba5ec57b7e242ada46e25df6ae770a2', 'A', 0, '2019-12-31 09:27:23', '2019-12-31 09:27:23', '记录时间', '6645ec3a7f4c432bacebfa1e11f9897a', NULL, 'exportHistoryTime', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('5b49a1fcec7f44d996fd10f2cb698648', 'A', 0, '2019-12-31 09:27:09', '2019-12-31 09:27:09', '备注', '6645ec3a7f4c432bacebfa1e11f9897a', NULL, 'exportRemark', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('43ecea1689194782a6254e776c18c246', 'A', 0, '2019-12-31 09:26:58', '2019-12-31 09:26:58', '需要协调', '6645ec3a7f4c432bacebfa1e11f9897a', NULL, 'exportNeedHelp', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('924f71fd5b7d49328e26f92d377fa46d', 'A', 0, '2019-12-31 09:26:48', '2019-12-31 09:26:48', '今日未完成', '6645ec3a7f4c432bacebfa1e11f9897a', NULL, 'exportTodayUnfinish', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('f6729ed7dbdf4271b60666493281d5aa', 'A', 0, '2019-12-31 09:26:37', '2019-12-31 09:26:37', '今日已完成', '6645ec3a7f4c432bacebfa1e11f9897a', NULL, 'exportTodayFinish', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('44eadadba7a748a8922fa1a372f24eae', 'A', 0, '2019-12-31 09:26:30', '2019-12-31 09:27:36', '填报人员', '6645ec3a7f4c432bacebfa1e11f9897a', NULL, 'exportReportUser', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('e29c7dfd114f4ae1b609f13f65a71fb7', 'A', 0, '2019-12-31 09:26:18', '2019-12-31 09:26:18', '更新时间', 'b2dd808ade7146efbadbda7d40405d1a', NULL, 'exportUpdatetime', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('2baff8fc138d4e91ab4b6debcd7bd0aa', 'A', 0, '2019-12-31 09:26:02', '2019-12-31 09:26:02', '结束时间', 'b2dd808ade7146efbadbda7d40405d1a', NULL, 'exportFinishTime', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('8a87199bcde5420d986a47ff6d03f81f', 'A', 0, '2019-12-31 09:25:43', '2019-12-31 09:25:43', '开始时间', 'b2dd808ade7146efbadbda7d40405d1a', NULL, 'exportStartTime', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('3b69158c71824fd69a3940fc5095fee5', 'A', 0, '2019-12-31 09:25:29', '2019-12-31 09:25:29', '任务类型', 'b2dd808ade7146efbadbda7d40405d1a', NULL, 'exportCategory', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('6a9b063e47fc407e92767be147234d2a', 'A', 0, '2019-12-31 09:25:13', '2019-12-31 09:25:13', '任务状态', 'b2dd808ade7146efbadbda7d40405d1a', NULL, 'exportTaskStatus', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('415305d5f14241048d0de2d273cc6d86', 'A', 0, '2019-12-31 09:24:55', '2019-12-31 09:24:55', '运维人员', 'b2dd808ade7146efbadbda7d40405d1a', NULL, 'exportExecutor', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('72d8c0f86135412582899f0404b26c90', 'A', 0, '2019-12-31 09:24:36', '2019-12-31 09:24:36', '企业名称', 'b2dd808ade7146efbadbda7d40405d1a', NULL, 'exportCompany', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('49999448a8d5443b9a0c9d31ab9e8e79', 'A', 0, '2019-12-31 09:24:07', '2019-12-31 09:24:07', '任务名称', 'b2dd808ade7146efbadbda7d40405d1a', NULL, 'exportTaskName', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('6645ec3a7f4c432bacebfa1e11f9897a', 'A', 0, '2019-12-31 09:23:17', '2019-12-31 09:23:17', '任务记录导出字段', 'e63083e3e07746259a1c471bb2942b1c', NULL, 'taskHistoryExportColumn', NULL, NULL);
INSERT INTO `sys_dict`(`id`, `status`, `deleted`, `createtime`, `updatetime`, `name`, `pid`, `type`, `number`, `desc`, `rank`) VALUES ('b2dd808ade7146efbadbda7d40405d1a', 'A', 0, '2019-12-31 09:22:46', '2019-12-31 09:22:46', '任务导出字段', 'e63083e3e07746259a1c471bb2942b1c', NULL, 'taskExportColumn', NULL, NULL);
#############################已发布

#20200110
alter table sys_quartz
  add prev_time datetime null comment '上次触发时间';

alter table sys_quartz
  add next_time datetime null comment '下次触发时间';