create table if not exists sys_dict_data
(
    id          bigint                                    not null comment '字典编码'
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
    comment '字典数据表' engine = InnoDB;

create table if not exists sys_dict_type
(
    id          bigint                                    not null comment '字典主键'
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
    comment '字典类型表' engine = InnoDB;

create table if not exists sys_user
(
    id              bigint            not null comment '系统用户ID'
        primary key,
    username        varchar(128)      not null comment '登录用户名',
    nickname        varchar(128)      not null comment '登录显示昵称',
    password        varchar(20)       null comment '用户密码',
    signature       varchar(50)       null comment '个性签名',
    account_type    int               null comment '账户类型',
    status          tinyint default 1 not null comment '用户是否启用 1: 启用, 0: 停用',
    last_login_ip   varchar(20)       null comment '最后登录IP',
    role_name       varchar(512)      null comment '用户角色',
    last_login_time datetime          null comment '最后登录时间',
    create_time     datetime          null comment '创建时间',
    update_time     datetime          null comment '修改时间',
    constraint id
        unique (id),
    constraint username
        unique (username)
)
    comment '系统用户表' engine = InnoDB;

create index sys_user_nickname_index
    on sys_user (nickname);

create index sys_user_username_index
    on sys_user (username);

create table if not exists tb_album
(
    id           bigint       not null comment '专辑表ID'
        primary key,
    album_name   varchar(512) not null comment '专辑名',
    sub_type     varchar(128) null comment '专辑版本（比如录音室版，现场版）',
    description  text         null comment '专辑简介',
    company      varchar(256) null comment '发行公司',
    publish_time datetime     null comment '专辑发布时间',
    user_id      bigint       null comment '上传用户ID',
    update_time  datetime     null comment '修改时间',
    create_time  datetime     null comment '创建时间',
    constraint tb_album_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete set null
)
    comment '歌曲专辑表' engine = InnoDB;

create index tb_album_album_name_index
    on tb_album (album_name);

create table if not exists tb_artist
(
    id           bigint       not null comment '歌手ID'
        primary key,
    artist_name  varchar(128) not null comment '歌手名',
    alias_name   varchar(255) null comment '歌手别名',
    sex          varchar(64)  null comment '歌手性别',
    birth        date         null comment '出生年月',
    location     varchar(64)  null comment '所在国家',
    introduction longtext     null comment '歌手介绍',
    user_id      bigint       null comment '上传用户ID',
    create_time  datetime     null comment '创建时间',
    update_time  datetime     null comment '修改时间',
    constraint tb_artist_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete set null
)
    comment '歌手表' engine = InnoDB;

create table if not exists tb_album_artist
(
    album_id  bigint not null comment '专辑ID',
    artist_id bigint not null comment '歌手ID',
    primary key (album_id, artist_id),
    constraint tb_album_artist_tb_album_id_fk
        foreign key (album_id) references tb_album (id)
            on update cascade on delete cascade,
    constraint tb_album_artist_tb_artist_id_fk
        foreign key (artist_id) references tb_artist (id)
            on update cascade on delete cascade
)
    comment '歌手和专辑中间表' engine = InnoDB;

create table if not exists tb_collect
(
    id             bigint       not null comment '歌单表ID'
        primary key,
    play_list_name varchar(256) not null comment '歌单名（包括用户喜爱歌单）',
    type           tinyint      not null comment '歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单',
    description    varchar(512) null comment '简介',
    user_id        bigint       null comment '创建人ID',
    sort           bigint       null comment '排序字段',
    create_time    datetime     null comment '创建时间',
    update_time    datetime     null comment '修改时间',
    constraint tb_collect_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade
)
    comment '歌单列表' engine = InnoDB;

create index tb_collect_play_list_name_index
    on tb_collect (play_list_name);

create table if not exists tb_history
(
    id          bigint        not null
        primary key,
    user_id     bigint        not null comment '用户ID',
    middle_id   bigint        not null comment '播放ID，可能是歌曲，专辑，歌单，mv',
    type        tinyint       null comment '播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑, 3mv',
    count       int default 0 null comment '歌曲播放次数',
    create_time datetime      null comment '创建时间',
    update_time datetime      null comment '更新时间',
    constraint id
        unique (id),
    constraint tb_history_pk
        unique (middle_id, type),
    constraint tb_history_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade
)
    comment '音乐播放历史(包括歌单，音乐，专辑）' engine = InnoDB;

create index tb_rank_sys_user_id_fk
    on tb_history (user_id);

create table if not exists tb_music
(
    id          bigint       not null comment '音乐ID'
        primary key,
    music_name  varchar(128) null comment '音乐名',
    alias_name  varchar(512) null comment '歌曲别名，数组则使用逗号分割',
    album_id    bigint       null comment '专辑ID',
    sort        bigint auto_increment comment '排序字段',
    user_id     bigint       null comment '上传用户ID',
    time_length int          null comment '歌曲时长',
    update_time datetime     null comment '更新时间',
    create_time datetime     null comment '创建时间',
    constraint tb_music_id_music_name_alias_name_album_id_uindex
        unique (id, music_name, alias_name, album_id),
    constraint tb_music_pk
        unique (sort),
    constraint tb_music_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete set null,
    constraint tb_music_tb_album_id_fk
        foreign key (album_id) references tb_album (id)
            on update cascade on delete set null
)
    comment '所有音乐列表' engine = InnoDB;

create table if not exists tb_collect_music
(
    collect_id bigint not null comment '歌单ID',
    music_id   bigint not null comment '音乐ID',
    sort       bigint not null comment '添加顺序',
    primary key (collect_id, music_id),
    constraint tb_collect_music_tb_collect_id_fk
        foreign key (collect_id) references tb_collect (id)
            on update cascade on delete cascade,
    constraint tb_collect_music_tb_music_id_fk
        foreign key (music_id) references tb_music (id)
            on update cascade on delete cascade
)
    comment '歌单和音乐的中间表，用于记录歌单中的每一个音乐' engine = InnoDB;

create table if not exists tb_lyric
(
    id          bigint      not null comment '主键'
        primary key,
    music_id    bigint      not null comment '音乐ID',
    type        varchar(24) not null comment '歌词类型',
    lyric       longtext    not null comment '歌词',
    create_time datetime    not null comment '创建时间',
    update_time datetime    not null comment '修改时间',
    constraint id
        unique (id),
    constraint tb_lyric_tb_music_id_fk
        foreign key (music_id) references tb_music (id)
            on update cascade on delete cascade
)
    comment '歌词表' engine = InnoDB;

create index tb_music_alia_name_index
    on tb_music (alias_name);

create index tb_music_music_name_index
    on tb_music (music_name);

create table if not exists tb_music_artist
(
    music_id  bigint not null comment '音乐ID',
    artist_id bigint not null comment '艺术家ID',
    primary key (music_id, artist_id),
    constraint tb_music_artist_tb_artist_id_fk
        foreign key (artist_id) references tb_artist (id)
            on update cascade on delete cascade,
    constraint tb_music_artist_tb_music_id_fk
        foreign key (music_id) references tb_music (id)
            on update cascade on delete cascade
)
    comment '音乐与歌手中间表' engine = InnoDB;

create table if not exists tb_pic
(
    id          bigint       not null
        primary key,
    url         varchar(512) not null comment '音乐网络地址，或路径',
    md5         char(32)     not null,
    update_time datetime     not null comment '更新时间',
    create_time datetime     not null comment '创建时间',
    constraint id
        unique (id),
    constraint md5
        unique (md5)
)
    comment '音乐专辑歌单封面表' engine = InnoDB;

create table if not exists tb_middle_pic
(
    id        bigint  not null
        primary key,
    middle_id bigint  not null comment '中间表',
    pic_id    bigint  not null comment '封面ID',
    type      tinyint not null comment '封面类型,歌单-1,专辑-2,歌手-3,歌手-3.用户头像-4,用户背景-5, mv标签-6',
    constraint tb_middle_pic_pk
        unique (middle_id, type),
    constraint tb_middle_pic_tb_pic_id_fk
        foreign key (pic_id) references tb_pic (id)
            on update cascade on delete cascade
)
    comment '封面中间表' engine = InnoDB;

create index tb_pic_md5_index
    on tb_pic (md5);

create table if not exists tb_plugin
(
    id          bigint       not null comment '插件ID'
        primary key,
    plugin_name varchar(255) not null comment '插件名称',
    create_name varchar(255) null comment '插件创建者',
    type        varchar(255) not null comment '插件类型',
    code        longtext     null comment '插件代码',
    user_id     bigint       not null comment '插件创建者',
    description text         null comment '插件描述',
    create_time datetime     not null comment '创建时间',
    update_time datetime     not null comment '更新时间',
    constraint id
        unique (id),
    constraint tb_plugin_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade
)
    comment '插件表' engine = InnoDB;

create table if not exists tb_plugin_task
(
    id          bigint   not null comment '任务ID'
        primary key,
    plugin_id   bigint   not null comment '插件ID',
    status      tinyint  not null comment '当前任务执行状态,0: stop, 1: run, 2: error',
    params      text     null comment '插件入参',
    user_id     bigint   not null comment '用户创建ID',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '更新时间',
    constraint id
        unique (id),
    constraint tb_plugin_task_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade,
    constraint tb_plugin_task_tb_plugin_id_fk
        foreign key (plugin_id) references tb_plugin (id)
            on update cascade on delete cascade
)
    comment '插件任务表' engine = InnoDB;

create table if not exists tb_plugin_msg
(
    id          bigint   not null comment '插件消息ID'
        primary key,
    plugin_id   bigint   not null comment '插件ID',
    task_id     bigint   not null comment '任务ID',
    user_id     bigint   not null comment '用户ID',
    level       tinyint  null comment '插件消息等级,0 info 1 debug 2 warn 3 error',
    msg         text     null comment '插件运行消息',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '更新时间',
    constraint id
        unique (id),
    constraint tb_plugin_msg_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade,
    constraint tb_plugin_msg_tb_plugin_id_fk
        foreign key (plugin_id) references tb_plugin (id)
            on update cascade on delete cascade,
    constraint tb_plugin_msg_tb_plugin_task_id_fk
        foreign key (task_id) references tb_plugin_task (id)
            on update cascade on delete cascade
)
    comment '插件消息表' engine = InnoDB;

create index tb_plugin_msg_task_id_user_id_index
    on tb_plugin_msg (task_id, user_id);

create table if not exists tb_resource
(
    id          bigint       not null comment '主键'
        primary key,
    music_id    bigint       not null comment '音乐ID',
    rate        int          null comment '比特率，音频文件的信息',
    path        varchar(512) null comment '音乐地址, 存储相对路径',
    md5         char(32)     not null comment '保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在',
    level       char(8)      null comment '音乐质量',
    encode_type char(10)     null comment '文件格式类型',
    size        bigint       null comment '文件大小',
    user_id     bigint       null comment '上传用户ID',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '修改时间',
    constraint id
        unique (id),
    constraint md5
        unique (md5),
    constraint tb_music_url_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete set null,
    constraint tb_music_url_tb_music_id_fk
        foreign key (music_id) references tb_music (id)
            on update cascade on delete cascade
)
    comment '存储地址' engine = InnoDB;

create table if not exists tb_mv
(
    id           bigint       not null
        primary key,
    source_id    bigint       not null comment '视频资源ID',
    title        varchar(255) null comment '标题',
    description  text         null comment '视频内容简介',
    duration     int          null comment '视频时长',
    user_id      bigint       null comment '上传用户',
    publish_time datetime     null comment '发布时间',
    create_time  datetime     null comment '创建时间',
    update_time  datetime     null comment '更新时间',
    constraint id
        unique (id),
    constraint tb_mv_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete set null,
    constraint tb_mv_tb_music_url_id_fk
        foreign key (source_id) references tb_resource (id)
            on update cascade on delete cascade
)
    comment '音乐短片' engine = InnoDB;

create table if not exists tb_mv_artist
(
    mv_id     bigint not null,
    artist_id bigint not null,
    primary key (mv_id, artist_id),
    constraint tb_mv_artist_tb_artist_id_fk
        foreign key (artist_id) references tb_artist (id)
            on update cascade on delete cascade,
    constraint tb_mv_artist_tb_mv_id_fk
        foreign key (mv_id) references tb_mv (id)
            on update cascade on delete cascade
)
    comment 'mv和歌手中间表' engine = InnoDB;

create table if not exists tb_origin
(
    id          bigint       not null
        primary key,
    music_id    bigint       not null comment '音乐ID',
    resource_id bigint       not null comment '源地址ID',
    origin      varchar(256) not null comment '来源',
    origin_url  varchar(256) null comment '来源地址',
    constraint id
        unique (id),
    constraint tb_origin_tb_music_id_fk
        foreign key (music_id) references tb_music (id)
            on update cascade on delete cascade,
    constraint tb_origin_tb_music_url_id_fk
        foreign key (resource_id) references tb_resource (id)
            on update cascade on delete cascade
)
    comment '音乐来源' engine = InnoDB;

create index tb_music_url_md5_index
    on tb_resource (md5);

create index tb_music_url_music_id_index
    on tb_resource (music_id);

create index tb_music_url_size_index
    on tb_resource (size);

create table if not exists tb_schedule_task
(
    id          bigint       not null comment 'ID'
        primary key,
    name        varchar(128) not null comment '定时任务名',
    plugin_id   bigint       not null comment '插件ID',
    cron        varchar(128) not null comment 'cron表达式',
    params      tinytext     null comment '插件入参json格式',
    status      tinyint      null comment '是否执行(true执行, false不执行)',
    user_id     bigint       not null comment '用户ID',
    create_time datetime     not null comment '创建时间',
    update_time datetime     not null comment '更新时间',
    constraint id
        unique (id),
    constraint tb_schedule_task_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade,
    constraint tb_schedule_task_tb_plugin_id_fk
        foreign key (plugin_id) references tb_plugin (id)
            on update cascade on delete cascade
)
    comment '定时任务表' engine = InnoDB;

create table if not exists tb_tag
(
    id          bigint       not null
        primary key,
    tag_name    varchar(128) null comment '风格（标签）',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '修改时间',
    constraint id
        unique (id)
)
    comment '标签表（风格）' engine = InnoDB;

create table if not exists tb_middle_tag
(
    id        bigint  not null
        primary key,
    middle_id bigint  not null comment '中间ID, 包括歌曲，歌单，专辑',
    tag_id    bigint  not null comment 'tag ID',
    type      tinyint not null comment '0: 流派, 1: 歌曲tag, 2: 歌单tag, 3: mv标签',
    constraint tb_collect_tag_tb_tag_id_fk
        foreign key (tag_id) references tb_tag (id)
            on update cascade on delete cascade
)
    comment '歌单风格中间表' engine = InnoDB;

create index tb_middle_tag_pk
    on tb_middle_tag (middle_id, type, tag_id);

create table if not exists tb_user_album
(
    user_id  bigint not null comment '用户ID',
    album_id bigint not null comment '专辑ID',
    primary key (user_id, album_id),
    constraint tb_user_album_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade,
    constraint tb_user_album_tb_album_id_fk
        foreign key (album_id) references tb_album (id)
            on update cascade on delete cascade
)
    comment '用户收藏专辑表' engine = InnoDB;

create table if not exists tb_user_artist
(
    user_id   bigint not null comment '用户ID',
    artist_id bigint not null comment '歌手ID',
    primary key (user_id, artist_id),
    constraint tb_user_artist_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade,
    constraint tb_user_artist_tb_artist_id_fk
        foreign key (artist_id) references tb_artist (id)
            on update cascade on delete cascade
)
    comment '用户关注歌曲家' engine = InnoDB;

create table if not exists tb_user_collect
(
    user_id    bigint not null comment '用户ID',
    collect_id bigint not null comment '歌单ID',
    primary key (user_id, collect_id),
    constraint tb_user_collect_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade,
    constraint tb_user_collect_tb_collect_id_fk
        foreign key (collect_id) references tb_collect (id)
            on update cascade on delete cascade
)
    comment '用户收藏歌单' engine = InnoDB;

create table if not exists tb_user_mv
(
    user_id bigint not null comment '用户ID',
    mv_id   bigint not null comment 'MV ID',
    primary key (user_id, mv_id),
    constraint tb_user_mv_sys_user_id_fk
        foreign key (user_id) references sys_user (id)
            on update cascade on delete cascade,
    constraint tb_user_mv_tb_mv_id_fk
        foreign key (mv_id) references tb_mv (id)
            on update cascade on delete cascade
)
    comment '用户收藏mv' engine = InnoDB;

