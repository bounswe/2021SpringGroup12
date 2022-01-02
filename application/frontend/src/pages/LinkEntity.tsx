import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Table,Space,Button} from "antd";
import { url } from "inspector";

const token = localStorage.getItem("token")

export function LinkEntity() {
    const [possible_entities, setEntities] = useState([])
    const[possible_sugoals ,setPossibleSubGoals]=useState([])
    const[goal_type,setGoalType]=useState("")
    const[goal_id,setGoalId]=useState(null)
    const[groupgoal_id,setGroupGoalId]=useState(null)
  


    const linkEntities = (target_entity:any) => {
        let urlElements = window.location.href.split('/')
        let page = urlElements[3];
        let entitiType=urlElements[4];
        let entity_id =urlElements[5];

        console.log("target:" +target_entity.entitiType)
        console.log(urlElements)
        console.log("entity id: " + entity_id)
        console.log('Received values of delete: ', typeof(JSON.stringify(target_entity)));
        var values = {childId: "", childType: ""}
        values.childId=target_entity.id
        values.childType=target_entity.entitiType
        console.log("values: " + JSON.stringify(values))
        axios.post(`/entities/${entity_id}/link`,values,
            {
                headers: { Authorization: `Bearer ${token}`},
            })
    };

    const user_id=localStorage.getItem("user_id")
    const columns = [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
            render: (text: any,
                     entity: {key: number}) =>
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
                     entity: { key:any, entitiType:any}) =>
                (   <div>
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
        let entitiType=urlElements[4];
        let entity_id =urlElements[5];
        console.log(urlElements)
        console.log("entity id: " + entity_id)
    
        axios.get(`/entities/${entitiType}/${entity_id}`,
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
            .then(data => {
                if(data.goal_id!==null){
                    setGoalType("goal")
                    setGoalId(data.goal_id)
                    console.log(data.goal_id)
                    console.log(data.goal_id)
                }
                else{
                    setGoalType("groupgoal")
                    setGroupGoalId(data.groupgoal_id)
                }
                console.log(goal_type)
            })
            .catch(error => {
                console.error('There was an error!', error);
            });


        console.log(urlElements)
        console.log("entity id: " + entity_id)
        //get all possible entities


        goal_type!=="" && axios.get(`/entities/${goal_type}/${goal_id}`,
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
            .then(data => {
                
                let tmp = []
                console.log(data);
                for (let i = 0; i < data.length; i++) {
                    if(data[i].id!=entity_id){
                    tmp.push({
                        id: data[i]['id'],
                        title: data[i]['title'],
                        description: data[i]['description'],
                        entityType: data[i]['entitiType'],
                    })
                }
                }
                // @ts-ignore
                setEntities(tmp)

            })
            .catch(error => {
                console.error('There was an error!', error);
            });
            //get all possible subgoals
            axios.get(`/subgoals/of_user/${user_id}`,
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
            .then(data => {
                
                let tmp = []
                console.log(data);
                for (let i = 0; i < data.length; i++) {
                    if(data[i].id!=entity_id){
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

    }, [goal_type,goal_id]);


    let message;
    if (isSubmitted) {
        message = <h2>Entity Added Successfully!</h2>
    }

    return (

        <div>
            <h1>Link to:</h1>
            <Table columns={columns} dataSource={possible_entities.concat(possible_sugoals)} />

            {message}
        </div>

    );
    
}

