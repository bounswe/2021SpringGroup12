import { Form, Input, Button,Upload, message  } from "antd";
import * as React from "react";
import { LoadingOutlined, PlusOutlined,UploadOutlined } from '@ant-design/icons';


export const GoalForm = (onFinish: ((values: any) => void) | undefined,
                         defaultValues = {title: "", description: "", deadline: ""}) => {
    const title = defaultValues['title']
    const description = defaultValues['description']
    const deadline = defaultValues['deadline']

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

    return (
        <Form
            name="basic"
            initialValues={{ remember: true }}
            onFinish={onFinish}
        >
            <h2>Title</h2>
            <Form.Item
                name="title"
                rules={[{ required: true, message: 'Please input Goal Title!' }]}
            >
                <Input placeholder="Goal Name" defaultValue={title}/>
            </Form.Item>
            <h2>Description</h2>
            <Form.Item
                name="description"
                rules={[{ required: true, message: 'Please input your Goal Description!' }]}
            >
                <Input placeholder="Goal Description" defaultValue={description}/>
            </Form.Item>
            {/*<h2>Deadline</h2>
            <Form.Item
                name="deadline"
            >
                <Input placeholder="Goal Deadline" defaultValue={deadline}/>
            </Form.Item>*/}
            <Form.Item>
                <Button type="primary" htmlType="submit">
                    Submit
                </Button>
            </Form.Item>
            <Form.Item>
                <Upload {...Deneme}>
                    <Button icon={<UploadOutlined />}>Click to Upload</Button>
                </Upload>
            </Form.Item>
        </Form>
    );
};


