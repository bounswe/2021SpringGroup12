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


@app.route('/cocktails/', methods=['GET'])
def get_cocktails():
    # if name parameter is not supplied, return 400
    if "cocktail_name" not in request.args:
        return Response("Please type a Cocktail name :)", status=400)

    cocktail_name = request.args.get("cocktail_name")#.title()
    r = requests.get("http://www.thecocktaildb.com/api/json/v1/1/search.php?s={}".format(cocktail_name))
    r = r.json()

    if r["drinks"] == None:
        return Response("No cocktail including the keyword!", status=404)
    cocktails = [mapper.cocktail_mapper(s) for s in r["drinks"]]
    # We can have more than one cocktail including the cocktail name (margarita, blue margarita) or the keyword can be any place in the name of the cocktail.
    con = sqlite3.connect("./sqlfiles/practice-app.db")
    
    cur = con.cursor()
    for cocktail in cocktails:
        try:
            cur.execute("INSERT INTO Cocktails(cocktail_name, ingredient_1, ingredient_2, ingredient_3,ingredient_4, glass, instructions) VALUES (?,?,?,?,?,?,?)", (cocktail.cocktail_name, cocktail.ingredient_1, cocktail.ingredient_2, cocktail.ingredient_3, cocktail.ingredient_4, cocktail.glass, cocktail.instructions))
            print("sql connceted")
        except:
            # do nothing upon failure, this is not a critical process
            continue
    con.commit()
    con.close()

    return schemas.CocktailResponse(cocktails=cocktails).__dict__

@app.route('/cocktails/', methods=['POST'])
def create_cocktail():
    cocktail_fields = request.get_json()

    # make sure that necessary information are given
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
        Cocktail = mapper.cocktail_mapper(cocktail_fields)
    except Exception as err:
        return Response(str(err), status=409)
    # connect to Database
    con = sqlite3.connect("./sqlfiles/practice-app.db")
    cur = con.cursor()

    try:
        cur.execute("INSERT INTO Cocktails(cocktail_name,ingredient_1, ingredient_2, ingredient_3,ingredient_4, glass, instructions) VALUES (?,?,?,?,?,?,?)", (Cocktail.cocktail_name, Cocktail.ingredient_1, Cocktail.ingredient_2, Cocktail.ingredient_3, Cocktail.ingredient_4, Cocktail.glass, Cocktail.instructions))
    except Exception as err:
        return Response(str(err), status=403)

    con.commit()
    con.close()
    return Response("Cocktail added succesfully!" ,status=200)




@app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    #return make_response(jsonify({'error': 'Task was not found'}), 404)
    return "404"


if __name__ == '__main__':
    app.run(debug=True)