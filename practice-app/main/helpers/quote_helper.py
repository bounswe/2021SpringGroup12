import sqlite3
from flask import Response
import requests
from main.db.mapper import quote_mapper

DB_PATH = "C:\\Users\gokay\\Desktop\\TERM-6\\2021SpringGroup12\\practice-app\\sqlfiles"
DB_PATH = "/home/veyis/Desktop/2021SpringGroup12-8e1c54b7896240a6d22027d0a291d6359b737675/practice-app/sqlfiles/practice-app.db"


def get_genres():
    t = requests.get("https://quote-garden.herokuapp.com/api/v3/genres")
    t = t.json()
    return t['data']


def validate_input(params):

    if "genre" not in params:
        temp = "Please provide a valid genre name!"
        return Response(temp, status=400)


def call_quote_api(params):
    r = requests.get("https://quote-garden.herokuapp.com/api/v3/quotes?genre={}".format(params))

    if r.status_code == 400:
        return Response(f"Genre \"{params}\" could not found in database!", status=400)
    r = r.json()
    return r["data"]


def add_quotes_quote_garden(quotes):
    con = sqlite3.connect(DB_PATH)
    cur = con.cursor()
    for quote in quotes:
        try:
            cur.execute("INSERT INTO Quotes(quoteId, quoteAuthor, quoteGenre, quoteText) VALUES (?,?,?,?)", (quote.quoteId, quote.quoteAuthor, quote.quoteGenre, quote.quoteText))
        except:
            continue
    con.commit()
    con.close()


def add_quote_from_user(quote):
    try:
        con = sqlite3.connect(DB_PATH )
        cur = con.cursor()
        # try to insert quote to DB, return forbidden upon failure
        cur.execute("INSERT INTO Quotes(quoteId, quoteAuthor, quoteGenre, quoteText) VALUES (?,?,?,?)",
                    (quote.quoteId, quote.quoteAuthor, quote.quoteGenre, quote.quoteText))
        con.commit()
        con.close()
    except Exception as err:
        return Response(str(err), status=403)


def quote_validate(quote_fields):
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
        return quote_mapper(quote_fields)
    except Exception as err:
        return Response(str(err), status=409)