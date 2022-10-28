create table if not exists music_box.sys_dict_data
(
    id          bigint auto_increment comment '字典编码'
        primary key,
    dict_sort   int                           default 0   null comment '字典排序',
    dict_label  varchar(100) collate utf8_bin default ''  null comment '字典记录实际数据',
    dict_value  varchar(100) collate utf8_bin default ''  null comment '字典键值',
    dict_type   varchar(100) collate utf8_bin default ''  null comment '字典类型',
    status      char collate utf8_bin         default '0' null comment '状态（0正常 1停用）',
    create_by   varchar(64) collate utf8_bin  default ''  null comment '创建者',
    update_by   varchar(64) collate utf8_bin  default ''  null comment '更新者',
    remark      varchar(500) collate utf8_bin             null comment '备注',
    create_time datetime                                  null comment '创建时间',
    update_time datetime                                  null comment '更新时间'
)
    comment '字典数据表';

create table if not exists music_box.sys_dict_type
(
    id          bigint auto_increment comment '字典主键'
        primary key,
    dict_name   varchar(100) collate utf8_bin default ''  null comment '字典名称',
    dict_type   varchar(100) collate utf8_bin default ''  null comment '字典类型',
    status      char collate utf8_bin         default '0' null comment '状态（0正常 1停用）',
    remark      varchar(500) collate utf8_bin             null comment '备注',
    create_by   varchar(64) collate utf8_bin  default ''  null comment '创建者',
    update_by   varchar(64) collate utf8_bin  default ''  null comment '更新者',
    update_time datetime                                  null comment '更新时间',
    create_time datetime                                  null comment '创建时间',
    constraint dict_type
        unique (dict_type)
)
    comment '字典类型表';

create table if not exists music_box.sys_user
(
    id              bigint auto_increment comment '系统用户ID'
        primary key,
    username        varchar(128) not null comment '登录用户名',
    nickname        varchar(128) not null comment '登录显示昵称',
    password        varchar(20)  null comment '用户密码',
    avatar_url      varchar(256) null comment '头像URL',
    background_url  varchar(256) null comment '背景照片URL',
    signature       varchar(50)  null comment '个性签名',
    account_type    int          null comment '账户类型',
    last_login_ip   varchar(20)  null comment '最后登录IP',
    last_login_time datetime     null comment '最后登录时间',
    create_time     datetime     null comment '创建时间',
    update_time     datetime     null comment '修改时间'
)
    comment '系统用户表';

create index sys_user_nickname_index
    on music_box.sys_user (nickname);

create index sys_user_username_index
    on music_box.sys_user (username);

create table if not exists music_box.tb_album
(
    id          bigint auto_increment comment '专辑表ID'
        primary key,
    album_name  varchar(64) charset utf8 not null comment '专辑名',
    pic         varchar(64) charset utf8 null comment '专辑封面地址',
    indirect    varchar(255)             null comment '专辑简介',
    update_time date                     null comment '修改时间',
    create_time date                     null comment '创建时间'
)
    comment '歌曲专辑表';

create index tb_album_album_name_index
    on music_box.tb_album (album_name);

create table if not exists music_box.tb_collect
(
    id             bigint auto_increment comment '歌单表ID'
        primary key,
    play_list_name varchar(256)              not null comment '歌单名',
    pic            varchar(64) charset utf8  null comment '封面地址',
    description    varchar(512) charset utf8 null comment '简介',
    tag_id         bigint                    null comment '歌单标签，表示歌单风格。保存标签到标签表，逗号分割',
    user_id        bigint                    null comment '创建人ID',
    sort           bigint                    null comment '排序字段',
    create_time    datetime                  null comment '创建时间',
    update_time    datetime                  null comment '修改时间'
)
    comment '歌单列表';

create index tb_collect_play_list_name_index
    on music_box.tb_collect (play_list_name);

create table if not exists music_box.tb_collect_music
(
    collect_id bigint not null comment '歌单ID',
    music_id   bigint not null comment '音乐ID',
    primary key (collect_id, music_id)
)
    comment '歌单和音乐的中间表，用于记录歌单中的每一个音乐';

create table if not exists music_box.tb_collect_tag
(
    collect_id bigint not null comment '歌单ID',
    tag_id     bigint not null comment 'tag ID',
    primary key (collect_id, tag_id)
)
    comment '歌单风格中间表';

create table if not exists music_box.tb_history
(
    music_id    bigint   not null comment '歌曲ID'
        primary key,
    count       int      null comment '听歌次数',
    type        int      null comment '历史类型',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间'
)
    comment '音乐播放历史(包括歌单，音乐，专辑）';

create table if not exists music_box.tb_like
(
    user_id     bigint auto_increment comment '我喜欢的歌单ID和用户ID相同'
        primary key,
    song_name   varchar(256)              null comment '歌单名',
    pic         varchar(64) charset utf8  null comment '封面地址',
    description varchar(512) charset utf8 null comment '简介',
    tag         bigint                    null comment '歌单标签，表示歌单风格。使用字典表',
    update_time datetime                  null comment '修改时间',
    create_time datetime                  null comment '创建时间'
)
    comment '喜爱歌单';

create table if not exists music_box.tb_like_music
(
    like_id  bigint not null comment '喜爱歌单ID',
    music_id bigint not null comment '音乐ID',
    primary key (like_id, music_id)
)
    comment '喜爱歌单中间表';

create table if not exists music_box.tb_music
(
    id          bigint auto_increment comment '所有音乐列表ID'
        primary key,
    music_name  varchar(64) charset utf8  null comment '音乐名',
    alia_name   varchar(64)               null comment '歌曲别名，数组则使用逗号分割',
    singer_id   bigint                    null comment '歌手信息，id是歌手和歌曲的中间表',
    lyric       varchar(512) charset utf8 null comment '歌词',
    album_id    bigint                    null comment '专辑ID',
    time_length time                      null comment '歌曲时长',
    sort        bigint                    null comment '排序字段',
    update_time datetime                  null comment '更新时间',
    create_time datetime                  null comment '创建时间'
)
    comment '所有音乐列表';

create index tb_music_alia_name_index
    on music_box.tb_music (alia_name);

create index tb_music_music_name_index
    on music_box.tb_music (music_name);

create table if not exists music_box.tb_music_singer
(
    music_id  bigint not null comment '歌曲ID',
    singer_id bigint not null comment '歌曲ID',
    primary key (music_id, singer_id)
)
    comment '歌曲和歌手的中间表';

create table if not exists music_box.tb_music_url
(
    id          bigint auto_increment comment '主键'
        primary key,
    music_id    bigint        not null comment '音乐ID',
    url         varchar(255)  null comment '音乐地址',
    rate        int           null comment '比特率，音频文件的信息',
    quality     char(3)       null comment '音乐质量(sq: 无损，l：低质量，m：中质量，h：高质量，a：未知)',
    md5         bigint        null comment '保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在',
    update_time datetime      null comment '修改时间',
    create_time datetime      null comment '创建时间',
    encodeType  varbinary(10) null comment '文件格式类型'
)
    comment '音乐下载地址';

create index tb_music_url_music_id_index
    on music_box.tb_music_url (music_id);

create table if not exists music_box.tb_rank
(
    user_id         bigint   not null comment '用户ID'
        primary key,
    music_id        bigint   not null comment '音乐ID',
    broadcast_count int      null comment '歌曲播放次数',
    create_time     datetime null comment '创建时间',
    update_time     datetime null comment '创建时间'
)
    comment '音乐播放排行榜';

create table if not exists music_box.tb_singer
(
    id           bigint auto_increment comment '歌手ID'
        primary key,
    singer_name  varchar(64) charset utf8  not null comment '歌手名',
    sex          varchar(64) charset utf8  null comment '歌手性别',
    pic          varchar(64) charset utf8  null comment '封面',
    birth        date                      null comment '出生年月',
    location     varchar(64) charset utf8  null comment '所在国家',
    introduction varchar(128) charset utf8 null comment '歌手介绍',
    create_time  datetime                  null comment '创建时间',
    update_time  datetime                  null comment '修改时间'
)
    comment '歌手表';

create table if not exists music_box.tb_tag
(
    id          bigint auto_increment
        primary key,
    tag_name    varchar(20) null comment '风格（标签）',
    create_time datetime    null comment '创建时间',
    update_time datetime    null comment '修改时间',
    constraint id
        unique (id)
)
    comment '标签表（风格）';

