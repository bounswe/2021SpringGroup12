import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Space, Table, Tag} from "antd";
import { LinkEntityForm } from "../components/LinkEntityForm";

const token = localStorage.getItem("token")

export function LinkEntity() {
    const [possible_entities, setEntities] = useState([])
    const [user_id, setUserID] = useState([])

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
    ];


    const [isSubmitted, setSubmitted] = useState(false)
    // @ts-ignore

    const {goal_id, entity_id} = useParams();

    const get_user_id = (goal_id: { goal_id:number}) => {
        axios.get(`/goals/${goal_id}`,
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
            setUserID(data.user_id)
            // @ts-ignore
            console.log("user-id: "+user_id)

        })
        .catch(error => {
            console.error('There was an error!', error);
        });

    };


    useEffect(() => {
        //to get the user id
        get_user_id(goal_id);
        console.log("user: "+user_id)
        axios.get(`/entities/user/${user_id}`,
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
                    tmp.push({
                        id: data[i]['id'],
                        title: data[i]['title'],
                        description: data[i]['description'],
                        entityType: data[i]['entitiType'],
                    })
                }
                // @ts-ignore
                setEntities(tmp)

            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }, []);

    console.log("possibke: "+ possible_entities)

    let message;
    if (isSubmitted) {
        message = <h2>Entity Added Successfully!</h2>
    }
    return (

        <div>
            <h1>Entitites to Link:</h1>
            <Table columns={columns} dataSource={possible_entities} />
            <Link to={"/entity/" + entity_id} >
                <button type="button">
                    Return to Entity
                </button>
            </Link>
            {message}
        </div>

    );
    
    if (isSubmitted) {
        return <h2>...</h2>
    }
}

