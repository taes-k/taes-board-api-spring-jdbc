insert into user(id, password, name, email) values ('test-admin', '123', 'test-admin', 'test@email.com');

insert into user_role(user_id, role) values ('test-admin', 'USER');
insert into user_role(user_id, role) values ('test-admin', 'ADMIN');

insert into ban_word(word) values ('바보');
insert into ban_word(word) values ('멍청이');
insert into ban_word(word) values ('해삼');
insert into ban_word(word) values ('멍게');
insert into ban_word(word) values ('말미잘');