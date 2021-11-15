import {Button, Space, Table, Tag} from 'antd';
import * as React from "react";
import { Link } from 'react-router-dom';
import axios from "axios";
export class GoalsPage extends React.Component {
    state = {
        isLoaded: false,
        goals: [],
    };
    user_id = localStorage.getItem("user_id")
    token = localStorage.getItem("token");

    deleteGoal = (goal: { key: any; }) => {
        console.log('Received values of delete: ', goal);
        axios.delete(`/goals/${goal.key}`,
            {
                headers: { Authorization: `Bearer ${this.token}`},
                data: {}
            }).then(() => this.getGoals())
    };

    getGoals() {
        console.log(axios.defaults.baseURL)
        axios.get(`/goals/of_user/${this.user_id}`,
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
                    tmp.push({
                        key: data[i]['id'],
                        title: data[i]['title'],
                        description: data[i]['description'],
                        goalType: data[i]['goalType'],
                        isDone: data[i]['isDone'],
                        deadline: data[i]['deadline']
                    })
                }
                this.setState({
                    isLoaded: true,
                    goals: tmp
                })

                console.log('Success!')
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    componentDidMount() {
        this.getGoals()
    }

    columns = [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
            render: (text: any,
                     goal: { key: string | number | boolean | {} | React.ReactElement<any, string | React.JSXElementConstructor<any>> | React.ReactNodeArray | React.ReactPortal | null | undefined; }) =>
                <Link to={"/goal/" + goal.key}> {text} </Link>
            ,
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Goal Type',
            dataIndex: 'goalType',
            key: 'goalType',
        },
        {
            title: 'Deadline',
            dataIndex: 'deadline',
            key: 'deadline',
        },
        {
            title: 'Is Done',
            dataIndex: 'isDone',
            key: 'isDone',
            render: (isDone: boolean) => {
                let color = 'red';
                let text = 'NOT DONE'
                if (isDone) {
                    color = 'green';
                    text = 'DONE'
                }
                return (
                    <Tag color={color}>
                        {text}
                    </Tag>
                );
            }
        },
        {
            title: 'Action',
            key: 'action',
            render: (text: any,
                     goal: { key: string | number | boolean | {} | React.ReactElement<any, string | React.JSXElementConstructor<any>> | React.ReactNodeArray | React.ReactPortal | null | undefined; }) =>
                (   <div>
                        <Space size="middle">
                            <Button type="primary" onClick={() => this.deleteGoal(goal)}>
                                Delete
                            </Button>
                        </Space>
                        <Link to={"/editGoal/" + goal.key}>
                            <button type="button">
                                Edit
                            </button>
                        </Link>
                    </div>

                ),
        },
    ];

    render() {
        const { isLoaded,
            goals,
        } = this.state;

        if (!isLoaded) {
            return <div>Loading...</div>;
        }

        console.log('goals', goals)

        return (
            <div>
                <Table columns={this.columns} dataSource={goals} />
                <Link to="/addGoal">
                    <button type="button">
                        Add Goal
                    </button>
                </Link>
            </div>

        )

    }
}
