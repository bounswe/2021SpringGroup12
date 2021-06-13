import os, sys
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)
import unittest
import requests
from main.helpers import cocktail_helper
from werkzeug.datastructures import ImmutableMultiDict
import json
from main import main

class TestCocktailsEndpoint(unittest.TestCase):


    def setUp(self):
        main.app.testing = True
        self.app = main.app.test_client()


    def test_validate_input_wrong_input(self):
        request_args = ImmutableMultiDict([])
        response = cocktail_helper.validate_get_input(request_args)
        self.assertTrue(response.status_code == 400, msg="Couldn't handle empty name parameter")

    def test_endpoint_get(self):
        response = self.app.get(f'/cocktails/get_cocktails/?cocktail_name=godfather')
        self.assertEqual(response.status, '200 OK')


    def test_endpoint_get_wrong(self):
        response = self.app.get(f'/cocktails/get_cocktails/')
        self.assertEqual(response.status, '400 BAD REQUEST')

  
    def test_endpoint_post(self):
        response = self.app.post(f'/cocktails/create_cocktail/', json={})
        self.assertEqual(response.status, '400 BAD REQUEST')

    def test_endpoint_get_wrong_2(self):
        response = self.app.get('/cocktails/get_cocktails/?cocktail_name=notacocktail')
        self.assertEqual(response.text, "Please provide an existing cocktail name!")

    def test_create_existing_cocktail_name(self):
        response = self.app.post('/cocktails/create_cocktail/', json={
    "strDrink": "batuhancocktail",
    "strIngredient1": "gin",
    "strIngredient2": "tonic",
    "strIngredient3": "lemon",
    "strIngredient4": "",
    "strGlass": "visky glass",
    "strInstructions": "limonu sık abi sonra karıştır"})
                                    
        self.assertEqual(response.text, "This cocktail name is taken already! Try different cocktail name.")

if __name__ == '__main__':
    unittest.main()