import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Form ,Input, Space, Table, Tag,Select,List} from "antd";
import {GoalTypes} from "../helpers/GoalTypes";

const token = localStorage.getItem("token");
const user_id = localStorage.getItem("user_id")
const { Option } = Select;


export function GoalPage(params :{goalType: any}) {

    const goalType = params.goalType
    console.log("goalType", goalType)

    const [goal, setGoal] = useState({
        title: "Loading",
        description: "Loading",
        token: "Loading",
        isDone: false,
        assignees: [],
        members: [],
        entities: [],
        subgoals: [],
        user_id: -1
    })
    const [entities, setEntities] = useState([])
    const [subgoals, setSubgoals] = useState([])
    const [isLoaded, setLoaded] = useState(false)
    const[delete_count,setDeleteCount] =useState(0)
    const [returnLink, setReturnLink] = useState("/goalsPage")
    const [isDeleted, setDeleted] = useState(false)
    const [assignables, setAssignabels] = useState([])
    const [toAssignee, setToAssignee] = useState([])
    // @ts-ignore
    const {goal_id} = useParams();

    let editLink = ""
    if (goalType === GoalTypes.Sub) {
        editLink = "/editSubgoal/"
    } else if (goalType === GoalTypes.Normal) {
        editLink = "/editGoal/"
    } else if (goalType === GoalTypes.Group) {
        editLink = "/editGroupgoal/"
    }
    editLink += goal_id

    let delete_count_goal=0;
    const deleteEntity = (entity: { key: any, entitiType: string,id:number}) => {
        console.log('Received values of delete: ', goal);
        axios.delete(`/entities/${entity.entitiType.toLowerCase()}/${entity.id}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then(() => setDeleteCount(delete_count+1))
    };

    const deleteGoal = () => {
        axios.delete(`/${goalType}/${goal_id}`,
        {
            headers: { Authorization: `Bearer ${token}`},
            data: {}
        }).then((res) => {
        if (res && res.status === 200) {
            console.log(res.data)
            window.alert("Goal deleted successfully");
            setDeleted(true)
        }
    })
        .catch((error) => {
            window.alert(`A problem occurred while trying to delete goal! \n ${error}`);
        });
};

    const { Option } = Select;
    const rateAndComplete = (values: any) => {
        const rating = values.rating
        console.log(values)
        axios.put(`/${goalType}/complete/${goal_id}/${rating}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then((res) => {
                if (res && res.status === 200) {
                    console.log(res.data)
                    window.alert("Subgoal rated and completed successfully");
                    window.location.reload()
                }
            })
                .catch((error) => {
                    window.alert(`A problem occurred while trying to rate subgoal! \n ${error}`);
                });
        };

        const complete = () => {
            axios.put(`/${goalType}/complete/${goal_id}/`,
                {
                    headers: { Authorization: `Bearer ${token}`},
                    data: {}
                }).then((res) => {
                if (res && res.status === 200) {
                    console.log(res.data)
                    window.alert("Goal completed successfully");
                    window.location.reload()
                }
            })
                .catch((error) => {
                    window.alert(`A problem occurred while trying to complete goal! \n ${error}`);
                });
        };

        const handleAssigneeChange = (value: any) => {
            console.log('value', value)
            setToAssignee(value)
        }
        const assign = () => {
            const user_ids = toAssignee.map((user_id: any) => "user_ids=" + user_id)
            axios.post(`/subgoals/${goal_id}/assignees?${user_ids}`, {}, {
                headers: { Authorization: `Bearer ${token}`},
            }).then(() => window.alert("I Hope added!"))
    
        }
    

    

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
            goal_info['entities'].forEach((entity: any, i: number) => {
                entity.key = i
                if (entity.deadline !== null) {
                    entity.deadline = entity.deadline.substr(0,10)
                }
            })
            if (goal_info.main_goal_id != null) {
                setReturnLink(`/${GoalTypes.Normal}/${goal_info.main_goal_id}`)
            } else if (goal_info.main_groupgoal_id != null) {
                setReturnLink(`/${GoalTypes.Group}/${goal_info.main_groupgoal_id}`)
            } else if (goal_info.parent_subgoal_id != null) {
                setReturnLink(`/${GoalTypes.Sub}/${goal_info.parent_subgoal_id}`)
            }
            console.log("goal", goal)
            console.log("received", goal_info)
            setGoal(goal_info)

            if (goal_info.main_groupgoal_id != null) {
                axios.get(`/${GoalTypes.Group}/${goal_info.main_groupgoal_id}`,
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
                    }).then(parent_goal => {
                        setAssignabels(parent_goal['members'])
                }).catch(error => {
                    console.error('There was an error!', error);
                })
            } else if (goal_info.parent_subgoal_id != null) {
                axios.get(`/${GoalTypes.Sub}/${goal_info.parent_subgoal_id}`,
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
                    }).then(parent_goal => {
                    setAssignabels(parent_goal['assignees'])
                }).catch(error => {
                    console.error('There was an error!', error);
                })
            }
        })
        .catch(error => {
            console.error('There was an error!', error);
        });
}, [goal_id]);

const showManageDiv = goalType !== GoalTypes.Group || goal.user_id === Number(user_id)
    const showAssignees = goal['assignees'] !== undefined && goal['assignees'].length > 0
    let addSubgoalLink = ""
    if (goalType === GoalTypes.Sub) {
        addSubgoalLink = "/addSubToSub/"
    } else if (goalType === GoalTypes.Normal) {
        addSubgoalLink = "/addSubToNormal/"
    } else if (goalType === GoalTypes.Group) {
        addSubgoalLink = "/addSubToGroup/"
    }
    addSubgoalLink += goal_id

    if (isDeleted) {
        return <Link to={returnLink}>
            <button type="button">
                Go Back
            </button>
        </Link>
    }

    console.log(assignables)
    return (
        <div>
            {showManageDiv &&
            <div>
                <Link to={editLink}>
                    <button type="button">
                        Edit
                    </button>
                </Link>
                <Button
                    type="primary"
                    onClick={deleteGoal}
                >
                    Delete
                </Button>
                {assignables.length > 0 &&
                <div>
                    <Select
                        mode="multiple"
                        style={{ width: '100%' }}
                        placeholder="Please select"
                        defaultValue={[]}
                        onChange={handleAssigneeChange}
                    >
                        {assignables.map((member: any) => <Option key={member.username} value={member.user_id}>{member.username}</Option>)}
                    </Select>
                    <Button
                        type="primary"
                        onClick={assign}
                    >
                        Assign
                    </Button>
                </div>}
                {(!goal['isDone'] && goalType === GoalTypes.Sub) &&
                <Form onFinish={rateAndComplete}>
                    <Form.Item name="rating" label="Rating" rules={[{ required: true }]}>
                        <Select
                            placeholder="Select a option and change input text above"
                        >
                            <Option value="1" key={1}>&#9734;</Option>
                            <Option value="2" key={2}>&#9734;&#9734;</Option>
                            <Option value="3" key={3}>&#9734;&#9734;&#9734;</Option>
                            <Option value="4" key={4}>&#9734;&#9734;&#9734;&#9734;</Option>
                            <Option value="5" key={5}>&#9734;&#9734;&#9734;&#9734;&#9734;</Option>
                        </Select>
                    </Form.Item>
                    <Form.Item >
                        <Button type="primary" htmlType="submit">
                            Rate and Complete Subgoal!
                        </Button>
                    </Form.Item>
                </Form>}
                {(!goal['isDone'] && goalType === GoalTypes.Normal) &&
                <Button
                    type="primary"
                    onClick={complete}
                >
                    Complete Goal!
                </Button>}
            </div>}

            {goal['isDone'] &&
            <h2>DONE!</h2>}

            <h2>Name: {goal['title']}</h2>
            <h2>Description: {goal['description']}</h2>
            {goalType === GoalTypes.Group &&
            <div>
                <h2>Token: {goal['token']}</h2>
                <List
                    size="small"
                    header={<div>Members</div>}
                    bordered
                    dataSource={goal['members'].map((member: any) => member.username)}
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
            <Link to={addSubgoalLink}>
                <button type="button">
                    Add SubGoal
                </button>
            </Link>
            <Table columns={columns} dataSource={goal.entities} />
            <Link to={"/addEntity/"+goalType.slice(0, -1) +"/question/" + goal_id}>
                <button type="button">
                    Add Question
                </button>
            </Link>
            <Link to={"/addEntity/"+ goalType.slice(0, -1)+ "/reflection/" + goal_id}>
                <button type="button">
                    Add Reflection
                </button>
            </Link>
            <Link to={"/addEntity/"+ goalType.slice(0, -1)+"/routine/" + goal_id}>
                <button type="button">
                    Add Routine
                </button>
            </Link>
            <Link to={"/addEntity/"+ goalType.slice(0, -1)+ " /task/" + goal_id}>
                <button type="button">
                    Add Task
                </button>
            </Link>
        </div>)
}



