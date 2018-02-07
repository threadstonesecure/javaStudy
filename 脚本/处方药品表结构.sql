
/**
  处方单表变动
*/
alter table olt_prescription_ol add column  DCT_APP_CLIENT_ID varchar(30) comment '医生APP_client_id';
alter table olt_prescription_ol add column  BATCH_NO   bigint(15) comment '开单批次号';

create index idx_access_visit_id on olt_prescription_ol
(
   ACCESS_VISIT_ID
);


/**
  处方单药品明细表变动
*/
alter table olt_prescription_med_detail  add column  MIN_PACK_NUM   int(3) comment '最小包装数量';

alter table olt_prescription_med_detail  add column  REMARK varchar(255) comment '备注';



drop table if exists olt_drug_frequency;

/*==============================================================*/
/* Table: olt_drug_frequency                                    */
/*==============================================================*/
create table olt_drug_frequency
(
   DF_CODE              varchar(20) not null comment '编码',
   DF_NAME              varchar(40) not null comment '名称',
   DF_EXECUTE_NUM       tinyint(2) not null comment '执行次数',
   DF_EXECUTE_TIME      varchar(50) comment '执行时间',
   DF_DAY_NUM           tinyint(2) not null comment '天数',
   STATUS               tinyint(1) not null default 1 comment '状态(0：未启用，1：启用)',
   REMARK               varchar(255) comment '备注',
   primary key (DF_CODE)
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8;

alter table olt_drug_frequency comment ' 药品服用频次表';



drop index idx_olt_drug_catalogue_wb on olt_drug_catalogue;

drop index idx_olt_drug_catalogue_CHEM_NAME on olt_drug_catalogue;

drop index idx_olt_drug_catalogue_pinyin on olt_drug_catalogue;

drop index udx_olt_drug_catalogue_code on olt_drug_catalogue;

drop table if exists olt_drug_catalogue;

/*==============================================================*/
/* Table: olt_drug_catalogue                                    */
/*==============================================================*/
create table olt_drug_catalogue
(
   DRUG_ID              bigint(15) not null auto_increment comment '药品目录流水',
   DRUG_CODE            varchar(20) not null comment '云医院药品编码',
   DRUG_CHEM_NAME       varchar(100) not null comment '药品化学名',
   DRUG_PINYIN          varchar(100) not null comment '药品拼音',
   DRUG_WB              varchar(100) comment '药品五笔',
   DRUG_TRADE_NAME      varchar(100) comment '商品名',
   DRUG_SPEC            varchar(50) not null comment '规格',
   DRUG_DOSE            varchar(20) not null comment '剂型',
   DRUG_MANUFATURER     varchar(100) comment '生产厂家',
   DRUG_LICENSE_NO      varchar(50) comment '国药准字',
   DRUG_SOURCE_TYPE     tinyint(1) comment '药品目录来源1华润，2国药',
   DRUG_PACKING_UNIT    varchar(20) comment '药品包装单位',
   DRUG_DOSAGE_UNIT     varchar(20) not null comment '药品含量单位',
   DRUG_DOSAGE_NUM      double not null comment '药品含量',
   DRUG_APPERANCE_NUM   double comment '药品形状数量',
   DRUG_APPERANCE_UNIT  varchar(255) comment '药品形状单位',
   DRUG_PHOTO           text comment '药品图片',
   DRUG_TRADE_CODE      varchar(255) comment '药品交易平台药品编码',
   DRUG_SPEC_TYPE       tinyint(1) comment '1-国产；2-进口；3-合资',
   STATUS               tinyint(1) default 1 comment '状态(0：未启用，1：启用)',
   REMARK               varchar(255) comment '备注',
   primary key (DRUG_ID)
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8;
alter table olt_drug_catalogue comment '药品目录表';

/*==============================================================*/
/* Index: udx_olt_drug_catalogue_code                           */
/*==============================================================*/
create unique index udx_olt_drug_catalogue_code on olt_drug_catalogue
(
   DRUG_CODE
);

/*==============================================================*/
/* Index: idx_olt_drug_catalogue_pinyin                         */
/*==============================================================*/
create index idx_olt_drug_catalogue_pinyin on olt_drug_catalogue
(
   DRUG_PINYIN
);

/*==============================================================*/
/* Index: idx_olt_drug_catalogue_CHEM_NAME                      */
/*==============================================================*/
create index idx_olt_drug_catalogue_CHEM_NAME on olt_drug_catalogue
(
   DRUG_CHEM_NAME
);

/*==============================================================*/
/* Index: idx_olt_drug_catalogue_wb                             */
/*==============================================================*/
create index idx_olt_drug_catalogue_wb on olt_drug_catalogue
(
   DRUG_WB
);



drop index idx_icd_disease_docid on icd_disease;

drop index idx_icd_disease_wb on icd_disease;

drop index idx_icd_disease_pinyin on icd_disease;

drop index idx_icd_disease_name on icd_disease;

drop table if exists icd_disease;

/*==============================================================*/
/* Table: icd_disease                                           */
/*==============================================================*/
create table icd_disease
(
   ICD_CODE             varchar(32) not null comment 'ICD-10编码',
   ICD_NAME             varchar(255) not null comment 'ICD疾病名称',
   ICD_PINYIN           varchar(50) comment 'ICD首字母',
   ICD_WB               varchar(20) comment 'ICD五笔',
   STATUS               tinyint(1) not null default 1 comment '状态(0：未启用，1：启用)',
   DISEASE_TYPE         tinyint(1) comment '疾病类型(0:普通疾病,1:常见疾病)',
   DOC_ID               bigint(15) comment '医生ID',
   primary key (ICD_CODE)
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8;

alter table icd_disease comment 'ICD疾病表';

/*==============================================================*/
/* Index: idx_icd_disease_name                                  */
/*==============================================================*/
create index idx_icd_disease_name on icd_disease
(
   ICD_NAME
);

/*==============================================================*/
/* Index: idx_icd_disease_pinyin                                */
/*==============================================================*/
create index idx_icd_disease_pinyin on icd_disease
(
   ICD_PINYIN
);

/*==============================================================*/
/* Index: idx_icd_disease_wb                                    */
/*==============================================================*/
create index idx_icd_disease_wb on icd_disease
(
   ICD_WB
);

/*==============================================================*/
/* Index: idx_icd_disease_docid                                 */
/*==============================================================*/
create index idx_icd_disease_docid on icd_disease
(
   DOC_ID
);



drop index idx_olt_drug_catalogue_r_hos_2 on olt_drug_catalogue_r_hos;

drop index idu_olt_drug_catalogue_r_hos_1 on olt_drug_catalogue_r_hos;

drop table if exists olt_drug_catalogue_r_hos;

/*==============================================================*/
/* Table: olt_drug_catalogue_r_hos                              */
/*==============================================================*/
create table olt_drug_catalogue_r_hos
(
   DRUG_CODE            varchar(20) not null comment '云医院药品编码',
   HOS_ID               bigint(15) not null comment '医院ID',
   HOS_DRUG_CODE        varchar(60) not null comment 'his药品目录',
   CREATE_TIME          datetime not null comment '创建时间',
   UPDATE_TIME          datetime not null comment '更新时间'
);

alter table olt_drug_catalogue_r_hos comment '云医院药品目录与医院映射关系表';

/*==============================================================*/
/* Index: idu_olt_drug_catalogue_r_hos_1                        */
/*==============================================================*/
create unique index idu_olt_drug_catalogue_r_hos_1 on olt_drug_catalogue_r_hos
(
   DRUG_CODE,
   HOS_ID,
   HOS_DRUG_CODE
);

/*==============================================================*/
/* Index: idx_olt_drug_catalogue_r_hos_2                        */
/*==============================================================*/
create index idx_olt_drug_catalogue_r_hos_2 on olt_drug_catalogue_r_hos
(
   HOS_ID,
   HOS_DRUG_CODE
);



drop table if exists olt_drug_provider_r_hos;

/*==============================================================*/
/* Table: olt_drug_provider_r_hos                               */
/*==============================================================*/
create table olt_drug_provider_r_hos
(
   DPH_ID               bigint(15) not null auto_increment comment '关系ID',
   HOS_ID               bigint(15) not null comment '医院ID',
   DP_ID                bigint(15) not null comment '药商ID',
   STATUS               tinyint(1) default 1 comment '状态(0：未启用，1：启用)',
   CREATE_TIME          datetime not null comment '创建时间',
   UPDATE_TIME          datetime not null comment '更新时间',
   primary key (DPH_ID),
   key IDX_DPH (HOS_ID, DP_ID)
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8;

alter table olt_drug_provider_r_hos comment '医院药商关系表';


drop table if exists olt_opm_log;

CREATE TABLE olt_opm_log (
  `LOG_ID` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '日志流水',
  `LOG_TYPE` int(2) DEFAULT NULL COMMENT '日志类型:1-匹配;2-计算快递费;3-确认订单;4-药商请求我方地址库信息;5-药商提交快递信息',
  `DP_ID` bigint(15) DEFAULT NULL COMMENT '药商ID',
  `TRANSACTION_ID` varchar(30) DEFAULT NULL COMMENT '接口请求流水',
  `ORDER_NO` bigint(15) DEFAULT NULL COMMENT '当logType in (1,2)处方单号;logType in (3,5)订单号',
  `REQ_MSG` text COMMENT '请求信息',
  `RES_MSG` text COMMENT '返回信息',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`LOG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



alter table olt_drug_provider add (
   PROD_RSA_FILE_PATH VARCHAR(50) DEFAULT NULL COMMENT '生产环境药商公钥文件路径',
   TEST_RSA_FILE_PATH VARCHAR(50) DEFAULT NULL COMMENT '测试环境药商公钥文件路径',
   PROD_DOCKING_URL VARCHAR(100) DEFAULT NULL COMMENT '生产环境药商接口地址',
   TEST_DOCKING_URL VARCHAR(100) DEFAULT NULL COMMENT '测试环境药商接口地址',
   PARTNER_CODE VARCHAR(32) DEFAULT NULL COMMENT '药商合作编号',
   YUN_PARTNER_CODE VARCHAR(32) DEFAULT NULL COMMENT '药商给云医院的合作编号',
   OWN_ADDRESS tinyint(1) DEFAULT 0 COMMENT '是否使用药商自己的地址库，1是0非'
);

