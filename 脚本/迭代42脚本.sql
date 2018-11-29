-- 处方购药-医生开处方提醒 模版
-- 【广东云医院】{XXX}您好，{XX医院XX医生}已为您开单，请及时点击http://yuntai.com/123查看。为保证信息安全，请不要将短信内容轻易转发他人。如有疑问请致电4008933900
update sms_template set TEMPLATE = '${appName!}】${patName!}您好，${hosName!}${docName!}医生已为您${psName!}，请及时点击${buyDrugUrl!}查看。为保证信息安全，请不要将短信内容轻易转发他人。如有疑问请致电4008933900。'
   where sms_type = 103;

-- cfg_prd_param  -- 购药url配置
begin;
set @buyDrugUrl = 'https://m.hsyuntai.com/e/310000/prescription_qrorder?pscriptIds=[%s]';

delete from cfg_prd_param
     where prd_type= 'DOC' and prd_code='DCT-000001' and param_code='PAT_SMS_BUY_DRUG_URL';

insert into cfg_prd_param(param_id, prd_type,prd_code,param_code,param_name,param_value,is_deleted)
  values(uuid(),'DOC','DCT-000001','PAT_SMS_BUY_DRUG_URL','短信购药连接',@buyDrugUrl,'N');
commit;




-- prp_cons_tkt_request  会诊请求
--增加邀请医生id
alter table  prp_cons_tkt_request add column invite_doc_id bigint(15) comment '邀请医生id';
alter table  prp_cons_tkt_request add column invite_desc varchar(500) comment '邀请说明';

alter table prp_cons_tkt_request add key idx_ct_id(ct_id);
