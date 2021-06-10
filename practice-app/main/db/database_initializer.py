# RUN THIS FILE ONCE TO CREATE THE DATABASE TABLES

import sqlite3

con = sqlite3.connect("/usr/src/app/./sqlfiles/practice-app.db")
cur = con.cursor()
cur.executescript(open("/usr/src/app/./sqlfiles/create_tables.sql", "r").read())
con.close()