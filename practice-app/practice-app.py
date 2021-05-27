# Practice-app Flask Application
# To run: read README.md
##
# NOTE: Remember you have to set your virtual environment and install flask

from flask import Flask, jsonify, Response, json, make_response, abort, request
import requests
from pydantic import BaseModel
from werkzeug.exceptions import HTTPException
import schemas, mapper
import sqlite3


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
    author_name = request.args.get("name").title().replace(" ","+")
    r = requests.get("https://api.nytimes.com/svc/books/v3/reviews.json?author={}&api-key=Ug3r4KRJxcd69bNw5i89CWxArqiGlrGB".format(author_name))
    r = r.json()
    # now we have books of this author in r.
    books = [mapper.book_mapper(s) for s in r["results"]]
    # Let's store this book in database for further references.
    con = sqlite3.connect("./sqlfiles/practice-app.db")
    cur = con.cursor()
    for book in books:
        try:
            cur.execute("INSERT INTO Books(book_title, book_author, url, publication_dt, summary, uuid, uri) VALUES (?,?,?,?,?,?,?)", (book.book_title, book.book_author, book.url, book.publication_dt, book.summary, book.uuid, book.uri))
            cur.execute("SELECT book_id FROM Books WHERE book_title = ? AND book_author = ?", (book.book_title, book.book_author))
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
        r["copyright"] =""

    ## don't return all books, return just as much as user wants
    if request.args.get("max_results") is not None and int(r["num_results"]) > int(request.args.get("max_results")):
        max_results = int(request.args.get("max_results"))
        r["num_results"] = max_results
        books = books[:max_results]
   
    return schemas.BookResponse(copyright =r["copyright"], num_results = r["num_results"],books=books).__dict__

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
        cur.execute("INSERT INTO Books(book_title, book_author, url, publication_dt, summary, uuid, uri) VALUES (?,?,?,?,?,?,?)", (Book.book_title, Book.book_author, Book.url, Book.publication_dt, Book.summary, Book.uuid, Book.uri))
    except Exception as err:
        return Response(str(err), status=403)
    # since isbn's can be a list. Insert those to a seperate table.
    cur.execute("select * from Books")
    if Book.isbn13 is not []:
        data = cur.execute("SELECT book_id FROM Books WHERE book_title = ? AND book_author = ?", (Book.book_title,Book.book_author))
        book_id = data.fetchone()[0]
        for isbn in list(set(Book.isbn13)):
            cur.execute("INSERT INTO BookISBNs(book_id, isbn) VALUES (?,?)", (book_id,isbn))
    
    con.commit()
    con.close()
    return Response("Book added succesfully!" ,status=200)






@app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    #return make_response(jsonify({'error': 'Task was not found'}), 404)
    return "404"


if __name__ == '__main__':
    app.run(debug=True)