# Practice-app Flask Application
# To run: read README.md
##
# NOTE: Remember you have to set your virtual environment and install flask

from flask import Flask, jsonify, Response, request
import requests
from db import schemas, mapper
import sqlite3

from helpers import issue_helper, books_helper, name_info_helper
from helpers.issue_helper import ALL_ISSUES
import random

app = Flask(__name__)
app.config['JSON_SORT_KEYS'] = False

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
    print(request.args)
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


@app.route('/quotes/', methods=['POST'])
def add_quote():
    quote_fields = request.get_json()
    # make sure that necessary information are given
    if "_id" not in quote_fields.keys():
        return Response("Please provide an Id for the quote", status=400)
    if "quoteAuthor" not in quote_fields.keys():
        return Response("Please provide the name of the author!", status=400)
    if "quoteGenre" not in quote_fields.keys():
        return Response("Please provide a genre for your quote", status=400)
    if "quoteText" not in quote_fields.keys():
        return Response("Please provide the quote", status=400)
    try:
        quote = mapper.quote_mapper(quote_fields)
    except Exception as err:
        return Response(str(err), status=409)

    # connect to Database
    print(quote)
    con = sqlite3.connect("../../sqlfiles/practice-app.db")
    cur = con.cursor()
    # try to insert quote to DB, return forbidden upon failure
    try:
        cur.execute("INSERT INTO Quotes(quoteId, quoteAuthor, quoteGenre, quoteText) VALUES (?,?,?,?)", (quote.quoteId, quote.quoteAuthor, quote.quoteGenre, quote.quoteText))
    except Exception as err:
        return Response(str(err), status=403)
    con.commit()
    con.close()

    return Response("Quote added succesfully!" ,status=200)


@app.route('/quotes/', methods=['GET'])
def get_quote_opt():
    t = requests.get("https://quote-garden.herokuapp.com/api/v3/genres")
    t = t.json()
    t = t['data']
    temp = ''
    for i in t:
        temp = temp + i + ', '
    #select one of them denilebilir html'e geçince ?!?

    if request.args.get("random") is not None:
        rnd = int(random.uniform(0,len(t)))
        r = requests.get("https://quote-garden.herokuapp.com/api/v3/quotes?genre={}".format(t[rnd]))
        r = r.json()
    elif request.args.get("genre") is not None:   
        genre_type = request.args.get("genre")
        r = requests.get("https://quote-garden.herokuapp.com/api/v3/quotes?genre={}".format(genre_type))
        r = r.json()
    else:
        temp = "Please provide an genre name or indicate it is random! Possible genres: " + temp
        return Response(temp, status=400)
    quotes = [mapper.quote_mapper(s) for s in r['data']]
    con = sqlite3.connect("../../sqlfiles/practice-app.db")
    cur = con.cursor()
    for quote in quotes:
        print(quote.quoteId)
        try:
            cur.execute("INSERT INTO Quotes(quoteId, quoteAuthor, quoteGenre, quoteText) VALUES (?,?,?,?)", (quote.quoteId, quote.quoteAuthor, quote.quoteGenre, quote.quoteText)) 
        except:
            continue
    con.commit()
    con.close()

    
    return schemas.QuoteResponse(data = quotes).__dict__


# Uses Agify api which is on https://api.agify.io/
# Here the assumption that "Agify returns the average age calculated from the data" is made
@app.route('/name_information', methods=['GET'])
def get_name_information():
    name = request.args.get("name")
    country = request.args.get("country")
    onApi = False
    onDB = False
    countryBased = False
    
    if name == None or name.strip() == "":
        return Response("Please provide the name!", status=400)
    
    api_url = f'https://api.agify.io/?name={name}'
    
    if country != None and country.strip()!="":
        countryBased = True
        api_url+=f'&country_id={country}'
    
    r = requests.get(api_url)
    response = r.json()
    
    if response["age"] != None:
        onApi = True
        countOnApi = response["count"]
        ageOnApi = response["age"]
    
    
    #Check db for matching entries to add to the results coming from Agify api
    dbData = name_info_helper.get_name_info(name, country, countryBased)
    
    
    onDB = dbData!=None and dbData[1] != 0
    if onDB:
        ageOnDB = dbData[0]
        countOnDB = dbData[1]
        print(dbData)
    
    #Combine results coming from api and db
    if onApi and onDB:
        resultCount = countOnApi + countOnDB
        resultAge = ((countOnApi*ageOnApi)+(countOnDB*ageOnDB))/resultCount
    elif onApi:
        resultCount = countOnApi
        resultAge = ageOnApi
    elif onDB:
        resultCount = countOnDB
        resultAge = ageOnDB
    else:
        return Response("No registered data for this querry", status=200)
    result = schemas.NameInfoResponse()
    result.name=name
    result.age=int(resultAge)
    result.count=resultCount
    if countryBased:
        result.country = country
    return jsonify(result.__dict__)
    
    
@app.route('/name_information', methods=['POST'])
def save_new_name_info():
    body = request.get_json()

    if body==None:
        return Response("Please provide correct information in body as json", status=400)
    # make sure that necessary information are given
    if "name" not in body:
        return Response("Please provide your name to save to the database!", status=400)
    if "age" not in body:
        return Response("Please provide your age to save to the database!", status=400)
    if "country" not in body:
        return Response("Please provide your country to save to the database!", status=400)
    
    try:
        nameInfo = schemas.NameInfo(
            name=body["name"],
            age=body["age"],
            country=body["country"])
    except Exception:
        return Response("Please provide correct information in body as json", status=400)
        
    success = name_info_helper.insert_name_info(nameInfo)
    
    if not success:
        return Response("Your information couldnt be saved! Please try again later!", status=403)

    return Response("Your name information has been added to database!", status=200) 


@app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    # return make_response(jsonify({'error': 'Task was not found'}), 404)
    return "404"


if __name__ == '__main__':
    app.run(debug=True)
