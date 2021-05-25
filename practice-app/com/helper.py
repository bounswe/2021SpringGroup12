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
