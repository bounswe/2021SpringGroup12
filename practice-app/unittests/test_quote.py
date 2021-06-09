import unittest
<<<<<<< HEAD
import os
from main.helpers.quote_helper import *
import requests
import json
from main import main

=======
from main.helpers.quote_helper import *
import json
from main import main
import requests
>>>>>>> master


class TestQuotesEndpoint(unittest.TestCase):

<<<<<<< HEAD
    mock_data = """ 
                "_id": "bask1",
                "quoteAuthor": "Lebron James",
                "quoteGenre": "basketball",
                "quoteText": "basketball is great"
                """
    """
    def setUp(self):
        main.app.testing = True
        self.app = main.app.test_client()
    """
    # test genres endpoint
=======
    def setUp(self):
        main.app.testing = True
        self.app = main.app.test_client()
    # test genres endpoint to see if this endpoint still works
>>>>>>> master
    def test_other_endpoint_get_genres(self):
        t = requests.get("https://quote-garden.herokuapp.com/api/v3/genres")
        t = t.json()
        self.assertEqual(t["statusCode"], 200)
<<<<<<< HEAD
=======

    # test quotes endpoint to see if this endpoint still works
>>>>>>> master
    def test_other_endpoint_get_quotes(self):
        t = requests.get("https://quote-garden.herokuapp.com/api/v3/quotes?genre=beauty")
        t = t.json()
        self.assertEqual(t["statusCode"], 200)

    def test_endpoint_get(self):
        return_value = self.app.get(f'/quotes/?genre=age')
        self.assertEqual(return_value.status, '200 OK')

    def test_endpoint_get_random(self):
        return_value = self.app.get(f'/randomQuotes/')
        self.assertEqual(return_value.status, '200 OK')


    def test_endpoint_get_wrong(self):
        return_value = self.app.get(f'/quotes/')
        self.assertEqual(return_value.status, '400 BAD REQUEST')

    def test_endpoint_get_wrong_2(self):
        return_value = self.app.get(f'/quotes/?genre=notagenre')
        self.assertEqual(return_value.status, '200 OK')
        self.assertEqual(return_value.json, {"data": []})

    def test_endpoint_post(self):
        return_value = self.app.post(f'/addQuotes/', json={})
        self.assertEqual(return_value.status, '400 BAD REQUEST')

<<<<<<< HEAD

=======
    """def test_endpoint_post_correct(self):
        return_value = self.app.post("/addQuotes", json= {"_id": "bask1",
                "quoteAuthor": "Lebron James",
                "quoteGenre": "basketball",
                "quoteText": "basketball is great"})

        self.assertEqual(return_value.status, "200 OK")"""
>>>>>>> master

if __name__ == '__main__':
    unittest.main()