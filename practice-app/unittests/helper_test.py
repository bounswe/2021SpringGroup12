import unittest
import os
from main.helpers.issue_helper import make_issue
import json
current_directory = os.getcwd()


class HelperTest(unittest.TestCase):
    def test_make_issue_git_response(self):
        git_issue = json.loads("""{
    "url": "https://api.github.com/repos/bounswe/2021SpringGroup12/issues/96",
    "repository_url": "https://api.github.com/repos/bounswe/2021SpringGroup12",
    "labels_url": "https://api.github.com/repos/bounswe/2021SpringGroup12/issues/96/labels{/name}",
    "comments_url": "https://api.github.com/repos/bounswe/2021SpringGroup12/issues/96/comments",
    "events_url": "https://api.github.com/repos/bounswe/2021SpringGroup12/issues/96/events",
    "html_url": "https://github.com/bounswe/2021SpringGroup12/issues/96",
    "id": 869267146,
    "node_id": "MDU6SXNzdWU4NjkyNjcxNDY=",
    "number": 96,
    "title": "Creating a sequence diagram",
    "user": {
      "login": "gokayyildiz",
      "id": 42673110,
      "node_id": "MDQ6VXNlcjQyNjczMTEw",
      "avatar_url": "https://avatars.githubusercontent.com/u/42673110?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/gokayyildiz",
      "html_url": "https://github.com/gokayyildiz",
      "followers_url": "https://api.github.com/users/gokayyildiz/followers",
      "following_url": "https://api.github.com/users/gokayyildiz/following{/other_user}",
      "gists_url": "https://api.github.com/users/gokayyildiz/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/gokayyildiz/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/gokayyildiz/subscriptions",
      "organizations_url": "https://api.github.com/users/gokayyildiz/orgs",
      "repos_url": "https://api.github.com/users/gokayyildiz/repos",
      "events_url": "https://api.github.com/users/gokayyildiz/events{/privacy}",
      "received_events_url": "https://api.github.com/users/gokayyildiz/received_events",
      "type": "User",
      "site_admin": false
    },
    "labels": [
      {
        "id": 2848395789,
        "node_id": "MDU6TGFiZWwyODQ4Mzk1Nzg5",
        "url": "https://api.github.com/repos/bounswe/2021SpringGroup12/labels/documentation",
        "name": "documentation",
        "color": "0075ca",
        "default": true,
        "description": "Improvements or additions to documentation"
      },
      {
        "id": 2860874477,
        "node_id": "MDU6TGFiZWwyODYwODc0NDc3",
        "url": "https://api.github.com/repos/bounswe/2021SpringGroup12/labels/priority:%20high",
        "name": "priority: high",
        "color": "CEB310",
        "default": false,
        "description": ""
      }
    ],
    "state": "closed",
    "locked": false,
    "assignee": {
      "login": "gokayyildiz",
      "id": 42673110,
      "node_id": "MDQ6VXNlcjQyNjczMTEw",
      "avatar_url": "https://avatars.githubusercontent.com/u/42673110?v=4",
      "gravatar_id": "",
      "url": "https://api.github.com/users/gokayyildiz",
      "html_url": "https://github.com/gokayyildiz",
      "followers_url": "https://api.github.com/users/gokayyildiz/followers",
      "following_url": "https://api.github.com/users/gokayyildiz/following{/other_user}",
      "gists_url": "https://api.github.com/users/gokayyildiz/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/gokayyildiz/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/gokayyildiz/subscriptions",
      "organizations_url": "https://api.github.com/users/gokayyildiz/orgs",
      "repos_url": "https://api.github.com/users/gokayyildiz/repos",
      "events_url": "https://api.github.com/users/gokayyildiz/events{/privacy}",
      "received_events_url": "https://api.github.com/users/gokayyildiz/received_events",
      "type": "User",
      "site_admin": false
    },
    "assignees": [
      {
        "login": "gokayyildiz",
        "id": 42673110,
        "node_id": "MDQ6VXNlcjQyNjczMTEw",
        "avatar_url": "https://avatars.githubusercontent.com/u/42673110?v=4",
        "gravatar_id": "",
        "url": "https://api.github.com/users/gokayyildiz",
        "html_url": "https://github.com/gokayyildiz",
        "followers_url": "https://api.github.com/users/gokayyildiz/followers",
        "following_url": "https://api.github.com/users/gokayyildiz/following{/other_user}",
        "gists_url": "https://api.github.com/users/gokayyildiz/gists{/gist_id}",
        "starred_url": "https://api.github.com/users/gokayyildiz/starred{/owner}{/repo}",
        "subscriptions_url": "https://api.github.com/users/gokayyildiz/subscriptions",
        "organizations_url": "https://api.github.com/users/gokayyildiz/orgs",
        "repos_url": "https://api.github.com/users/gokayyildiz/repos",
        "events_url": "https://api.github.com/users/gokayyildiz/events{/privacy}",
        "received_events_url": "https://api.github.com/users/gokayyildiz/received_events",
        "type": "User",
        "site_admin": false
      }
    ],
    "milestone": null,
    "comments": 0,
    "created_at": "2021-04-27T21:10:11Z",
    "updated_at": "2021-05-01T08:06:09Z",
    "closed_at": "2021-05-01T08:06:09Z",
    "author_association": "COLLABORATOR",
    "active_lock_reason": null,
    "body": "Creating a sequence diagram\r\nEvent: Link a Task to a Question",
    "performed_via_github_app": null
  }""", strict=False)
        issue = make_issue(git_issue)
        expected_issue = {
            'number': 96,
            'assignees': ['gokayyildiz'],
            'description': 'Creating a sequence diagram\r\nEvent: Link a Task to a Question',
            'labels': ['documentation', 'priority: high'],
            'state': 'closed'
        }
        self.assertDictEqual(issue, expected_issue)

    def test_make_issue_empty_dict(self):
        git_issue = {
            'deneme': 'hop',
            'fail': 'za'
        }
        issue = make_issue(git_issue)
        expected_issue = {}

        self.assertDictEqual(issue, expected_issue)


if __name__ == '__main__':
    unittest.main()
