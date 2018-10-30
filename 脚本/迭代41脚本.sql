
alter table olt_drug_provider add column dp_type tinyint(1) default 1 comment '1:正常,2:虚拟药商(虚拟药品目录)';

alter table olt_dp_drug_catalogue add column rel_drug_code  varchar(20)  comment '关联药品编码';

alter table olt_dp_drug_catalogue add column lack_status tinyint(1) default 0  comment '缺货状态：0:有货；1:缺货';
