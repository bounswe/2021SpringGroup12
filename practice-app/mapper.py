from schemas import Book, CurrencyRate


def book_mapper(book: dict) -> Book:
    return Book(
        book_title= book["book_title"],
        book_author= book["book_author"],
        url=book["url"] if 'url' in book.keys() else "",
        publication_dt= book["publication_dt"] if 'publication_dt' in book.keys() else "",
        summary= book["summary"] if 'summary' in book.keys() else "",
        uuid= book["uuid"] if 'uuid' in book.keys() else "",
        uri= book["uri"] if 'uri' in book.keys() else "",
        isbn13= book["isbn13"] if 'isbn13' in book.keys() else [],
        )


def currency_rate_mapper(currency_rate: dict) -> CurrencyRate:
    return CurrencyRate(
        date= currency_rate["date"],
        from_curr= currency_rate["query"]["from"] if 'query' in currency_rate.keys() else currency_rate["from"],
        to_curr= currency_rate["query"]["to"] if 'query' in currency_rate.keys() else currency_rate["to"],
        rate= currency_rate["info"]["rate"] if 'info' in currency_rate.keys() else currency_rate["rate"],
    )

