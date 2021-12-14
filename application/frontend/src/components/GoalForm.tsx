import { Form, Input, Button,Upload, message  } from "antd";
import * as React from "react";



export const GoalForm = (onFinish: ((values: any) => void) | undefined,
                         defaultValues = {title: "", description: "", deadline: ""}) => {
    const title = defaultValues['title']
    const description = defaultValues['description']
    const deadline = defaultValues['deadline']


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
            
        </Form>
    );
};


