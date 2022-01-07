
## System Requirements

- minimum JDK version: 11
- Gradle

*** 

## Installation Instructions

To build the image we first need to build a bootable jar. Then we can create our image using Dockerfile provided, then run it with `docker-compose.yml` file and `docker-compose`.

- To build the bootable jar of our application, in the root directory of our backend project we need to run:
```
./gradlew bootJar
```

- After gradle built our project, we can create our Docker Image by running
```
docker build . -t beabeeapp/beabee
```
in the same directory as before.

- After creating our Docker Image we can run it using docker compose. In the parent directory of our application, run:
```
docker-compose up -d beabee-server
```
This will run our backend server and an PostgreSQL container at the same docker network. Even if you do not have PostgreSQL image in your system it will pull it from the DockerHub.
