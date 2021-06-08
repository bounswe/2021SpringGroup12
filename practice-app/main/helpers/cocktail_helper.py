import os, sys
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)
import sqlite3
from sqlite3 import Cursor
from typing import List
from flask import Response, request
import requests
from requests.api import request

from db.schemas import Cocktail
from db.mapper import cocktail_mapper

DB_PATH="/Users/batuhan_mac/Desktop/cmpe/Cmpe352/week9/cocktails/2021SpringGroup12/practice-app/sqlfiles/"

def validate_get_input(params):
    # necesssary info check
    if "cocktail_name" not in params or params== "":
        return Response("Please provide the a cocktail name!", status=400)


def non_existing_cocktail_name_check(r):
    #check "to" field validity
    if r["drinks"] == None:
        return "Please provide an existing cocktail name!"

def add_cocktails_from_user(cocktails):
    con = sqlite3.connect(DB_PATH+"practice-app.db")
    cur = con.cursor()
    for cocktail in cocktails:
        try:
            cur.execute("INSERT INTO Cocktails(cocktail_name, ingredient_1, ingredient_2, ingredient_3,ingredient_4, glass, instructions) VALUES (?,?,?,?,?,?,?)", (cocktail.cocktail_name, cocktail.ingredient_1, cocktail.ingredient_2, cocktail.ingredient_3, cocktail.ingredient_4, cocktail.glass, cocktail.instructions))
        except:
            # do nothing upon failure, this is not a critical process
            continue
    con.commit()
    con.close()

def add_cocktail_from_user(Cocktail):
    # connect to Database
    con = sqlite3.connect(DB_PATH+"practice-app.db")
    cur = con.cursor()
    try:
        cur.execute("INSERT INTO Cocktails(cocktail_name,ingredient_1, ingredient_2, ingredient_3,ingredient_4, glass, instructions) VALUES (?,?,?,?,?,?,?)", (Cocktail.cocktail_name, Cocktail.ingredient_1, Cocktail.ingredient_2, Cocktail.ingredient_3, Cocktail.ingredient_4, Cocktail.glass, Cocktail.instructions))
    except Exception as err:
        return "This cocktail name is taken already! Try different cocktail name."
    con.commit()
    con.close()


def validate_post_cocktail(cocktail_fields):

    if "strDrink" not in cocktail_fields.keys():
        return Response("Please provide the name of the cocktail!", status=400)
    if "strIngredient1" not in cocktail_fields.keys():
        return Response("Please provide the name of the ingredient 1!", status=400)
    if "strIngredient2" not in cocktail_fields.keys():
        return Response("Please provide the name of the ingredient 2!", status=400)    
    if "strIngredient3" not in cocktail_fields.keys():
        return Response("Please provide the name of the ingredient 3!", status=400)
    if "strIngredient4" not in cocktail_fields.keys():
        return Response("Please provide the name of the ingredient 4!", status=400)
    if "strGlass" not in cocktail_fields.keys():
        return Response("Please provide the type of the glass!", status=400)                   
    if "strInstructions" not in cocktail_fields.keys():
        return Response("Please provide the instructions", status=400) 
    try:
        return cocktail_mapper(cocktail_fields)
    except Exception as err:
        return Response(str(err), status=409)