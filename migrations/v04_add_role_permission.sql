insert into permission(name) values ('Users manager');
insert into permission(name) values ('Grade view');
insert into permission(name) values ('Grade edit');

insert into role_permission(role_id, permission_id,should_be_displayed) values (1,1,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,2,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (1,3,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,1,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,2,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (2,3,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,1,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,2,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (3,3,false);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,1,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,2,true);
insert into role_permission(role_id, permission_id,should_be_displayed) values (4,3,true);
