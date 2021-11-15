import { Form, Input, Button, Select } from "antd";
import * as React from "react";
import { useParams } from "react-router";

export const EntityForm = (onFinish: ((values: any) => void) | undefined,
                         defaultValues = {title: "", description: "", deadline: ""}) => {
    const title = defaultValues['title']
    const description = defaultValues['description']
    const deadline = defaultValues['deadline']
    const { Option } = Select;



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
            <Form.Item>
                <Button type="primary" htmlType="submit">
                    Submit
                </Button>
            </Form.Item>
        </Form>
    );
};
