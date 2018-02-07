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


update olt_drug_catalogue_r_dp set STATUS = 1
     where dp_id =  -2
       and STATUS = -1;
