alter table olt_opm_order add column  check_status tinyint(1) default 0  comment '0:未审核；1:审核通过；2:审核不通过';
--alter table olt_opm_order modify column  check_status tinyint(1) default 0  comment '0:未审核；1:审核通过；2:审核不通过';


alter table olt_opm_order add column  print_status tinyint(1) default 0  comment '0:未打印；1:已打印';
