insert into role(name, description, secret_code) values ('Unauthorized','A person who has just logged in and has not been verified in any form at all','$2a$10$7f4XCjvaUToX3TSSEp8x7On24ERbQ7leqB/7ukYW//ynMLmeqP.Fq');
insert into role(name, description, secret_code) values ('Student','Student attending the course','$2a$10$HEyjzhsZv4daLihyZFD87Ov.qZnS7mG62RlC2Ei2Fb.SME6TRs0QW');
insert into role(name, description, secret_code) values ('Lecturer','Teacher who prepares and delivers classes in various settings','$2a$10$bhz5toZ2ywfz3MSHO5jzwerSDiJNDkw15.EP9Genp5hhyECvjPLde');
insert into role(name, description, secret_code) values ('Coordinator','The subject coordinator may be a head of the subject area task force and is responsible for the organization, development, implementation, supervision, and evaluation of the program in his subject','$2a$10$8Z46aKr8kuqRV4jD0clzkuhQrbrFRmxkVwCL3vPfBaSjlkbPeXBdi');

insert into permission(name) values ('Sent course group notification'),
('Sent course member notification'),('Create activity category'),
('Create activity'),('Users manager');

