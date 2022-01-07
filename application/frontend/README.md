## System Requirements

- Minimum npm version: 8.0.0
- One of web browsers: Chrome, firefox, safari
- Devices need Internet connection to connect the frontend

***

## Installation Instructions
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
