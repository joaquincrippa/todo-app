# todo-app
This app allows you to create and list to-do tasks
It is created to check technical knowledge about Angular and Spring Boot.

## Main features
* Create task.
* List tasks.
* Filter tasks, by description, ID and status.
* Change status, from Pending to Resolved.

## Dependencies
Before you can build this project, you must install and configure the following dependencies on your machine:

    Node.js
    Java 8

## Development

Run the following commands in two separate terminals:

    mvnw
    ng serve

## API Documentation

http://localhost:8080/swagger-ui.html

## Build for Production

Run in order the following commands:

    mvnw -Pprod clean package
    java -jar target/*.war

