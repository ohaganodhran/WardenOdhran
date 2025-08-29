create database if not exists WardenOdhran;

CREATE USER 'WardenUser'@'localhost' IDENTIFIED BY 'LdoZ6jWuyZ';

CREATE USER 'WardenUser'@'%' IDENTIFIED BY 'LdoZ6jWuyZ';

GRANT ALL PRIVILEGES ON WardenOdhran.* TO 'WardenUser'@'localhost';

GRANT ALL PRIVILEGES ON WardenOdhran.* TO 'WardenUser'@'%';

flush privileges;
