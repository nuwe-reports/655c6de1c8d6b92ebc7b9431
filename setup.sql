CREATE DATABASE IF NOT EXISTS accwe-hospital;

-- create the users for each database
CREATE USER 'accweuser'@'%' IDENTIFIED BY 'somepassword';
GRANT CREATE, ALTER, INDEX, LOCK TABLES, REFERENCES, UPDATE, DELETE, DROP, SELECT, INSERT ON `accwe-hospital`.* TO 'accweuser'@'%';

FLUSH PRIVILEGES;