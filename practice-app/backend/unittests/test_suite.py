from unittests import test_books, test_animes, test_cocktails, test_currency, test_movies, test_name_age, test_quote
from unittests import helper_test
import HtmlTestRunner  # Import the HTMLTestRunner Module
import os
import unittest
import sys
sys.path.append(".")
sys.path.append("../main")

# Get the Present Working Directory to store the report

current_directory = os.getcwd()


# Create a class to test the runner to produce nice HTML reports

class HTML_TestRunner_TestSuite(unittest.TestCase):
    def test_demo_suite(self):
        # create a test suite
        loader = unittest.TestLoader()
        suite = unittest.TestSuite()
        # add two tests to the suite -- in principle they should be related, here they are demo
        suite.addTest(loader.loadTestsFromModule(test_books))
        suite.addTest(loader.loadTestsFromModule(test_animes))
        suite.addTest(loader.loadTestsFromModule(test_cocktails))
        suite.addTest(loader.loadTestsFromModule(test_currency))
        suite.addTest(loader.loadTestsFromModule(test_movies))
        suite.addTest(loader.loadTestsFromModule(test_name_age))
        suite.addTest(loader.loadTestsFromModule(test_quote))

        html_runner = HtmlTestRunner.HTMLTestRunner(
            combine_reports=True,
            output='./reports/html/',
            report_name="PracticeAppTest",
            add_timestamp=True)
        html_runner.run(suite)

# main function to call unittests


if __name__ == '__main__':
    unittest.main(testRunner=HtmlTestRunner.HTMLTestRunner(
        output='reports/html/'))
