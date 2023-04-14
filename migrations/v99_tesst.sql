insert into login_user(username,password,firstname,lastname,role_id) values ('test','$2a$10$LFqfv5Nt22enZ0pctrAT4e7Ru4RNc6rFeHGDWrhOyLQaaW2pygzTO','test','test',1);
insert into login_user(username,password,firstname,lastname,role_id) values ('student','$2a$10$bzGDiQIL/YVOFAGXrAIXk.7hf4JioELIsmy6kwT39Gn4i.Q.CHpTy','student','student',2);
insert into login_user(username,password,firstname,lastname,role_id) values ('mod','$2a$10$XwMIl8yC/TxaD4Xzpmla.OntZ3cnYIeW7OGSxFjCI3NVS3BY7KsWO','student','student',3);
insert into login_user(username,password,firstname,lastname,role_id) values ('admin','$2a$10$5NEvaF7euFDlEwoVEWqD.eoNk356nt2DzxbYGrRrththXAZzv8rqi','student','student',4);


insert into course_group(name, instructor_id, secret_code) values('inf1',1,'iLoveKotlin<3'),('inf2',1,'iLoveIcon<3');

insert into activity_category(name, description, instructor_id)
values
    ('praca na zajeciach','wykonanie wszystkich cwiczen, aktywne uczestnictwo w zajeciach',1),
    ('wejsciowki','sprawdzenie czy student przyswoil sobie material z poprzednich labow',1),
    ('zadanie domowe','samodzielna praca domowa',1),
    ('inne','...',1);

insert into activity(name, duration, max_score, description, activity_type_id, activity_category_id)
values  ('kartkowka','PT12H',4.5,'jakis opis',1,2);
--
insert into course_group_activity(activity_id, course_group_id, start_date) values (1,1,'2023-12-12T12:00:00');
insert into course_group_activity(activity_id, course_group_id, start_date) values (1,2,'2023-12-12T15:00:00');