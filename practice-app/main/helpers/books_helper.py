import sqlite3
from flask import Response
import requests
import time
from main.db.mapper import book_mapper

# TODO change this variable in deployment phase!
DB_PATH = "/home/veyis/Desktop/2021SpringGroup12/practice-app/sqlfiles"
DB_PATH = "/home/veyis/Desktop/a2021SpringGroup12/practice-app/sqlfiles"


def validate_input(params):
    # if name parameter is not supplied, return 400
    if "name" not in params:
        return Response("Please provide an author name!", status=400)
    else:
        if params.get("name").replace(" ", "") == "":
            return Response("Please provide nonempty author name!", status=400)


def call_nytimes(params):
    # now we are sure name parameter is supplied, request books of this author from NYTimes API.
    author_name = params.title().replace(" ", "+")
    r = requests.get(
        f"https://api.nytimes.com/svc/books/v3/reviews.json?author={author_name}&api-key=gJkqRRyjYRV0YDiUDAEXwsa0uZLL6YLh")
    if r.status_code >= 500:
        # server is crashed, there is nothing I can do.
        return Response(f"Third party API is down.", status=r.status_code)
    if r.status_code == 400:
        return Response(f"Author \"{author_name}\" could not found in database!", status=400)
    if r.status_code == 429:
        # wait 10 seconds and try again. If that fails again, return 429.
        time.sleep(10)
        r = requests.get(
            f"https://api.nytimes.com/svc/books/v3/reviews.json?author={author_name}&api-key=gJkqRRyjYRV0YDiUDAEXwsa0uZLL6YLh")
        if r.status_code == 400:
            return Response(f"Author \"{author_name}\" could not found in database!", status=400)
        if r.status_code == 429:
            return Response(f"You have sent too many request recently, please wait!", status=429)
    r = r.json()
    return r["results"]


def add_book_from_user(book):
    # connect to Database
    try:
        con = sqlite3.connect(DB_PATH + "/practice-app.db")
        cur = con.cursor()
        cur.execute(
            "INSERT INTO Books(book_title, book_author, url, publication_dt, summary, uuid, uri) VALUES (?,?,?,?,?,?,?)",
            (book.book_title, book.book_author, book.url, book.publication_dt, book.summary, book.uuid, book.uri))
        # since isbn's can be a list. Insert those to a seperate table.
        if book.isbn13 != []:
            cur.execute("select * from Books")
            data = cur.execute("SELECT book_id FROM Books WHERE book_title = ? AND book_author = ?",
                               (book.book_title, book.book_author))
            book_id = data.fetchone()[0]
            for isbn in list(set(book.isbn13)):
                cur.execute(
                    "INSERT INTO BookISBNs(book_id, isbn) VALUES (?,?)", (book_id, isbn))
        con.commit()
        con.close()
    except Exception as err:
        return Response(str(err), status=403)


def add_books_from_nytimes(books):
    con = sqlite3.connect(DB_PATH + "/practice-app.db")
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
                cur.execute(
                    "INSERT INTO BookISBNs(book_id, isbn) VALUES (?,?)", (book_id, isbn))
        except:
            # do nothing upon failure, this is not a critical process
            continue
    con.commit()
    con.close()


def get_n(books, args):
    if args.get("max_results") is None:
        return books
    else:
        max_results = args.get("max_results")
    if not max_results.isdigit():
        return Response(f"max_results should be integer!", status=400)
    if len(books) > int(max_results):
        max_results = int(max_results)
        return books[:max_results]
    else:
        return books


def validate_body(book_fields):
    # make sure that necessary information are given
    if "book_title" not in book_fields.keys():
        return Response("Please provide the name of the book!", status=400)
    if "book_author" not in book_fields.keys():
        return Response("Please provide the name of the author!", status=400)
    try:
        return book_mapper(book_fields)
    except Exception as err:
        return Response(str(err), status=409)
