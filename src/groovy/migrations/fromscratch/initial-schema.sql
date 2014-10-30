CREATE TABLE activation_key
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    activation_email_sent TINYINT NOT NULL,
    activation_key VARCHAR(255) NOT NULL,
    date_created DATETIME NOT NULL,
    user_id BIGINT NOT NULL
);
CREATE TABLE bookmark
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    date_created DATETIME NOT NULL,
    note_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);
CREATE TABLE context
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    context_name VARCHAR(1024) NOT NULL,
    date_created DATETIME NOT NULL,
    description_as_note VARCHAR(280),
    last_updated DATETIME NOT NULL,
    owner_id BIGINT NOT NULL,
    owner_is_teacher TINYINT NOT NULL,
    url VARCHAR(255)
);
CREATE TABLE context_follower
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    context_id BIGINT NOT NULL,
    date_created DATETIME NOT NULL,
    follower_id BIGINT NOT NULL,
    follower_is_teacher TINYINT NOT NULL,
    is_no_more_subscribed TINYINT NOT NULL,
    unsusbscription_date DATETIME
);
CREATE TABLE live_session
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    date_created DATETIME NOT NULL,
    end_date DATETIME,
    note_id BIGINT NOT NULL,
    start_date DATETIME,
    status VARCHAR(10) NOT NULL,
    result_matrix_as_json VARCHAR(255)
);
CREATE TABLE live_session_response
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    answer_list_as_string VARCHAR(255),
    live_session_id BIGINT NOT NULL,
    percent_credit REAL,
    user_id BIGINT NOT NULL
);
CREATE TABLE note
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    author_id BIGINT NOT NULL,
    content VARCHAR(280) NOT NULL,
    context_id BIGINT,
    date_created DATETIME NOT NULL,
    fragment_tag_id BIGINT,
    parent_note_id BIGINT,
    score INT NOT NULL
);
CREATE TABLE note_mention
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    mention_id BIGINT NOT NULL,
    note_id BIGINT NOT NULL
);
CREATE TABLE note_tag
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    note_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL
);
CREATE TABLE packed_question
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    in_text VARCHAR(255) NOT NULL,
    question_type INT NOT NULL
);
CREATE TABLE resource
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    date_created DATETIME NOT NULL,
    description_as_note VARCHAR(280),
    metadata VARCHAR(255),
    url VARCHAR(255) NOT NULL
);
CREATE TABLE resource_follower
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    date_created DATETIME NOT NULL,
    follower_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL
);
CREATE TABLE role
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    authority VARCHAR(12) NOT NULL
);
CREATE TABLE tag
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);
CREATE TABLE user
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    account_expired TINYINT NOT NULL,
    account_locked TINYINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    enabled TINYINT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    normalized_username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    password_expired TINYINT NOT NULL,
    username VARCHAR(255) NOT NULL
);
CREATE TABLE user_answer
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    user_result_id BIGINT NOT NULL
);
CREATE TABLE user_result
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    version BIGINT NOT NULL,
    percent_credit REAL NOT NULL,
    question_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);
CREATE TABLE user_role
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, user_id)
);
ALTER TABLE activation_key ADD FOREIGN KEY (user_id) REFERENCES user (id);
CREATE INDEX FKAFE4D3B62E7CCBC2 ON activation_key (user_id);
ALTER TABLE bookmark ADD FOREIGN KEY (user_id) REFERENCES user (id);
ALTER TABLE bookmark ADD FOREIGN KEY (note_id) REFERENCES note (id);
CREATE INDEX FK7787A536B958760E ON bookmark (note_id);
CREATE INDEX FK7787A5362E7CCBC2 ON bookmark (user_id);
ALTER TABLE context ADD FOREIGN KEY (owner_id) REFERENCES user (id);
CREATE INDEX FK38B735AF9A637BDA ON context (owner_id);
ALTER TABLE context_follower ADD FOREIGN KEY (follower_id) REFERENCES user (id);
ALTER TABLE context_follower ADD FOREIGN KEY (context_id) REFERENCES context (id);
CREATE INDEX FKEF2B10AECD492546 ON context_follower (context_id);
CREATE INDEX FKEF2B10AE96BFD28F ON context_follower (follower_id);
ALTER TABLE live_session ADD FOREIGN KEY (note_id) REFERENCES note (id);
CREATE INDEX FK6C792E43B958760E ON live_session (note_id);
ALTER TABLE live_session_response ADD FOREIGN KEY (user_id) REFERENCES user (id);
ALTER TABLE live_session_response ADD FOREIGN KEY (live_session_id) REFERENCES live_session (id);
CREATE INDEX FK9E4F72BD7BA22C99 ON live_session_response (live_session_id);
CREATE INDEX FK9E4F72BD2E7CCBC2 ON live_session_response (user_id);
ALTER TABLE note ADD FOREIGN KEY (fragment_tag_id) REFERENCES tag (id);
ALTER TABLE note ADD FOREIGN KEY (author_id) REFERENCES user (id);
ALTER TABLE note ADD FOREIGN KEY (parent_note_id) REFERENCES note (id);
ALTER TABLE note ADD FOREIGN KEY (context_id) REFERENCES context (id);
CREATE INDEX FK33AFF28F35BE02 ON note (author_id);
CREATE INDEX FK33AFF2CD492546 ON note (context_id);
CREATE INDEX FK33AFF273CC1035 ON note (fragment_tag_id);
CREATE INDEX FK33AFF2C87CDB79 ON note (parent_note_id);
ALTER TABLE note_mention ADD FOREIGN KEY (note_id) REFERENCES note (id);
ALTER TABLE note_mention ADD FOREIGN KEY (mention_id) REFERENCES user (id);
CREATE INDEX FKD2FA629DE177143 ON note_mention (mention_id);
CREATE INDEX FKD2FA629DB958760E ON note_mention (note_id);
ALTER TABLE note_tag ADD FOREIGN KEY (tag_id) REFERENCES tag (id);
ALTER TABLE note_tag ADD FOREIGN KEY (note_id) REFERENCES note (id);
CREATE INDEX FK5E4355CDB958760E ON note_tag (note_id);
CREATE INDEX FK5E4355CD6A432F66 ON note_tag (tag_id);
ALTER TABLE resource_follower ADD FOREIGN KEY (resource_id) REFERENCES resource (id);
ALTER TABLE resource_follower ADD FOREIGN KEY (follower_id) REFERENCES user (id);
CREATE INDEX FKB2B73D2F96BFD28F ON resource_follower (follower_id);
CREATE INDEX FKB2B73D2F68909C2A ON resource_follower (resource_id);
ALTER TABLE user_answer ADD FOREIGN KEY (user_result_id) REFERENCES user_result (id);
CREATE INDEX FKD80386B24E6C4DD9 ON user_answer (user_result_id);
ALTER TABLE user_result ADD FOREIGN KEY (user_id) REFERENCES user (id);
ALTER TABLE user_result ADD FOREIGN KEY (question_id) REFERENCES packed_question (id);
CREATE INDEX FKF4870F1178EB499A ON user_result (question_id);
CREATE INDEX FKF4870F112E7CCBC2 ON user_result (user_id);
ALTER TABLE user_role ADD FOREIGN KEY (user_id) REFERENCES user (id);
ALTER TABLE user_role ADD FOREIGN KEY (role_id) REFERENCES role (id);
CREATE INDEX FK143BF46A895207E2 ON user_role (role_id);
CREATE INDEX FK143BF46A2E7CCBC2 ON user_role (user_id);
