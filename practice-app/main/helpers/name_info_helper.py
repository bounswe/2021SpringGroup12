import sqlite3
from sqlite3 import Cursor
from typing import List

from main.db.schemas import NameInfo


DB_LOC = "C:/Users/fb_be/Documents/GitHub/2021SpringGroup12/practice-app/sqlfiles/practice-app.db"
DB_PATH = "/home/veyis/Desktop/2021SpringGroup12-8e1c54b7896240a6d22027d0a291d6359b737675/practice-app/sqlfiles/practice-app.db"
DB_PATH = "/usr/src/app/./sqlfiles/practice-app.db"

def insert_name_info(nameInfo: NameInfo):
    con = sqlite3.connect(DB_PATH)
    cur = con.cursor()
    try:
        cur.execute(
            "INSERT INTO Name_Infos(name, age, country) VALUES (?,?,?)",
            (nameInfo.name, nameInfo.age, nameInfo.country))
    except Exception as e:
        return False
    finally:
        con.commit()
        con.close()
    return True

##Returns a tuple of (name, age) represents name and average age for that name
def get_name_info(name: str, country: str, countryBased: bool):
    con = sqlite3.connect(DB_PATH)
    cur = con.cursor()
    
    try:
        if countryBased:
            cur.execute("SELECT AVG(age) as average_age, COUNT(age) as cnt FROM Name_Infos WHERE name = ? AND country = ?", (name, country))
        else:
            cur.execute("SELECT AVG(age) as average_age, COUNT(age) as cnt FROM Name_Infos WHERE name = ?", [name])
                               
        dbData = cur.fetchone()
        print(dbData)
    except Exception:
        print(f'An error occurred getting name informations')
        return None
    finally:
        con.close()
    print(dbData)
    return dbData