from typing import List, Optional
from pydantic import BaseModel


class CreateBook(BaseModel):
    book_title: str
    book_author: str
    url: Optional[str] = ""
    publication_dt: Optional[str] = ""
    summary: Optional[str] = ""
    uuid: Optional[str] = ""
    uri: Optional[str] = ""
    isbn13: Optional[List[str]]= ""

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
    copyright: Optional[str]
    num_results: int
    books: List[dict]
