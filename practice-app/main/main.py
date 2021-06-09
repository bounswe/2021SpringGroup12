# Practice-app Flask Application
# To run: read README.md
##
# NOTE: Remember you have to set your virtual environment and install flask

from flask import Flask, jsonify, Response, request
import requests
from db import schemas, mapper
import sqlite3
from helpers import issue_helper, books_helper, quote_helper, cocktail_helper
from helpers.issue_helper import ALL_ISSUES
import random
from flask_cors import CORS

app = Flask(__name__)
app.config['JSON_SORT_KEYS'] = False
CORS(app)
"""
to read body: request.get_json() or request.form
to read query parameters:  request.args.get(<argname>) or request.args.to_dict() or request.query_string.decode("utf-8")
"""


@app.route('/')
def get_tasks():
    # get all tasks
    return "group12 practice-app"


@app.route('/books/', methods=['GET'])
def get_books():
    # validate parameters
    x = books_helper.validate_input(request.args)
    if x is not None:
        return x
    # fetch results from 3rd party api
    books = books_helper.call_nytimes(request.args.get("name"))
    if type(books) != list:
        return books
    # now we have books of this author in books variable.
    # Let's store this book in database for further references.
    books_helper.add_books_from_nytimes(books)
    ## don't return all books, return just as much as user wants
    books = books_helper.get_n(books,request.args)
    return books if type(books) != list else schemas.BookResponse(num_results=len(books), books=books).__dict__


@app.route('/books/', methods=['POST'])
def create_book():
    if request.get_json() is None:
        return Response("Body is empty!",status=400)
    print(request.get_json())
    book = books_helper.validate_body(request.get_json())
    if type(book) is not schemas.Book:
        return book
    # try to insert book to DB, return forbidden upon failure
    db_response = books_helper.add_book_from_user(book)
    return db_response if db_response is not None else Response("Book added succesfully!", status=200)


@app.route('/download_issues', methods=['GET'])
def download_issues():
    param = {**request.args,
             'per_page': 100}
    if 'state' not in param:
        param['state'] = 'all'
    r = requests.get("https://api.github.com/repos/bounswe/2021SpringGroup12/issues",
                     params=param).json()
    issue_list = [mapper.git_issue_mapper(element) for element in r]
    issue_helper.insert_multiple_issue(issue_list)
    total_issue = issue_helper.get_issue_count()
    return f'{len(r)} issues are downloaded. There are total {total_issue} issues in the system'


@app.route('/issue', methods=['POST'])
def post_issue():
    issue = mapper.issue_mapper(request.get_json())
    issue_helper.insert_single_issue(issue)
    total_issue = issue_helper.get_issue_count()
    return f'There are total {total_issue} issues in the system'


@app.route('/issue/<int:number>', methods=['GET'])
def get_issue(number: int):
    issue = issue_helper.get_issue(number)
    return jsonify(issue.__dict__)


@app.route('/issue', methods=['GET'])
def get_all_issues():
    max_results = 30
    if request.args.get("max_results") is not None:
        max_results = request.args.get("max_results")
    issue_list = issue_helper.get_all_issues(max_results)
    return jsonify([issue.__dict__ for issue in issue_list])


@app.route('/addQuotes/', methods=['POST'])
def add_quote():
    if request.get_json() is None:
        return Response("Body is empty!",status=400)
    quote = quote_helper.quote_validate(request.get_json())
    if type(quote) is not schemas.Quote:
        return quote

    db_response = quote_helper.add_quote_from_user(quote)

    return db_response if db_response is not None else Response("Quote added succesfully!", status=200)


@app.route('/quotes/', methods=['GET'])
def get_quote_opt():
    x = quote_helper.validate_input(request.args)
    if x is not None:
        return x

    quoted = quote_helper.call_quote_api(request.args.get("genre"))
    if type(quoted) != list:
        return quoted

    quotes = [mapper.quote_mapper(s) for s in quoted]

    quote_helper.add_quotes_quote_garden(quotes)

    return quotes if type(quotes) != list else schemas.QuoteResponse(data = quotes).__dict__


@app.route('/randomQuotes/', methods=['GET'])
def get_quotes():
    t = quote_helper.get_genres()
    # we get all genres and select one randomly to call
    rnd = int(random.uniform(0, len(t)))

    quoted = quote_helper.call_quote_api(t[rnd])
    if type(quoted) != list:
        return quoted

    quotes = [mapper.quote_mapper(s) for s in quoted]
    quote_helper.add_quotes_quote_garden(quotes)
    return quotes if type(quotes) != list else schemas.QuoteResponse(data=quotes).__dict__


@app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    # return make_response(jsonify({'error': 'Task was not found'}), 404)
    return "page not found :("

@app.route('/cocktails/get_cocktails/', methods=['GET'])
def get_cocktails():
    x = cocktail_helper.validate_get_input(request.args)
    if x is not None:
        return x
        #cocktail_name has captured
    cocktail_name = request.args.get("cocktail_name")
        #taking data in json formats
    r = requests.get("http://www.thecocktaildb.com/api/json/v1/1/search.php?s={}".format(cocktail_name))
    r = r.json()
   
    check = cocktail_helper.non_existing_cocktail_name_check(r)
    if check is not None:
        return check

    cocktails = [mapper.cocktail_mapper(s) for s in r["drinks"]]

            # We can have more than one cocktail including the cocktail name (margarita, blue margarita) or the keyword can be any place in the name of the cocktail.
    cocktail_helper.add_cocktails_from_user(cocktails)

    return schemas.CocktailResponse(cocktails=cocktails).__dict__ 


@app.route('/cocktails/create_cocktail/', methods=['GET','POST'])
def create_cocktail():
    cocktail_fields=request.get_json()
    if cocktail_fields is None:
        return Response("Body is empty!",status=400)
    cocktail = cocktail_helper.validate_post_cocktail(cocktail_fields)
    if type(cocktail) is not schemas.Cocktail:
        return cocktail
        # connect to Database
    db_response=cocktail_helper.add_cocktail_from_user(cocktail)

    return db_response if db_response is not None else Response("Cocktail added succesfully!", status=200)
    
if __name__ == '__main__':
    app.run(debug=True)
