insert into permission(name) values ('Users manager');
insert into permission(name) values ('Grade view');
insert into permission(name) values ('Grade edit');
insert into permission(name) values ('Activity manager');
insert into permission(name) values ('Activity category manager');
insert into permission(name) values ('Notification view');
insert into permission(name) values ('CSV generation');
insert into permission(name) values ('Temporary event view');
insert into permission(name) values ('Leaderboard view');

insert into role_permission(role_id, permission_id,should_be_displayed) values (1,1,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,2,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,3,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,4,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,5,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,6,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,7,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,8,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,9,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,1,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,2,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,3,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,4,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,5,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,6,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,7,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,8,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,9,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,1,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,2,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,3,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,4,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,5,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,6,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,7,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,8,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,9,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,1,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,2,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,3,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,4,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,5,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,6,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,7,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,8,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,9,false);
