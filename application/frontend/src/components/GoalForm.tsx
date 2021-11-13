import { Form, Input, Button } from "antd";
import * as React from "react";

export const GoalForm = (onFinish: ((values: any) => void) | undefined) => {

    return (
        <Form
            name="basic"
            initialValues={{ remember: true }}
            onFinish={onFinish}
        >
            <Form.Item
                name="name"
                rules={[{ required: true, message: 'Please input Goal Name!' }]}
            >
                <Input placeholder="Goal Name" />
            </Form.Item>
            <Form.Item
                name="description"
                rules={[{ required: true, message: 'Please input your Goal Description!' }]}
            >
                <Input
                    placeholder="Goal Description"
                />
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">
                    Submit Goal
                </Button>
            </Form.Item>
        </Form>
    );
};
