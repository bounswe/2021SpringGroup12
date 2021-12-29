import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Table,Space,Button} from "antd";

const token = localStorage.getItem("token")

export function LinkEntity() {
    const [possible_entities, setEntities] = useState([])


    const linkEntities = (target_entity:any) => {
        console.log('Received values of delete: ', target_entity);
        axios.post(`/entities/${entity_id}/link/${target_entity.id}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
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
                     entity: { key:any}) =>
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

    const {goal_id, entity_id} = useParams();



    useEffect(() => {
        //to get the user id
        console.log("user-id: "+user_id)
        axios.get(`/entities/groupgoal/${goal_id}`,
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
    }, []);


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
    
}

