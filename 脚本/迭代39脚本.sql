create table  olt_dp_lack_drug (
   id bigint(15) NOT NULL AUTO_INCREMENT COMMENT 'pk',
   drug_name varchar(100) not null COMMENT '药品名称',
   drug_manufaturer varchar(100) not null COMMENT '生产厂家',
   daily_use_num    varchar(100) not null comment '预计日均处方量',
   create_time datetime not null DEFAULT now() comment '创建时间',
   doc_id bigint(15) NOT NULL,
   doc_name  VARCHAR(20) comment '医生科室',
   hos_id bigint(15),
   hos_name varchar(50) DEFAULT NULL COMMENT '医院名称',
   sect_name varchar(50) comment '医生科室',
   dp_ids varchar(50) comment '医生对应药商id列表',
   PRIMARY KEY (id)
)ENGINE=InnoDB comment = '缺药登记表';
