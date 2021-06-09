import unittest
from HtmlTestRunner import HTMLTestRunner
from werkzeug.datastructures import ImmutableMultiDict
import sqlite3
from main.db.mapper import movie_mapper, movie_mapper2
import os
from main import main


class TestMoviesEndpoint(unittest.TestCase):
    input_movies_from_nytimes = {
        "status": "OK",
        "copyright": "Copyright (c) 2021 The New York Times Company. All Rights Reserved.",
        "has_more": False,
        "num_results": 4,
        "results": [
            {
                "display_title": "Still Life in Lodz",
                "mpaa_rating": "",
                "critics_pick": 0,
                "byline": "Ben Kenigsberg",
                "headline": "‘Still Life in Lodz’ Review: A Painting Becomes a Window",
                "summary_short": "This documentary examines how objects can create through lines across history.",
                "publication_date": "2021-03-11",
                "opening_date": "null",
                "date_updated": "2021-03-11 12:02:02",
                "link": {
                    "type": "article",
                    "url": "https://www.nytimes.com/2021/03/11/movies/still-life-in-lodz-review.html",
                    "suggested_link_text": "Read the New York Times Review of Still Life in Lodz"
                },
                "multimedia": {
                    "type": "mediumThreeByTwo210",
                    "src": "https://static01.nyt.com/images/2021/03/10/arts/stilllife1/merlin_184639725_d0e76cda-d167-48b0-ac98-8ef873f15601-mediumThreeByTwo440.jpg",
                    "height": 140,
                    "width": 210
                }
            },
            {
                "display_title": "Still Life",
                "mpaa_rating": "",
                "critics_pick": 0,
                "byline": "STEPHEN HOLDEN",
                "headline": "A Designated Mourner, Flirting Warily With Joy",
                "summary_short": "In Uberto Pasolini&#8217;s &#8220;Still Life,&#8221; Eddie Marsan plays a Londoner who arranges for the burial of dead people whose bodies have not been claimed by relatives.",
                "publication_date": "2015-01-15",
                "opening_date": " null",
                "date_updated": "2017-11-02 04:16:39",
                "link": {
                    "type": "article",
                    "url": "https://www.nytimes.com/2015/01/16/movies/uberto-pasolinis-still-life-starring-eddie-marsan.html",
                    "suggested_link_text": "Read the New York Times Review of Still Life"
                },
                "multimedia": {
                    "type": "mediumThreeByTwo210",
                    "src": "https://static01.nyt.com/images/2015/01/16/arts/16STILL-ALT/STILL-ALT-mediumThreeByTwo210.jpg",
                    "height": 140,
                    "width": 210
                }
            },
            {
                "display_title": "Still Life",
                "mpaa_rating": "Unrated",
                "critics_pick": 1,
                "byline": "MANOHLA DARGIS",
                "headline": "Those Days of Doom on the Yangtze",
                "summary_short": "In “Still Life,” the blood and the sweat run directly into the Yangtze River, where they mingle with more than a few tears.",
                "publication_date": "2008-01-18",
                "opening_date": "null",
                "date_updated": "2017-11-02 04:16:27",
                "link": {
                    "type": "article",
                    "url": "https://www.nytimes.com/2008/01/18/movies/18stil.html",
                    "suggested_link_text": "Read the New York Times Review of Still Life"
                },
                "multimedia": "null"
            },
            {
                "display_title": "Frida: Naturaleza Vita",
                "mpaa_rating": "",
                "critics_pick": "0",
                "byline": "WALTER GOODMAN",
                "headline": "FRIDA (MOVIE)",
                "summary_short": "Mexican painter Frida Kahlo recalls Leon Trotsky and muralist Diego Rivera.",
                "publication_date": "1988-02-17",
                "opening_date": "1988-02-17",
                "date_updated": "2017-11-02 04:17:38",
                "link": {
                    "type": "article",
                    "url": "https://www.nytimes.com/1988/02/17/movies/film-frida-tribute-to-mexican-artist.html",
                    "suggested_link_text": "Read the New York Times Review of Frida: Naturaleza Vita"
                },
                "multimedia": "null"
            }
        ]
    }
    input_movie_from_user = {
        "display_title": "Apocalypse '45",
        "mpaa_rating": "",
        "critics_pick": 0,
        "byline": "Natalia Winkelman",
        "headline": "‘Apocalypse ’45’ Review: Graphic Images of Wartime",
        "summary_short": "Candid testimonies from World War II veterans accompany vivid archival footage in this immersive documentary that showcases the myths we tell ourselves about war.",
        "link": "https://www.nytimes.com/2021/05/27/movies/apocalypse-45-review.html"
    }
    bad_input_movie_from_user = {
        "display_title": "Apocalypse '45",
        "mpaa_rating": "",
        "critics_pick": 0,
        "byline": "Natalia Winkelman",
        "headline": "‘Apocalypse ’45’ Review: Graphic Images of Wartime",
        "summary_short": "Candid testimonies from World War II veterans accompany vivid archival footage in this immersive documentary that showcases the myths we tell ourselves about war.",
        "link": "https://www.nytimes.com/2021/05/27/movies/apocalypse-45-review.html",
        "wrong": "wrong"
    }
    output_movie_for_science_of = {
        "Breaking Boundaries: The Science of Our Planet": {
            "display_title": "Breaking Boundaries: The Science of Our Planet",
            "mpaa_rating": "",
            "critics_pick": 0,
            "byline": "Calum Marsh",
            "headline": "\u2018Breaking Boundaries: The Science of Our Planet\u2019 Review: A Dire Warning",
            "summary_short": "We have a lot more than just climate change to worry about, argues this nature doc narrated by Sir David Attenborough.",
            "link": "https://www.nytimes.com/2021/06/04/movies/breaking-boundaries-the-science-of-our-planet-review.html"
        },
        "The Science of Sleep": {
            "display_title": "The Science of Sleep",
            "mpaa_rating": "R",
            "critics_pick": 1,
            "byline": "By A.O. SCOTT",
            "headline": "A Parisian Love Story in Forward, and Sideways, Motion",
            "summary_short": "Michel Gondry's beguiling new film is so profoundly idiosyncratic, and so confident in its oddity, that any attempt to describe it is bound to be misleading. ",
            "link": "https://www.nytimes.com/2006/09/22/movies/22slee.html"
        },
        "Thrill Ride: The Science of Fun": {
            "display_title": "Thrill Ride: The Science of Fun",
            "mpaa_rating": "G",
            "critics_pick": 0,
            "byline": "Lawrence Van Gelder",
            "headline": "Thrill Ride: The Science of Fun (Movie)  ",
            "summary_short": "",
            "link": "https://www.nytimes.com/1997/07/11/movies/ah-terror-the-ecstasy-of-roller-coaster-agony.html"
        }
    }

    def setUp(self):
        main.app.testing = True
        self.app = main.app.test_client()

    def test_get_movies_wrong_1(self):
        return_value = self.app.get('/movies/')
        print(return_value)
        self.assertEqual(return_value.status, '400 BAD REQUEST')
        self.assertEqual(return_value.data,
                         b'Please provide a keyword!')

    def test_get_movies_wrong_2(self):
        return_value = self.app.get(
            '/movies/?keyword=iamPrettySureThisIsNotAValidKeyword')
        self.assertEqual(return_value.status, '200 OK')
        self.assertEqual(return_value.json, {})

    def test_get_movies_correct_1(self):
        return_value = self.app.get(
            f'/movies/?keyword=science%20of')
        self.assertEqual(return_value.status, '200 OK')
        self.assertEqual(return_value.json, self.output_movie_for_science_of)

    def test_post_endpoint_wrong1(self):
        return_value = self.app.post(
            "/movies_addReview/", json={"book_title": "hello", "book_author": "world"})
        self.assertEqual(return_value.status, "400 BAD REQUEST")
        self.assertEqual(
            return_value.data, b'Please provide the title of the movie!')

    def test_post_endpoint_correct(self):
        return_value = self.app.post(
            "/movies_addReview/", json=self.input_movie_from_user)
        self.assertEqual(return_value.status, "200 OK")
        self.assertEqual(
            return_value.data, b'Movie Review added succesfully!')

    def test_post_endpoint_wrong4(self):
        return_value = self.app.post("/movies_addReview/", json={})
        self.assertEqual(return_value.status, "400 BAD REQUEST")
        self.assertEqual(
            return_value.data, b'Please provide the required information!')


"""

    def test_get_endpoint_correct_2(self):
        return_value = self.app.get(
            f'/books/?name={self.input_books_from_nytimes[0].get("book_author")}&max_results=1')
        self.assertEqual(return_value.status, '200 OK')
        self.assertEqual(return_value.json, {
                         "num_results": 1, "books": self.input_books_from_nytimes[:1]})

    def test_get_endpoint_wrong_3(self):
        return_value = self.app.get(
            '/books/?name=Cem Say&max_results=letsputastringhere')
        self.assertEqual(return_value.status, '400 BAD REQUEST')
        self.assertEqual(return_value.data, b'max_results should be integer!')

    def test_post_endpoint_wrong_1(self):
        return_value = self.app.post("/books/", json=self.input_book_from_user)
        self.assertEqual(return_value.status, "403 FORBIDDEN")
        self.assertEqual(
            return_value.data, b'UNIQUE constraint failed: Books.book_title, Books.book_author')

    def test_post_endpoint_wrong_2(self):
        return_value = self.app.post("/books/", json={})
        self.assertEqual(return_value.status, "400 BAD REQUEST")

    def test_post_endpoint_correct(self):
        return_value = self.app.post(
            "/books/", json={"book_title": "hello", "book_author": "world"})
        self.assertEqual(return_value.status, "200 OK")

    ################### HELPERS ########################
    def test_validate_input_correct_input(self):
        request_args = ImmutableMultiDict([('name', 'aldous huxley')])
        response = books_helper.validate_input(request_args)
        self.assertTrue(response is None, msg="Couldn't handle name parameter")

    def test_validate_input_wrong_input(self):
        request_args = ImmutableMultiDict([])
        response = books_helper.validate_input(request_args)
        self.assertTrue(response.status_code == 400,
                        msg="Couldn't handle empty name parameter")

    def test_call_nytimes_correct_input(self):
        expected_output = self.input_books_from_nytimes
        output = books_helper.call_nytimes("aldous huxley")
        self.assertListEqual(output, expected_output,
                             msg="Couldn't parse correct input parameter.")

    def test_call_nytimes_wrong_input(self):
        expected_output = []
        output = books_helper.call_nytimes("aldoushuxley")
        self.assertListEqual(output, expected_output,
                             msg="Couldn't parse correct input parameter.")

    def test_add_books_from_nytimes_correct_input(self):
        books_helper.add_books_from_nytimes(self.input_books_from_nytimes)
        con = sqlite3.connect(
            "/home/veyis/Desktop/2021SpringGroup12/practice-app/sqlfiles/practice-app.db")
        cur = con.cursor()
        cur.execute("SELECT * FROM Books WHERE book_author = 'Aldous Huxley'")
        books_from_db = cur.fetchall()
        for book in books_from_db:
            self.assertTrue(book[2] == "Aldous Huxley",
                            msg="Couldn't add the book to database.")
            self.assertTrue(book[1] in [s["book_title"] for s in self.input_books_from_nytimes],
                            msg="Couldn't add the book to database.")

    def test_add_books_from_nytimes_wrong_input(self):
        # since this method does not return anything upon failure, there is nothing to test.
        pass

    def test_call_nytimes_blank_input(self):
        # send blank input
        output = books_helper.call_nytimes("")
        self.assertTrue(output.status_code == 400,
                        msg="Couldn't handle wrong input.")

    def test_get_n_correct_input_1(self):
        request_args = ImmutableMultiDict(
            [('name', 'aldous huxley'), ('max_results', '3')])

        output_books = books_helper.get_n(
            self.input_books_from_nytimes, request_args)
        length = len(self.input_books_from_nytimes) if len(self.input_books_from_nytimes) < int(
            request_args.get('max_results')) else int(
            request_args.get('max_results'))
        self.assertTrue(len(output_books) == length)

    def test_get_n_correct_input_2(self):
        request_args = ImmutableMultiDict(
            [('name', 'aldous huxley'), ('max_results', '1')])

        output_books = books_helper.get_n(
            self.input_books_from_nytimes, request_args)
        length = len(self.input_books_from_nytimes) if len(self.input_books_from_nytimes) < int(
            request_args.get('max_results')) else int(
            request_args.get(
                'max_results'))
        self.assertTrue(len(output_books) == length)

    def test_get_n_wrong_input(self):
        # input is string, it should have been int. Let's see if we can handle
        request_args = ImmutableMultiDict(
            [('name', 'aldous huxley'), ('max_results', 'a')])

        output_books = books_helper.get_n(
            self.input_books_from_nytimes, request_args)
        self.assertTrue(output_books.status_code == 400,
                        msg="Couldn't handle wrong input.")

    def test_get_n_blank_input(self):
        request_args = ImmutableMultiDict([('name', 'aldous huxley')])
        output_books = books_helper.get_n(
            self.input_books_from_nytimes, request_args)
        self.assertListEqual(output_books, self.input_books_from_nytimes,
                             msg="Couldn't handle blank max_results")

    def test_validate_body_correct_input(self):
        book = books_helper.validate_body(self.input_book_from_user)
        self.assertFalse(str(type(book)) ==
                         "<class 'flask.wrappers.Response'>")

    def test_validate_body_optional_correct_input(self):
        optional_json = {
            "url": "",
            "book_title": "50 Soruda Yapay Zeka",
            "book_author": "Cem Say"
        }
        book = books_helper.validate_body(optional_json)
        self.assertFalse(str(type(book)) ==
                         "<class 'flask.wrappers.Response'>")

    def test_validate_body_wrong_input1(self):
        name = self.input_book_from_user.get("book_title")
        self.input_book_from_user.pop("book_title")
        book = books_helper.validate_body(self.input_book_from_user)
        self.input_book_from_user["book_title"] = name
        self.assertTrue(book.status_code == 400,
                        msg="Couldn't handle nonexistent key")

    def test_validate_body_wrong_input2(self):
        self.input_book_from_user.pop("url")
        self.input_book_from_user["urll"] = ""
        book = books_helper.validate_body(self.input_book_from_user)
        self.assertFalse(str(type(book)) ==
                         "<class 'flask.wrappers.Response'>")

    def test_validate_body_blank_input(self):
        book = books_helper.validate_body({})
        self.assertTrue(book.status_code == 400,
                        msg="Couldn't handle blank request")

    def test_add_book_from_user_correct_input(self):
        book = book_mapper(self.input_book_from_user)
        books_helper.add_book_from_user(book)
        con = sqlite3.connect(
            "/home/veyis/Desktop/2021SpringGroup12/practice-app/sqlfiles/practice-app.db")
        cur = con.cursor()
        cur.execute("SELECT * FROM Books WHERE book_author = (?) AND book_title = (?)",
                    (self.input_book_from_user.get("book_author"), self.input_book_from_user.get("book_title")))
        book_from_db = cur.fetchone()
        self.assertTrue(book_from_db[2] == self.input_book_from_user.get("book_author") and book_from_db[
            1] == self.input_book_from_user.get("book_title"),
            msg="Couldn't add the book to database.")

    def test_add_book_from_user_wrong_input(self):
        # try to add same book to the database, aka fail unique key constraint
        book = book_mapper(self.input_book_from_user)
        response = books_helper.add_book_from_user(book)
        self.assertTrue(response.status_code == 403,
                        msg="Failed to handle unique key constraint")
"""


def tearDown(self):
    pass


if __name__ == '__main__':
    # create a runner to see the output test reports
    root_dir = os.path.dirname(__file__)
    test_loader = unittest.TestLoader()
    package_tests = test_loader.discover(start_dir=root_dir)
    #  runner = HTMLTestRunner(combine_reports=True, report_name="MyReport", add_timestamp=False)
    runner = HTMLTestRunner(combine_reports=True, output='./reports/html/', report_name="Hello-World-Tests-Report",
                            add_timestamp=True)
    unittest.main(testRunner=runner)
