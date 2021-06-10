To gain experience with development of a project as a team, we built a practice app here.

Here we have an API, written using The Flask framework.

### How To Run
* Open a terminal.
* Clone the repository: `git clone https://github.com/bounswe/2021SpringGroup12.git;
* Enter the folder: `cd 2021SpringGroup12/practice-up`
* Make sure docker is installed, if not follow: https://docs.docker.com/get-docker/
* To run on your computer, change `        - VUE_APP_API_URL=18.184.69.221:5000` to `         - VUE_APP_API_URL=127.0.0.1:5000` in docker-compose.yml file.
* To run on an AWS instance, change  `        - VUE_APP_API_URL=18.184.69.221:5000` to `         - VUE_APP_API_URL={public_ip_of_the_instance}:5000` in docker-compose.yml file.
* Type `docker-compose build`
* Type `docker-compose up`
* Open up browser, and go `127.0.0.1` if you run on your local or "ip address of the instance" if you run on AWS instance.
