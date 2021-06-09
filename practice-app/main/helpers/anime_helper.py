from os import stat, stat_result
from flask import Response
import requests
import sqlite3
from main.db.mapper import create_anime_mapper
import json


def validate_search_params(params):
    if "query" not in params:
        return Response("Please enter a query string", status=400)
    if len(params["query"]) < 3:
        return Response("Query string must be at least 3 characters long", status=400)
    if "limit" not in params or not params["limit"].isnumeric():
        return Response("Please enter a valid integer as your limit.", status=400)


def jikan_api_search(params):
    query = params["query"]
    limit = params["limit"]
    response = requests.get(
        f"https://api.jikan.moe/v3/search/anime?q={query}&page=1&limit={limit}").json()
    if "status" in response and response["status"] == '429':
        return Response("Api is rate limited please wait and try again", status=429)
    return response["results"]


def jikan_api_get(id):
    response = requests.get(f"https://api.jikan.moe/v3/anime/{id}").json()
    if "status" in response and response["status"] == 404:
        return Response("Anime does not exist", status=404)
    if "status" in response and response["status"]==429:
        return Response("Api is rate limited please wait", status=429)
    return response


def add_mal_anime_to_db(anime):
    try:
        sequel_id = None
        prequel_id = None
        con = sqlite3.connect("/usr/src/app/./sqlfiles/practice-app.db")
        cur = con.cursor()
        if anime["sequel"] != None:
          cur.execute("INSERT OR IGNORE INTO RelatedAnimes(title, mal_id) VALUES (?,?)",
          (anime["sequel"].title, anime["sequel"].mal_id))
          data = cur.execute("SELECT id FROM RelatedAnimes WHERE title=? AND mal_id=?",
          (anime["sequel"].title, anime["sequel"].mal_id))
          sequel_id = data.fetchone()[0]
        if anime["prequel"] != None:
          cur.execute("INSERT OR IGNORE INTO RelatedAnimes(title, mal_id) VALUES (?,?)",
          (anime["prequel"].title, anime["prequel"].mal_id))
          data = cur.execute("SELECT id FROM RelatedAnimes WHERE title=? AND mal_id=?",
          (anime["prequel"].title, anime["prequel"].mal_id))
          prequel_id = data.fetchone()[0]
        cur.execute(
            "INSERT OR IGNORE INTO MalAnimes(title, mal_id, episodes, image, airing, start_date, end_date, score, rating, \"type\", synopsis, duration, sequel, prequel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            (anime["title"], anime["mal_id"], anime["episodes"], anime["image"], anime["airing"], anime["start_date"], anime["end_date"], anime["score"], anime["rating"], anime["type"], anime["synopsis"], anime["duration"], sequel_id, prequel_id)
        )
        anime_data = cur.execute("SELECT id FROM MalAnimes WHERE title = ? AND mal_id = ?", (anime["title"], anime["mal_id"]))
        anime_id = anime_data.fetchone()[0]
        for genre in anime["genres"]:
            cur.execute("INSERT OR IGNORE INTO Genres(name) VALUES (?)", (genre,))
            genre_data = cur.execute("SELECT id from Genres WHERE name = ?", (genre,))
            genre_id = genre_data.fetchone()[0]
            cur.execute("INSERT OR IGNORE INTO GenreRelation(anime_id, genre_id) VALUES (?,?)", (anime_id, genre_id))
        con.commit()
        con.close()
    except Exception as err:
      return Response(str(err), status=403)
      
def validate_and_map_user_anime(body):
    fields = ["title", "episodes", "image", "airing", "start_date", "end_date", "score", "rating", "type", "synopsis"]
    for field in fields:
        if not field in body:
            return Response(f"Body must have {field} field.", status=400)
    try:
        return create_anime_mapper(body).dict()
    except Exception as error:
        errors = []
        for err in error.errors():
            type = err["type"].split(".")[1]
            if type == "none":
                errors.append({"field":err["loc"][0], "msg":"Field cannot be empty"})
            else:
                errors.append({"field":err["loc"][0], "msg":"Please enter a valid "+type})
        return Response(response=json.dumps({"err":"Validation", "errs":errors}), status=403)            

def add_user_anime_to_db(anime):
    try:
        con = sqlite3.connect("/usr/src/app/./sqlfiles/practice-app.db")
        cur = con.cursor()
        cur.execute("INSERT INTO UserAnimes(title,episodes,image,airing,start_date,end_date,score,rating,type,synopsis) VALUES (?,?,?,?,?,?,?,?,?,?)",
        (anime["title"], anime["episodes"], anime["image"], anime["airing"], anime["start_date"], anime["end_date"], anime["score"], anime["rating"], anime["type"], anime["synopsis"]))
        con.commit()
        con.close()
    except Exception as err:
        return Response(str(err), status=403)
