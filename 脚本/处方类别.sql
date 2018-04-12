
alter table olt_prescription_ol add column pscript_class char(1) comment '处方类别-> 1:普通、2:儿科、3:急诊、4:精二';
begin;
delete from  cfg_enum_dict where ENUM_TYPE='PRSCRPT_CLASS';
INSERT INTO cfg_enum_dict(ENUM_NAME,ENUM_TYPE,ENUM_VALUE,ENUM_TXT,IS_DELETED)
VALUES('处方类别',
       'PRSCRPT_CLASS',
       '1',
       '普通',
       'N'), ('处方类别',
              'PRSCRPT_CLASS',
              '2',
              '儿科',
              'N'), ('处方类别',
                     'PRSCRPT_CLASS',
                     '3',
                     '急诊',
                     'N'), ('处方类别',
                            'PRSCRPT_CLASS',
                            '4',
                            '精二',
                            'N');
commit;

alter table olt_opm_order add column PARENT_ORDER_NO bigint(20) comment '父订单订单号';
