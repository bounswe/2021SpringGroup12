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

CREATE TABLE IF NOT EXISTS Quotes (
    quoteId            TEXT NOT NULL,
  	quoteAuthor        TEXT NOT NULL,
    quoteGenre         TEXT NOT NULL,
    quoteText          TEXT,
	PRIMARY KEY(quoteID)
);

CREATE TABLE IF NOT EXISTS Currency_History (
    date               TEXT,
    from_curr          TEXT,
    to_curr            TEXT,
    rate               REAL,
    PRIMARY KEY(date,from_curr,to_curr)
);

  
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