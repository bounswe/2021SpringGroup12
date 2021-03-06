from main.helpers import issue_helper
from main.db.schemas import Book, Issue, Quote, CurrencyRate, SearchedAnime, UserAnime, Anime, RelatedAnime, Movie, Cocktail

import sys
sys.path.append("..")


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
        #last = quote["__v"]
    )


def movie_mapper(movie: dict) -> Movie:
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


def quote_mapper(quote: dict) -> Quote:
    return Quote(
        quoteId=quote["_id"],
        quoteAuthor=quote["quoteAuthor"],
        quoteGenre=quote["quoteGenre"],
        quoteText=quote["quoteText"],
        #last = quote["__v"]
    )


def searched_anime_mapper(anime: dict) -> SearchedAnime:
    return SearchedAnime(
        title=anime["title"],
        image=anime["image_url"],
        synopsis=anime["synopsis"] if anime["synopsis"] != None else "",
        type=anime["type"] if anime["type"] != None else "",
        start_date=anime["start_date"] if anime["start_date"] != None else "",
        end_date=anime["end_date"] if anime["end_date"] != None else "",
        score=anime["score"] if anime["score"] != None else 0.0,
        rating=anime["rated"] if anime["rated"] != None else "",
        airing=anime["airing"] if anime["airing"] != None else False,
        mal_id=anime["mal_id"] if anime["mal_id"] != None else 0
    )

def anime_mapper(anime: dict) -> Anime:
    return Anime(
        title=anime["title"],
        mal_id=anime["mal_id"],
        episodes=anime["episodes"] if anime["episodes"] != None else 0,
        image=anime["image_url"] if anime["image_url"] != None else "",
        airing=anime["airing"] if anime["airing"] != None else False,
        start_date=anime["aired"]["from"],
        end_date=anime["aired"]["to"],
        score=anime["score"] if anime["score"] != None else 0.0,
        rating=anime["rating"] if anime["rating"] != None else "",
        type=anime["type"] if anime["type"] != None else "",
        synopsis=anime["synopsis"] if anime["synopsis"] != None else "",
        duration=(int(anime["duration"].split(' ')[0])
            if not anime["duration"].split(" ")[1] == "hr"
            else int(anime["duration"].split(" ")[0])*60+int(anime["duration"].split(" ")[2])) 
                if anime["duration"] and anime["duration"] != "Unknown" 
                else 0,
        sequel=RelatedAnime(title=anime["related"]["Sequel"][0]["name"], mal_id=anime["related"]["Sequel"][0]["mal_id"]) if "Sequel" in anime["related"] else None,
        prequel=RelatedAnime(title=anime["related"]["Prequel"][0]["name"], mal_id=anime["related"]["Prequel"][0]["mal_id"]) if "Prequel" in anime["related"] else None,
        genres=[genre["name"] for genre in anime["genres"]]
    )

def create_anime_mapper(anime: dict) -> UserAnime:
    return UserAnime(
        title=anime["title"],
        episodes=anime["episodes"],
        image=anime["image"],
        airing=anime["airing"],
        start_date=anime["start_date"],
        end_date=anime["end_date"] if anime["end_date"] != None else "",
        score=anime["score"],
        rating=anime["rating"],
        type=anime["type"],
        synopsis=anime["synopsis"]
    )


def currency_rate_mapper(currency_rate: dict) -> CurrencyRate:
    return CurrencyRate(
        date=currency_rate["date"],
        from_curr=currency_rate["query"]["from"] if 'query' in currency_rate.keys(
        ) else currency_rate["from"],
        to_curr=currency_rate["query"]["to"] if 'query' in currency_rate.keys(
        ) else currency_rate["to"],
        rate=currency_rate["info"]["rate"] if 'info' in currency_rate.keys(
        ) else currency_rate["rate"],
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