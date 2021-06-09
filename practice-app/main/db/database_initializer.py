# RUN THIS FILE ONCE TO CREATE THE DATABASE TABLES

import sqlite3

<<<<<<< HEAD
con = sqlite3.connect("practice-app\sqlfiles\practice-app.db")
cur = con.cursor()
cur.executescript(open("practice-app\sqlfiles\create_tables.sql", "r").read())
con.close()
=======
con = sqlite3.connect("/usr/src/app/./sqlfiles/practice-app.db")
cur = con.cursor()
cur.executescript(open("/usr/src/app/./sqlfiles/create_tables.sql", "r").read())
con.close()
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
