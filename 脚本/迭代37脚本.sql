alter table olt_prescription_med_detail add column type tinyint(1) comment '类型：0-药品，1-商品';
alter table olt_prescription_med_detail add column dp_id bigint(15) comment '药商id';


begin;
update olt_prescription_med_detail dtl, olt_dp_drug_catalogue drug
     set dtl.type= drug.type, dtl.dp_id = drug.dp_id
     where dtl.MED_CODE = drug.drug_code
       and exists (select 1 from olt_prescription_ol a where a.PSCRIPT_ID =  dtl.PSCRIPT_ID and a.biz_type in (3,4,5));
commit;
