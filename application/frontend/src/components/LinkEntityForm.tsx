import { Form, Input, Button, Select } from "antd";
import * as React from "react";
import { useParams } from "react-router";

export const LinkEntityForm = (onFinish: ((values: any) => void) | undefined,
                               defaultValues = {title: "", target_id: ""},
                               target_entities = []) => {
    const title = defaultValues['title']
    const target_id = defaultValues['target_id']
    const { Option } = Select;
    let target_list = <h2>There is not any other entities</h2>

    if (target_entities.length > 0) {
       target_list = (<Form.Item name="entityType">
                            <Select  style={{ width: 120 }} onChange={handleChange}>
                                {target_entities.map(entity => <Option value={entity['id']}>{entity['title']}</Option>)}
                            </Select>
                        </Form.Item>
       )
    }

    function handleChange(value:any) {
        console.log(`selected ${value}`);
    }


    return (
        <Form
            name="basic"
            initialValues={{ remember: true }}
            onFinish={onFinish}
        >
            <h2>Target Entity Name</h2>
            <Form.Item
                name="title"
                rules={[{ required: true, message: 'Please input Entity Title!' }]}
            >
                <Input placeholder="Entity Name" defaultValue={title}/>
            </Form.Item>
            <h2>Entity Id</h2>
            {target_list}
            <br/>
            <br/>
            <Button type="primary" htmlType="submit">
                Submit
            </Button>
        </Form>
    );
};