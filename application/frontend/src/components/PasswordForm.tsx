import { Form, Input, Button, notification } from "antd";
import axios from "axios";
import { useState } from "react";
import { useHistory } from "react-router";

export const PasswordForm = () => {
  const history = useHistory();

  const [visible, setVisible] = useState(false);
  const [userEmail, setUserEmail] = useState("");
  const [userUserName, setUserUsername] = useState("");

  const openNotification = (
    type: "success" | "info" | "warning",
    message: string
  ) => {
    notification[type]({
      message,
      placement: "bottomRight",
    });
  };

  const onFinish = (values: { username: string; email: string }) => {
    setUserEmail(values.email);
    setUserUsername(values.username);
    axios
      .get(`users/forgot/${values.username}`)
      .then((response) => {
        // check for error response
        if (response.status === 200) {
          setVisible(true);
          openNotification(
            "success",
            "Reset mail is sent to your mail address!"
          );
        }
      })
      .catch((error) => {
        openNotification("warning", `Error: ${error}`);
        console.error("There was an error!", error);
      });
  };

  const onFinishToken = (values: { token: string; password: string }) => {
    axios
      .post(`users/reset/${values.token.trim()}`, {
        username: userUserName,
        email: userEmail,
        password: values.password,
      })
      .then((response) => {
        // check for error response
        if (response.status === 200) {
          openNotification("success", "Password is changed successfully!");
          history.push("/login");
        }
      })
      .catch((error) => {
        openNotification("warning", `Error: ${error}`);
        console.error("There was an error!", error);
      });
  };

  if (visible) {
    return (
      <Form
        name="basic"
        labelCol={{ span: 8 }}
        wrapperCol={{ span: 16 }}
        onFinish={onFinishToken}
        autoComplete="off"
      >
        <Form.Item
          label="Token"
          name="token"
          rules={[{ required: true, message: "Please input your token!" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          name="password"
          label="New Password"
          rules={[
            {
              required: true,
              message: "Please input your new password!",
            },
          ]}
          hasFeedback
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          name="confirm"
          label="Confirm New Password"
          dependencies={["password"]}
          hasFeedback
          rules={[
            {
              required: true,
              message: "Please confirm your new password!",
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

        <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
          <Button type="primary" htmlType="submit">
            Reset Password
          </Button>
        </Form.Item>
      </Form>
    );
  } else {
    return (
      <Form
        name="basic"
        labelCol={{ span: 8 }}
        wrapperCol={{ span: 16 }}
        initialValues={{ remember: true }}
        onFinish={onFinish}
        autoComplete="off"
      >
        <Form.Item
          label="Username"
          name="username"
          rules={[{ required: true, message: "Please input your username!" }]}
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

        <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
          <Button type="primary" htmlType="submit">
            Send Reset E-mail
          </Button>
        </Form.Item>
      </Form>
    );
  }
};
