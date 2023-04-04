insert into login_user(username,password,firstname,lastname,role_id) values ('test','$2a$10$LFqfv5Nt22enZ0pctrAT4e7Ru4RNc6rFeHGDWrhOyLQaaW2pygzTO','test','test',1);

insert into course_group(name, instructor_id, secret_code) values('inf1',1,'iLoveKotlin<3'),('inf2',1,'iLoveIcon<3');

insert into activity(name,start_date,end_date,max_score,description,activity_type_id,activity_category_id) values  ('kartkowka','2024-04-02','2024-04-03',5,'łatwizna!!',1,2);

insert into course_group_activity(activity_id, course_group_id) values (1,1);

insert into course_group_activity(activity_id, course_group_id) values (1,2);