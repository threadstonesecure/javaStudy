CREATE TABLE `hdp_dubbo_registry` (
  `registry_id` int(11) NOT NULL,
  `protocol` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'multicast,zookeeper',
  `address` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `username` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `enable` int(1) NOT NULL,
  `memo` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `groupname` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`registry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Hdp  dubbo 注册中心配置';


CREATE TABLE `hdp_updatahandler` (
  `command` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '指令code',
  `interfacename` varchar(80) COLLATE utf8_bin NOT NULL COMMENT '服务接口类全名',
  `registry_id` int(11) NOT NULL,
  `enable` int(1) NOT NULL,
  `groupname` varchar(50) COLLATE utf8_bin NOT NULL,
  `memo` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`command`),
  KEY `hdp_updatahandler_ibfk_1` (`registry_id`),
  CONSTRAINT `hdp_updatahandler_ibfk_1` FOREIGN KEY (`registry_id`) REFERENCES `hdp_dubbo_registry` (`registry_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
