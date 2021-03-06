------------------- BOOKS --------------
CREATE TABLE IF NOT EXISTS Books (
    book_id             INTEGER,
  	book_title          TEXT NOT NULL,
    book_author         TEXT NOT NULL,
    url                 TEXT,
    publication_dt      TEXT,
    summary             TEXT,
    uuid                TEXT,
    uri                 TEXT,
	PRIMARY KEY(book_id),
    UNIQUE(book_title,book_author)   -- restrict the same book to be added multiple times
);

CREATE TABLE IF NOT EXISTS BookISBNs (
    book_id             INTEGER,
  	isbn                TEXT,
	PRIMARY KEY(book_id,isbn),
    FOREIGN KEY(book_id) REFERENCES Books(book_id) ON DELETE CASCADE,
    UNIQUE(book_id,isbn)   -- restrict the same isbn of the book to be added multiple times
);

------------------- NAME-AGE --------------
CREATE TABLE IF NOT EXISTS Name_Infos (
	name    TEXT,
	age     INTEGER,
	country TEXT
);

------------------- ISSUE--------------
CREATE TABLE IF NOT EXISTS Issues (
    number             INTEGER,
    description         TEXT,
    state               TEXT,
	PRIMARY KEY(number)
);

CREATE TABLE IF NOT EXISTS Assignees (
    issue_number       INTEGER,
    assignee           TEXT,
	UNIQUE(issue_number,assignee)
);

CREATE TABLE IF NOT EXISTS Labels (
    issue_number       INTEGER,
    label           TEXT,
	UNIQUE(issue_number,label)
);

------------------- QUOTE --------------
CREATE TABLE IF NOT EXISTS Quotes (
    quoteId            TEXT NOT NULL,
  	quoteAuthor        TEXT NOT NULL,
    quoteGenre         TEXT NOT NULL,
    quoteText          TEXT,
	PRIMARY KEY(quoteID)
);

------------------- MOVIES --------------
CREATE TABLE IF NOT EXISTS Movie (
    movie_id            INTEGER,
  	display_title       TEXT NOT NULL,
    byline              TEXT NOT NULL,
    mpaa_rating         TEXT,
    critics_pick        INTEGER,
    headline            TEXT,
    summary_short       TEXT,
    link                TEXT NOT NULL,
	PRIMARY KEY(movie_id)
);
------------------- ANIME --------------
CREATE TABLE IF NOT EXISTS RelatedAnimes(
    id INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    mal_id INTEGER NOT NULL,
    UNIQUE(title, mal_id)
);

CREATE TABLE IF NOT EXISTS Genres(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS MalAnimes (
    id                  INTEGER PRIMARY KEY,
    title               TEXT NOT NULL,
    mal_id              INTEGER NOT NULL,
    episodes            INTEGER NOT NULL,
    image               TEXT NOT NULL,
    airing              BOOLEAN NOT NULL,
    start_date          DATE,
    end_date            DATE,
    score               REAL NOT NULL,
    rating              TEXT NOT NULL,
    type                TEXT NOT NULL,
    synopsis            TEXT NOT NULL,
    duration            INTEGER NOT NULL,
    sequel              INTEGER,
    prequel             INTEGER,
    UNIQUE(title, mal_id),
    FOREIGN KEY (sequel) REFERENCES RelatedAnimes(id),
    FOREIGN KEY (prequel) REFERENCES RelatedAnimes(id)
);

CREATE TABLE IF NOT EXISTS GenreRelation(
    id INTEGER PRIMARY KEY,
    anime_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    UNIQUE(anime_id, genre_id)
    FOREIGN KEY (anime_id) REFERENCES MalAnimes(id),
    FOREIGN KEY (genre_id) REFERENCES Genres(id)
);

CREATE TABLE IF NOT EXISTS UserAnimes(
    id                  INTEGER PRIMARY KEY,
    title               TEXT NOT NULL,
    episodes            INTEGER NOT NULL,
    image               TEXT NOT NULL,
    airing              BOOLEAN NOT NULL,
    start_date          DATE,
    end_date            DATE,
    score               REAL NOT NULL,
    rating              TEXT NOT NULL,
    type                TEXT NOT NULL,
    synopsis            TEXT NOT NULL,
    UNIQUE(title, type)
);

------------------- CURRENCY --------------
CREATE TABLE IF NOT EXISTS Currency_History (
    date               TEXT,
    from_curr          TEXT,
    to_curr            TEXT,
    rate               REAL,
    PRIMARY KEY(date,from_curr,to_curr)
);

-------------------  COCKTAIL --------------
CREATE TABLE IF NOT EXISTS Cocktails (
    cocktail_name TEXT,
    ingredient_1  TEXT,
    ingredient_2  TEXT,
    ingredient_3  TEXT,
    ingredient_4  TEXT,
    glass TEXT,
    instructions  TEXT,
    PRIMARY KEY (cocktail_name)
);