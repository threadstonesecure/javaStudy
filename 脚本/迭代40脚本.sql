/*
药师随访患者任务表
*/
create table  olt_doc_visit_pat_task (
 task_id bigint(15) NOT NULL AUTO_INCREMENT COMMENT '任务id',
 `PSCRIPT_ID` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '处方ID', `ACC_BIZ_ID` varchar(64) DEFAULT NULL COMMENT '处方单ID',
 `RESULT` varchar(255) DEFAULT NULL COMMENT '诊断结果',
 `PAT_ID` bigint(15) DEFAULT NULL COMMENT '患者ID',
 `PAT_NAME` varchar(20) NOT NULL COMMENT '患者姓名',
 `pat_age_desc` varchar(4) DEFAULT NULL COMMENT '就诊人年龄文本,包含单位',
 `pat_sex` tinyint(1) DEFAULT NULL COMMENT '就诊人性别, 0-女,1-男',
 `HOS_ID` bigint(15) NOT NULL COMMENT '医院ID',
 `DEP_ID` bigint(15) DEFAULT NULL COMMENT '门诊id',
 `DEP_NAME` varchar(30) NOT NULL COMMENT '门诊名',
 `DOC_ID` bigint(15) DEFAULT NULL COMMENT '医生ID',
 `DOC_NAME` varchar(20) NOT NULL COMMENT '医生姓名',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status  tinyint(2) not null comment '0:未设置任务，1:有设置任务；2:任务结束'，
  visit_frequency int(2) null comment '随访频率',
  visit_type tinyint(2)  null comment '随访类型(0:默认随访单,1:结核病专用,3:糖尿病专用)',
  PRIMARY KEY (task_id),
  KEY  (doc_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '药师随访患者任务表';

/*
药师随访患者记录表

single_room tinyint(2) null comment '单独居室 0:无 ，1: 有',
airiness tinyint(2) null comment '通风情况 1:良好，2:一般，3:差',
per_day_smoke_num int(5) null comment '吸烟（支/天)',
per_day_drink_num int(5) null comment '饮酒（两/天',
visit_class tinyint(2) null comment '随访分类',
*/
create table  olt_doc_visit_pat_record (
   record_id bigint(15) NOT NULL AUTO_INCREMENT COMMENT '记录id',
   task_id bigint(15) NULL COMMENT '任务id',
   visit_type tinyint(2) default 0 null comment '随访类型(0:默认随访单,1:结核病专用,3:糖尿病专用)',
   status  tinyint(2) default 0 not null comment '0:未随访，1:已随访'，
   visit_date  date not null COMMENT '随访日期',
   `PAT_ID` bigint(15) DEFAULT NULL COMMENT '患者ID',
   `PAT_NAME` varchar(20) NOT NULL COMMENT '患者姓名',
   `pat_age_desc` varchar(4) DEFAULT NULL COMMENT '就诊人年龄文本,包含单位',
   `pat_sex` tinyint(1) DEFAULT NULL COMMENT '就诊人性别(0-女,1-男)',
   `HOS_ID` bigint(15) NOT NULL COMMENT '医院ID',
   `DOC_ID` bigint(15) DEFAULT NULL COMMENT '医生ID',
   `DOC_NAME` varchar(20) NOT NULL COMMENT '医生姓名',
    blood_low int(3) null comment '血压-舒张压(mmHg)',
    blood_high int(3) null comment '血压-收缩压(mmHg)',
    heart_rate int(3) null comment '心率(次/分钟)',
    symptom_signs varchar(50) null comment '症状及体征',
    symptom_sign_other varchar(255) null comment '其他症状及体征',
    medical_effect varchar(255) null comment '药物不良反应',
    visit_form tinyint(2) null comment '随访形式(1:文字，2:电话，3:视频)',
    compliance_action tinyint(2) null comment '遵医行为(1:良好，2:一般，3:差)',
    take_medicine tinyint(2) null comment '服药依从性(1:良好，2:一般，3:差)',
    understand_take_medicine null comment '是否了解药品服用方法(0:否 1:是)',
    visit_summary varchar(255) null comment '随访小结',
    other_info varchar(255) null comment '补充信息',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   PRIMARY KEY (record_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '药师随访患者记录表';
