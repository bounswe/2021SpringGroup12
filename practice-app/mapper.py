from schemas import Book
from schemas import Movie


def book_mapper(book: dict) -> Book:
    return Book(
        book_title=book["book_title"],
        book_author=book["book_author"],
        url=book["url"] if 'url' in book.keys() else "",
        publication_dt=book["publication_dt"] if 'publication_dt' in book.keys(
        ) else "",
        summary=book["summary"] if 'summary' in book.keys() else "",
        uuid=book["uuid"] if 'uuid' in book.keys() else "",
        uri=book["uri"] if 'uri' in book.keys() else "",
        isbn13=book["isbn13"] if 'isbn13' in book.keys() else []
    )


def movie_mapper(movie: dict) -> Movie:
    return Movie(
        display_title=movie["display_title"],
        mpaa_rating=movie["mpaa_rating"] if 'mpaa_rating' in movie.keys(
        ) else "",
        critics_pick=movie["critics_pick"] if 'critics_pick' in movie.keys(
        ) else "",
        byline=movie["byline"],
        headline=movie["headline"] if 'headline' in movie.keys() else "",
        summary_short=movie["summary_short"] if 'summary_short' in movie.keys(
        ) else "",
        link=movie["link"]["url"]
    )


def movie_mapper2(movie: dict) -> Movie:
    return Movie(
        display_title=movie["display_title"],
        mpaa_rating=movie["mpaa_rating"] if 'mpaa_rating' in movie.keys(
        ) else "",
        critics_pick=movie["critics_pick"] if 'critics_pick' in movie.keys(
        ) else 0,
        byline=movie["byline"],
        headline=movie["headline"] if 'headline' in movie.keys() else "",
        summary_short=movie["summary_short"] if 'summary_short' in movie.keys(
        ) else "",
        link=movie["link"]
    )
