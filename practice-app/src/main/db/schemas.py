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
    copyright: Optional[str] = ""
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
