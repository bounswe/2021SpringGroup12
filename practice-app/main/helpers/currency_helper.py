import sqlite3
from flask import Response
from main.db.mapper import currency_rate_mapper

# TODO change this variable in deployment phase!
DB_PATH = "C:/Users/ihsan/PycharmProjects/2021SpringGroup12/practice-app/sqlfiles/practice-app.db"


def validate_get_input(params):
    # necesssary info check
    if "from" not in params:
        return Response("Please provide the -from- field of the currency rate!", status=400)
    if "to" not in params:
        return Response("Please provide the -to- field of the currency rate!", status=400)


def non_existing_curr_rate_check(r):
    #check "to" field validity
    if r["result"] == None:
        return Response("Please provide a legal -to- of the currency rate!", status=400)


def add_db_from_exchangerate(cr):
    # save the currency rate record to db if it is not in db
    con = sqlite3.connect(DB_PATH)
    cur = con.cursor()
    try:
        cur.execute("INSERT INTO Currency_History (date, from_curr, to_curr, rate) VALUES (?,?,?,?)",
                    (cr.date, cr.from_curr, cr.to_curr, cr.rate))
    except:
        pass
    con.commit()
    con.close()


def calculate_amount(r, params):
    if "amount" in params:
        amount = float(params.get("amount"))
        rate = r["info"]["rate"]
        r["result"] = amount * rate
        r["query"]["amount"] = amount
    return r

def add_db_from_user(cr):
    # try to insert record to db.
    con = sqlite3.connect(DB_PATH)
    cur = con.cursor()
    try:
        cur.execute("INSERT INTO Currency_History (date, from_curr, to_curr, rate) VALUES (?,?,?,?)",
                    (cr.date, cr.from_curr, cr.to_curr, cr.rate))
    except Exception as err:
        return Response(str(err), status=403)
    con.commit()
    con.close()


def validate_post_input(curr_fields):
    if "rate" not in curr_fields.keys():
        return Response("Please provide the currency rate value!", status=400)
    if "to" not in curr_fields.keys():
        return Response("Please provide the 'to' of the currency rate!", status=400)
    if "date" not in curr_fields.keys():
        return Response("Please provide the date of the currency rate!", status=400)
    if "from" not in curr_fields.keys():
        return Response("Please provide the 'from' of the currency rate!", status=400)
    try:
        return currency_rate_mapper(curr_fields)
    except Exception as err:
        return Response(str(err), status=409)
