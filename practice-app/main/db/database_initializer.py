# RUN THIS FILE ONCE TO CREATE THE DATABASE TABLES

import sqlite3

DB_PATH = "/home/veyis/Desktop/2021SpringGroup12-8e1c54b7896240a6d22027d0a291d6359b737675/practice-app/sqlfiles/"


con = sqlite3.connect(DB_PATH + "practice-app.db")
cur = con.cursor()
cur.executescript(open(DB_PATH + "create_tables.sql", "r").read())
""" 
con = sqlite3.connect("/usr/src/app/./sqlfiles/practice-app.db")
cur = con.cursor()
cur.executescript(open("/usr/src/app/./sqlfiles/create_tables.sql", "r").read()) 
"""
con.commit()
con.close()