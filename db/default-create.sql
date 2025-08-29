create database if not exists WardenOdhran;

CREATE USER '[username]'@'localhost' IDENTIFIED BY '[password]';

CREATE USER '[username]'@'%' IDENTIFIED BY '[password]';

GRANT ALL PRIVILEGES ON WardenOdhran.* TO '[username]'@'localhost';

GRANT ALL PRIVILEGES ON WardenOdhran.* TO '[username]'@'%';

flush privileges;
