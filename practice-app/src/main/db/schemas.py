from typing import List, Optional
from pydantic import BaseModel


class Book(BaseModel):
    book_title: str
    book_author: str
    url: str
    publication_dt: str
    summary: str
    uuid: str
    uri: str
    isbn13: List[str]


class BookResponse(BaseModel):
    num_results: int
    books: List[dict]

class Quote(BaseModel):
    quoteId: str
    quoteAuthor: str
    quoteGenre: str
    quoteText: str
    #last: int
    
class QuoteResponse(BaseModel):
    data: List[dict]


class ErrorResponse(BaseModel):
    message: Optional[str] = ""


class Issue(BaseModel):
    number: int
    assignees: List[str]
    description: str
    labels: List[str]
    state: str


class CurrencyRate(BaseModel):
    date: str
    from_curr: str
    to_curr: str
    rate: float

class Cocktail(BaseModel):
    cocktail_name: str
    ingredient_1: str
    ingredient_2: str
    ingredient_3: str
    ingredient_4: str
    ingredient_4: str
    glass: str
    instructions: str
    
class CocktailResponse(BaseModel):
    cocktails: List[dict]
