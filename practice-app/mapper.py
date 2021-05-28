from schemas import CreateBook, Book, Quote

def book_mapper(book: dict) -> Book:
    return Book(
        book_title= book["book_title"],
        book_author= book["book_author"],
        url=book["url"],
        publication_dt= book["publication_dt"],
        summary= book["summary"],
        uuid= book["uuid"],
        uri= book["uri"],
        isbn13= book["isbn13"] if 'isbn13' in book.keys() else [],
        )

def quote_mapper(quote: dict) -> Quote:
    return Quote(
        id= quote["_id"],
        quoteAuthor= quote["quoteAuthor"],
        quoteGenre= quote["quoteGenre"],
        quoteText= quote["quoteText"],
        #last = quote["__v"]
        )
