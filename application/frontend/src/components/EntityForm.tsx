import { Form, Input, Button, Select ,message,Upload} from "antd";
import * as React from "react";
import { useParams } from "react-router";
import {UploadOutlined } from '@ant-design/icons';

export const EntityForm = (onFinish: ((values: any) => void) | undefined,  
                         defaultValues = {title: "", description: "", deadline: ""}, isEdit = false
                        ) => {
    const title = defaultValues['title']
    const description = defaultValues['description']
    const deadline = defaultValues['deadline']
    const { Option } = Select;


    const Deneme = {
        name: 'file',
        action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
        headers: {
          authorization: 'authorization-text',
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

    return (
        <Form
            name="basic"
            initialValues={{ remember: true }}
            onFinish={onFinish}
        >
            <h2>Entity Title</h2>
            <Form.Item
                name="title"
                rules={[{ required: true, message: 'Please input Entity Title!' }]}
            >
                <Input placeholder="Entity Name" defaultValue={title}/>
            </Form.Item>
            <h2>Entity Description</h2>
            <Form.Item
                name="description"
                rules={[{ required: true, message: 'Please input your Entity Description!' }]}
            >
                <Input placeholder="Entity Description" defaultValue={description}/>
            </Form.Item>
            {!isEdit && 
            (<div>
            <h2>Entity Type</h2>
            <Form.Item name="entityType">
                <Select defaultValue="subgoal" style={{ width: 120 }} onChange={handleChange}>
                <Option value="subgoal">Sub-Goal</Option>
                <Option value="question">Question</Option>
                <Option value="reflection">Reflection</Option>
                <Option value="routine">Routine</Option>
                <Option value="task">Task</Option>
                 </Select> 
            </Form.Item>
            </div>)
            }
            <Form.Item>
                <Upload {...Deneme}>
                    <Button icon={<UploadOutlined />}>Click to Upload</Button>
                </Upload>
            </Form.Item>

            <Form.Item>
                <Button type="primary" htmlType="submit">
                    Submit
                </Button>
            </Form.Item>
        </Form>
    );
};
