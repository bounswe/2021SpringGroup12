# RUN THIS FILE ONCE TO CREATE THE DATABASE TABLES

import sqlite3

con = sqlite3.connect(
    "c:/Users/HP/Desktop/2021SpringGroup12/practice-app/sqlfiles/practice-app.db")
cur = con.cursor()
cur.executescript(open(
    "c:/Users/HP/Desktop/2021SpringGroup12/practice-app/sqlfiles/create_tables.sql", "r").read())
con.close()
