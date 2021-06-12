# Practice-app Flask Application
# To run: read README.md
# NOTE: Remember you have to set your virtual environment and install flask

from pydantic import ValidationError
import sqlite3
from main.db import schemas, mapper
import requests
from flask import Flask, jsonify, Response, request, make_response
import sys, os
sys.path.append(".")
from main.helpers import issue_helper, books_helper, name_info_helper, anime_helper, currency_helper, quote_helper, cocktail_helper
from flask_swagger_ui import get_swaggerui_blueprint
import random
from flask_cors import CORS
NYTIMESKEY= os.getenv("NYTIMES_KEY")

DB_PATH = os.getenv("DB_PATH","/usr/src/app/./sqlfiles/practice-app.db")
app = Flask(__name__)
app.config['JSON_SORT_KEYS'] = False

CORS(app)

"""
to read body: request.get_json() or request.form
to read query parameters:  request.args.get(<argname>) or request.args.to_dict() or request.query_string.decode("utf-8")
"""

################################ SWAGGER ############################

""" 
@app.route('/static/<path:path>')
def send_static(path):
    return send_from_directory('static', path)
 """
SWAGGER_URL = '/api/docs'
API_URL = '/static/swagger.json'
swagger_ui_blueprint = get_swaggerui_blueprint(
    SWAGGER_URL,
    API_URL,
    config = {
        'app_name': 'CMPE 352 GROUP 12 PRACTICE-APP'
    }
)

app.register_blueprint(swagger_ui_blueprint, url_prefix=SWAGGER_URL)
#app.register_blueprint(app.get_blueprint())



################################ BOOKS ############################


@app.route('/books/', methods=['GET'])
def get_books():
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

################################ ISSUES ############################


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
    return Response(f'{len(r)} issues are downloaded. There are total {total_issue} issues in the system', 200)


@app.route('/issues', methods=['POST'])
def post_issue():
    try:
        issue = mapper.issue_mapper(request.get_json())
    except:
        return Response(f'An error occurred while posting json. Json = {request.get_json()}', status=400)
    issue_helper.insert_multiple_issue([issue])
    total_issue = issue_helper.get_issue_count()
    return Response(f'There are total {total_issue} issues in the system', 200)


@app.route('/issues/<int:number>', methods=['GET'])
def get_issue(number: int):
    issue = issue_helper.get_issue(number)
    if issue is None:
        print("Buradayım")
        return Response(f"Issue number {number} not found!", status=400)
    return jsonify(issue.__dict__)


@app.route('/issues', methods=['GET'])
def get_all_issues():
    max_results = 30
    if request.args.get("max_results") is not None:
        max_results = request.args.get("max_results")
    issue_list = issue_helper.get_all_issues(max_results)
    return jsonify([issue.__dict__ for issue in issue_list])


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
@app.route('/addQuotes/', methods=['POST'])
def add_quote():
    if request.get_json() is None:
        return Response("Body is empty!",status=400)
    quote = quote_helper.quote_validate(request.get_json())
    if type(quote) is not schemas.Quote:
        return quote

    db_response = quote_helper.add_quote_from_user(quote)

    return db_response if db_response is not None else Response("Quote added succesfully!", status=200)


@app.route('/quotes/', methods=['GET'])
def get_quote_opt():
    x = quote_helper.validate_input(request.args)
    if x is not None:
        return x

    quoted = quote_helper.call_quote_api(request.args.get("genre"))
    if type(quoted) != list:
        return quoted

    quotes = [mapper.quote_mapper(s) for s in quoted]

    quote_helper.add_quotes_quote_garden(quotes)

    return quotes if type(quotes) != list else schemas.QuoteResponse(data = quotes).__dict__


@app.route('/randomQuotes/', methods=['GET'])
def get_quotes():
    t = quote_helper.get_genres()
    # we get all genres and select one randomly to call
    rnd = int(random.uniform(0, len(t)))

    quoted = quote_helper.call_quote_api(t[rnd])
    if type(quoted) != list:
        return quoted

    quotes = [mapper.quote_mapper(s) for s in quoted]
    quote_helper.add_quotes_quote_garden(quotes)
    return quotes if type(quotes) != list else schemas.QuoteResponse(data=quotes).__dict__

################################ MOVIES ############################

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
        "https://api.nytimes.com/svc/movies/v2/reviews/search.json?query={}&api-key={}".format(keyword,NYTIMESKEY))
    r = r.json()

    # now we have the movies.
    # storing movies in database for further references.
    dict = {}
    if r["results"] != None:
        movies = [mapper.movie_mapper(s) for s in r["results"]]
        con = sqlite3.connect(DB_PATH)
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
    return jsonify(dict)


@ app.route('/movies_addReview/', methods=['GET', 'POST'])
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
        if movie_review['critics_pick'] != "1" and movie_review['critics_pick'] != "0":
            return Response("Criticks pick must be 1 or 0")
    try:
        movie = mapper.movie_mapper2(movie_review)
        print(movie)
    except Exception as err:
        return Response(str(err), status=400)
        # connect to Database
    con = sqlite3.connect(DB_PATH)
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

################################ NAME-AGE ############################

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
    
    try:
        r = requests.get(api_url)
        response = r.json()
        
        if response["age"] != None:
            onApi = True
            countOnApi = response["count"]
            ageOnApi = response["age"]
    except Exception:
        pass
    
    
    #Check db for matching entries to add to the results coming from Agify api
    dbData = name_info_helper.get_name_info(name, country, countryBased)
    
    
    onDB = dbData!=None and dbData[1] != 0
    if onDB:
        ageOnDB = dbData[0]
        countOnDB = dbData[1]
    
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

################################ COCKTAIL ############################
@app.route('/cocktails/get_cocktails/', methods=['GET'])
def get_cocktails():
    x = cocktail_helper.validate_get_input(request.args)
    if x is not None:
        return x
        #cocktail_name has captured
    cocktail_name = request.args.get("cocktail_name")
        #taking data in json formats
    r = requests.get("http://www.thecocktaildb.com/api/json/v1/1/search.php?s={}".format(cocktail_name))
    r = r.json()
   
    check = cocktail_helper.non_existing_cocktail_name_check(r)
    if check is not None:
        return check

    cocktails = [mapper.cocktail_mapper(s) for s in r["drinks"]]

            # We can have more than one cocktail including the cocktail name (margarita, blue margarita) or the keyword can be any place in the name of the cocktail.
    cocktail_helper.add_cocktails_from_user(cocktails)

    return schemas.CocktailResponse(cocktails=cocktails).__dict__ 


@app.route('/cocktails/create_cocktail/', methods=['GET','POST'])
def create_cocktail():
    cocktail_fields=request.get_json()
    if cocktail_fields is None:
        return Response("Body is empty!",status=400)
    cocktail = cocktail_helper.validate_post_cocktail(cocktail_fields)
    if type(cocktail) is not schemas.Cocktail:
        return cocktail
        # connect to Database
    db_response=cocktail_helper.add_cocktail_from_user(cocktail)

    return db_response if db_response is not None else Response("Cocktail added succesfully!", status=200)
    

################################ GENERAL RESPONSES ############################
@app.route('/')
def get_tasks():
    # get all tasks
    return "group12 practice-app"

@app.errorhandler(404)
def not_found(error):
    # a friendlier error handling message
    return make_response({"msg": "Page not found"}, 404)


@app.errorhandler(ValidationError)
def validation_error(error):
    return make_response({"msg": "Validation error"}, 400)


@app.errorhandler(500)
def internal_error(error):
    return make_response({"msg": "Internal error"}, 500)


@app.errorhandler(429)
def rate_limit(error):
    return make_response({"msg": "Api is rate limited please wait and try again"}, 429)


if __name__ == '__main__':
    app.run(debug=True)
