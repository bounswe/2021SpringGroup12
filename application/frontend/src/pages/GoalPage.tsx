import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Input, Space, Table, Tag} from "antd";

const token = localStorage.getItem("token");
const user_id = localStorage.getItem("user_id")

export function GoalPage() {
    const [goal, setGoal] = useState({
        title: "Loading",
        description: "Loading"
    })
    const [entities, setEntities] = useState([])
    const [subgoals, setSubgoals] = useState([])
    const [isLoaded, setLoaded] = useState(false)
    const[delete_count,setDeleteCount] =useState(0)
    // @ts-ignore
    const {goal_id} = useParams();

    console.log(goal_id)
    let delete_count_goal=0;
    const deleteEntity = (entity: { key: any, entitiType: string,id:number}) => {
        console.log('Received values of delete: ', goal);
        axios.delete(`/entities/${entity.entitiType.toLowerCase()}/${entity.id}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then(() => setDeleteCount(delete_count+1))
    };

    const deleteGoal = (goal: { key: any; }) => {
        console.log('Received values of delete: ', goal);
        axios.delete(`/goals/${goal.key}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then(() => delete_count_goal++)
    };
    


    useEffect(() => {
        axios.get(`/goals/${goal_id}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            })
            .then(response => {
                // check for error response
                console.log("response: " , response)
                if (response.status === 200) {
                    return response.data
                }
                throw response
            })
            .then(goal_info => {
                console.log(typeof goal_info["entities"])
                setGoal(goal_info)
                setEntities(goal_info["entities"])
                for (let i = 0; i < entities.length; i++) {
                    
                }
                setSubgoals(goal_info["subgoals"])
                setLoaded(true)
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }, [delete_count]);

    const subgoal_columns =  [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
            render: (text: any,
                     goal: { key: string | number | boolean | {} | React.ReactElement<any, string | React.JSXElementConstructor<any>> | React.ReactNodeArray | React.ReactPortal | null | undefined; }) =>
                <Link to={"/goals/" + goal.key}> {text} </Link>
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
                 goal: { key: string | number | boolean | {} | React.ReactElement<any, string | React.JSXElementConstructor<any>> | React.ReactNodeArray | React.ReactPortal | null | undefined; }) =>
            (   <div>
                    <Space size="middle">
                        <Button type="primary" onClick={() => deleteGoal(goal)}>
                            Delete
                        </Button>
                    </Space>
                    <Link to={"/editGoal/" + goal.key}>
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
            <Link to={"/entity/" + entity.entitiType + "/" + entity.id}> {text} </Link>
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
        id: "id",
        render: (text: any,
                 entity: { key: number, entitiType: string, id: number}) =>
            (   <div>
                    <Space size="middle">
                        <Button type="primary" onClick={() => deleteEntity(entity)}>
                            Delete
                        </Button>
                    </Space>
                    <Link to={"/editEntity/" + entity.entitiType + "/" + entity.id}>
                        <button type="button">
                            Edit
                        </button>
                    </Link>
                </div>

            ),
    },
];

    if (!isLoaded) {
        return <h2>Loading...</h2>
    }
    return (
        <div>
            <h2>Name: {goal['title']}</h2>
            <h2>Description: {goal['description']}</h2>
            <Table columns={subgoal_columns} dataSource={subgoals} />
            <Link to={"/addEntity/" + goal_id}>
                <button type="button">
                    Add SubGoal
                </button>
            </Link>
            <Table columns={columns} dataSource={entities} />
            <Link to={"/addEntity/goal/question/" + goal_id}>
                <button type="button">
                    Add Question
                </button>
            </Link>
            <Link to={"/addEntity/goal/reflection/" + goal_id}>
                <button type="button">
                    Add Reflection
                </button>
            </Link>
            <Link to={"/addEntity/goal/routine/" + goal_id}>
                <button type="button">
                    Add Routine
                </button>
            </Link>
            <Link to={"/addEntity/goal/task/" + goal_id}>
                <button type="button">
                    Add Task
                </button>
            </Link>
        </div>)
}

