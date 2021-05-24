# Practice-app Flask Application
# To run: read README.md
##
# NOTE: Remember you have to set your virtual environment and install flask

from flask import Flask
from flask import json
from flask import make_response
from flask import abort
from flask import request
import requests

app = Flask(__name__)


"""
to read body: request.get_json()
to read query parameters:  request.args.get(<argname>) or request.query_string.decode("utf-8")
"""


@app.route('/')
def home():
    return "group12 practice-app"


@app.route('/books/', methods=['GET'])
def get_books():

    author_name = request.args.get("name").title().replace(" ","+")
    r = requests.get("https://api.nytimes.com/svc/books/v3/reviews.json?author={}&api-key=Ug3r4KRJxcd69bNw5i89CWxArqiGlrGB".format(author_name))
    r = r.json()

    ## don't return all books, return just as much as user wants
    if request.args.get("max_results") is not None and int(r["num_results"]) > int(request.args.get("max_results")):
        max_results = int(request.args.get("max_results"))
        r["num_results"] = max_results
        r["results"] = r["results"][:max_results]
    
    ## TODO: implement other query parameters like sorting wrt to publish date, excluding isbn, etc.

    """
    # for debug purposes:
         for entry in r.json():
        if entry =="results":
            print(entry, " : ")
            for entry2 in r.json()[entry]:
                if type(entry2) == dict:
                    for entry3 in entry2:
                        print("\t",entry3,":",entry2[entry3])
                else:
                    print(entry2)
                print()
        else:
            print(entry, " : ",r.json()[entry])
    """
    return json.dumps(r)



@app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    #return make_response(jsonify({'error': 'Task was not found'}), 404)
    return "404"
