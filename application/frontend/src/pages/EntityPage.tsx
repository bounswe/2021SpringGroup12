import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Space, Table, Tag,message,Form,Upload} from "antd";
import {UploadOutlined } from '@ant-design/icons';

const token = localStorage.getItem("token")

export function EntityPage() {
    const [entity, setEntity] = useState({
        title: "Loading",
        description: "Loading"
    })
    const [entities, setEntities] = useState([])
    const [deadline, setDeadline] = useState("")
    const [isLoaded, setLoaded] = useState(false)
    // @ts-ignore
    const {entitiType,entity_id} = useParams();
    const [resources, setResources] = useState([]);
    const [resource_count, setResourceCount]=useState(0);

    const Deneme = {
        name: 'resource',
        action: 'http://3.144.201.198:8085/v2/resources/' + entity_id,
        headers: {
          authorization: 'authorization',
        },
        onChange(info: any) {
          if (info.file.status !== 'uploading') {
            console.log(info.file, info.fileList);
          }
          if (info.file.status === 'done') {
            message.success(`${info.file.name} file uploaded successfully`);
            setResourceCount(resource_count+1);
          } else if (info.file.status === 'error') {
            message.error(`${info.file.name} file upload failed.`);
          }
        },
      };

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
            title: 'Rating',
            dataIndex: 'rating',
            key: 'rating',
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

    const Resourcecolumns = [
        {
            title: 'Title',
            dataIndex: 'name',
            key: 'name',
            render: (text: any,
                     resource: any) =>
                <Link to={"/resources/" + resource.id}> {text} </Link>
            ,
        },
        {
            title: 'file type',
            dataIndex: 'contentType',
            key: 'contentType',
        },
        {
            title: 'Created At',
            dataIndex: 'createdAt',
            key: 'createdAt',
        },
        {
            title: 'Delete',
            key: 'resource_id',
            render: (text: any,
                     resource: { key: string,id:number}) =>
                (   <div>
                        <Space size="middle">
                            <Button type="primary" onClick={() => deleteResource(resource)}>
                                Delete Resource
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

    const deleteResource = (resource: { key: any,id:number}) => {
        console.log('Received values of delete: ', resource.id);
        axios.delete(`/resources/${resource.id}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then(() => getEntities())
    };


    const [goal_id,setGoalID]=useState()

    const getEntities = () => {
        console.log(axios.defaults.baseURL)
        axios.get(`/entities/${entitiType.toLowerCase()}/${entity_id}`,
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
                let sublinked_entities=data.sublinked_entities
                console.log("data:" + JSON.stringify(data))
                if(entitiType.toLowerCase() =="task" || entitiType.toLowerCase() =="routine"  ){
                    setDeadline(data.deadline)
                    console.log(data.deadline)
                }
                
                for (let i = 0; i < sublinked_entities.length; i++) {
                    console.log(sublinked_entities)
                    tmp.push({
                        key: sublinked_entities[i]['id'],
                        title: sublinked_entities[i]['title'],
                        description: sublinked_entities[i]['description'],
                        entityType: sublinked_entities[i]['entitiType'],
                        //isDone: sublinks[i]['isDone'],
                        //period: sublinks[i]['period'],
                        //rating: sublinks[i]['rating'],
                        //deadline: sublinks[i]['deadline']
                    })
                }
                // @ts-ignore
                setEntities(tmp)
                setLoaded(true)
                setResources(data.resources)
                setGoalID(data.goal_id)
                console.log(resources)
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    useEffect(() => {
        axios.get(`/entities/${entitiType.toLowerCase()}/${entity_id}`,  // we need goal id from params
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
            <h2>{entitiType}</h2>
            <h2>Name: {entity['title']}</h2>
            <h2>Description: {entity['description']}</h2>
            {(entitiType.toLowerCase() == "routine" || entitiType.toLowerCase() == "task") && 
            <h2>Deadline: {deadline.slice(0,10)}</h2>
                        }
            <h2>Linked Entities:</h2>
            <Table columns={columns} dataSource={entities} />
            <br></br>
            <Link to={"/linkEntityfrom/" +goal_id+ "/"+ entity_id}> 
                <button type="button">
                    Link Entity
                </button>
            </Link>
            <br></br>
            <h2>Resources:</h2>
            <Table columns={Resourcecolumns} dataSource={resources} />
            <Form.Item>
                <Upload {...Deneme}>
                    <Button icon={<UploadOutlined />}>Upload Resources (max 3mb)</Button>
                </Upload>
            </Form.Item>  

        </div>)
}

