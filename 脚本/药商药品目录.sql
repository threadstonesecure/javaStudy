CREATE TABLE `olt_dp_drug_catalogue` (
  `DRUG_ID` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '药品目录流水',
  `DRUG_CODE` varchar(20) NOT NULL COMMENT '云医院药品编码',
  `DRUG_CHEM_NAME` varchar(100) NOT NULL COMMENT '药品化学名',
  `DRUG_PINYIN` varchar(100) DEFAULT NULL COMMENT '药品拼音',
  `DRUG_WB` varchar(100) DEFAULT NULL COMMENT '药品五笔',
  `DRUG_TRADE_NAME` varchar(100) DEFAULT NULL COMMENT '商品名',
  `DRUG_SPEC` varchar(50) DEFAULT '' COMMENT '规格',
  `DRUG_DOSE` varchar(20) DEFAULT '' COMMENT '剂型',
  `DRUG_MANUFATURER` varchar(100) DEFAULT NULL COMMENT '生产厂家',
  `DRUG_LICENSE_NO` varchar(50) DEFAULT NULL COMMENT '国药准字',
  `DRUG_SOURCE_TYPE` tinyint(1) DEFAULT NULL COMMENT '药品目录来源1华润，2国药',
  `DRUG_PACKING_UNIT` varchar(20) DEFAULT NULL COMMENT '药品包装单位',
  `DRUG_DOSAGE_UNIT` varchar(20) DEFAULT '' COMMENT '药品含量单位',
  `DRUG_DOSAGE_NUM` double DEFAULT NULL COMMENT '药品含量',
  `DRUG_APPERANCE_NUM` double DEFAULT NULL COMMENT '药品形状数量',
  `DRUG_APPERANCE_UNIT` varchar(255) DEFAULT NULL COMMENT '药品形状单位',
  `DRUG_PHOTO` text COMMENT '药品图片',
  `DRUG_TRADE_CODE` varchar(255) DEFAULT NULL COMMENT '药品交易平台药品编码',
  `DRUG_SPEC_TYPE` tinyint(1) DEFAULT NULL COMMENT '1-国产；2-进口；3-合资',
  `STATUS` tinyint(1) DEFAULT '1' COMMENT '药品状态(0：未启用，1：启用)',
  `REMARK` varchar(255) DEFAULT NULL COMMENT '备注',
  `type` tinyint(1) DEFAULT '0' COMMENT '类型：0-药品，1-商品',
   dp_id bigint(15) NOT NULL comment '药商id',
   drug_price decimal(9,2) NULL comment '药品价格',
   drug_type_code varchar(50) NULL  comment '药品分类',
   dp_drug_code varchar(20) NULL comment '药商药品编号',
  PRIMARY KEY (`DRUG_ID`),
  UNIQUE KEY `udx_olt_dp_drug_catalogue_drugcode` (`DRUG_CODE`),
  key idx_olt_dp_drug_catalogue_dpid_typecode(dp_id,drug_type_code),
  KEY `idx_olt_dp_drug_catalogue_pinyin` (`DRUG_PINYIN`),
  KEY `idx_olt_dp_drug_catalogue_CHEM_NAME` (`DRUG_CHEM_NAME`),
  KEY `idx_olt_dp_drug_catalogue_wb` (`DRUG_WB`)
) ENGINE=InnoDB AUTO_INCREMENT=18992 DEFAULT CHARSET=utf8 COMMENT='药商药品目录表';

alter table olt_prescription_ol add column dp_id bigint(15)  NULL comment '药商id';

-- alter table olt_drug_provider add column parent_dp_id bigint(15) comment '药商父id';

-- 增加何贤药商，为国药的子药商
/*
insert into olt_drug_provider  (dp_name,parent_dp_id) values('何贤','3');
commit;
*/

-- 药商视图

create or replace view v_olt_drug_provider as
  select DP_ID,
         DP_NAME,
         LINKMAN,
         LINKMAN_PHONE,
         LEVEL,
         ADDRESS,
         CREATE_TIME,
         UPDATE_TIME,
         IS_DELETED,
         PROD_RSA_FILE_PATH,
         TEST_RSA_FILE_PATH,
         PROD_DOCKING_URL,
         TEST_DOCKING_URL,
         PARTNER_CODE,
         YUN_PARTNER_CODE,
         OWN_ADDRESS,
         api_type,
         express_type,
         delivery_scope,
         dp_info,
         parent_dp_id
      from olt_drug_provider
    where parent_dp_id is NULL
 union all
 select a.DP_ID,
        a.DP_NAME,
        b.LINKMAN,
        b.LINKMAN_PHONE,
        b.LEVEL,
        b.ADDRESS,
        b.CREATE_TIME,
        b.UPDATE_TIME,
        b.IS_DELETED,
        b.PROD_RSA_FILE_PATH,
        b.TEST_RSA_FILE_PATH,
        b.PROD_DOCKING_URL,
        b.TEST_DOCKING_URL,
        ifnull(a.PARTNER_CODE,b.PARTNER_CODE),
        b.YUN_PARTNER_CODE,
        b.OWN_ADDRESS,
        b.api_type,
        b.express_type,
        b.delivery_scope,
        b.dp_info,
        a.parent_dp_id
     from olt_drug_provider a, olt_drug_provider b
   where a.parent_dp_id = b.dp_id;

-- 生成康发药品数据
begin;
insert into olt_dp_drug_catalogue (
     DRUG_ID             ,
     DRUG_CODE           ,
     DRUG_CHEM_NAME      ,
     DRUG_PINYIN         ,
     DRUG_WB             ,
     DRUG_TRADE_NAME     ,
     DRUG_SPEC           ,
     DRUG_DOSE           ,
     DRUG_MANUFATURER    ,
     DRUG_LICENSE_NO     ,
     DRUG_SOURCE_TYPE    ,
     DRUG_PACKING_UNIT   ,
     DRUG_DOSAGE_UNIT    ,
     DRUG_DOSAGE_NUM     ,
     DRUG_APPERANCE_NUM  ,
     DRUG_APPERANCE_UNIT ,
     DRUG_PHOTO          ,
     DRUG_TRADE_CODE     ,
     DRUG_SPEC_TYPE      ,
     STATUS              ,
     REMARK              ,
     type                ,
     dp_id               ,
     drug_price          ,
     drug_type_code      ,
     dp_drug_code
)
select
    null                  ,
    a.DRUG_CODE           ,
    a.DRUG_CHEM_NAME      ,
    a.DRUG_PINYIN         ,
    a.DRUG_WB             ,
    a.DRUG_TRADE_NAME     ,
    a.DRUG_SPEC           ,
    a.DRUG_DOSE           ,
    a.DRUG_MANUFATURER    ,
    a.DRUG_LICENSE_NO     ,
    a.DRUG_SOURCE_TYPE    ,
    a.DRUG_PACKING_UNIT   ,
    a.DRUG_DOSAGE_UNIT    ,
    a.DRUG_DOSAGE_NUM     ,
    a.DRUG_APPERANCE_NUM  ,
    a.DRUG_APPERANCE_UNIT ,
    a.DRUG_PHOTO          ,
    a.DRUG_TRADE_CODE     ,
    a.DRUG_SPEC_TYPE      ,
    b.STATUS              ,
    a.REMARK              ,
    a.type                ,
    b.dp_id               ,
    b.drug_price          ,
    left(a.DRUG_CODE,6)   ,
    null
 from olt_drug_catalogue a,  olt_drug_catalogue_r_dp b
   where a.DRUG_CODE = b.DRUG_CODE
     and b.dp_id = -2
     and b.STATUS = 1;

update olt_drug_catalogue_r_dp set STATUS = -1
     where dp_id =  -2
       and STATUS = 1;
commit;
