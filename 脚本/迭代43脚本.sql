alter table olt_prescription_ol modify PS_TYPE int(2) comment '处方类型，0-处方,1-快速处方，2-模板, 3-药师下单';

alter table prp_cons_log modify column operation_type tinyint(2) not null comment '操作类型：0 创建，1 修改，2 提交,3 查看会诊单,4 同意会诊请求，5 拒绝会诊请求，6 填写会诊意见，7 出具会诊报告, 8 超时,9 邀请其他专家';
alter table prp_cons_log add column invited_doc_id bigint(15) comment '被邀请医生';
alter table prp_cons_log add column invited_doc_name  varchar(20) comment '被邀请医生姓名';

-- 处方单保存点
create table olt_prescription_save_point(
    opsp_id bigint(15) NOT NULL AUTO_INCREMENT COMMENT '处方单保存点Id',
    pscript_id bigint(15) DEFAULT NULL COMMENT '处方单ID',
    pscript_content text not null comment '处方单内容，json',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (opsp_id),
    key idx_pscript_id (pscript_id)
);

-- olt_prescription_review_log
-- 增加修改处方记录
alter table olt_prescription_review_log modify column `pharmacist_id` bigint(15) NULL COMMENT '审核药师ID';
alter table olt_prescription_review_log modify column `pharmacist_name` varchar(30) NULL DEFAULT '' COMMENT '审核药师名称';
alter table olt_prescription_review_log modify column `review_result` tinyint(2)  NULL COMMENT '审核结果: 1-不通过,2-审核通过,3-系统自动通过,4-双签通过,5-自动跳过,6-医生修改处方';
alter table olt_prescription_review_log add column opsp_id bigint(15)  NULL  COMMENT '处方单保存点Id';
