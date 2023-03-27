-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2023-03-27 12:28:56.665

-- tables
-- Table: activity
CREATE TABLE activity (
                          id int  NOT NULL,
                          name varchar(30)  NOT NULL,
                          startDate date  NOT NULL,
                          endDate int  NOT NULL,
                          activity_type_id int  NOT NULL,
                          max_score int  NOT NULL,
                          description text  NOT NULL,
                          activity_category_id int  NOT NULL,
                          group_id int  NOT NULL,
                          CONSTRAINT activity_pk PRIMARY KEY (id)
);

-- Table: activity_category
CREATE TABLE activity_category (
                                   id int  NOT NULL,
                                   name varchar(50)  NOT NULL,
                                   description text  NOT NULL,
                                   CONSTRAINT activity_category_pk PRIMARY KEY (id)
);

-- Table: activity_type
CREATE TABLE activity_type (
                               id int  NOT NULL,
                               name varchar(50)  NOT NULL,
                               CONSTRAINT activity_type_pk PRIMARY KEY (id)
);

-- Table: group
CREATE TABLE "group" (
                         id int  NOT NULL,
                         name varchar(50)  NOT NULL,
                         instructor_id int  NOT NULL,
                         CONSTRAINT group_pk PRIMARY KEY (id)
);

-- Table: group_notification
CREATE TABLE group_notification (
                                    id int  NOT NULL,
                                    group_id int  NOT NULL,
                                    date date  NOT NULL,
                                    description text  NOT NULL,
                                    CONSTRAINT group_notification_pk PRIMARY KEY (id)
);

-- Table: instructor
CREATE TABLE instructor (
                            id int  NOT NULL,
                            login varchar(50)  NOT NULL,
                            password varchar(50)  NOT NULL,
                            CONSTRAINT instructor_pk PRIMARY KEY (id)
);

-- Table: score
CREATE TABLE score (
                       id int  NOT NULL,
                       student_index varchar(6)  NOT NULL,
                       activity_id int  NOT NULL,
                       value int  NOT NULL,
                       CONSTRAINT score_pk PRIMARY KEY (id)
);

-- Table: student
CREATE TABLE student (
                         index varchar(6)  NOT NULL,
                         nick varchar(50)  NOT NULL,
                         password varchar(50)  NOT NULL,
                         github_link varchar  NOT NULL,
                         group_id int  NOT NULL,
                         CONSTRAINT student_pk PRIMARY KEY (index)
);

-- Table: student_notification
CREATE TABLE student_notification (
                                      id int  NOT NULL,
                                      student_index varchar(6)  NOT NULL,
                                      date date  NOT NULL,
                                      description int  NOT NULL,
                                      CONSTRAINT id PRIMARY KEY (id)
);

-- foreign keys
-- Reference: activity_activity_type (table: activity)
ALTER TABLE activity ADD CONSTRAINT activity_activity_type
    FOREIGN KEY (activity_type_id)
        REFERENCES activity_type (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: activity_group (table: activity)
ALTER TABLE activity ADD CONSTRAINT activity_group
    FOREIGN KEY (group_id)
        REFERENCES "group" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: category_id (table: activity)
ALTER TABLE activity ADD CONSTRAINT category_id
    FOREIGN KEY (activity_category_id)
        REFERENCES activity_category (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: group_instructor (table: group)
ALTER TABLE "group" ADD CONSTRAINT group_instructor
    FOREIGN KEY (instructor_id)
        REFERENCES instructor (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: message_group (table: group_notification)
ALTER TABLE group_notification ADD CONSTRAINT message_group
    FOREIGN KEY (group_id)
        REFERENCES "group" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: notification_student (table: student_notification)
ALTER TABLE student_notification ADD CONSTRAINT notification_student
    FOREIGN KEY (student_index)
        REFERENCES student (index)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: points_activities (table: score)
ALTER TABLE score ADD CONSTRAINT points_activities
    FOREIGN KEY (activity_id)
        REFERENCES activity (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: points_student (table: score)
ALTER TABLE score ADD CONSTRAINT points_student
    FOREIGN KEY (student_index)
        REFERENCES student (index)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: student_group (table: student)
ALTER TABLE student ADD CONSTRAINT student_group
    FOREIGN KEY (group_id)
        REFERENCES "group" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- End of file.

