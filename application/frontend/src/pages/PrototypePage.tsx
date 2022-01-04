import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Table} from "antd";
import {GoalTypes} from "../helpers/GoalTypes";

const token = localStorage.getItem("token");
const user_id = localStorage.getItem("user_id")


export function PrototypePage(params :{goalType: any}) {

    const goalType = params.goalType
    console.log("goalType", goalType)

    const [goal, setGoal] = useState({
        title: "Loading",
        description: "Loading",
        download_count: 0,
        entities: [],
        subgoals: [],
        user_id: -1
    })
    const [tagData, setTagData] =useState([""]);

    // @ts-ignore
    const {goal_id} = useParams();

    const subgoal_columns =  [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
            render: (text: any,
                     goal: any) =>
                <Link to={`/prototypes/${GoalTypes.Sub}/${goal.id}`}> {text} </Link>
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
                <Link to={"/prototypes/entiti/" + entity.id}> {text} </Link>
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

        }
    ];

    useEffect(() => {
        let subgoal_str = ""
        if (goalType === GoalTypes.Sub) {
            subgoal_str = "/subgoal"
        }
        axios.get(`/prototypes${subgoal_str}/${goal_id}`,
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
                    goal_info['subgoals'] = goal_info['child_subgoals']
                } else {
                    goal_info['entities'].forEach((entity: any, i: number) => {
                        entity.key = i
                    })
                }
                console.log("goal", goal)
                console.log("received", goal_info)
                setGoal(goal_info)
                if(goal_info.tags !== undefined && goal_info.tags !== null){
                    setTagData(goal_info.tags)
                }
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }, [goal_id]);

    const copyPrototype = () => {
        axios.post(`/goals/copy_prototype/${user_id}/${goal_id}`, {}, {
            headers: { Authorization: `Bearer ${token}`},
        }).then(() => window.alert("I Hope copied!"))
    }

    return (
        <div>
            <h2>Name: {goal['title']}</h2>
            <h2>Description: {goal['description']}</h2>
            {goalType === GoalTypes.Normal &&
                <h2>Download count: {goal['download_count']}</h2>
            }
            {goalType === GoalTypes.Normal &&
            <Button
                type="primary"
                onClick={copyPrototype}
            >
                Copy Prototype!
            </Button>
            }
            <Table columns={subgoal_columns} dataSource={goal['subgoals']} />
            {goalType === GoalTypes.Normal &&
                <Table columns={columns} dataSource={goal.entities} />
            }
            {goalType === GoalTypes.Normal &&
                <div className="tag-input">
                    <ul className="tags">
                        {tagData.map((tag, index) => (
                            <li key={index} className="tag">
                                <Button type="dashed" > {tag} </Button>
                            </li>
                        ))}
                    </ul>
                </div>
            }
        </div>)




}



