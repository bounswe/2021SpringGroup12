import * as React from "react";
import { useParams } from "react-router";
import { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { Table, Space, Button } from "antd";
import { url } from "inspector";

const token = localStorage.getItem("token")

export function LinkEntity() {
    const [possible_entities, setEntities] = useState([])
    const [possible_sugoals, setPossibleSubGoals] = useState([])
    const [goal_type, setGoalType] = useState("")
    const [goal_id, setGoalId] = useState(null)
    const [groupgoal_id, setGroupGoalId] = useState(null)
    const [already_linked_entities, setAlreadyLinkedEntities] = useState([""])
    const [already_linked_subgoals, setAlreadyLinkedSubgoals] = useState([""])
    const [pressedLink, setPressedLink] = useState(1)


    let urlElements = window.location.href.split('/')
    let entity_id = urlElements[5];


    const linkEntities = (target_entity: any) => {
        let urlElements = window.location.href.split('/')
        let page = urlElements[3];
        let entitiType = urlElements[4];
        let entity_id = urlElements[5];

        console.log("target:" + target_entity.entityType)
        console.log(urlElements)
        console.log("entity id: " + entity_id)
        var values = { childId: "", childType: "ENTITI" }
        values.childId = target_entity.id
        if (target_entity.entityType.toString().toLowerCase() == "sub-goal") {
            values.childType = "SUBGOAL"
        }
        console.log("values: " + JSON.stringify(values))
        axios.post(`/entities/${entity_id}/link`, values,
            {
                headers: { Authorization: `Bearer ${token}` },
            })
        setPressedLink(pressedLink + 1)
        axios.get(`/entities/${entitiType}/${entity_id}`,
            {
                headers: { Authorization: `Bearer ${token}` },
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
                for (let i = 0; i < data.sublinked_entities.length; i++) {
                    tmp.push(data.sublinked_entities[i].id)
                }
                let tmp2 = []
                for (let i = 0; i < data.sublinked_entities.length; i++) {
                    tmp2.push(data.sublinked_entities[i].id)
                }

                setAlreadyLinkedEntities(tmp)
                setAlreadyLinkedSubgoals(tmp2)

                console.log(goal_type)
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    };

    const user_id = localStorage.getItem("user_id")
    const columns = [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
            render: (text: any,
                entity: { key: number }) =>
                <Link to={"/entity/" + entity.key}> {text} </Link>
            ,
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Entity Type',
            dataIndex: 'entityType',
            key: 'entityType',
        },
        {
            title: 'Link',
            key: 'id',
            render: (text: any,
                entity: { key: any, entityType: any }) =>
            (<div>
                <Space size="middle">
                    <Button type="primary" onClick={() => linkEntities(entity)}>
                        Link
                    </Button>
                </Space>
            </div>

            ),
        },
    ];


    const [isSubmitted, setSubmitted] = useState(false)
    // @ts-ignore

    useEffect(() => {
        //to get the user id
        let urlElements = window.location.href.split('/')
        let page = urlElements[3];
        let entitiType = urlElements[4];
        let entity_id = urlElements[5];
        console.log(urlElements)
        console.log("entity id: " + entity_id)

        axios.get(`/entities/${entitiType}/${entity_id}`,
            {
                headers: { Authorization: `Bearer ${token}` },
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
                if (data.goal_id !== null) {
                    setGoalType("goal")
                    setGoalId(data.goal_id)
                    console.log(data.goal_id)
                    console.log(data.goal_id)
                }
                else {
                    setGoalType("groupgoal")
                    setGroupGoalId(data.groupgoal_id)
                }
                let tmp = []
                for (let i = 0; i < data.sublinked_entities.length; i++) {
                    tmp.push(data.sublinked_entities[i].id)
                }
                let tmp2 = []
                for (let i = 0; i < data.sublinked_entities.length; i++) {
                    tmp2.push(data.sublinked_entities[i].id)
                }

                //setAlreadyLinkedEntities(tmp)
                console.log("tmp: " + tmp)
                console.log("")
                //setAlreadyLinkedSubgoals(tmp2)

                console.log(goal_type)
            })
            .catch(error => {
                console.error('There was an error!', error);
            });


        console.log(urlElements)
        console.log("entity id: " + entity_id)
        //get all possible entities


        if ((goal_type !== "" && goal_id !== null) || (goal_type !== "" && groupgoal_id !== null)) {
            axios.get(`/entities/${goal_type}/${goal_id}`,
                {
                    headers: { Authorization: `Bearer ${token}` },
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
                    console.log("already linked: " + already_linked_entities);
                    for (let i = 0; i < data.length; i++) {
                        console.log(data[i].id)
                        if (data[i].id != entity_id) {
                            if (!already_linked_entities.includes(data[i].id)) {
                                tmp.push({
                                    id: data[i]['id'],
                                    title: data[i]['title'],
                                    description: data[i]['description'],
                                    entityType: data[i]['entitiType'],
                                })
                            }
                        }
                    }
                    // @ts-ignore
                    setEntities(tmp)
                    console.log("possbile entities: "+JSON.stringify(possible_entities))
                })
                .catch(error => {
                    console.error('There was an error!', error);
                });
        }
        //get all possible subgoals
        axios.get(`/subgoals/of_user/${user_id}`,
            {
                headers: { Authorization: `Bearer ${token}` },
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
                console.log(data);
                for (let i = 0; i < data.length; i++) {
                    if (!already_linked_subgoals.includes(data.id)) {
                        tmp.push({
                            id: data[i]['id'],
                            title: data[i]['title'],
                            description: data[i]['description'],
                            entityType: "SUB-GOAL",
                        })
                    }
                }
                // @ts-ignore
                setPossibleSubGoals(tmp)
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
        console.log("pressedLink: " + pressedLink)

    }, [goal_type, goal_id, pressedLink,already_linked_entities]);


    let message;


    return (

        <div>
            <h1>Link to:</h1>
            <Table columns={columns} dataSource={possible_entities.concat(possible_sugoals)} />
            <h1>Create Entity under this Entity:</h1>
            <Link to={"/addEntity/" + goal_type + "/question/" + goal_id + "/ENTITI/" + entity_id}>
                <button type="button">
                    Add Question
                </button>
            </Link>
            <Link to={"/addEntity/" + goal_type + "/reflection/" + goal_id + "/ENTITI/" + entity_id}>
                <button type="button">
                    Add Reflection
                </button>
            </Link>
            <Link to={"/addEntity/" + goal_type + "/routine/" + goal_id + "/ENTITI/" + entity_id}>
                <button type="button">
                    Add Routine
                </button>
            </Link>
            <Link to={"/addEntity/" + goal_type + " /task/" + goal_id + "/ENTITI/" + entity_id}>
                <button type="button">
                    Add Task
                </button>
            </Link>
            {message}
        </div>

    );

}

