import unittest
import sys
sys.path.append(".")
sys.path.append("../main")
import os
import HtmlTestRunner  # Import the HTMLTestRunner Module
from unittests import helper_test
from unittests import test_books

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

        html_runner = HtmlTestRunner.HTMLTestRunner(
            combine_reports=True,
            output='./reports/html/',
            report_name="PracticeAppTest",
            add_timestamp=True)
        html_runner.run(suite)

# main function to call unittests

if __name__ == '__main__':
    unittest.main(testRunner=HtmlTestRunner.HTMLTestRunner(output='reports/html/'))
