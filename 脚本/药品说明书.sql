CREATE TABLE `olt_drug_direction` (
  `ODD_ID` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '药品说明书流水',
  `DRUG_CHEM_NAME` varchar(100) NOT NULL COMMENT '药品化学名',
  `DRUG_TRADE_NAME` varchar(100) DEFAULT NULL COMMENT '商品名',
  `DRUG_SPEC` varchar(50) DEFAULT '' COMMENT '规格',
  `DRUG_MANUFATURER` varchar(100) DEFAULT NULL COMMENT '生产厂家',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
   PRIMARY KEY (`ODD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='药品说明书';

CREATE TABLE `olt_drug_direction_item` (
   odd_id bigint(15) NOT NULL COMMENT '药品说明书流水',
   item_label varchar(20) COMMENT '字段label',
   item_value text      COMMENT '字段value',
   item_order tinyint   COMMENT '排序',
  UNIQUE KEY `UNIQUE_HOS_DOCTOR_DOC_ID32` (odd_id,item_label)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='药品说明书明细';
