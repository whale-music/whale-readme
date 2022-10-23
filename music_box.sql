SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '�ֵ����',
    `dict_sort`   int(11) NULL DEFAULT 0 COMMENT '�ֵ�����',
    `dict_label`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '�ֵ��¼ʵ������',
    `dict_value`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '�ֵ��ֵ',
    `dict_type`   varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '�ֵ�����',
    `status`      char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '״̬��0���� 1ͣ�ã�',
    `create_by`   varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '������',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_by`   varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '������',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `remark`      varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '��ע',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '�ֵ����ݱ�' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '�ֵ�����',
    `dict_name`   varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '�ֵ�����',
    `dict_type`   varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '�ֵ�����',
    `status`      char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '״̬��0���� 1ͣ�ã�',
    `create_by`   varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '������',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_by`   varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '������',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `remark`      varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '��ע',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '�ֵ����ͱ�' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ϵͳ�û�ID',
    `username`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '��¼�û���',
    `nickname`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '��¼��ʾ�ǳ�',
    `password`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '�û�����',
    `avatar_url`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ͷ��URL',
    `background_url`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '������ƬURL',
    `signature`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '����ǩ��',
    `account_type`    int(11) NULL DEFAULT NULL COMMENT '�˻�����',
    `last_login_ip`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '����¼IP',
    `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '����¼ʱ��',
    `create_time`     datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_time`     datetime(0) NULL DEFAULT NULL COMMENT '�޸�ʱ��',
    PRIMARY KEY (`id`, `username`, `nickname`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'ϵͳ�û���' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_album
-- ----------------------------
DROP TABLE IF EXISTS `tb_album`;
CREATE TABLE `tb_album`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ר����ID',
    `album_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ר����',
    `pic`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ר�������ַ',
    `indirect`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ר�����',
    `update_time` date NULL DEFAULT NULL COMMENT '�޸�ʱ��',
    `create_time` date NULL DEFAULT NULL COMMENT '����ʱ��',
    PRIMARY KEY (`id`, `album_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '����ר����' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_collect
-- ----------------------------
DROP TABLE IF EXISTS `tb_collect`;
CREATE TABLE `tb_collect`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '�赥��ID',
    `song_name`   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '�赥��',
    `pic`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '�����ַ',
    `introduce`   varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '���',
    `style`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '�赥��ǩ����ʾ�赥��񡣱����ǩ����ǩ�����ŷָ�',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '�޸�ʱ��',
    `user_id`     bigint(20) NULL DEFAULT NULL COMMENT '������ID',
    PRIMARY KEY (`id`, `song_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '�赥�б�' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_collect_music
-- ----------------------------
DROP TABLE IF EXISTS `tb_collect_music`;
CREATE TABLE `tb_collect_music`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `collect_id` bigint(20) NOT NULL COMMENT '�赥ID',
    `music_id`   bigint(20) NOT NULL COMMENT '����ID',
    PRIMARY KEY (`id`, `collect_id`, `music_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '�赥�����ֵ��м�����ڼ�¼�赥�е�ÿһ������' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_collect_style
-- ----------------------------
DROP TABLE IF EXISTS `tb_collect_style`;
CREATE TABLE `tb_collect_style`
(
    `id`         bigint(20) NOT NULL AUTO_INCREMENT,
    `collect_id` bigint(20) NULL DEFAULT NULL,
    `style_id`   bigint(20) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '�赥������м����ǩ��' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_history
-- ----------------------------
DROP TABLE IF EXISTS `tb_history`;
CREATE TABLE `tb_history`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '���ֲ�����ʷID',
    `music_id`    bigint(20) NOT NULL COMMENT '����ID',
    `count`       int(11) NULL DEFAULT NULL COMMENT '�������',
    `type`        int(11) NULL DEFAULT NULL COMMENT '��ʷ����',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '�޸�ʱ��',
    PRIMARY KEY (`id`, `music_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '���ֲ�����ʷ(�����赥�����֣�ר����' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_like
-- ----------------------------
DROP TABLE IF EXISTS `tb_like`;
CREATE TABLE `tb_like`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ϲ���赥ID',
    `song_name`   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '�赥��',
    `pic`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '�����ַ',
    `introduce`   varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '���',
    `style`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '�赥��ǩ����ʾ�赥���ʹ���ֵ��',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '�޸�ʱ��',
    `user_id`     bigint(20) NULL DEFAULT NULL COMMENT '�����û�ID',
    PRIMARY KEY (`id`, `song_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'ϲ���赥' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_like_music
-- ----------------------------
DROP TABLE IF EXISTS `tb_like_music`;
CREATE TABLE `tb_like_music`
(
    `id`       bigint(20) NOT NULL AUTO_INCREMENT,
    `like_id`  bigint(20) NOT NULL COMMENT 'ϲ���赥ID',
    `music_id` bigint(20) NOT NULL COMMENT '����ID',
    PRIMARY KEY (`id`, `like_id`, `music_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'ϲ���赥�м��' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_music
-- ----------------------------
DROP TABLE IF EXISTS `tb_music`;
CREATE TABLE `tb_music`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '���������б�ID',
    `music_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '������',
    `alia_name`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '����������������ʹ�ö��ŷָ�',
    `singer_id`   bigint(20) NULL DEFAULT NULL COMMENT '������Ϣ��id�Ǹ��ֺ͸������м��',
    `lyric`       varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '���',
    `time_length` time(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `album_id`    bigint(20) NULL DEFAULT NULL COMMENT 'ר��ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1201 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '���������б�' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_music_singer
-- ----------------------------
DROP TABLE IF EXISTS `tb_music_singer`;
CREATE TABLE `tb_music_singer`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `music_id`  bigint(20) NOT NULL COMMENT '����ID',
    `singer_id` bigint(20) NOT NULL COMMENT '����ID',
    PRIMARY KEY (`id`, `music_id`, `singer_id`) USING BTREE,
    UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '�����͸��ֵ��м��' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_music_url
-- ----------------------------
DROP TABLE IF EXISTS `tb_music_url`;
CREATE TABLE `tb_music_url`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '����',
    `music_id`    bigint(20) NOT NULL COMMENT '����ID',
    `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '���ֵ�ַ',
    `rate`        int(11) NULL DEFAULT NULL COMMENT '�����ʣ���Ƶ�ļ�����Ϣ',
    `quality`     char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '��������(sq: ����l����������m����������h����������a��δ֪)',
    `md5`         bigint(20) NULL DEFAULT NULL COMMENT '�������ֱ����md5�����ϴ��µ�����ʱ���Ƚϡ������ͬ���ʾ�Ѵ���',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '�޸�ʱ��',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `encodeType`  varbinary(10) NULL DEFAULT NULL COMMENT '�ļ���ʽ����',
    PRIMARY KEY (`id`, `music_id`) USING BTREE,
    UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '�������ص�ַ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_rank
-- ----------------------------
DROP TABLE IF EXISTS `tb_rank`;
CREATE TABLE `tb_rank`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '���ֲ������а�ID',
    `music_id`        bigint(20) NOT NULL COMMENT '����ID',
    `user_id`         bigint(20) NOT NULL COMMENT '�û�ID',
    `broadcast_count` int(11) NULL DEFAULT NULL COMMENT '�������Ŵ���',
    `create_time`     datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_time`     datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    PRIMARY KEY (`id`, `music_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '���ֲ������а�' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_singer
-- ----------------------------
DROP TABLE IF EXISTS `tb_singer`;
CREATE TABLE `tb_singer`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '����ID',
    `singer_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '������',
    `sex`          varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '�����Ա�',
    `pic`          varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '����',
    `birth`        date NULL DEFAULT NULL COMMENT '��������',
    `location`     varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '���ڹ���',
    `introduction` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '���ֽ���',
    `create_time`  datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_time`  datetime(0) NULL DEFAULT NULL COMMENT '�޸�ʱ��',
    PRIMARY KEY (`id`, `singer_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '���ֱ�' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_style
-- ----------------------------
DROP TABLE IF EXISTS `tb_style`;
CREATE TABLE `tb_style`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `style`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '��񣨱�ǩ��',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '����ʱ��',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '�޸�ʱ��',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '��ǩ�����' ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
