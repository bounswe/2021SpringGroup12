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
        ##


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
