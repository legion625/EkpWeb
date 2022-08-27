CREATE TABLE `sys_attr` (
  `uid` varchar(45) NOT NULL,
  `type_idx` tinyint(4) DEFAULT NULL,
  `attr_key` varchar(45) DEFAULT NULL,
  `attr_value` varchar(45) DEFAULT NULL,
  `object_create_time` bigint(20) DEFAULT NULL,
  `object_update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `system_seq` (
  `item_id` varchar(45) NOT NULL,
  `current_num` bigint(20) DEFAULT NULL,
  `last_num` bigint(20) DEFAULT NULL,
  `max_num` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
