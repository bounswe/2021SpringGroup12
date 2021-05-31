from db.schemas import Book, Quote


def book_mapper(book: dict) -> Book:
    return Book(
        book_title=book["book_title"],
        book_author=book["book_author"],
        url=book["url"] if 'url' in book.keys() else "",
        publication_dt=book["publication_dt"] if 'publication_dt' in book.keys() else "",
        summary=book["summary"] if 'summary' in book.keys() else "",
        uuid=book["uuid"] if 'uuid' in book.keys() else "",
        uri=book["uri"] if 'uri' in book.keys() else "",
        isbn13=book["isbn13"] if 'isbn13' in book.keys() else [],
    )

def quote_mapper(quote: dict) -> Quote:
    return Quote(
        quoteId= quote["_id"],
        quoteAuthor= quote["quoteAuthor"],
        quoteGenre= quote["quoteGenre"],
        quoteText= quote["quoteText"],
        #last = quote["__v"]
        )