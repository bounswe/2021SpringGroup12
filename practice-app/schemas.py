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

class ErrorResponse(BaseModel):
    message: Optional[str] =""


class CurrencyRate(BaseModel):
        date: str
        from_curr: str
        to_curr: str
        rate: float
