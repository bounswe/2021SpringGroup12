import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Table} from "antd";

const token = localStorage.getItem("token")

export function PrototypeEntityPage() {
    const [entity, setEntity] = useState({
        title: "Loading",
        description: "Loading",
        entitiType: "Loading",
        child_entities: []
    })
    // @ts-ignore
    const {entitiType,entity_id} = useParams();

    const columns = [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
            render: (text: any,
                     entity: {key: number}) =>
                <Link to={"/prototypes/entiti/" + entity.key}> {text} </Link>
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
            title: 'Rating',
            dataIndex: 'rating',
            key: 'rating',
        }
    ];

    useEffect(() => {
        axios.get(`/prototypes/entiti/${entity_id}`,
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
                data.child_entities.forEach((child: any) => {
                    child.key = child.id
                })
                setEntity(data)

            })
            .catch(error => {
                console.error('There was an error!', error);
            });

    }, []);
    return (
        <div>
            <h2>{entitiType}</h2>
            <h2>Name: {entity['title']}</h2>
            <h2>Description: {entity['description']}</h2>
            <h2>Type: {entity['entitiType']}</h2>
            <h2>Linked Entities:</h2>
            <Table columns={columns} dataSource={entity.child_entities} />
        </div>)
}

