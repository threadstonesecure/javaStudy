-- @author denglt 
-- 创建药商账号

SET @username = '邓隆通';
SET @phone_no = '13825101877';  -- 15814589872
SET @dp_id = 17; --药商id
SET @hos_id = NULL; --医院id 
SET @_salt = 'denglt';
SET @_passwd = '123456';
SET @salt = md5(@_salt);
SET @passwd = md5(concat(sha2(@_passwd, 512),@salt));

INSERT INTO olt_opm_user (username, phone_no, passwd, salt, dp_id, hos_id, create_time, update_time) 
VALUES(@username,@phone_no,@passwd,@salt,@dp_id,@hos_id,now(),now()); 
 
/**
-- 药商基础权限
insert into olt_opm_privilege_r_user (ou_id,pv_id)
SELECT 
    (SELECT ou_id
    FROM olt_opm_user
    WHERE phone_no =@phone_no), pv_id
FROM olt_opm_privilege
WHERE pv_code IN ('LIST_DP_ORDER','GET_DP_ORDER','RECEIVED_DP_ORDER','REFUND_DP_ORDER','DISPATCH_DP_ORDER'); 

-- 医院基础权限
insert into olt_opm_privilege_r_user (ou_id,pv_id)
SELECT 
    (SELECT ou_id
    FROM olt_opm_user
    WHERE phone_no =@phone_no), pv_id
FROM olt_opm_privilege
WHERE pv_code IN ('LIST_DP_ORDER','GET_DP_ORDER','HOS_RECEIVED_ORDER','HOS_SEND_DRUG_TO_USER'); 
*/

-- 药商所有权限 (不包括医院签收、医院发药)
insert into olt_opm_privilege_r_user (ou_id,pv_id)
SELECT 
    (SELECT ou_id
    FROM olt_opm_user
    WHERE phone_no =@phone_no), pv_id
FROM olt_opm_privilege
WHERE pv_code NOT IN ('HOS_RECEIVED_ORDER','HOS_SEND_DRUG_TO_USER'); 

-- 所有权限
insert into olt_opm_privilege_r_user (ou_id,pv_id)
SELECT 
    (SELECT ou_id
    FROM olt_opm_user
    WHERE phone_no =@phone_no), pv_id
FROM olt_opm_privilege; 


