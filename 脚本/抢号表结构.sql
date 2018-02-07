drop table qh_order;
CREATE TABLE IF NOT EXISTS qh_order(
  `qh_order_id` BIGINT(15) NOT NULL AUTO_INCREMENT COMMENT '抢号订单id',
  `prd_code` VARCHAR(20) NOT NULL COMMENT '产品编码',
  `us_id` BIGINT(15) NOT NULL COMMENT '用户ID',
  `pat_id` BIGINT(15) NOT NULL COMMENT '就诊人ID',
  `pat_name` VARCHAR(20) NOT NULL COMMENT '患者姓名',
  `pc_id` BIGINT(15) NOT NULL COMMENT '就诊人卡ID',
  `ACCESS_PAT_ID` VARCHAR(64) NOT NULL COMMENT 'HIS系统患者ID',
  `PAT_CARD_NO` VARCHAR(32) NOT NULL COMMENT 'HIS系统患者卡号',
  `pat_phone_no` VARCHAR(11) NULL,
  `doc_id` BIGINT(15) NOT NULL COMMENT '医生ID',
  `doc_name` VARCHAR(20) NOT NULL,
  `ACCESS_DOC_ID` VARCHAR(32) NOT NULL COMMENT 'HIS系统医生ID',
  `hos_id` BIGINT(15) NOT NULL COMMENT '医院ID',
  `hos_name` VARCHAR(32) NOT NULL COMMENT '医院名称',
  `DEPT_ID` BIGINT(15) NOT NULL,
  `dept_name` VARCHAR(45) NOT NULL COMMENT '医院门诊名称',
  `ACCESS_DEPT_ID` VARCHAR(32) NOT NULL COMMENT 'HIS系统门诊ID',
  `paid_mode` TINYINT(1) NOT NULL COMMENT '免密支付方式（1:支付宝、2:微信）',
  `create_time` DATETIME NOT NULL DEFAULT now() COMMENT '创建时间',
  `status` TINYINT(1) NOT NULL COMMENT '抢号状态：0:等待挂号、1:代挂进行中、2:代挂成功、3:代挂失败、4:已取消',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `remark` VARCHAR(45) NULL,
  PRIMARY KEY (`qh_order_id`),
  INDEX `idx_qh_order_usid` (`us_id` ASC))
ENGINE = InnoDB
COMMENT = '抢号订单表';

drop table qh_order_detail;
CREATE TABLE IF NOT EXISTS qh_order_detail (
  `qhd_id` BIGINT(15) NOT NULL AUTO_INCREMENT,
  `qh_order_id` BIGINT(15) NOT NULL COMMENT '抢号订单id',
  `status` TINYINT(1) NOT NULL COMMENT '抢号状态：0:等待客服挂号、1:客服代挂进行中、2:本次门诊客服代挂成功，请到挂号订单页查看详情、3:医院已取消本次门诊，不可代挂、4:医院已停诊本次门诊，不可代挂、5:本次门诊已满号,如医院后续放号(或用户退号)，将持续为您代挂号、6:已存在代挂成功门诊，后续订单自动失效',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `DAY_TYPE` TINYINT(2) NULL COMMENT '午别(0全天 1上午  12中午  2下午  4晚上)',
  `SCH_DATE` DATE NOT NULL COMMENT '排班日期',
  `SCH_LEVEL` VARCHAR(32) NULL COMMENT '排班级别',
  `REG_FEE` DECIMAL(9,2) NOT NULL COMMENT '挂号费用',
  `release_time` DATETIME NOT NULL COMMENT '放号时间',
  `REG_ID` BIGINT(15) NULL COMMENT '预约挂号ID(抢号成功后回写)',
  `remark` VARCHAR(45) NULL,
   PRIMARY KEY (`qhd_id`),
   INDEX `idx_qhod_orderid` (`qh_order_id` ASC),
   INDEX `idx_qhod_releasetime` (`release_time` ASC),
   INDEX `idx_qhod_regid` (`REG_ID` ASC))
ENGINE = InnoDB
COMMENT = '抢号订单详细信息表';
