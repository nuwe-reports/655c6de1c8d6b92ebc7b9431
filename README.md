# java-template

API rest for handling appointment in a health center. User can book appointment using
a rest service.

This service is designed according to this class diagrams which indicate how the objects
interact to solve this business

# Class Diagram

![](/home/georman/Documents/COURSES/655c6de1c8d6b92ebc7b9431/Appointment.png)

An appointment is booked by room, doctor and start and finish date. this will be
more or less one hour ,and it can't overlap with another appointment in the same room
with the same Doctor.

# How to compile the code

1. Clone the project in your local machine
```bash
   git clone https://github.com/nuwe-reports/655c6de1c8d6b92ebc7b9431.git
```

2. Build the project
```bash
  mvn clean package
```

3. Run locally, be careful tu use the .env file
```bash
  mvn springboot:run

```

# How to deploy the code
 the code was build using docker compose to make a quick deployment in your local machine
but it is needed a .env file with the following values 
 MYSQLDB_USER= data database user
 MYSQLDB_ROOT_PASSWORD= root database password
 MYSQLDB_DATABASE= database name
 MYSQLDB_PASSWORD= database password
 MYSQLDB_LOCAL_PORT= local port for mysql default 3306
 MYSQLDB_DOCKER_PORT= local port fo mysql in the host machine
 SPRING_LOCAL_PORT= spring local port default is 8080
 SPRING_DOCKER_PORT= spring docker port default is 8080
 DOCKER_DEBUG_PORT=  debug port to test the container code
 DATE_FORMAT=

```bash
  docker-compose up --build
```

if you have built the docker images only can execute
```bash
  docker-compose up 
```
