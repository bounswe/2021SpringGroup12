# CMPE451 Group-12
### Description

This is the github repository that we use for CMPE451 as Group-12. You can see more details on our [Wiki](https://github.com/bounswe/2021SpringGroup12/wiki) page

## Group Members
* [Adalet Veyis Turgut](https://github.com/bounswe/2021SpringGroup12/wiki/Adalet-Veyis-Turgut)
* [İhsan Gökcül](https://github.com/bounswe/2021SpringGroup12/wiki/%C4%B0hsan-G%C3%B6kc%C3%BCl)
* [Kürşat Talha Berk Yamanoğlu](https://github.com/bounswe/2021SpringGroup12/wiki/K%C3%BCr%C5%9Fat-Talha-Berk-Yamano%C4%9Flu)
* [Burakcan Kazım Yeşil](https://github.com/bounswe/2021SpringGroup12/wiki/Burakcan-Kazım-Yeşil)
* [Refika Kalyoncu](https://github.com/bounswe/2021SpringGroup12/wiki/Refika-Kalyoncu) (Communicator)
* [Ömer Yılmaz](https://github.com/bounswe/2021SpringGroup12/wiki/%C3%96mer-Y%C4%B1lmaz)
* [Zahit Batuhan Tongarlak](https://github.com/bounswe/2021SpringGroup12/wiki/Batuhan-Tongarlak)
* [Ahmet Berat Can](https://github.com/bounswe/2021SpringGroup12/wiki/Ahmet-Berat-Can)

## Old Members
* [Mehmet Gökay Yıldız](https://github.com/bounswe/2021SpringGroup12/wiki/Mehmet-G%C3%B6kay-Y%C4%B1ld%C4%B1z)
* [Doğukan Türksoy](https://github.com/bounswe/2021SpringGroup12/wiki/Doğukan-Türksoy)</del>


## System Requirements

###  Android

- Minimum Android Sdk version: **21** (Devices with SDK 21 or higher can run the application)
- Compile SDK version: **31** (A computer with SDK 31 can compile the application to create '.apk' file)
- Devices need Internet connection to run the application

###  Backend

- minimum JDK version: 11
- Gradle

###  Frontend

- Minimum npm version: 8.0.0
- One of web browsers: Chrome, firefox, safari
- Devices need Internet connection to connect the frontend

***

## Installation Instructions

###  Android 
Opening the project using Android Studio:

1) After cloning or downloading the project to your machine, you need to open [Android Studio](https://developer.android.com/studio)
2) From Android Studio open an existing project using the menu File>Open. 
3) You need to select '<downloaded_folder>/application/android/' folder from the opened popup menu to open BeABee Android Application.

Creating an .apk file:

1) Open Build>Build Bundle(s) / Apk(s)>Build Apk(s) menu.
2) After the apk is generated there is gonna be dialog on bottom right of the window, from there you can select 'locate' to find the apk file.

Running the application on Emulator:

1) Set-up an emulator device with at least SDK 21, using the instructions on this [page](https://developer.android.com/studio/run/managing-avds)
2) Run the application on the created emulator using the instructions on this [page](https://developer.android.com/studio/run/emulator#runningapp)

###  Backend

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

###  Frontend

Since the backend URL is hardcoded in the project files, first we need to clone the repository. Locate the index.tsx file. In that file change 

```
axios.defaults.baseURL = "http://3.144.201.198:8085/v2";
```
to
```
axios.defaults.baseURL = "http://'backend-ip-address':'port'/v2";
```

To create a Docker Image of the Web Application, we need to build the project via react build script in our machine first. To achieve this we install dependencies.

- To install dependencies in the frontend project's root directory run 
```
npm install
```

- To build the project in the frontend project's root directory run
```
npm run build
```
This uses the build script coming from `react-scripts` dependency to build a product ready build of the application.

- To create the Docker Image, again in the root directory run:
```
docker build . -t beabeeapp/beabee-front
```

- And then you can run this Docker Image you've created via `docker-compose` file in the parent directory of the project run.
```
docker-compose up -d beaabee-front
```
This will run frontend application, backend application and database in the same Docker network.<br />
However since frontend application depends on the backend application, you need to create the backend application's image first. 
