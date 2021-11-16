import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Space, Table, Tag} from "antd";

const token = localStorage.getItem("token")

export function EntityPage() {
    const [entity, setEntity] = useState({
        title: "Loading",
        description: "Loading"
    })
    const [entities, setEntities] = useState([])
    const [isLoaded, setLoaded] = useState(false)
    // @ts-ignore
    const {entity_id} = useParams();

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
            title: 'Period',
            dataIndex: 'period',
            key: 'period',
        },
        {
            title: 'Rating',
            dataIndex: 'rating',
            key: 'rating',
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
                     entity: { key: number, entityType: string}) =>
                (   <div>
                        <Space size="middle">
                            <Button type="primary" onClick={() => deleteLink(entity)}>
                                Delete Link
                            </Button>
                        </Space>
                    </div>

                ),
        },
    ];

    const deleteLink = (entity: { key: any}) => {
        console.log('Received values of delete: ', entity);
        axios.delete(`/entities/${entity_id}/delete_link/${entity.key}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then(() => getEntities())
    };


    const getEntities = () => {
        console.log(axios.defaults.baseURL)
        axios.get(`/entities/${entity_id}/sublinks`,
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
                for (let i = 0; i < data.length; i++) {
                    tmp.push({
                        key: data[i]['id'],
                        title: data[i]['title'],
                        description: data[i]['description'],
                        entityType: data[i]['entitiType'],
                        isDone: data[i]['isDone'],
                        period: data[i]['period'],
                        rating: data[i]['rating'],
                        deadline: data[i]['deadline']
                    })
                }
                // @ts-ignore
                setEntities(tmp)
                setLoaded(true)
                console.log('Success!')
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    useEffect(() => {
        axios.get(`/entities/entiti/${entity_id}`,  // we need goal id from params
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
            .then(entity => {
                setEntity(entity)
                getEntities()
                console.log('Burada!')

            })
            .catch(error => {
                console.error('There was an error!', error);
            });

    }, []);
    if (!isLoaded) {
        return <h2>Loading...</h2>
    }
    return (
        <div>
            <h2>Name: {entity['title']}</h2>
            <h2>Description: {entity['description']}</h2>
            <h2>Linked Entities</h2>
            <Table columns={columns} dataSource={entities} />
            <Link to={"/linkEntityfrom/" + entity_id}>
                <button type="button">
                    Link Entity
                </button>
            </Link>
        </div>)
}

