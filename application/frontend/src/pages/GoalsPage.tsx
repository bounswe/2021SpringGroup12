import {Button, Form, Input, Table} from 'antd';
import * as React from "react";
import {Link} from 'react-router-dom';
import axios from "axios";
import {GoalTypes} from "../helpers/GoalTypes";

export class GoalsPage extends React.Component {
    state = {
        isGroupsLoaded: false,
        goals: [],
        isGroupGoalsLoaded: false,
        groupGoals: [],
    };
    user_id = localStorage.getItem("user_id")
    token = localStorage.getItem("token");

    deleteGoal = (goal: { key: any; }) => {
        console.log('Received values of delete: ', goal);
        axios.delete(`/${GoalTypes.Normal}/${goal.key}`,
            {
                headers: { Authorization: `Bearer ${this.token}`},
                data: {}
            }).then(() => this.getGoals())
    };

    joinGroup = (input: {token: string}) => {
        const token = input.token
        console.log('Token: ', token);
        axios.post(`/${GoalTypes.Group}/${this.user_id}/join`, {},
            {
                headers: {Authorization: `Bearer ${this.token}`},
                params: {
                    token: token
                }
            }).then(response => {
            if (response.status === 200) {
                window.alert('Joined group successfully!')
                this.getGoals(true)
            } else {
                window.alert(`An error occurred while joining a group goal! ${response.data}`)
            }
        })
    }

    getGoals(isGroupGoal: boolean = false) {
        console.log(this.token)
        console.log(axios.defaults.baseURL)
        const url = isGroupGoal ? `/${GoalTypes.Group}/member_of/${this.user_id}` : `/${GoalTypes.Normal}/of_user/${this.user_id}`
        console.log(url)
        axios.get(url,
            {
                headers: { Authorization: `Bearer ${this.token}`},
                data: {}
            })
            .then(response => {
                // check for error response
                if (response.status === 200) {
                    return response.data
                }
                throw response
            })
            .then(data => {
                let tmp = []
                for (let i = 0; i < data.length; i++) {
                    let deadline = data[i]['deadline']
                    if (deadline !== null) {
                        deadline = deadline.substr(0,10)
                    }
                    tmp.push({
                        key: data[i]['id'],
                        title: data[i]['title'],
                        description: data[i]['description'],
                        deadline: deadline
                    })
                }
                console.log('tmp', tmp)
                if (isGroupGoal) {
                    this.setState({
                        isGroupGoalsLoaded: true,
                        groupGoals: tmp,
                    })
                } else {
                    this.setState({
                        isGroupsLoaded: true,
                        goals: tmp
                    })
                }
                console.log('Success!', this.state)
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    componentDidMount() {
        this.getGoals(false)
        this.getGoals(true)
    }

    columns = (isGroupGoal: boolean = false) => {
        return [
            {
                title: 'Title',
                dataIndex: 'title',
                key: 'title',
                render: (text: any,
                         goal: { key: string | number | boolean | {} | React.ReactElement<any, string | React.JSXElementConstructor<any>> | React.ReactNodeArray | React.ReactPortal | null | undefined; }) =>
                    <Link
                        to={(isGroupGoal ? `/${GoalTypes.Group}/` : `/${GoalTypes.Normal}/`) + goal.key}> {text} </Link>
                ,
            },
            {
                title: 'Description',
                dataIndex: 'description',
                key: 'description',
            },
            {
                title: 'Deadline',
                dataIndex: 'deadline',
                key: 'deadline',
            },

        ]
    }

    render() {
        const { isGroupsLoaded,
            goals,
            isGroupGoalsLoaded,
            groupGoals
        } = this.state;

        console.log('goals', goals)
        console.log('groupGoals', groupGoals)

        return (
            <div>
                <h2>Goals</h2>
                {!isGroupsLoaded ? (
                    <div>Loading...</div>
                ) : (
                    <div>
                        <Table columns={this.columns(false)} dataSource={goals} />
                        <Link to="/addGoal">
                            <button type="button">
                                Add Goal
                            </button>
                        </Link>
                    </div>
                )}
                <h2>Group Goals</h2>
                {!isGroupGoalsLoaded ? (
                    <div>Loading...</div>
                ) : (
                    <div>
                        <Table columns={this.columns(true)} dataSource={groupGoals} />
                        <Link to="/addGroupGoal">
                            <button type="button">
                                Add Group Goal
                            </button>
                        </Link>
                    </div>
                )}
                <Form onFinish={this.joinGroup}>
                    <h2>Token</h2>
                    <Form.Item
                        name="token"
                        rules={[{ required: true, message: 'Please input a token!' }]}
                    >
                        <Input placeholder="Token"/>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="submit">
                            Submit
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        )
    }
}
