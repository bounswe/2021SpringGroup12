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
    let entitiType = urlElements[5].toLowerCase();
    let parent_id = urlElements[6]; //it is entity_id if edit entity will used
    let entity_id= urlElements[7]

    console.log(urlElements)

    
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
            <h2>{capitalize(entitiType.toLowerCase())} Title</h2>
            <Form.Item
                name="title"
                rules={[{ required: true, message: 'Please input Entity Title!' }]}
            >
                <Input placeholder="Entity Name" defaultValue={title}/>
            </Form.Item>
            <h2>{capitalize(entitiType.toLowerCase())} Description</h2>
            <Form.Item
                name="description"
                rules={[{ required: true, message: 'Please input your Entity Description!' }]}
            >
                <Input placeholder="Entity Description" defaultValue={description}/>
            </Form.Item>
          { (entitiType == "task" || entitiType == "routine") && 
          (<div>
            <h2>{capitalize(entitiType.toLowerCase())} Deadline</h2>
            <Form.Item
                name="deadline"
                rules={[{ required: true, message: 'Please input your Entity Description!' }]}
            >
                <DatePicker defaultValue={moment()} format={dateFormat} />
            </Form.Item>
          </div>)}
          { (entitiType == "routine") && 
          (<div>
            <h2>{capitalize(entitiType)} Period</h2>
            <Form.Item
                name="period"
                rules={[{ required: true, message: 'Please input your Period!' }]}
            >
             <Input placeholder="Period" defaultValue={description}/>
            </Form.Item>
          </div>)}
            <Form.Item>
                <Button type="primary" htmlType="submit">
                    Submit
                </Button>
            </Form.Item>
           
        </Form>
    );
};
