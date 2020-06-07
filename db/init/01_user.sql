USE g_exam_gateway;
SET CHARACTER_SET_CLIENT = utf8;
SET CHARACTER_SET_CONNECTION = utf8;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    login_id varchar(255) unique not null,
    password blob not null
);
