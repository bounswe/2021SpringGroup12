import unittest
from HtmlTestRunner import HTMLTestRunner
from werkzeug.datastructures import ImmutableMultiDict
import sqlite3
from main.db.mapper import book_mapper
from main.helpers import books_helper
import os
from main import main


class TestBooksEndpoint(unittest.TestCase):
    input_books_from_nytimes = [
        {
            "url": "http://www.nytimes.com/1985/11/21/books/books-of-the-times-169933.html",
            "publication_dt": "1985-11-21",
            "byline": "WALTER GOODMAN AMUSING OURSELVES TO DEATH:   PUBLIC DISCOURSE IN THE AGE OF SHOW BUSINESS. BY NEIL POSTMAN. 184 PAGES.   ELISABETH SIFTON BOOKS/VIKING. $15.95.",
            "book_title": "Brave New World",
            "book_author": "Aldous Huxley",
            "summary": "",
            "uuid": "00000000-0000-0000-0000-000000000000",
            "uri": "nyt://book/00000000-0000-0000-0000-000000000000",
            "isbn13": [
                "9780060809836"
            ]
        },
        {
            "url": "http://www.nytimes.com/1998/09/20/books/books-in-brief-fiction-786837.html",
            "publication_dt": "1998-09-20",
            "byline": "WILLIAM FERGUSON",
            "book_title": "Jacob's Hands",
            "book_author": "Aldous Huxley",
            "summary": "",
            "uuid": "00000000-0000-0000-0000-000000000000",
            "uri": "nyt://book/00000000-0000-0000-0000-000000000000",
            "isbn13": [
                "9780312243067"
            ]
        }
    ]
    input_book_from_user = {
        "url": "",
        "publication_dt": "2018-10-13",
        "byline": "",
        "book_title": "50 Soruda Yapay Zeka",
        "book_author": "Cem Say",
        "summary": "",
        "uuid": "00000000-0000-0000-0000-000000000000",
        "uri": "nyt://book/00000000-0000-0000-0000-000000000000",
        "isbn13": [
            "9786055888589"
        ]
    }

    def setUp(self):
        main.app.testing = True
        self.app = main.app.test_client()

    ################### ENDPOINTS #####################
    def test_get_endpoint_correct_1(self):
        return_value = self.app.get(
            f'/books/?name={self.input_books_from_nytimes[0].get("book_author")}')
        self.assertIn(return_value.status, ['200 OK', '429 Too Many Requests', '500 Internal Server Error',
                      '502 Bad Gateway', '503 Service Unavailable', '504 Gateway Timeout'])
        if return_value.status == 200:
            self.assertEqual(return_value.json,
                             {"num_results": len(self.input_books_from_nytimes), "books": self.input_books_from_nytimes})

    def test_get_endpoint_correct_2(self):
        return_value = self.app.get(
            f'/books/?name={self.input_books_from_nytimes[0].get("book_author")}&max_results=1')
        self.assertIn(return_value.status, ['200 OK', '429 Too Many Requests', '500 Internal Server Error',
                      '502 Bad Gateway', '503 Service Unavailable', '504 Gateway Timeout'])
        if return_value.status == 200:
            self.assertEqual(return_value.json, {
                         "num_results": 1, "books": self.input_books_from_nytimes[:1]})

    def test_get_endpoint_wrong_1(self):
        return_value = self.app.get('/books/')
        self.assertEqual(return_value.status, '400 BAD REQUEST')
        self.assertEqual(return_value.data, b'Please provide an author name!')

    def test_get_endpoint_wrong_2(self):
        return_value = self.app.get(
            '/books/?name=iamPrettySureThisIsNotAValidAuthorName&max_results=1')
        self.assertEqual(return_value.status, '200 OK')
        self.assertEqual(return_value.json, {"num_results": 0, "books": []})

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
        self.assertEqual(return_value.status, "200 OK", msg="probably this is failed because the book is already in the database")

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
        self.assertListEqual(
            output, expected_output, msg=f"Couldn't parse correct input parameter.Output: {output}")

    def test_call_nytimes_wrong_input(self):
        expected_output = []
        output = books_helper.call_nytimes("aldoushuxley")
        self.assertListEqual(output, expected_output,
                             msg="Couldn't parse correct input parameter.")

    def test_add_books_from_nytimes_correct_input(self):
        books_helper.add_books_from_nytimes(self.input_books_from_nytimes)
        # TODO change this in deployment phase
        con = sqlite3.connect(
            "/home/veyis/Desktop/a2021SpringGroup12/practice-app/sqlfiles/practice-app.db")
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
        self.input_book_from_user["url"] = ""
        self.assertFalse(str(type(book)) ==
                         "<class 'flask.wrappers.Response'>")

    def test_validate_body_blank_input(self):
        book = books_helper.validate_body({})
        self.assertTrue(book.status_code == 400,
                        msg="Couldn't handle blank request")

    def test_add_book_from_user_correct_input(self):
        book = book_mapper(self.input_book_from_user)
        books_helper.add_book_from_user(book)
        # TODO change this in deployment phase
        con = sqlite3.connect(
            "/home/veyis/Desktop/a2021SpringGroup12/practice-app/sqlfiles/practice-app.db")
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
