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
    comment '字典数据表';

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
    comment '字典类型表';

create table if not exists sys_user
(
    id              bigint       not null comment '系统用户ID'
        primary key,
    username
                    varchar(128) not null comment '登录用户名',
    nickname        varchar(128) not null comment '登录显示昵称',
    password        varchar(20)  null comment '用户密码',
    avatar_url      varchar(256) null comment '头像URL',
    background_url  varchar(256) null comment '背景照片URL',
    signature       varchar(50)  null comment '个性签名',
    account_type    int          null comment '账户类型',
    last_login_ip   varchar(20)  null comment '最后登录IP',
    last_login_time datetime     null comment '最后登录时间',
    create_time     datetime     null comment '创建时间',
    update_time     datetime     null comment '修改时间',
    constraint username
        unique
        (
         username
            )
)
    comment '系统用户表';

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
    pic          varchar(512) null comment '专辑封面地址',
    publish_time datetime     null comment '专辑发布时间',
    update_time  datetime     null comment '修改时间',
    create_time  datetime     null comment '创建时间'
    )
    comment '歌曲专辑表';

create index tb_album_album_name_index
    on tb_album (album_name);

create table if not exists tb_artist
(
    id           bigint       not null comment '歌手ID'
    primary key,
    artist_name  varchar(128) not null comment '歌手名',
    alias_name   varchar(255) null comment '歌手别名',
    sex          varchar(64)  null comment '歌手性别',
    pic          varchar(512) null comment '封面',
    birth        date         null comment '出生年月',
    location     varchar(64)  null comment '所在国家',
    introduction longtext     null comment '歌手介绍',
    create_time  datetime     null comment '创建时间',
    update_time  datetime     null comment '修改时间'
    )
    comment '歌手表';

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
    comment '歌手和专辑中间表';

create table if not exists tb_collect
(
    id             bigint               not null comment '歌单表ID'
    primary key,
    play_list_name varchar(256)         not null comment '歌单名（包括用户喜爱歌单）',
    pic            varchar(512)         null comment '封面地址',
    type           tinyint              not null comment '歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单',
    subscribed     tinyint(1) default 0 not null comment '该歌单是否订阅(收藏). 0: 为创建,1: 为订阅(收藏)',
    description    varchar(512)         null comment '简介',
    user_id        bigint               null comment '创建人ID',
    sort           bigint               null comment '排序字段',
    create_time    datetime             null comment '创建时间',
    update_time    datetime             null comment '修改时间',
    constraint tb_collect_sys_user_id_fk
    foreign key (user_id) references sys_user (id)
    on update cascade on delete set null
    )
    comment '歌单列表';

create index tb_collect_play_list_name_index
    on tb_collect (play_list_name);

create table if not exists tb_music
(
    id          bigint       not null comment '音乐ID'
    primary key,
    music_name  varchar(128) null comment '音乐名',
    alias_name  varchar(512) null comment '歌曲别名，数组则使用逗号分割',
    pic         varchar(512) null comment '歌曲封面地址',
    album_id    bigint       null comment '专辑ID',
    sort        bigint       null comment '排序字段',
    time_length int          null comment '歌曲时长',
    update_time datetime     null comment '更新时间',
    create_time datetime     null comment '创建时间',
    constraint tb_music_id_music_name_alias_name_album_id_uindex
    unique (id, music_name, alias_name, album_id),
    constraint tb_music_tb_album_id_fk
    foreign key (album_id) references tb_album (id)
    on update cascade on delete set null
    )
    comment '所有音乐列表';

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
    comment '歌单和音乐的中间表，用于记录歌单中的每一个音乐';

create table if not exists tb_history
(
    music_id    bigint   not null comment '歌曲ID'
    primary key,
    count       int      null comment '听歌次数',
    type        int      null comment '历史类型',
    create_time datetime null comment '创建时间',
    update_time datetime null comment '修改时间',
    constraint tb_history_tb_album_id_fk
    foreign key (music_id) references tb_album (id)
    on update cascade on delete cascade,
    constraint tb_history_tb_collect_id_fk
    foreign key (music_id) references tb_collect (id)
    on update cascade on delete cascade,
    constraint tb_history_tb_music_id_fk
    foreign key (music_id) references tb_music (id)
    on update cascade on delete cascade
    )
    comment '音乐播放历史(包括歌单，音乐，专辑）';

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
    comment '歌词表';

create index tb_music_alia_name_index
    on tb_music (alias_name);

create index tb_music_music_name_index
    on tb_music (music_name);

create table if not exists tb_music_url
(
    id          bigint        not null comment '主键'
    primary key,
    music_id    bigint        not null comment '音乐ID',
    rate        int           null comment '比特率，音频文件的信息',
    url         varchar(512)  null comment '音乐地址',
    md5         char(32)      null comment '保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在',
    level       char(8)       null comment '音乐质量',
    encode_type char(10)      null comment '文件格式类型',
    size        bigint        null comment '文件大小',
    user_id     bigint        not null comment '上传用户ID',
    origin      varbinary(64) null comment '音乐来源',
    create_time datetime      null comment '创建时间',
    update_time datetime      null comment '修改时间',
    constraint id
    unique (id),
    constraint tb_music_url_tb_music_id_fk
    foreign key (music_id) references tb_music (id)
    on update cascade on delete cascade
    )
    comment '音乐下载地址';

create index tb_music_url_md5_index
    on tb_music_url (md5);

create index tb_music_url_music_id_index
    on tb_music_url (music_id);

create index tb_music_url_size_index
    on tb_music_url (size);

create table if not exists tb_plugin
(
    id          bigint       not null comment '插件ID'
    primary key,
    plugin_name varchar(255) not null comment '插件名称',
    create_name varchar(255) null comment '插件创建者',
    code        longtext     null comment '插件代码',
    description text         null comment '插件描述',
    user_id     bigint       not null comment '插件创建者',
    create_time datetime     not null comment '创建时间',
    update_time datetime     not null comment '更新时间',
    constraint id
    unique (id)
    )
    comment '插件表';

create table if not exists tb_plugin_msg
(
    id          bigint   not null comment '插件消息ID'
        primary key,
    plugin_id   bigint   not null comment '插件ID',
    task_id     bigint   not null
        comment
            '任务ID',
    user_id
                bigint
                         not
                             null
        comment
            '用户ID',
    level
                tinyint
                         null
        comment
            '消息等级',
    msg
                text
                         null
        comment
            '插件运行消息',
    create_time
                datetime
                         not
                             null
        comment
            '创建时间',
    update_time datetime not null comment '更新时间',
    constraint id
        unique (id)
)
    comment '插件消息表';

create index tb_plugin_msg_task_id_user_id_index
    on tb_plugin_msg (task_id, user_id);

create table if not exists tb_plugin_task
(
    id          bigint   not null comment '任务ID'
    primary key,
    plugin_id   bigint   not null comment '插件ID',
    status      tinyint  not null comment '当前任务执行状态,0: stop, 1: run, 2: error',
    user_id     bigint   not null comment '用户创建ID',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '更新时间',
    constraint id
    unique (id)
    )
    comment '插件任务表';

create table if not exists tb_rank
(
    id              bigint        not null
    primary key,
    user_id         bigint        not null comment '用户ID',
    broadcast_id    int           null comment '播放ID，可能是歌曲，专辑，歌单',
    broadcast_type  int default 0 null comment '播放类型可能是音乐，歌单，专辑,0为音乐，1为歌单，2为专辑',
    broadcast_count int           null comment '歌曲播放次数',
    create_time     datetime      null comment '创建时间',
    update_time     datetime      null comment '更新时间',
    constraint id
    unique (id),
    constraint tb_rank_sys_user_id_fk
    foreign key (user_id) references sys_user (id)
    on update cascade on delete cascade
    )
    comment '音乐播放排行榜';

create table if not exists tb_tag
(
    id          bigint      not null
    primary key,
    tag_name    varchar(20) null comment '风格（标签）',
    create_time datetime    null comment '创建时间',
    update_time datetime    null comment '修改时间',
    constraint id
    unique (id)
    )
    comment '标签表（风格）';

create table if not exists tb_middle_tag
(
    id
        bigint
        not
            null
        comment
            '中间ID, 包括歌曲，歌单，专辑',
    tag_id
        bigint
        not
            null
        comment
            'tag ID',
    type
        tinyint
        not
            null
        comment
            '0流派, 1歌曲tag, 2歌单tag',
    primary
        key
        (
         id,
         tag_id
            ),
    constraint tb_collect_tag_tb_tag_id_fk
        foreign key
            (
             tag_id
                ) references tb_tag
            (
             id
                )
            on update cascade
            on delete cascade
)
    comment '歌单风格中间表';

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
    comment '用户收藏专辑表';

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
    comment '用户关注歌曲家';

