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
    FOREIGN KEY(book_id) REFERENCES Books(book_id), -- todo: on delete vsvs
    UNIQUE(book_id,isbn)   -- restrict the same isbn of the book to be added multiple times
);

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

 
 Movie(display_title, mpaa_rating, critics_pick, byline, headline,summary_short, link)