import unittest

from ..main.db.mapper import book_mapper

class BooksEndpointTest(unittest.TestCase):
    def test_book_mapper(self):

        book_dict = {
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
            }
        book = book_mapper(book_dict)
        #self.assertTrue(type(book) == Book)        


if __name__ == '__main__':
    unittest.main()
