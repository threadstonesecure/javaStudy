
alter table olt_drug_provider add column dp_type tinyint(1) default 1 comment '1:正常,2:虚拟药商(虚拟药品目录)';

alter table olt_dp_drug_catalogue add column rel_drug_id bigint(15)  comment '关联药品id';
