# CarWashService

## Description:
Car Wash Service is a web application for managing car wash appointment records and services. You can add appointments on specific time or next free time. 
Add new services and edit existing. 

## Technologies
Java 17,
Spring Boot 2.7.5,
PostgreSQL 14,
FlyWay 9.11.0,
Swagger 2

## Features:

- Manage car wash appointment records
- Display a list of all appointments and services
- Add new appointments and services
- Delete existing appointments and services
- Edit existing services

## User's guide:
### 1) Clone the repository to your computer

bash

git clone https://github.com/YakovKontarook/CarWashService.git


### 2) Open the project in a development environment, such as IntelliJ IDEA

### 3) Run the application through the CarWashServiceApplication.java class

### 4) Open a web browser and go to http://localhost:8080/swagger-ui/

### 4) Create a new user on signup endpoint

### 5) Add admin rights 

In the table user_roles change role_id to '2' 

### 6) Login
- Go to http://localhost:8080/swagger-ui/ 
- Choose login endpoint
- Enter username and password you just registered
- Copy token without quotes
![image](https://user-images.githubusercontent.com/88117408/216066985-8f2e74ea-62c1-4bd3-a760-cfc5252f8044.png)

- Click on 'Authorize' button and paste your token

### 6) Use any andpoint on http://localhost:8080/swagger-ui/

# NOTE 
Sometimes you need to change time settings of Database in a development environment 
In Intellij Idea: Data Sources and Drivers -> Options -> Make sure timezone matches your timezone
