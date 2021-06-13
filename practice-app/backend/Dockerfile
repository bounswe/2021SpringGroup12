FROM python:3.9-slim-buster

# set a directory for the app
WORKDIR /usr/src/app

# copy all the files to the container
COPY . .

# install dependencies
RUN pip install --no-cache-dir -r ./requirements.txt
#TODO run database_initializer.py here
ENV NYTIMES_KEY=gJkqRRyjYRV0YDiUDAEXwsa0uZLL6YLh
#ENV DB_PATH=/usr/src/app/./sqlfiles/practice-app.db

ARG DB_PATH
ENV DB_PATH $DB_PATH

RUN python3 ./main/db/database_initializer.py
ENV FLASK_APP=main/main.py
ENV FLASK_ENV=development

# tell the port number the container should expose
EXPOSE 5000

# run the command

CMD [ "python3", "-m" , "flask", "run", "-h", "0.0.0.0"]