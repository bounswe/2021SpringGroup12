import unittest
from HtmlTestRunner import HTMLTestRunner
from werkzeug.datastructures import ImmutableMultiDict
import sqlite3
from main.helpers import name_info_helper
import os
from main import main


class TestNameAgeEndpoint(unittest.TestCase):
    correct_body = {
        "name": "osman",
        "age": 45,
        "country": "turkey"
    }
    
    wrong_body1 = {
        "name": "osman",
        "country": "turkey"
    }
    
    wrong_body2 = {
        "name": "osman",
        "age": 45
    }
    
    wrong_body3 = {
        "age": 45,
        "country": "turkey"
    }

    def setUp(self):
        main.app.testing = True
        self.app = main.app.test_client()

    ################### ENDPOINTS #####################
    def test_get_endpoint_correct1(self):
        return_value = self.app.get(
            f'/name_information?name=berk')
        self.assertEqual(return_value.status, '200 OK')
        
        self.assertEqual(return_value.json["name"], "berk")
        
      
    def test_get_endpoint_correct2(self):
        return_value = self.app.get(
            f'/name_information?name=berk&country=TR')
        self.assertEqual(return_value.status, '200 OK')
        
        self.assertEqual(return_value.json["name"], "berk")
        self.assertEqual(return_value.json["country"], "TR")


    def test_get_endpoint_wrong_1(self):
        return_value = self.app.get('/name_information')
        self.assertEqual(return_value.status, '400 BAD REQUEST')
        

    def test_get_endpoint_wrong_2(self):
        return_value = self.app.get('/name_information?asd=123')
        self.assertEqual(return_value.status, '400 BAD REQUEST')


    def test_post_endpoint_wrong_1(self):
        return_value = self.app.post("/name_information", json=self.wrong_body1)
        self.assertEqual(return_value.status, "400 BAD REQUEST")
        

    def test_post_endpoint_wrong_2(self):
        return_value = self.app.post("/name_information", json=self.wrong_body2)
        self.assertEqual(return_value.status, "400 BAD REQUEST")
        
        
    def test_post_endpoint_wrong_3(self):
        return_value = self.app.post("/name_information", json=self.wrong_body3)
        self.assertEqual(return_value.status, "400 BAD REQUEST")
        

    def test_post_endpoint_correct(self):
        return_value = self.app.post("/name_information", json=self.correct_body)
        self.assertEqual(return_value.status, "200 OK")
        

    def tearDown(self):
        pass


if __name__ == '__main__':
    # create a runner to see the output test reports
    root_dir = os.path.dirname(__file__)
    test_loader = unittest.TestLoader()
    package_tests = test_loader.discover(start_dir=root_dir)
    #  runner = HTMLTestRunner(combine_reports=True, report_name="MyReport", add_timestamp=False)
    runner = HTMLTestRunner(combine_reports=True, output='./reports/html/', report_name="Name-Information-Tests-Report",
                            add_timestamp=True)
    unittest.main(testRunner=runner)
