import { Form, Input, Button, Select, message, Upload, DatePicker, Space  } from "antd";
import * as React from "react";
import { useParams} from "react-router";
import { LoadingOutlined, PlusOutlined,UploadOutlined } from '@ant-design/icons';
import moment from 'moment';


export const EntityForm = (onFinish: ((values: any) => void) | undefined,  
                         defaultValues = {title: "", description: "", deadline: ""},
                        ) => {
    const title = defaultValues['title']
    const description = defaultValues['description']
    const deadline = defaultValues['deadline']
    const { Option } = Select;
    const dateFormat = 'YYYY/MM/DD';

    let urlElements = window.location.href.split('/')
    let page = urlElements[3];
    let parentType = urlElements[4];
    let entitiType = urlElements[5];
    let parent_id = urlElements[6]; //it is entity_id if edit entity will used
    let entity_id= urlElements[3]
    console.log(urlElements)

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

    function handleChange(value:any) {
        console.log(`selected ${value}`);
      }
      const capitalize = (s: string) => {
        if (typeof s !== 'string') return ''
        return s.charAt(0).toUpperCase() + s.slice(1)
      }

    return (
        <Form
            name="basic"
            initialValues={{ remember: true }}
            onFinish={onFinish}
        >
            <h2>{capitalize(entitiType)} Title</h2>
            <Form.Item
                name="title"
                rules={[{ required: true, message: 'Please input Entity Title!' }]}
            >
                <Input placeholder="Entity Name" defaultValue={title}/>
            </Form.Item>
            <h2>{capitalize(entitiType)} Description</h2>
            <Form.Item
                name="description"
                rules={[{ required: true, message: 'Please input your Entity Description!' }]}
            >
                <Input placeholder="Entity Description" defaultValue={description}/>
            </Form.Item>
          { (entitiType == "question" || entitiType == "task") && 
          (<div>
            <h2>{capitalize(entitiType)} Deadline</h2>
            <Form.Item
                name="deadline"
                rules={[{ required: true, message: 'Please input your Entity Description!' }]}
            >
                <DatePicker defaultValue={moment()} format={dateFormat} />
            </Form.Item>
          </div>)}
            <Form.Item>
                <Button type="primary" htmlType="submit">
                    Submit
                </Button>
            </Form.Item>
            <h1>Resources: </h1>
            <Form.Item>
                <Upload {...Deneme}>
                    <Button icon={<UploadOutlined />}>Upload Resources</Button>
                </Upload>
            </Form.Item>  
        </Form>
    );
};
