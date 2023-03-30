-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2023-03-30 19:03:43.387

-- tables
-- Table: activity
CREATE TABLE activity (
                          id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                          name varchar  NOT NULL,
                          startDate date  NOT NULL,
                          endDate int  NOT NULL,
                          max_score int  NOT NULL,
                          description text  NOT NULL,
                          group_id int  NOT NULL,
                          activity_type_id int  NOT NULL,
                          activity_category_id int  NOT NULL,
                          CONSTRAINT activity_ak_1 UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                          CONSTRAINT activity_pk PRIMARY KEY (id)
);

-- Table: activity_category
CREATE TABLE activity_category (
                                   id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                                   name varchar  NOT NULL,
                                   description varchar  NOT NULL,
                                   CONSTRAINT activity_category_ak_1 UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                                   CONSTRAINT activity_category_pk PRIMARY KEY (id)
);

-- Table: activity_type
CREATE TABLE activity_type (
                               id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                               name varchar  NOT NULL,
                               CONSTRAINT activity_type_ak_1 UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                               CONSTRAINT activity_type_pk PRIMARY KEY (id)
);

-- Table: group
CREATE TABLE "group" (
                         id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                         name varchar  NOT NULL,
                         instructor_id int  NOT NULL,
                         secret_code varchar  NOT NULL,
                         CONSTRAINT group_pk PRIMARY KEY (id)
);

-- Table: group_notification
CREATE TABLE group_notification (
                                    id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                                    group_id int  NOT NULL,
                                    date timestamp  NOT NULL,
                                    description varchar  NOT NULL,
                                    autor_id int  NOT NULL,
                                    CONSTRAINT group_notification_pk PRIMARY KEY (id)
);

-- Table: login_user
CREATE TABLE login_user (
                            id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                            username varchar  NOT NULL,
                            password varchar  NOT NULL,
                            firstname varchar  NOT NULL,
                            lastname varchar  NOT NULL,
                            role_id int  NOT NULL,
                            CONSTRAINT login_user_ak_1 UNIQUE (username) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                            CONSTRAINT login_user_pk PRIMARY KEY (id)
);

-- Table: permission
CREATE TABLE permission (
                            id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                            name varchar  NOT NULL,
                            should_be_displayed boolean  NOT NULL,
                            CONSTRAINT permission_ak_1 UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                            CONSTRAINT permission_pk PRIMARY KEY (id)
);

-- Table: permission_role
CREATE TABLE permission_role (
                                 role_id int  NOT NULL,
                                 permission_id int  NOT NULL,
                                 CONSTRAINT permission_role_pk PRIMARY KEY (role_id,permission_id)
);

-- Table: role
CREATE TABLE role (
                      id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                      name varchar  NOT NULL,
                      description text  NOT NULL,
                      secret_code varchar  NOT NULL,
                      CONSTRAINT role_ak_1 UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                      CONSTRAINT role_pk PRIMARY KEY (id)
);

-- Table: score
CREATE TABLE score (
                       id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                       student_id int  NOT NULL,
                       activity_id int  NOT NULL,
                       value int  NOT NULL,
                       CONSTRAINT score_pk PRIMARY KEY (id)
);

-- Table: student_description
CREATE TABLE student_description (
                                     id int  NOT NULL,
                                     index int  NOT NULL,
                                     github_link varchar  NOT NULL DEFAULT '',
                                     group_id int  NOT NULL,
                                     CONSTRAINT student_description_ak_1 UNIQUE (index, github_link) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                                     CONSTRAINT student_description_pk PRIMARY KEY (id)
);

-- Table: student_notification
CREATE TABLE student_notification (
                                      id int  NOT NULL GENERATED ALWAYS AS IDENTITY,
                                      date timestamp  NOT NULL,
                                      description int  NOT NULL,
                                      autor_id int  NOT NULL,
                                      student_id int  NOT NULL,
                                      CONSTRAINT id PRIMARY KEY (id)
);

-- foreign keys
-- Reference: Copy_of_group_notification_group (table: group_notification)
ALTER TABLE group_notification ADD CONSTRAINT Copy_of_group_notification_group
    FOREIGN KEY (group_id)
        REFERENCES "group" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: Copy_of_score_Copy_of_activity (table: score)
ALTER TABLE score ADD CONSTRAINT Copy_of_score_Copy_of_activity
    FOREIGN KEY (activity_id)
        REFERENCES activity (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: activity_activity_category (table: activity)
ALTER TABLE activity ADD CONSTRAINT activity_activity_category
    FOREIGN KEY (activity_category_id)
        REFERENCES activity_category (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: activity_activity_type (table: activity)
ALTER TABLE activity ADD CONSTRAINT activity_activity_type
    FOREIGN KEY (activity_type_id)
        REFERENCES activity_type (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: autor_notification_login_user (table: student_notification)
ALTER TABLE student_notification ADD CONSTRAINT autor_notification_login_user
    FOREIGN KEY (autor_id)
        REFERENCES login_user (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: group_login_user (table: group)
ALTER TABLE "group" ADD CONSTRAINT group_login_user
    FOREIGN KEY (instructor_id)
        REFERENCES login_user (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: group_notification_login_user (table: group_notification)
ALTER TABLE group_notification ADD CONSTRAINT group_notification_login_user
    FOREIGN KEY (autor_id)
        REFERENCES login_user (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: login_user_role (table: login_user)
ALTER TABLE login_user ADD CONSTRAINT login_user_role
    FOREIGN KEY (role_id)
        REFERENCES role (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: permission_role_permission (table: permission_role)
ALTER TABLE permission_role ADD CONSTRAINT permission_role_permission
    FOREIGN KEY (permission_id)
        REFERENCES permission (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: permission_role_role (table: permission_role)
ALTER TABLE permission_role ADD CONSTRAINT permission_role_role
    FOREIGN KEY (role_id)
        REFERENCES role (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: score_student_description (table: score)
ALTER TABLE score ADD CONSTRAINT score_student_description
    FOREIGN KEY (student_id)
        REFERENCES student_description (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: student_group (table: student_description)
ALTER TABLE student_description ADD CONSTRAINT student_group
    FOREIGN KEY (group_id)
        REFERENCES "group" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: student_login_user (table: student_description)
ALTER TABLE student_description ADD CONSTRAINT student_login_user
    FOREIGN KEY (id)
        REFERENCES login_user (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: student_notification_login_user (table: student_notification)
ALTER TABLE student_notification ADD CONSTRAINT student_notification_login_user
    FOREIGN KEY (student_id)
        REFERENCES login_user (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- sequences
-- Sequence: Index
CREATE SEQUENCE Index
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    NO CYCLE
;

-- End of file.

