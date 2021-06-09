# Practice-app Flask Application
# To run: read README.md
##
# NOTE: Remember you have to set your virtual environment and install flask
from flask_cors import CORS
import random
<<<<<<< HEAD
from helpers import issue_helper, books_helper
import sqlite3
from db import schemas, mapper
import requests
from flask import Flask, jsonify, Response, request
=======
from main.helpers import issue_helper, books_helper, anime_helper, currency_helper
from pydantic import ValidationError
import sqlite3
from main.db import schemas, mapper
import requests
from flask import Flask, jsonify, Response, request, make_response, abort
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
import sys
sys.path.append(".")

app = Flask(__name__)
app.config['JSON_SORT_KEYS'] = False
<<<<<<< HEAD
CORS(app)
=======

CORS(app)

>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
"""
to read body: request.get_json() or request.form
to read query parameters:  request.args.get(<argname>) or request.args.to_dict() or request.query_string.decode("utf-8")
"""


@app.route('/')
def get_tasks():
    # get all tasks
    return "group12 practice-app"

<<<<<<< HEAD

@app.route('/books/', methods=['GET'])
def get_books():
=======
################################ BOOKS ############################


@app.route('/books/', methods=['GET'])
def get_books():
    print(request.args)
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
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
    # don't return all books, return just as much as user wants
    books = books_helper.get_n(books, request.args)
    return books if type(books) != list else schemas.BookResponse(num_results=len(books), books=books).__dict__


@app.route('/books/', methods=['POST'])
def create_book():
    if request.get_json() is None:
        return Response("Body is empty!", status=400)
    book = books_helper.validate_body(request.get_json())
    if type(book) is not schemas.Book:
        return book
    # try to insert book to DB, return forbidden upon failure
    db_response = books_helper.add_book_from_user(book)
    return db_response if db_response is not None else Response("Book added succesfully!", status=200)

<<<<<<< HEAD
=======
################################ ISSUES ############################

>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af

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


<<<<<<< HEAD
@app.route('/issue', methods=['POST'])
def post_issue():
    issue = mapper.issue_mapper(request.get_json())
    issue_helper.insert_single_issue(issue)
=======
@app.route('/issues', methods=['POST'])
def post_issue():
    try:
        issue = mapper.issue_mapper(request.get_json())
    except:
        return Response(f'An error occurred while posting json. Json = {request.get_json()}', status=400)
    issue_helper.insert_multiple_issue([issue])
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
    total_issue = issue_helper.get_issue_count()
    return f'There are total {total_issue} issues in the system'


<<<<<<< HEAD
@app.route('/issue/<int:number>', methods=['GET'])
def get_issue(number: int):
    issue = issue_helper.get_issue(number)
    return jsonify(issue.__dict__)


@app.route('/issue', methods=['GET'])
=======
@app.route('/issues/<int:number>', methods=['GET'])
def get_issue(number: int):
    issue = issue_helper.get_issue(number)
    if issue is None:
        return Response(f"Issue number {number} not found!", status=400)
    return jsonify(issue.__dict__)


@app.route('/issues', methods=['GET'])
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
def get_all_issues():
    max_results = 30
    if request.args.get("max_results") is not None:
        max_results = request.args.get("max_results")
    issue_list = issue_helper.get_all_issues(max_results)
    return jsonify([issue.__dict__ for issue in issue_list])


<<<<<<< HEAD
=======
####################################### ANIME ####################################
@app.route('/anime/search/', methods=['GET'])
def search_anime():
    params = request.args
    # Validation
    validation = anime_helper.validate_search_params(params)
    if validation is not None:
        return validation
    # API Connection
    search_result = anime_helper.jikan_api_search(params)
    if type(search_result) != list:
        return search_result
    # Map result
    searched_animes = [mapper.searched_anime_mapper(
        anime).dict() for anime in search_result]
    # Return results
    return jsonify(searched_animes)


@app.route('/anime/<int:id>', methods=['GET'])
def get_anime(id: int):
    anime_helper.jikan_api_get(id)
    # API Connection
    result = anime_helper.jikan_api_get(id)
    if type(result) == Response:
        return result
    # Map Result
    anime = mapper.anime_mapper(result).dict()
    # Add to DB
    db_response = anime_helper.add_mal_anime_to_db(anime)
    if type(db_response) == Response:
        return db_response
    # Return results
    return anime


@app.route('/anime/', methods=['POST'])
def post_anime():
    # Get the request body
    requestBody = request.json
    # Map to object
    post_anime = mapper.create_anime_mapper(requestBody).dict()
    # Add to DB
    database_response = anime_helper.add_user_anime_to_db(post_anime)
    return Response("Anime added successfully", status=200) if database_response is None else database_response


################################ QUOTES ############################
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
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
        cur.execute("INSERT INTO Quotes(quoteId, quoteAuthor, quoteGenre, quoteText) VALUES (?,?,?,?)",
                    (quote.quoteId, quote.quoteAuthor, quote.quoteGenre, quote.quoteText))
    except Exception as err:
        return Response(str(err), status=403)
    con.commit()
    con.close()

    return Response("Quote added succesfully!", status=200)


@app.route('/quotes/', methods=['GET'])
def get_quote_opt():
    t = requests.get("https://quote-garden.herokuapp.com/api/v3/genres")
    t = t.json()
    t = t['data']
    temp = ''
    for i in t:
        temp = temp + i + ', '
    # select one of them denilebilir html'e geçince ?!?

    if request.args.get("random") is not None:
        rnd = int(random.uniform(0, len(t)))
        r = requests.get(
            "https://quote-garden.herokuapp.com/api/v3/quotes?genre={}".format(t[rnd]))
        r = r.json()
    elif request.args.get("genre") is not None:
        genre_type = request.args.get("genre")
        r = requests.get(
            "https://quote-garden.herokuapp.com/api/v3/quotes?genre={}".format(genre_type))
        r = r.json()
    else:
        temp = "Please provide an genre name or indicate it is random! Possible genres: " + temp
        return Response(temp, status=400)
    quotes = [mapper.quote_mapper(s) for s in r['data']]
    con = sqlite3.connect("../../../sqlfiles/practice-app.db")
    cur = con.cursor()
    for quote in quotes:
        print(quote.quoteId)
        try:
            cur.execute("INSERT INTO Quotes(quoteId, quoteAuthor, quoteGenre, quoteText) VALUES (?,?,?,?)",
                        (quote.quoteId, quote.quoteAuthor, quote.quoteGenre, quote.quoteText))
        except:
            continue
    con.commit()
    con.close()

    return schemas.QuoteResponse(data=quotes).__dict__

<<<<<<< HEAD

# ----------------------------------------------------------------------REFIKA----------------------------------------------------------------------


@app.route('/movies_home/', methods=['GET', 'POST'])
def movies_home():
    return


@app.route('/movies/', methods=['GET'])
def get_movies():
    if "keyword" not in request.args:
        return Response("Please provide a keyword!", status=400)
    # now we are sure keyword parameter is supplied, request rewiew the moves with the keywords from NYTimes API.
    keyword = request.args.get("keyword").title().replace(" ", "+")
    r = requests.get(
        "https://api.nytimes.com/svc/movies/v2/reviews/search.json?query={}&api-key=gJkqRRyjYRV0YDiUDAEXwsa0uZLL6YLh".format(keyword))
    r = r.json()

    # now we have the movies.
    # storing movies in database for further references.
    dict = {}
    if r["results"] != None:
        movies = [mapper.movie_mapper(s) for s in r["results"]]
        con = sqlite3.connect(
            "c:/Users/HP/Desktop/2021SpringGroup12-son/practice-app/sqlfiles/practice-app.db")
        cur = con.cursor()
        for movie in movies:
            # -----------------------------------
            try:
                cur.execute("INSERT INTO Movie(display_title, mpaa_rating, critics_pick, byline, headline,summary_short, link) VALUES (?,?,?,?,?,?,?)",
                            (movie.display_title, movie.mpaa_rating, movie.critics_pick, movie.byline, movie.headline, movie.summary_short, movie.link.url))
            except:
                # do nothing upon failure, this is not a critical process
                continue

        for movie in movies:
            display_title = movie.display_title
            movie_info = {"display_title": movie.display_title, "mpaa_rating": movie.mpaa_rating, "critics_pick": movie.critics_pick,
                          "byline": movie.byline, "headline": movie.headline, "summary_short":  movie.summary_short, "link": movie.link}
            dict[display_title] = movie_info
    # print(jsonify(dict))
    return jsonify(dict)


@ app.route('/movies_addReview/', methods=['POST'])
def create_movie_review():
    #movie_review = request.form.to_dict(flat=True)
    movie_review = request.get_json()
    # make sure that necessary information are given
    # title byline and url should be provided
    if movie_review == {}:
        return Response("Please provide the required information!", status=400)
    if "display_title" not in movie_review.keys():
        return Response("Please provide the title of the movie!", status=400)
    if "byline" not in movie_review.keys():
        return Response("Please provide the reviewer of the movie!", status=400)
    if "link" not in movie_review.keys():
        return Response("Please provide the link!", status=400)
    if "critics_pick" in movie_review.keys():
        if movie_review['critics_pick'] != 1 and movie_review['critics_pick'] != 0:
            return Response("Criticks pick must be 1 or 0")
    try:
        movie = mapper.movie_mapper2(movie_review)
    except Exception as err:
        return Response(str(err), status=400)
        # connect to Database
    con = sqlite3.connect(
        "c:/Users/HP/Desktop/test/practice-app/sqlfiles/practice-app.db")
    cur = con.cursor()

    # try to insert movie to DB, return forbidden upon failure
    try:
        cur.execute("INSERT INTO Movie(display_title, mpaa_rating, critics_pick, byline, headline,summary_short, link) VALUES (?,?,?,?,?,?,?)",
                    (movie.display_title, movie.mpaa_rating, movie.critics_pick, movie.byline, movie.headline, movie.summary_short, movie.link))
    except Exception as err:
        return Response(str(err), status=403)
    con.commit()
    con.close()
    return Response("Movie Review added succesfully!", status=200)


@ app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    # return make_response(jsonify({'error': 'Task was not found'}), 404)
    return "page not found :("
=======
################################ CONVERT ############################


@app.route('/convert/', methods=['GET'])
def convert_currency():
    # necesssary info check
    x = currency_helper.validate_get_input(request.args)
    if x is not None:
        return x

    from_curr = request.args.get("from").upper()
    to_curr = request.args.get("to").upper()
    # get data from exchangerate api
    r = requests.get(
        'https://api.exchangerate.host/convert?from={}&to={}'.format(from_curr, to_curr))
    r = r.json()
    # check "to" rate exitance
    check = currency_helper.non_existing_curr_rate_check(r)
    if check is not None:
        return check

    cr = mapper.currency_rate_mapper(r)

    # save the currency rate record to db if it is not in db
    currency_helper.add_db_from_exchangerate(cr)

    # calculate money with amount value if wanted
    r = currency_helper.calculate_amount(r, request.args)

    r.pop("historical")
    r.pop("motd")

    return r


@app.route('/convert/', methods=['POST'])
def create_currency_hist():

    # check if the necessary information for the record is given. Otherwise 400 error.
    cr = currency_helper.validate_post_input(request.get_json())
    if type(cr) is not schemas.CurrencyRate:
        return cr

    # try to insert record to db.
    db_response = currency_helper.add_db_from_user(cr)

    return db_response if db_response is not None else Response("You have successfully inserted your currency rate to db", status=200)


################################ GENERAL RESPONSES ############################

@app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    # return make_response(jsonify({'error': 'Task was not found'}), 404)
    return "404"


@app.errorhandler(ValidationError)
def validation_error(error):
    return make_response({"msg": "Validation error"}, 400)


@app.errorhandler(500)
def internal_error(error):
    return make_response({"msg": "Internal error"}, 500)


@app.errorhandler(429)
def rate_limit(error):
    return make_response({"msg": "Api is rate limited please wait and try again"}, 429)
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af


if __name__ == '__main__':
    app.run(debug=True)
