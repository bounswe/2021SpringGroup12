# RUN THIS FILE ONCE TO CREATE THE DATABASE TABLES

import sqlite3

<<<<<<< HEAD
con = sqlite3.connect("../../sqlfiles/practice-app.db")
cur = con.cursor()
cur.executescript(open("../../sqlfiles/create_tables.sql", "r").read())
=======
con = sqlite3.connect("/usr/src/app/./sqlfiles/practice-app.db")
cur = con.cursor()
cur.executescript(open("/usr/src/app/./sqlfiles/create_tables.sql", "r").read())
>>>>>>> master
con.close()