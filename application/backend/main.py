from flask import Flask, request
from flask_cors import CORS

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})

GOALS = [
    {
        'name': 'goal_1',
        'description': 'desc_1'
    },
    {
        'name': 'goal_2',
        'description': 'desc_2'
    },
    {
        'name': 'goal_3',
        'description': 'desc_3'
    }
]


@app.route('/goals')
def goals():
    return {
               'data': GOALS
           }, 200


@app.route('/add_goal', methods=['POST'])
def add_goal():
    data = request.json
    GOALS.append(data)

    return {'message': 'success'}, 200


@app.route('/delete_goal', methods=['POST'])
def delete_goal():
    name = request.json['name']
    for i in range(len(GOALS)):
        if GOALS[i]['name'] == name:
            GOALS.pop(i)
            break

    return {'message': 'success'}, 200


@app.route('/goal/<name>', )
def get_goal(name: str):
    goal = {}
    for i in range(len(GOALS)):
        if GOALS[i]['name'] == name:
            goal = GOALS[i]
            break
    print(goal)

    return {'data': goal}, 200


if __name__ == "__main__":
    app.run(port=5002)
