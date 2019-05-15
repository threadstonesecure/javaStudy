
alter table olt_drug_provider add column acc_pscript_hos_id varchar(50) comment '处方对接hosId。多个hosId使用,分隔';

-- 刷新购药订单数据
begin
  update olt_opm_order
     set pay_status = 0
      where pay_mode = 2 and p.delivery_status = 5;

  UPDATE olt_opm_order SET DELIVERY_STATUS = 0
    WHERE DELIVERY_STATUS IN (4,5);       

  update olt_opm_order
      set pay_mode =1 ,
          pay_method = 1
    where pay_mode = 2 ; 

  update olt_opm_order set pay_method = 0   where pay_mode = 0 and pay_method is null;
  
  update olt_opm_order set pay_method = 0   where pay_mode = 1 and pay_method is null;

end;

-- 药商后台新增加权限
-- select * from olt_opm_privilege
-- select * from hos_hospital hos limit 1;
insert into olt_opm_privilege(pv_code,pv_name,uri,create_time) values('SEND_DRUG_TO_HOS','发货到院','/80200/dp/662' ,now());
insert into olt_opm_privilege(pv_code,pv_name,uri,create_time) values('HOS_RECEIVED_ORDER','医院签收','/80200/hos/640' ,now()); 
insert into olt_opm_privilege(pv_code,pv_name,uri,create_time) values('HOS_SEND_DRUG_TO_USER','医院发药','/80200/hos/660' ,now()); 

 