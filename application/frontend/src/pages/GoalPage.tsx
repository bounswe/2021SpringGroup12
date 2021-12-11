import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, List, Space, Table} from "antd";
import {GoalTypes} from "../helpers/GoalTypes";

const token = localStorage.getItem("token");
const user_id = localStorage.getItem("user_id")

export function GoalPage(params :{goalType: any}) {
    const goalType = params.goalType
    console.log("goalType", goalType)
    const [goal, setGoal] = useState({
        title: "Loading",
        description: "Loading",
        token: "Loading",
        assignees: [],
        members: [],
        entities: [],
        subgoals: []
    })
    // @ts-ignore
    const {goal_id} = useParams();

    let delete_count = 0
    console.log('Burada 22')

    const deleteEntity = (entity: { key: any, entityType: string}) => {
        console.log('Received values of delete: ', goal);
        axios.delete(`/entities/${entity.entityType.toLowerCase()}/${entity.key}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then(() => delete_count++)
    };

    const deleteGoal = (goal: { key: any; }) => {
        console.log('Received values of delete: ', goal);
        axios.delete(`/goals/${goal.key}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then(() => delete_count++)
    };

    const subgoal_columns =  [
            {
                title: 'Title',
                dataIndex: 'title',
                key: 'title',
                render: (text: any,
                         goal: any) =>
                    <Link to={`/${GoalTypes.Sub}/${goal.id}`}> {text} </Link>
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
        {
            dataIndex: "description",
            title: 'Action',
            key: 'action',
            render: (text: any,
                     goal: any) =>
                (   <div>
                        <Space size="middle">
                            <Button type="primary" onClick={() => deleteGoal(goal)}>
                                Delete
                            </Button>
                        </Space>
                        <Link to={"/editGoal/" + goal.id}>
                            <button type="button">
                                Edit
                            </button>
                        </Link>
                    </div>

                )
        }
        ];

    const columns = [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
            render: (text: any,
                     entity: any) =>
                <Link to={"/entity/" + entity.id}> {text} </Link>
            ,
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Entity Type',
            dataIndex: 'entitiType',
            key: 'entitiType',
        },
        {
            title: 'Rating',
            dataIndex: 'rating',
            key: 'rating',
        },
        {
            title: 'Action',
            key: 'action',
            render: (text: any,
                     entity: { key: number, entityType: string}) =>
                (   <div>
                        <Space size="middle">
                            <Button type="primary" onClick={() => deleteEntity(entity)}>
                                Delete
                            </Button>
                        </Space>
                        <Link to={"/editEntity/" + entity.key}>
                            <button type="button">
                                Edit
                            </button>
                        </Link>
                    </div>
                ),
        },
    ];

    useEffect(() => {
        axios.get(`/${goalType}/${goal_id}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            })
            .then(response => {
                // check for error response
                if (response.status === 200) {
                    return response.data
                }
                throw response
            })
            .then(goal_info => {
                console.log(typeof goal_info["entities"])
                console.log(goal_info)
                if (goalType === GoalTypes.Sub) {
                    goal_info['subgoals'] = goal_info['sublinks']
                }
                goal_info['subgoals'].forEach((subgoal: any, i: number) => {
                    subgoal.key = i
                })
                goal_info['entities'].forEach((entity: any, i: number) => {
                    entity.key = i
                })
                setGoal(goal_info)

            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }, [goal_id, goalType]);

    const showAssignees = goal['assignees'] !== undefined && goal['assignees'].length > 0
    return (
        <div>
            <h2>Name: {goal['title']}</h2>
            <h2>Description: {goal['description']}</h2>
            {goalType === GoalTypes.Group &&
            <div>
                <h2>Token: {goal['token']}</h2>
                <List
                    size="small"
                    header={<div>Members</div>}
                    bordered
                    dataSource={goal['members']}
                    renderItem={item => <List.Item>{item}</List.Item>}
                />
            </div>}
            {showAssignees &&
            <List
                size="small"
                header={<div>Assignees</div>}
                bordered
                dataSource={goal['assignees']}
                renderItem={item => <List.Item>{item}</List.Item>}
            />}
            <Table columns={subgoal_columns} dataSource={goal['subgoals']} />
            <Link to={"/addEntity/" + goal_id}>
                <button type="button">
                    Add SubGoal
                </button>
            </Link>
            <Table columns={columns} dataSource={goal['entities']} />
            <Link to={"/addEntity/" + goal_id}>
                <button type="button">
                    Add Entity
                </button>
            </Link>
        </div>)
}

