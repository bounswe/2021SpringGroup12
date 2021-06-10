# RUN THIS FILE ONCE TO CREATE THE DATABASE TABLES

import sqlite3,os

DB_PATH = os.getenv("DB_PATH","/usr/src/app/./sqlfiles/practice-app.db")

#DB_PATH  = os.environ.get("DB_PATH","asdhgsa")

con = sqlite3.connect(DB_PATH)
cur = con.cursor()
cur.executescript(open("/usr/src/app/./sqlfiles/create_tables.sql", "r").read()) 

con.commit()
con.close()