-- 购药提醒短信

--【广东云医院】您有一张处方还未支付，请及时点击http://yuntai.com/123查看。为保证信息安全，请不要将短信内容轻易转发他人。如有疑问请致电4008933900

insert into sms_template(template_name,sms_type,hos_id,template)
values('购药提醒短信','',null,'【${appName!}】您有一张处方还未支付，请及时点击${buyDrugUrl!}查看。为保证信息安全，请不要将短信内容轻易转发他人。如有疑问请致电4008933900');