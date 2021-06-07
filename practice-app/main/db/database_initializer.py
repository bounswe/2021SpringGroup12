# RUN THIS FILE ONCE TO CREATE THE DATABASE TABLES

import sqlite3

con = sqlite3.connect("practice-app\sqlfiles\practice-app.db")
cur = con.cursor()
cur.executescript(open("practice-app\sqlfiles\create_tables.sql", "r").read())
con.close()
