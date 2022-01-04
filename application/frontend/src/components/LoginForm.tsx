import { Form, Input, Button } from "antd";
import axios from "axios";
import { useDispatch } from "react-redux";
import { Link, useHistory } from "react-router-dom";
import { getUser } from "../redux/actionCreators";

export const LoginForm = () => {
  const history = useHistory();
  const dispatch = useDispatch();


  const onFinish = (values: any) => {

    console.log(values)
    axios
      .post("/login", {
        "password": values.password,
        "username": values.username,
      })
      .then((res) => {
        console.log(res)
        if (res && res.status === 200) {
          console.log(res.data)
          localStorage.setItem("token", res.data.jwt);
          localStorage.setItem("username", res.data.userCredentialsGetDTO.username);
          localStorage.setItem("user_id", res.data.userCredentialsGetDTO.user_id);
          
          //Sets User Field of app root State
          dispatch(getUser(res.data.userCredentialsGetDTO.username));
          
          history.push("/");
          window.location.reload();
        }
      })
      .catch((error) => {
        window.alert(`Login failed with error Try Again ! \n ${error}`);
      });
  };

  const onFinishFailed = (errorInfo: any) => {
    window.alert("Login Failed");
  };

  return (
    <Form
      name="basic"
      labelCol={{ span: 8 }}
      wrapperCol={{ span: 16 }}
      initialValues={{ remember: true }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
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
        label="Password"
        name="password"
        rules={[{ required: true, message: "Please input your password!" }]}
      >
        <Input.Password />
      </Form.Item>
      <Form.Item
        name="remember"
        valuePropName="checked"
        wrapperCol={{ offset: 8, span: 16 }}
      >
        <Link style={{ float: "right" }} to="/forgotpassword">
          Forgot Password
        </Link>
      </Form.Item>
      <Form.Item name="submit" wrapperCol={{ offset: 8, span: 16 }}>
        <Button type="primary" htmlType="submit">
          Login
        </Button>
      </Form.Item>
      <Form.Item name="link" wrapperCol={{ offset: 8, span: 16 }}>
        <Button type="link" href="/register">
          Have no account? Register
        </Button>
      </Form.Item>
      <Form.Item name="link" wrapperCol={{ offset: 8, span: 16 }}>
        <Button type="link" href="/forgotpassword">
          Forgot password ?
        </Button>
      </Form.Item>
    </Form>
  );
};
