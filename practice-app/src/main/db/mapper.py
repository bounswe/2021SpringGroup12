import sys
sys.path.append("..") 
from .schemas import Book, Issue, Quote, Genre, SearchResult, Anime, CreateAnime
from helpers import issue_helper


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


def git_issue_mapper(git_issue: dict) -> Issue:
    issue = issue_helper.make_issue(git_issue)
    return issue_mapper(issue)


def issue_mapper(issue: dict) -> Issue:
    return Issue(
        number=issue['number'],
        assignees=issue['assignees'],
        description=issue['description'],
        labels=issue['labels'],
        state=issue['state']
    )

def genre_mapper(genre: dict) -> Genre:
    return Genre(
        id=genre["mal_id"],
        type=genre["type"],
        name=genre["name"]
    )


def search_mapper(anime: dict) -> SearchResult:
    return SearchResult(
        title=anime["title"],
        episodes=anime["episodes"],
        id=anime["mal_id"],
        image_url=anime["image_url"],
        airing=anime["airing"],
        start_date=anime["start_date"],
        end_date=anime["end_date"],
        score=anime["score"],
        rating=anime["rated"] if anime["rated"] else "None",
        type=anime["type"],
        synopsis=anime["synopsis"]
    )


def anime_mapper(anime: dict) -> Anime:
    return Anime(
        title=anime["title"],
        episodes=anime["episodes"] if anime["episodes"] else 0,
        id=anime["mal_id"],
        image_url=anime["image_url"],
        airing=anime["airing"],
        start_date=anime["aired"]["from"],
        end_date=anime["aired"]["to"],
        score=anime["score"],
        rating=anime["rating"].split(' -')[0],
        type=anime["type"],
        synopsis=anime["synopsis"],
        duration=(int(anime["duration"].split(' ')[0]) if not anime["duration"].split(" ")[
            1] == "hr" else int(anime["duration"].split(" ")[0])*60+int(anime["duration"].split(" ")[2])) if anime["duration"] and anime["duration"] != "Unknown" else 0,
        sequel=anime["sequel"],
        prequel=anime["prequel"],
        genres=[genre_mapper(g).dict() for g in anime["genres"]]
    )

def create_anime_mapper(create_anime: dict) -> CreateAnime:
    return CreateAnime(
        title= create_anime["title"],
        episodes=create_anime["episodes"],
        id=create_anime["id"],
        image_url=create_anime["image_url"],
        airing=create_anime["airing"],
        start_date=create_anime["start_date"],
        end_date=create_anime["end_date"],
        score=create_anime["score"],
        rating=create_anime["rating"],
        type=create_anime["type"],
        synopsis=create_anime["synopsis"]
    )
def quote_mapper(quote: dict) -> Quote:
    return Quote(
        quoteId= quote["_id"],
        quoteAuthor= quote["quoteAuthor"],
        quoteGenre= quote["quoteGenre"],
        quoteText= quote["quoteText"],
        #last = quote["__v"]
        )

