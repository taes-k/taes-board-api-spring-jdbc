create table oauth_client_details
(
    client_id varchar(256) not null
        primary key,
    resource_ids varchar(256) null,
    client_secret varchar(256) null,
    scope varchar(256) null,
    authorized_grant_types varchar(256) null,
    web_server_redirect_uri varchar(256) null,
    authorities varchar(256) null,
    access_token_validity int null,
    refresh_token_validity int null,
    additional_information varchar(4096) null,
    autoapprove varchar(256) null
);

create table user
(
    id varchar(40) not null
        primary key,
    password varchar(400) not null,
    name varchar(20) not null,
    email varchar(200) not null,
    reg_dt timestamp default CURRENT_TIMESTAMP not null,
    deleted boolean not null default false
);

create table user_role
(
    seq int auto_increment
        primary key,
    user_id varchar(40) not null,
    role varchar(10) not null,
    deleted tinyint(1) default 0 not null,
    constraint user_role_user_id_role_uindex
        unique (user_id, role)
);

create table board
(
    seq int auto_increment comment '게시판 Seq'
        primary key,
    title varchar(100) not null comment '게시판 제목',
    contents varchar(200) null comment '게시판 내용',
    deleted tinyint(1) default 0 not null comment '삭제여부'
);

create table post
(
    seq int auto_increment comment '게시물 seq'
        primary key,
    title varchar(300) not null comment '게시물 제목',
    contents text not null comment '게시물 본문',
    board_seq int not null comment '게시판 seq',
    user_id varchar(200) not null comment '작성자 ID',
    reg_dt timestamp default CURRENT_TIMESTAMP not null comment '작성 일시',
    chg_dt timestamp default CURRENT_TIMESTAMP not null comment '수정 일시 ',
    deleted tinyint(1) default 0 not null comment '삭제 여부'
);

create index post_deleted_board_seq_index
    on post (deleted, board_seq);

create index post_deleted_user_id_reg_dt_index
    on post (deleted, user_id, reg_dt);


create table ban_word
(
    word varchar(100) not null comment '금칙어'
        primary key,
    reg_dt timestamp default CURRENT_TIMESTAMP null comment '등록 일시',
    deleted tinyint(1) default 0 not null comment '삭제여부'
);

create table comment
(
    seq int auto_increment comment '댓글 seq'
        primary key,
    contents varchar(500) not null comment '댓글 contents',
    post_seq int not null comment '게시글 seq',
    user_id varchar(200) not null comment '사용자 id',
    chg_dt timestamp default CURRENT_TIMESTAMP null comment '수정 일시',
    deleted tinyint(1) default 0 not null comment '삭제 여부',
    reg_dt timestamp default CURRENT_TIMESTAMP not null comment '등록 일시'
);

create index comment_post_seq_deleted_index
    on comment (post_seq, deleted);


create table user_rank
(
    user_id varchar(200) not null comment '사용자 id'
        primary key,
    score int default 0 not null comment '점수',
    `rank` int null comment '순위',
    reg_dt timestamp default CURRENT_TIMESTAMP not null comment '등록 일시',
    chg_dt timestamp default CURRENT_TIMESTAMP not null comment '수정 일시'
);



