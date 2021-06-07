# RUN THIS FILE ONCE TO CREATE THE DATABASE TABLES

import sqlite3

con = sqlite3.connect("../../../sqlfiles/practice-app.db")
cur = con.cursor()
cur.executescript(open("../../../sqlfiles/create_tables.sql", "r").read())
con.close()