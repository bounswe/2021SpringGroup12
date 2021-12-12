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
    useEffect(() => {
        //to get the user id
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
            console.log(data.user_id)

        })
        .catch(error => {
            console.error('There was an error!', error);
        });

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
                let sublinks=data.sublinks
                for (let i = 0; i < sublinks.length; i++) {
                    tmp.push({
                        key: data[i]['id'],
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


    let message;
    if (isSubmitted) {
        message = <h2>Entity Added Successfully!</h2>
    }
    return (
        <div>
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

