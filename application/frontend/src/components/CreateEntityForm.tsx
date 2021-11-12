import React, { useState } from 'react';
import { Form, Input, Button, Radio } from 'antd';
import { InfoCircleOutlined } from '@ant-design/icons';



export const CreateEntityForm = () => {
  const [form] = Form.useForm();


  return (
    <Form
      form={form}
      layout="vertical"
    >
      <Form.Item label="Entity Name" required tooltip="This is a required field" >
        <Input placeholder="input placeholder" />
      </Form.Item>
      <Form.Item
        name="entity_description"
        label="Entity Description"
        required tooltip="This is a required field"
        rules={[{ required: true, message: 'Please input an Entity Description' }]}
      >
        <Input.TextArea showCount maxLength={100} />
      </Form.Item>
      <Form.Item>
        <Button type="primary">Create</Button>
      </Form.Item>
    </Form>
  );
};

