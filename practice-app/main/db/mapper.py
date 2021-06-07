from .schemas import Book, Issue, Quote, Cocktail
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
def quote_mapper(quote: dict) -> Quote:
    return Quote(
        quoteId=quote["_id"],
        quoteAuthor=quote["quoteAuthor"],
        quoteGenre=quote["quoteGenre"],
        quoteText=quote["quoteText"],
        )

def cocktail_mapper(feature: dict) -> Cocktail:
    return Cocktail(
        cocktail_name=feature["strDrink"],
        ingredient_1=feature["strIngredient1"] if feature["strIngredient1"] != None else "",
        ingredient_2=feature["strIngredient2"] if feature["strIngredient2"] != None else "",
        ingredient_3=feature["strIngredient3"] if feature["strIngredient3"] != None else "",        
        ingredient_4=feature["strIngredient4"] if feature["strIngredient4"] != None else "", 
        glass=feature["strGlass"],
        instructions=feature["strInstructions"], 
    )