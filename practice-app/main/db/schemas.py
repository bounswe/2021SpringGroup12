from typing import List, Optional
from pydantic import BaseModel
from enum import Enum
from datetime import datetime

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

######################################## ANIME ######################################
class RatingEnum(str, Enum):
    g = "G"
    pg = "PG"
    pg13 = "PG-13"
    r17 = "R"
    r = "R+"
    rx = "Rx"
    none = "None"


class TypeEnum(str, Enum):
    tv = "TV"
    ova = "OVA"
    movie = "Movie"
    special = "Special"
    ona = "ONA"
    music = "Music"
    unknown = "Unknown"


class CreateAnime(BaseModel):
    title: str
    episodes: int
    id: int
    image_url: Optional[str] = ""
    airing: Optional[bool] = False
    start_date: Optional[datetime] = datetime.now()
    end_date: Optional[datetime] = datetime.now()
    score: Optional[float] = 0.0
    rating: Optional[RatingEnum] = "None"
    type: Optional[TypeEnum] = "Unknown"
    synopsis: Optional[str] = "" 

class SearchResult(BaseModel):
  title: str
  episodes: int
  id: int
  image_url: str
  airing: bool
  start_date: Optional[datetime]
  end_date: Optional[datetime]
  score: float
  rating: RatingEnum
  type: TypeEnum
  synopsis: str

class SearchResponse(BaseModel):
  num_of_results: int
  animes: List[SearchResult]
  
class Genre(BaseModel):
  id: int
  type: str
  name: str

class RelatedAnime(BaseModel):
  id: int
  name: str

class Anime(BaseModel):
    title: str
    episodes: Optional[int] = 0
    id: int
    image_url: str
    airing: bool
    start_date: Optional[datetime]
    end_date: Optional[datetime]
    score: Optional[float]
    rating: RatingEnum
    type: TypeEnum
    synopsis: str
    duration: int
    sequel: Optional[RelatedAnime]
    prequel: Optional[RelatedAnime]
    genres: List[Genre]