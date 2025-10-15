# 营养搭配服务平台数据库初始化
# @author zcw

-- 创建库
create database if not exists nutrition_platform;

-- 切换库
use nutrition_platform;

-- 用户表
create table if not exists user
(
    user_id     bigint auto_increment comment '用户ID' primary key,
    username    varchar(50)                           not null comment '用户名',
    password    varchar(255)                          not null comment '密码（加密存储）',
    email       varchar(100)                          null comment '邮箱',
    phone       varchar(20)                           null comment '手机号',
    real_name   varchar(50)                           null comment '真实姓名',
    gender      tinyint                               null comment '性别：0-女，1-男',
    age         int                                   null comment '年龄',
    height      decimal(5, 2)                         null comment '身高（cm）',
    weight      decimal(5, 2)                         null comment '体重（kg）',
    health_goal tinyint                               null comment '健康目标：1-减肥，2-增肌，3-维持健康',
    user_role   varchar(20) default 'user'            not null comment '用户角色：user/admin',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    status      tinyint     default 1                 not null comment '状态：0-禁用，1-启用',
    is_delete   tinyint     default 0                 not null comment '是否删除',
    unique key uk_username (username),
    unique key uk_email (email),
    index idx_phone (phone)
) comment '用户表' collate = utf8mb4_unicode_ci;

-- 用户健康档案表
create table if not exists user_health_profile
(
    profile_id          bigint auto_increment comment '档案ID' primary key,
    user_id             bigint                             not null comment '用户ID',
    chronic_diseases    text                               null comment '慢性疾病史',
    allergies           text                               null comment '过敏史',
    dietary_preferences text                               null comment '饮食偏好',
    forbidden_foods     text                               null comment '忌口食物',
    exercise_habits     text                               null comment '运动习惯',
    activity_level      tinyint                            null comment '活动水平：1-久坐，2-轻度，3-中度，4-重度',
    create_time         datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    foreign key (user_id) references user (user_id) on delete cascade,
    unique key uk_user_id (user_id)
) comment '用户健康档案表' collate = utf8mb4_unicode_ci;

-- 营养需求表
create table if not exists nutrition_requirement
(
    requirement_id  bigint auto_increment comment '需求ID' primary key,
    user_id         bigint                             not null comment '用户ID',
    bmi             decimal(5, 2) comment 'BMI指数',
    bmi_status      tinyint                            null comment 'BMI状态：0-偏瘦 1-正常，2-偏重，3-肥胖',
    bmr             decimal(8, 2)                      null comment '基础代谢率（kcal）',
    assessment_date date                               not null comment '评估日期',
    daily_calories  decimal(8, 2)                      not null comment '每日热量需求（kcal）',
    protein         decimal(8, 2)                      not null comment '蛋白质需求（g）',
    carbohydrate    decimal(8, 2)                      not null comment '碳水化合物需求（g）',
    fat             decimal(8, 2)                      not null comment '脂肪需求（g）',
    fiber           decimal(8, 2)                      null comment '膳食纤维需求（g）',
    vitamin_a       decimal(8, 2)                      null comment '维生素A需求（μg）',
    vitamin_c       decimal(8, 2)                      null comment '维生素C需求（mg）',
    calcium         decimal(8, 2)                      null comment '钙需求（mg）',
    iron            decimal(8, 2)                      null comment '铁需求（mg）',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    foreign key (user_id) references user (user_id) on delete cascade,
    index idx_user_date (user_id, assessment_date)
) comment '营养需求表' collate = utf8mb4_unicode_ci;

-- 食物营养数据表
create table if not exists food_nutrition
(
    food_id               bigint auto_increment comment '食物ID' primary key,
    food_name             varchar(100)                       not null comment '食物名称',
    food_category         varchar(50)                        null comment '食物分类',
    calories_per_100g     decimal(8, 2)                      not null comment '每100g热量（kcal）',
    protein_per_100g      decimal(8, 2)                      not null comment '每100g蛋白质（g）',
    carbohydrate_per_100g decimal(8, 2)                      not null comment '每100g碳水化合物（g）',
    fat_per_100g          decimal(8, 2)                      not null comment '每100g脂肪（g）',
    fiber_per_100g        decimal(8, 2)                      null comment '每100g膳食纤维（g）',
    vitamin_a_per_100g    decimal(8, 2)                      null comment '每100g维生素A（μg）',
    vitamin_c_per_100g    decimal(8, 2)                      null comment '每100g维生素C（mg）',
    calcium_per_100g      decimal(8, 2)                      null comment '每100g钙（mg）',
    iron_per_100g         decimal(8, 2)                      null comment '每100g铁（mg）',
    create_time           datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time           datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    unique key uk_food_name (food_name),
    index idx_category (food_category)
) comment '食物营养数据表' collate = utf8mb4_unicode_ci;

-- 饮食方案表
create table if not exists diet_plan
(
    plan_id            bigint auto_increment comment '方案ID' primary key,
    user_id            bigint                             not null comment '用户ID',
    plan_date          date                               not null comment '方案日期',
    plan_name          varchar(100)                       null comment '方案名称',
    breakfast          text                               null comment '早餐内容（JSON格式）',
    lunch              text                               null comment '午餐内容（JSON格式）',
    dinner             text                               null comment '晚餐内容（JSON格式）',
    snacks             text                               null comment '加餐内容（JSON格式）',
    total_calories     decimal(8, 2)                      null comment '总热量',
    total_protein      decimal(8, 2)                      null comment '总蛋白质',
    total_carbohydrate decimal(8, 2)                      null comment '总碳水化合物',
    total_fat          decimal(8, 2)                      null comment '总脂肪',
    status             tinyint  default 1                 not null comment '状态：1-草稿，2-已确认，3-已执行',
    create_time        datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    foreign key (user_id) references user (user_id) on delete cascade,
    index idx_user_date (user_id, plan_date)
) comment '饮食方案表' collate = utf8mb4_unicode_ci;

-- 饮食记录表
create table if not exists diet_record
(
    record_id    bigint auto_increment comment '记录ID' primary key,
    user_id      bigint                             not null comment '用户ID',
    record_date  date                               not null comment '记录日期',
    meal_type    tinyint                            not null comment '餐别：1-早餐，2-午餐，3-晚餐，4-加餐',
    food_name    varchar(100)                       not null comment '食物名称',
    food_amount  decimal(8, 2)                      not null comment '食物分量（g）',
    calories     decimal(8, 2)                      not null comment '热量（kcal）',
    protein      decimal(8, 2)                      not null comment '蛋白质（g）',
    carbohydrate decimal(8, 2)                      not null comment '碳水化合物（g）',
    fat          decimal(8, 2)                      not null comment '脂肪（g）',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    foreign key (user_id) references user (user_id) on delete cascade,
    index idx_user_date (user_id, record_date),
    index idx_meal_type (meal_type)
) comment '饮食记录表' collate = utf8mb4_unicode_ci;