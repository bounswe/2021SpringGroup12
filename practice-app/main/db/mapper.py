<<<<<<< HEAD:practice-app/src/main/db/mapper.py
import sys
sys.path.append("..") 
from .schemas import Book, Issue, Quote, CurrencyRate
from helpers import issue_helper
=======
from main.db.schemas import Book, Issue, Quote
from main.helpers import issue_helper
>>>>>>> veyis-practice-app-organized:practice-app/main/db/mapper.py


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
def quote_mapper(quote: dict) -> Quote:
    return Quote(
        quoteId= quote["_id"],
        quoteAuthor= quote["quoteAuthor"],
        quoteGenre= quote["quoteGenre"],
        quoteText= quote["quoteText"],
        #last = quote["__v"]
        )

def currency_rate_mapper(currency_rate: dict) -> CurrencyRate:
    return CurrencyRate(
        date= currency_rate["date"],
        from_curr= currency_rate["query"]["from"] if 'query' in currency_rate.keys() else currency_rate["from"],
        to_curr= currency_rate["query"]["to"] if 'query' in currency_rate.keys() else currency_rate["to"],
        rate= currency_rate["info"]["rate"] if 'info' in currency_rate.keys() else currency_rate["rate"],
    )
