-- 随访计划
create table olt_doc_visit_pat_plan(
    dvpp_id bigint(15) NOT NULL AUTO_INCREMENT COMMENT '药师随访计划id',
    DOC_ID bigint(15) NOT NULL COMMENT '医生ID',
    DOC_NAME varchar(20) NOT NULL COMMENT '医生姓名',
    US_ID bigint(15) NOT NULL COMMENT '用户Id',
    PAT_ID bigint(15) NOT NULL COMMENT '患者ID',
    PAT_NAME varchar(20) NOT NULL COMMENT '患者姓名',
    pat_age_desc varchar(4)  COMMENT '就诊人年龄文本,包含单位',
    pat_sex tinyint(1) COMMENT '就诊人性别, 0-女,1-男',
    visit_frequency tinyint(2) not null comment '随访频率',
    visit_type tinyint(2) not null comment '随访类型(1:默认随访单,2:结核病专用,3:糖尿病专用)',
    visit_num int(5) not NULL COMMENT '随访次数',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status  tinyint(2) not null default 0 comment '0:结束,1:未结束',
    end_time datetime not null  COMMENT '结束时间',
    PRIMARY KEY (dvpp_id),
    key(doc_id,status),
    key(doc_id,pat_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '药师随访患者计划表';

-- alter table olt_doc_visit_pat_plan add US_ID bigint(15) NOT NULL COMMENT '用户Id';

-- 药师随访患者任务表
create table  olt_doc_visit_pat_task (
  dvpt_id bigint(15) NOT NULL AUTO_INCREMENT COMMENT '药师随访任务id',
  dvpp_id bigint(15) NOT NULL COMMENT '药师随访计划id',
  DOC_ID bigint(15) NOT NULL COMMENT '医生ID',
  PAT_ID bigint(15) NOT NULL COMMENT '患者ID',
  visit_date  date not null COMMENT '随访日期',
  PRIMARY KEY (dvpt_id),
  key(dvpp_id),
  key(DOC_ID,visit_date)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '药师随访患者任务表';

insert into cfg_enum_dict( ENUM_NAME,ENUM_TYPE,ENUM_VALUE,ENUM_TXT,state)
   values('随访类型','PHA_DOC_VISIT_TYPE','1','默认随访单',1),
        ('随访类型','PHA_DOC_VISIT_TYPE','2','结核病专用',0),
        ('随访类型','PHA_DOC_VISIT_TYPE','3','糖尿病专用',0);
commit;
