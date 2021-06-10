import unittest
from HtmlTestRunner import HTMLTestRunner
import os
from main import main


class TestCurrency(unittest.TestCase):

    def setUp(self):
        main.app.testing = True
        self.app = main.app.test_client()

    def test_get_endpoint_correct_1(self):
        return_value = self.app.get(f'/convert/?from=USD&to=EUR')
        self.assertEqual(return_value.status, '200 OK')

    def test_get_endpoint_correct_2(self):
        return_value = self.app.get(f'/convert/?from=USD&to=EUR&amount=5')
        self.assertEqual(return_value.status, '200 OK')

    def test_get_endpoint_wrong_1(self):
        return_value = self.app.get('/convert/')
        self.assertEqual(return_value.status, '400 BAD REQUEST')
        self.assertEqual(return_value.data, b'Please provide the -from- field of the currency rate!')

    def test_get_endpoint_wrong_2(self):
        return_value = self.app.get('/convert/?from=USD')
        self.assertEqual(return_value.status, '400 BAD REQUEST')
        self.assertEqual(return_value.data, b'Please provide the -to- field of the currency rate!')

    def test_get_endpoint_wrong_3(self):
        return_value = self.app.get('/convert/?from=USD&to=ASD')
        self.assertEqual(return_value.status, '400 BAD REQUEST')
        self.assertEqual(return_value.data, b'Please provide a legal -to- of the currency rate!')

    def test_post_endpoint_wrong_1_non_unique(self):
        get_result = self.app.get(f'/convert/?from=USD&to=JPY')
        return_value = self.app.post("/convert/", json={"from": get_result.json["query"]["from"],
                                                        "to":   get_result.json["query"]["to"],
                                                        "date": get_result.json["date"],
                                                        "rate": get_result.json["info"]["rate"]})
        self.assertEqual(return_value.status, "403 FORBIDDEN")
        self.assertEqual(return_value.data, b'UNIQUE constraint failed: Currency_History.date, Currency_History.from_curr, Currency_History.to_curr')

    def test_post_endpoint_wrong_2(self):
        return_value = self.app.post("/convert/", json={})
        self.assertEqual(return_value.status, "400 BAD REQUEST")

    def test_post_endpoint_correct(self):
        return_value = self.app.post("/convert/", json={"from": "QWE", "to": "XYZ", "date": "2000-01-01", "rate": "1.0"})
        self.assertEqual(return_value.status, "200 OK")

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