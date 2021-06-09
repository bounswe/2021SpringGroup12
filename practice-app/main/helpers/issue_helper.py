import sqlite3
from sqlite3 import Cursor
from typing import List

from main.db.schemas import Issue

ALL_ISSUES = {}


def make_issue(git_issue):
    try:
        return {
            'number': git_issue['number'],
            'assignees': [assignee['login'] for assignee in git_issue['assignees']],
            'description': git_issue['body'],
            'labels': [label['name'] for label in git_issue['labels']],
            'state': git_issue['state']
        }
    except Exception:
        return {}


def get_simple_issue(json_issue):
    ret_issue = {
        'number': json_issue['number'],
        'assignees': [],
        'description': json_issue['description'],
        'labels': [],
        'state': json_issue['state']
    }
    if 'assignees' in json_issue:
        ret_issue['assignees'] = json_issue['assignees']

    if 'labels' in json_issue:
        ret_issue['labels'] = json_issue['labels']

    return ret_issue


DB_LOC = "C:/GIT/2021SpringGroup12/practice-app/sqlfiles/practice-app.db"


def insert_issue(cur: Cursor, issue: Issue):
    cur.execute(
        "INSERT OR IGNORE INTO Issues(number, description, state) VALUES (?,?,?)",
        (issue.number, issue.description, issue.state))
    for assignee in issue.assignees:
        cur.execute(
            "INSERT OR IGNORE INTO Assignees(issue_number, assignee) VALUES (?,?)",
            (issue.number, assignee))
    for label in issue.labels:
        cur.execute(
            "INSERT OR IGNORE INTO Labels(issue_number, label) VALUES (?,?)",
            (issue.number, label))


<<<<<<< HEAD
def insert_single_issue(issue: Issue):
    con = sqlite3.connect(DB_LOC)
    cur = con.cursor()
    try:
        insert_issue(cur, issue)
    except Exception as e:
        print(f"An exception occurred inserting issue {issue.number}")
        raise e
    finally:
        con.commit()
        con.close()


=======
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
def insert_multiple_issue(issue_list: List[Issue]):
    con = sqlite3.connect(DB_LOC)
    cur = con.cursor()
    for issue in issue_list:
        try:
            insert_issue(cur, issue)
        except Exception as e:
            print(f"An exception occurred inserting issue {issue.number}")
    con.commit()
    con.close()


def get_issue_count() -> int:
    con = sqlite3.connect(DB_LOC)
    cur = con.cursor()
    try:
        data = cur.execute("select count(*) from Issues")
        return data.fetchone()[0]
    except Exception:
        print(f'An exception occurred while getting issue count')
        return -1
    finally:
        con.commit()
        con.close()


def get_issue(issue_number: int) -> Issue:
    con = sqlite3.connect(DB_LOC)
    cur = con.cursor()

    try:
<<<<<<< HEAD
        data = cur.execute(
            "SELECT * FROM Issues WHERE number=?", (issue_number,))
        _, description, state = data.fetchone()
        data = cur.execute(
            "SELECT assignee FROM Assignees WHERE issue_number=?", (issue_number,))
        assignees = [row[0] for row in data.fetchall()]
        data = cur.execute(
            "SELECT label FROM Labels WHERE issue_number=?", (issue_number,))
=======
        data = cur.execute("SELECT * FROM Issues WHERE number=?", (issue_number,))
        _, description, state = data.fetchone()
        data = cur.execute("SELECT assignee FROM Assignees WHERE issue_number=?", (issue_number,))
        assignees = [row[0] for row in data.fetchall()]
        data = cur.execute("SELECT label FROM Labels WHERE issue_number=?", (issue_number,))
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
        labels = [row[0] for row in data.fetchall()]
        return Issue(number=issue_number,
                     description=description,
                     state=state,
                     assignees=assignees,
                     labels=labels)
    except Exception:
        print(f'An exception occurred while getting issue {issue_number}')
    finally:
        con.commit()
        con.close()


def get_all_issues(limit: int) -> List[Issue]:
    con = sqlite3.connect(DB_LOC)
    cur = con.cursor()

    issue_list = []

    try:
        data = cur.execute("SELECT * FROM Issues LIMIT ?", (limit,))
        issues = data.fetchall()
        for issue_number, description, state in issues:
<<<<<<< HEAD
            data = cur.execute(
                "SELECT assignee FROM Assignees WHERE issue_number=?", (issue_number,))
            assignees = [row[0] for row in data.fetchall()]
            data = cur.execute(
                "SELECT label FROM Labels WHERE issue_number=?", (issue_number,))
=======
            data = cur.execute("SELECT assignee FROM Assignees WHERE issue_number=?", (issue_number,))
            assignees = [row[0] for row in data.fetchall()]
            data = cur.execute("SELECT label FROM Labels WHERE issue_number=?", (issue_number,))
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
            labels = [row[0] for row in data.fetchall()]
            issue_list.append(Issue(number=issue_number,
                                    description=description,
                                    state=state,
                                    assignees=assignees,
                                    labels=labels)
                              )
    except Exception:
        print(f'An error occurred getting all issues')
    finally:
        con.commit()
        con.close()
        return issue_list
