# Practice-app Flask Application
# To run: read README.md
##
# NOTE: Remember you have to set your virtual environment and install flask
from flask_cors import CORS
import random
from helpers import issue_helper, books_helper
import sqlite3
from db import schemas, mapper
import requests
from flask import Flask, jsonify, Response, request
import sys
sys.path.append(".")

app = Flask(__name__)
app.config['JSON_SORT_KEYS'] = False
CORS(app)
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


@app.route('/issue', methods=['POST'])
def post_issue():
    issue = mapper.issue_mapper(request.get_json())
    issue_helper.insert_single_issue(issue)
    total_issue = issue_helper.get_issue_count()
    return f'There are total {total_issue} issues in the system'


@app.route('/issue/<int:number>', methods=['GET'])
def get_issue(number: int):
    issue = issue_helper.get_issue(number)
    return jsonify(issue.__dict__)


@app.route('/issue', methods=['GET'])
def get_all_issues():
    max_results = 30
    if request.args.get("max_results") is not None:
        max_results = request.args.get("max_results")
    issue_list = issue_helper.get_all_issues(max_results)
    return jsonify([issue.__dict__ for issue in issue_list])


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


# ----------------------------------------------------------------------REFIKA----------------------------------------------------------------------


@app.route('/movies_home/', methods=['GET', 'POST'])
def movies_home():
    if request.method == 'POST':
        if request.form.get('action1') == 'Show reviews with keyword':
            keyword = request.form.get('keyword')
            s = (url_for('get_movies'))+"?keyword=" + keyword
            return redirect(s)
        elif request.form.get('action2') == 'Add new review':
            return redirect(url_for('open_add_review'))
    return "nljlşj"
    # render_template("movies_home.html")


@app.route('/movies/', methods=['GET'])
def get_movies():
    if "keyword" not in request.args:
        return Response("Please provide a keyword!", status=400)
    # now we are sure keyword parameter is supplied, request rewiew the moves with the keywords from NYTimes API.
    keyword = request.args.get("keyword").title().replace(" ", "+")
    r = requests.get(
        "https://api.nytimes.com/svc/movies/v2/reviews/search.json?query={}&api-key=gJkqRRyjYRV0YDiUDAEXwsa0uZLL6YLh".format(keyword))

    r = r.json()
    # print(r)

    # now we have the movies.
    # storing movies in database for further references.
    movies = [mapper.movie_mapper(s) for s in r["results"]]
    con = sqlite3.connect(
        "c:/Users/HP/Desktop/2021SpringGroup12/practice-app/sqlfiles/practice-app.db")
    cur = con.cursor()
    for movie in movies:
        # -----------------------------------
        try:
            cur.execute("INSERT INTO Movie(display_title, mpaa_rating, critics_pick, byline, headline,summary_short, link) VALUES (?,?,?,?,?,?,?)",
                        (movie.display_title, movie.mpaa_rating, movie.critics_pick, movie.byline, movie.headline, movie.summary_short, movie.link.url))
        except:
            # do nothing upon failure, this is not a critical process
            continue
    dict = {}
    for movie in movies:
        display_title = movie.display_title
        movie_info = {"display_title": movie.display_title, "mpaa_rating": movie.mpaa_rating, "critics_pick": movie.critics_pick,
                      "byline": movie.byline, "headline": movie.headline, "summary_short":  movie.summary_short, "link": movie.link}
        dict[display_title] = movie_info
    # print(jsonify(dict))
    return jsonify(dict)


"""
@app.route('/movies_table/', methods=['GET', 'POST'])
def table():
    return render_template('table.html')
"""


"""
@ app.route('/movies_addReview/', methods=['GET'])
def open_add_review():
    return render_template("movies_addReview.html",  user=current_user)
"""


@ app.route('/movies_addReview/', methods=['GET', 'POST'])
def create_movie_review():
    movie_review = request.form.to_dict(flat=True)
    # if request.args.get("max_results") is not None:
    # movie_review = request.get_json()
    # print(movie_review)
    # make sure that necessary information are given
    # title byline and url should be provided
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
    except Exception as err:
        return Response(str(err), status=409)
        # connect to Database
    con = sqlite3.connect(
        "c:/Users/HP/Desktop/2021SpringGroup12/practice-app/sqlfiles/practice-app.db")
    cur = con.cursor()
    # try to insert book to DB, return forbidden upon failure
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


if __name__ == '__main__':
    app.run(debug=True)
