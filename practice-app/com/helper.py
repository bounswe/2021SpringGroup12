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


def get_issue(json_issue):
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
