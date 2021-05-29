# Practice-app Flask Application
# To run: read README.md
##
# NOTE: Remember you have to set your virtual environment and install flask

from flask import Flask, jsonify, Response, request
import requests
from db import schemas, mapper
import sqlite3
from helpers import issue_helper
from helpers.issue_helper import ALL_ISSUES

app = Flask(__name__)

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
    # if name parameter is not supplied, return 400
    if "name" not in request.args:
        return Response("Please provide an author name!", status=400)
    # now we are sure name parameter is supplied, request books of this author from NYTimes API.
    author_name = request.args.get("name").title().replace(" ", "+")
    r = requests.get(
        "https://api.nytimes.com/svc/books/v3/reviews.json?author={}&api-key=Ug3r4KRJxcd69bNw5i89CWxArqiGlrGB".format(
            author_name))
    r = r.json()
    # now we have books of this author in r.
    books = [mapper.book_mapper(s) for s in r["results"]]
    # Let's store this book in database for further references.
    con = sqlite3.connect("./sqlfiles/practice-app.db")
    cur = con.cursor()
    for book in books:
        try:
            cur.execute(
                "INSERT INTO Books(book_title, book_author, url, publication_dt, summary, uuid, uri) VALUES (?,?,?,?,?,?,?)",
                (book.book_title, book.book_author, book.url, book.publication_dt, book.summary, book.uuid, book.uri))
            cur.execute("SELECT book_id FROM Books WHERE book_title = ? AND book_author = ?",
                        (book.book_title, book.book_author))
            book_id = cur.fetchone()[0]
            for isbn in book.isbn13:
                cur.execute("INSERT INTO BookISBNs(book_id, isbn) VALUES (?,?)", (book_id, isbn))
        except:
            # do nothing upon failure, this is not a critical process
            continue
    con.commit()
    con.close()
    # if user wants to remove copyright content, make it empty string.
    if request.args.get("no_copyright") == "1":
        r["copyright"] = ""

    ## don't return all books, return just as much as user wants
    if request.args.get("max_results") is not None and int(r["num_results"]) > int(request.args.get("max_results")):
        max_results = int(request.args.get("max_results"))
        r["num_results"] = max_results
        books = books[:max_results]

    return schemas.BookResponse(copyright=r["copyright"], num_results=r["num_results"], books=books).__dict__


@app.route('/books/', methods=['POST'])
def create_book():
    book_fields = request.get_json()

    # make sure that necessary information are given
    if "book_title" not in book_fields.keys():
        return Response("Please provide the name of the book!", status=400)
    if "book_author" not in book_fields.keys():
        return Response("Please provide the name of the author!", status=400)
    try:
        Book = mapper.book_mapper(book_fields)
    except Exception as err:
        return Response(str(err), status=409)
    # connect to Database
    con = sqlite3.connect("./sqlfiles/practice-app.db")
    cur = con.cursor()
    # try to insert book to DB, return forbidden upon failure
    try:
        cur.execute(
            "INSERT INTO Books(book_title, book_author, url, publication_dt, summary, uuid, uri) VALUES (?,?,?,?,?,?,?)",
            (Book.book_title, Book.book_author, Book.url, Book.publication_dt, Book.summary, Book.uuid, Book.uri))
    except Exception as err:
        return Response(str(err), status=403)
    # since isbn's can be a list. Insert those to a seperate table.
    cur.execute("select * from Books")
    if Book.isbn13 is not []:
        data = cur.execute("SELECT book_id FROM Books WHERE book_title = ? AND book_author = ?",
                           (Book.book_title, Book.book_author))
        book_id = data.fetchone()[0]
        for isbn in list(set(Book.isbn13)):
            cur.execute("INSERT INTO BookISBNs(book_id, isbn) VALUES (?,?)", (book_id, isbn))

    con.commit()
    con.close()
    return Response("Book added succesfully!", status=200)


@app.route('/download_issues', methods=['GET'])
def download_issues():
    param = {**request.args,
             'per_page': 100}
    if 'state' not in param:
        param['state'] = 'all'
    r = requests.get("https://api.github.com/repos/bounswe/2021SpringGroup12/issues",
                     params=param).json()
    for element in r:
        issue = issue_helper.make_issue(element)
        ALL_ISSUES[issue['number']] = issue
    return f'{len(r)} issues are downloaded. There are total {len(ALL_ISSUES)} issues in the system'


@app.route('/issue', methods=['POST'])
def post_issue():
    issue = issue_helper.get_issue(request.get_json())
    ALL_ISSUES[issue['number']] = issue
    return f'There are total {len(ALL_ISSUES)} issues in the system'


@app.route('/issue/<int:number>', methods=['GET'])
def get_issue(number: int):
    issue = ALL_ISSUES[number]
    return jsonify(issue)


@app.route('/issue', methods=['GET'])
def get_all_issues():
    if request.args.get("max_results") is not None:
        return jsonify(list(ALL_ISSUES.values())[:min(len(ALL_ISSUES), int(request.args.get("max_results")))])
    return jsonify(ALL_ISSUES.values())


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
    con = sqlite3.connect("../../sqlfiles/practice-app.db")
    cur = con.cursor()
    
    if countryBased:
        cur.execute("SELECT AVG(age) as average_age, COUNT(age) as cnt FROM Name_Infos WHERE name = ? AND country = ?",
                           (name, country))
    else:
        cur.execute("SELECT AVG(age) as average_age, COUNT(age) as cnt FROM Name_Infos WHERE name = ?",
                           [name])
                           
    dbData = cur.fetchone()
    con.close()
    print(dbData)
    
    ageOnDB = dbData[0]
    countOnDB = dbData[1]
    onDB = countOnDB != 0
    
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
        
    result = f'Name: {name}\nAverage Age: {int(resultAge)}\nCount: {resultCount}'
    if countryBased:
        result+=f'\nCountry: {country}'
        
    return Response(result, status=200) 
    
    
@app.route('/put_name_information', methods=['POST'])
def save_new_name_ino():
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
    
    con = sqlite3.connect("../../sqlfiles/practice-app.db")
    cur = con.cursor()
    
    try:
        cur.execute(
            "INSERT INTO Name_Infos(name, age, country) VALUES (?,?,?)",
            (body["name"] ,body["age"], body["country"]))
    except Exception as err:
        return Response(str(err), status=403)
    
    con.commit()
    con.close()

    return Response("Your name information has been added to database!", status=200) 


@app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    # return make_response(jsonify({'error': 'Task was not found'}), 404)
    return "404"


if __name__ == '__main__':
    app.run(debug=True)
