import { Form, Input, Button, Checkbox, Modal } from "antd";
import axios from "axios";
import { useState } from "react";
import { useHistory } from "react-router";
import { PrivacyPolicy } from "./PrivacyPolicy";
import { TermsAndConditions } from "./TermsAndConditions";

export type IregisterForm = {
  email: string;
  password: string;
  username: string;
};

const formItemLayout = {
  labelCol: {
    xs: {
      span: 24,
    },
    sm: {
      span: 8,
    },
  },
  wrapperCol: {
    xs: {
      span: 24,
    },
    sm: {
      span: 16,
    },
  },
};
const tailFormItemLayout = {
  wrapperCol: {
    xs: {
      span: 24,
      offset: 0,
    },
    sm: {
      span: 16,
      offset: 8,
    },
  },
};

const RegistrationForm = () => {
  const [form] = Form.useForm();
  const history = useHistory();
  const[visible,setVisible] = useState(false)
  const[privacyVisible, setPrivacyVisible] = useState(false)

  const onFinish = (values: IregisterForm) => {
    axios
      .post("/signup", {
        email: values.email,
        password: values.password,
        username: values.username,
      })
      .then((res) => {
        if (res.data.messageType === "SUCCESS") {
          window.alert(res.data.message);
          history.push("/login");
          window.location.reload();
        }else{
          window.alert(res.data.message);
        }
      })
      .catch((err) => {
        window.alert(`Registration failed with error Try Again ! \n${err}`)
      });
  };

  return (
    <>
      <Form
        {...formItemLayout}
        form={form}
        name="register"
        onFinish={onFinish}
        initialValues={{
          residence: ["zhejiang", "hangzhou", "xihu"],
          prefix: "86",
        }}
        scrollToFirstError
      >
        <Form.Item
          name="username"
          label="Username"
          tooltip="What do you want others to call you?"
          rules={[
            {
              required: true,
              message: "Please input your username!",
              whitespace: true,
            },
          ]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="email"
          label="E-mail"
          rules={[
            {
              type: "email",
              message: "The input is not valid E-mail!",
            },
            {
              required: true,
              message: "Please input your E-mail!",
            },
          ]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          name="password"
          label="Password"
          rules={[
            {
              required: true,
              message: "Please input your password!",
            },
          ]}
          hasFeedback
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          name="confirm"
          label="Confirm Password"
          dependencies={["password"]}
          hasFeedback
          rules={[
            {
              required: true,
              message: "Please confirm your password!",
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("password") === value) {
                  return Promise.resolve();
                }

                return Promise.reject(
                  new Error("The two passwords that you entered do not match!")
                );
              },
            }),
          ]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item
          name="agreement"
          valuePropName="checked"
          rules={[
            {
              validator: (_, value) =>
                value
                  ? Promise.resolve()
                  : Promise.reject(new Error("Should accept agreement")),
            },
          ]}
          {...tailFormItemLayout}
        >
          <Checkbox>
            I have read the <a onClick={()=> setVisible(true)}>Terms and Conditions</a>
          </Checkbox>
        </Form.Item>

        <Form.Item {...tailFormItemLayout}>
          <Button type="primary" htmlType="submit">
            Register
          </Button>
        </Form.Item>
        <Form.Item {...tailFormItemLayout}>
          <Button type="link" href="/login">
            Already have an account ?
          </Button>
          <Button type="link" onClick={()=> setPrivacyVisible(true)}>
           Check our privacy policy
          </Button>
        </Form.Item>
        
      </Form>

      <Modal
        
        centered
        visible={visible}
        onOk={() => setVisible(false)}
        onCancel={() => setVisible(false)}
        width={1000}
      >
        <TermsAndConditions/>
      </Modal>

      <Modal
        
        centered
        visible={privacyVisible}
        onOk={() => setPrivacyVisible(false)}
        onCancel={() => setPrivacyVisible(false)}
        width={1000}
      >
        <PrivacyPolicy/>
      </Modal>
    </>
  );
};

export default RegistrationForm;
