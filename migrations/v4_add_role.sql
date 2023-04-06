insert into role(name, description, secret_code) values ('Unauthorized','A person who has just logged in and has not been verified in any form at all','');
insert into role(name, description, secret_code) values ('Student','Student attending the course','');
insert into role(name, description, secret_code) values ('Lecturer','Teacher who prepares and delivers classes in various settings','');
insert into role(name, description, secret_code) values ('Coordinator','The subject coordinator may be a head of the subject area task force and is responsible for the organization, development, implementation, supervision, and evaluation of the program in his subject','');

insert into permission(name) values ('Sent course group notification'),
('Sent course member notification'),('Create activity category'),
('Create activity'),('Users manager');

insert into permission_role(role_id, permission_id,should_be_displayed) values
(1,1,TRUE)

