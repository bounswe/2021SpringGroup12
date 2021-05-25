# Practice-app Flask Application
# To run: read README.md
##
# NOTE: Remember you have to set your virtual environment and install flask

from flask import Flask,jsonify
from flask import json
from flask import make_response
from flask import abort
from flask import request
import requests
from pydantic import BaseModel
import schemas, mapper

app = Flask(__name__)


"""
to read body: request.get_json()
to read query parameters:  request.args.get(<argname>) or request.args.to_dict() or request.query_string.decode("utf-8")
"""


@app.route('/')
def get_tasks():
    # get all tasks
    return "group12 practice-app"


@app.route('/books/', methods=['GET'])
def get_books():

    author_name = request.args.get("name").title().replace(" ","+")
    r = requests.get("https://api.nytimes.com/svc/books/v3/reviews.json?author={}&api-key=Ug3r4KRJxcd69bNw5i89CWxArqiGlrGB".format(author_name))
    r = r.json()
    if request.args.get("no_copyright" == 0):
        r.pop("copyright")

    ## don't return all books, return just as much as user wants
    if request.args.get("max_results") is not None and int(r["num_results"]) > int(request.args.get("max_results")):
        max_results = int(request.args.get("max_results"))
        r["num_results"] = max_results
        r["results"] = r["results"][:max_results]
    if request.args.get("no_copyright" == 0):
        r = schemas.BookResponse(num_results = r["num_results"],books=[mapper.book_mapper(s).__dict__ for s in r["results"]]).__dict__
    else:
        r = schemas.BookResponse(copyright =r["copyright"], num_results = r["num_results"],books=[mapper.book_mapper(s).__dict__ for s in r["results"]]).__dict__
    return r



@app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    #return make_response(jsonify({'error': 'Task was not found'}), 404)
    return "404"
