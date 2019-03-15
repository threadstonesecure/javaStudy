create table olt_doc_visit_pat_record_dynamic(
  dvprd_id bigint(15) NOT NULL AUTO_INCREMENT COMMENT '药师随访记录动态id',
  dvpr_id bigint(15) NOT NULL COMMENT '药师随访记录id',
  item_key varchar(20) NOT NULL COMMENT '项目key',
  item_key_text varchar(50) NOT NULL COMMENT 'key的显示文本',
  item_value  varchar(1000) COMMENT '项目value',
  item_value_text varchar(2000) COMMENT 'value的显示文本',
  PRIMARY KEY (dvprd_id),
  UNIQUE KEY dvpt_id (dvpr_id,item_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='药师随访患者记录动态表';

insert into cfg_enum_dict( ENUM_NAME,ENUM_TYPE,ENUM_VALUE,ENUM_TXT,state)
   values('随访类型','PHA_DOC_VISIT_TYPE','4','萎缩性胃炎专用',1);
