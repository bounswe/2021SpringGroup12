FROM python:3.9-slim-buster

# set a directory for the app
WORKDIR /usr/src/app

# copy all the files to the container
COPY . .

# install dependencies
RUN pip install --no-cache-dir -r ./requirements.txt
ENV FLASK_APP=main/main.py
ENV FLASK_ENV=development
# tell the port number the container should expose
EXPOSE 5000

# run the command

CMD [ "python3", "-m" , "flask", "run", "-h", "0.0.0.0"]
