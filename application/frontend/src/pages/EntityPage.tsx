import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Space, Table, Tag,message,Form,Upload} from "antd";
import { LoadingOutlined, PlusOutlined,UploadOutlined } from '@ant-design/icons';

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
                let sublinks=data.sublinks
                console.log("data:" + JSON.stringify(data))
                if(entitiType.toLowerCase() =="task"){
                    setDeadline(data.deadline)
                    console.log(data.deadline)
                }
                for (let i = 0; i < sublinks.length; i++) {
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
            <h2>Name: {entity['title']}</h2>
            <h2>Description: {entity['description']}</h2>
            {(entitiType.toLowerCase() == "routine" || entitiType.toLowerCase() == "task") && 
            <h2>Deadline: {deadline}</h2>
                        }
            <h2>Linked Entities</h2>
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

