from typing import List, Optional, NamedTuple
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
    
class NameInfo(BaseModel):
    name: str
    age: int
    country: str
    
class NameInfoResponse():
    name: str
    age: int
    country: str
    count: int

class SearchedAnime(BaseModel):
    title: str
    image: str
    synopsis: str
    type: str
    start_date: str
    end_date: str
    score: float
    rating: str
    airing: bool
    mal_id: int

class RelatedAnime(NamedTuple):
    title: str
    mal_id: int

class Anime(BaseModel):
    title: str
    mal_id: int
    episodes: int 
    image: str
    airing: bool
    start_date:Optional[str]
    end_date:Optional[str]
    score: int
    rating: str
    type: str
    synopsis: str
    duration: int
    sequel: Optional[RelatedAnime]
    prequel: Optional[RelatedAnime]
    genres: List[str]

class UserAnime(BaseModel):
    title: str
    episodes: int
    image: str
    airing: bool
    start_date: str
    end_date: str
    score: float
    rating: str
    type: str
    synopsis: str


class CurrencyRate(BaseModel):
    date: str
    from_curr: str
    to_curr: str
    rate: float
