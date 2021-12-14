import { Form, Input, Button } from "antd";
import * as React from "react";

export const GoalForm = (onFinish: ((values: any) => void) | undefined,
                         defaultValues = {title: "", description: "", deadline: ""}) => {
    const title = defaultValues['title']
    const description = defaultValues['description']
    const deadline = defaultValues['deadline']
    console.log('deadline before:', deadline)
    return (
        <Form
            name="basic"
            initialValues={{ remember: true }}
            onFinish={onFinish}
        >
            <h2>Title</h2>
            <Form.Item
                name="title"
            >
                <Input placeholder="Goal Name" defaultValue={title}/>
            </Form.Item>
            <h2>Description</h2>
            <Form.Item
                name="description"
            >
                <Input placeholder="Goal Description" defaultValue={description}/>
            </Form.Item>
            <Form.Item
                name="deadline"
            >
                <Input type="date" placeholder="Goal Deadline" defaultValue={deadline}/>
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">
                    Submit
                </Button>
            </Form.Item>
        </Form>
    );
};