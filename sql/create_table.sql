create database if not exists train;

use train;

drop table if exists user;
-- auto-generated definition
create table user
(
    id         bigint auto_increment comment '用户id'
        primary key,
    username   varchar(64)                        not null comment '用户账号',
    password   varchar(255)                       not null comment '用户密码',
    nickname   varchar(64)                        not null comment '用户昵称',
    email      varchar(100)                       null comment '用户邮箱',
    mobile     varchar(100)                       null comment '手机号码',
    sex        tinyint  default 0                 not null comment '用户性别 0-男 1-女',
    avatar     varchar(255)                       null comment '头像地址',
    status     tinyint  default 2                 null comment '账号状态 0-正常 1-停用 2-审核',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '用户表';

drop table if exists role;
-- auto-generated definition
create table role
(
    id         bigint auto_increment comment '角色id'
        primary key,
    name       varchar(30)                        not null comment '角色名称',
    code       varchar(100)                       not null comment '角色权限字符串',
    status     tinyint  default 0                 not null comment '角色状态 0-正常 1-停用',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '角色表';

drop table if exists user_role;
-- auto-generated definition
create table user_role
(
    id         bigint auto_increment comment 'id'
        primary key,
    userId     bigint                             not null comment '用户id',
    roleId     bigint                             not null comment '角色id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '用户角色关联表';

drop table if exists menu;
-- auto-generated definition
create table menu
(
    id         bigint auto_increment comment '菜单id'
        primary key,
    name       varchar(50)                        not null comment '菜单名称',
    permission varchar(100)                       null comment '权限标识(多个用逗号分隔，如：user:list,user:create)',
    type       tinyint                            null comment '菜单类型 0-目录 1-菜单 2-按钮',
    parentId   bigint   default 0                 not null comment '父菜单id 1级菜单id为0',
    url        varchar(200)                       null comment '菜单接口url',
    icon       varchar(50)                        null comment '菜单图标',
    orderNum   int                                null comment '排序',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '菜单表';

drop table if exists role_menu;
-- auto-generated definition
create table role_menu
(
    id         bigint auto_increment comment 'id'
        primary key,
    roleId     bigint                             not null comment '角色id',
    menuId     bigint                             not null comment '菜单id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '角色菜单关联表';

drop table if exists log;
-- auto-generated definition
create table log
(
    id         bigint auto_increment comment '日志id'
        primary key,
    username   varchar(50)                        null comment '用户名',
    operation  varchar(50)                        null comment '用户操作',
    method     varchar(200)                       null comment '请求方法',
    params     varchar(5000)                      null comment '请求参数',
    time       bigint                             not null comment '执行时长(毫秒)',
    ip         varchar(64)                        null comment 'IP地址',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '系统日志表';

drop table if exists captcha;
-- auto-generated definition
create table captcha
(
    uuid       char(36)   not null comment 'uuid'
        primary key,
    code       varchar(6) not null comment '验证码',
    expireTime datetime   null comment '过期时间'
)
    comment '系统验证码';

drop table if exists user_token;
-- auto-generated definition
create table user_token
(
    userId     bigint       not null
        primary key,
    token      varchar(100) not null comment 'token',
    expireTime datetime     null comment '过期时间',
    updateTime datetime     null comment '更新时间',
    constraint token
        unique (token)
)
    comment '系统用户Token';

###################################再次确认########################################
drop table if exists notice;
-- auto-generated definition
create table notice
(
    id         bigint auto_increment comment '公告id'
        primary key,
    title      varchar(50)                        not null comment '公告标题',
    content    text                               not null comment '公告内容',
    status     tinyint  default 0                 not null comment '公告状态 0-正常 1-关闭',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updater    datetime                           null comment '更新时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '公告表';

drop table if exists message;
-- auto-generated definition
create table message
(
    id         bigint auto_increment comment '消息id'
        primary key,
    title      varchar(50)                        not null comment '消息标题',
    content    text                               not null comment '消息内容',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updater    datetime                           null comment '更新时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '消息表';

drop table if exists message_user;
-- auto-generated definition
create table message_user
(
    id         bigint auto_increment comment 'id'
        primary key,
    messageId  bigint                             not null comment '消息id',
    userId     bigint                             not null comment '学员id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '学员消息关联表';

drop table if exists organ;
-- auto-generated definition
create table organ
(
    id           bigint auto_increment comment '机构id'
        primary key,
    name         varchar(255)                       not null comment '机构名称',
    organTypeId  bigint                             not null comment '机构类型id',
    organLogo    varchar(255)                       null comment '机构logo图片地址',
    phone        varchar(20)                        null comment '机构联系电话',
    chargePerson varchar(64)                        null comment '机构负责人',
    email        varchar(64)                        null comment '邮箱',
    intro        varchar(1024)                      null comment '机构简介',
    creator      varchar(64)                        null comment '创建者',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater      varchar(64)                        null comment '更新者',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '培训机构表';

drop table if exists organ_type;
-- auto-generated definition
create table organ_type
(
    id         bigint                             not null auto_increment comment '机构类型id'
        primary key,
    typeName   varchar(64)                        null comment '类型名称',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '机构类型表';

drop table if exists organ_plan;
-- auto-generated definition
create table organ_plan
(
    id          bigint                             not null auto_increment comment '机构培训计划id'
        primary key,
    organId     bigint                             not null comment '培训机构id',
    content     text                               null comment '培训内容',
    startTime   datetime                           null comment '培训开始时间',
    trainPeriod int      default 7                 not null comment '培训周期 默认7天',
    endTime     datetime                           null comment '培训结束时间 开始时间+培训周期',
    creator     varchar(64)                        null comment '创建者',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)                        null comment '更新者',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '培训机构计划表';

drop table if exists exam;
-- auto-generated definition
create table exam
(
    id         bigint                             not null auto_increment comment '考试id'
        primary key,
    examName   varchar(30)                        not null comment '考试课程名称',
    examDesc   varchar(255)                       null comment '考试介绍',
    examDate   datetime                           null comment '考试时间',
    totalTime  int                                not null comment '持续时间-单位为秒',
    totalScore int      default 100               not null comment '考试总分',
    passScore  int      default 60                not null comment '考试通过线',
    valid      tinyint  default 0                 not null comment '考试有效 0-有效 1-无效',
    tips       varchar(1024)                      null comment '学员须知',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0
)
    comment '考试表';

drop table if exists paper;
-- auto-generated definition
create table paper
(
    id         bigint                             not null auto_increment comment '试卷id'
        primary key,
    examId     bigint                             not null comment '考试id',
    paperName  varchar(255)                       not null comment '试卷名称',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '试卷表';

drop table if exists choice_question;
-- auto-generated definition
create table choice_question
(
    id         bigint                             not null auto_increment comment '选择题题目id'
        primary key,
    examName   varchar(30)                        not null comment '课程名称',
    question   varchar(255)                       not null comment '题目内容',
    answerA    varchar(255)                       not null comment '选型A',
    answerB    varchar(255)                       not null comment '选型B',
    answerC    varchar(255)                       not null comment '选型C',
    answerD    varchar(255)                       not null comment '选型D',
    answer     varchar(20)                        not null comment '正确答案',
    analysis   varchar(255)                       null comment '题目解析',
    score      int      default 4                 not null comment '题目分数',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '选择题题库';

drop table if exists judge_question;
-- auto-generated definition
create table judge_question
(
    id         bigint                             not null auto_increment comment '判断题题目id'
        primary key,
    examName   varchar(30)                        not null comment '课程名称',
    question   varchar(255)                       not null comment '题目内容',
    answer     tinyint                            null comment '0-正确 1-错误',
    analysis   varchar(255)                       null comment '题目解析',
    score      int      default 2                 not null comment '题目分数',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '判断题题库';

drop table if exists paper_question;
-- auto-generated definition
create table paper_question
(
    id               bigint                             not null auto_increment comment 'id'
        primary key,
    choiceQuestionId bigint comment '选择题id',
    judgeQuestionId  bigint comment '判断题id',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '试卷题目关联表';

drop table if exists score;
-- auto-generated definition
create table score
(
    id         bigint                             not null auto_increment comment '成绩id'
        primary key,
    examId     bigint                             not null comment '考试id',
    userId     bigint                             not null comment '用户id',
    examName   bigint                             not null comment '考试名称',
    finalScore int                                not null comment '总成绩',
    answerTime varchar(20) comment '答题时间',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '成绩表';

drop table if exists video;
-- auto-generated definition
create table video
(
    id          bigint                             not null auto_increment comment '视频id'
        primary key,
    organPlanId bigint                             not null comment '培训计划id',
    videoTypeId bigint                             not null comment '视频类别id',
    videoTitle  varchar(255)                       not null comment '视频标题',
    coverUrl    varchar(255)                       null comment '视频封面地址',
    videoUrl    varchar(255)                       not null comment '视频地址',
    status      tinyint  default 2                 not null comment '视频状态 0-正常 1-停用 2-审核',
    isPublish   tinyint  default 1                 not null comment '是否发布 0-发布 1-未发布',
    creator     varchar(64)                        null comment '创建者',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater     varchar(64)                        null comment '更新者',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '视频表';

drop table if exists video_type;
-- auto-generated definition
create table video_type
(
    id         bigint                             not null auto_increment comment '视频类别id'
        primary key,
    typeName   varchar(64)                        not null comment '类别名称',
    imageUrl   varchar(255)                       null comment '视频类别图地址',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '视频类别表';

drop table if exists learn_condition;
-- auto-generated definition
create table learn_condition
(
    id         bigint                             not null auto_increment comment 'id'
        primary key,
    userId     bigint                             not null comment '学员id',
    videoId    bigint                             not null comment '视频id',
    isDone     tinyint                            not null default 0 comment '是否完成 0-未完成 1-完成',
    doneTime   datetime                           null comment '完成时间',
    complete   int                                not null check ( complete <= 100 ) default 0 comment '视频观看完成度',
    creator    varchar(64)                        null comment '创建者',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updater    varchar(64)                        null comment '更新者',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除 0-不删除 1-删除'
)
    comment '学习情况表';

