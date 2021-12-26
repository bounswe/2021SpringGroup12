import {
  Button,
  Card,
  Col,
  Form,
  Input,
  Modal,
  Row,
  Table,
  notification,
} from "antd";
import * as React from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import { GoalTypes } from "../helpers/GoalTypes";
import { PlusOutlined } from "@ant-design/icons";

export class GoalsPage extends React.Component {
  state = {
    isGroupsLoaded: false,
    goals: [],
    isGroupGoalsLoaded: false,
    groupGoals: [],
    loading: false,
    visible: false,
  };
  user_id = localStorage.getItem("user_id");
  token = localStorage.getItem("token");

  deleteGoal = (goal: { key: any }) => {
    console.log("Received values of delete: ", goal);
    axios
      .delete(`/${GoalTypes.Normal}/${goal.key}`, {
        headers: { Authorization: `Bearer ${this.token}` },
        data: {},
      })
      .then(() => this.getGoals());
  };

  joinGroup = (input: { token: string }) => {
    const token = input.token;
    axios
      .post(
        `/${GoalTypes.Group}/${this.user_id}/join`,
        {},
        {
          headers: { Authorization: `Bearer ${this.token}` },
          params: {
            token: token,
          },
        }
      )
      .then((response) => {
        if (response.status === 200) {
          this.getGoals(true);
          this.setState({
            loading: false,
            visible: false,
          });
          this.openNotification("success", "Joined Group Succesfully!");
        }
      })
      .catch((err) => {
        this.openNotification("warning", `Error: ${err}`);
        console.error(err);
      });
  };

  getGoals(isGroupGoal: boolean = false) {
    console.log(this.token);
    console.log(axios.defaults.baseURL);
    const url = isGroupGoal
      ? `/${GoalTypes.Group}/member_of/${this.user_id}`
      : `/${GoalTypes.Normal}/of_user/${this.user_id}`;
    console.log(url);
    axios
      .get(url, {
        headers: { Authorization: `Bearer ${this.token}` },
        data: {},
      })
      .then((response) => {
        // check for error response
        if (response.status === 200) {
          return response.data;
        }
        throw response;
      })
      .then((data) => {
        let tmp = [];
        for (let i = 0; i < data.length; i++) {
          let deadline = data[i]["deadline"];
          if (deadline !== null) {
            deadline = deadline.substr(0, 10);
          }
          tmp.push({
            key: data[i]["id"],
            title: data[i]["title"],
            description: data[i]["description"],
            deadline: deadline,
          });
        }
        console.log("tmp", tmp);
        if (isGroupGoal) {
          this.setState({
            isGroupGoalsLoaded: true,
            groupGoals: tmp,
          });
        } else {
          this.setState({
            isGroupsLoaded: true,
            goals: tmp,
          });
        }
        console.log("Success!", this.state);
      })
      .catch((error) => {
        console.error("There was an error!", error);
      });
  }

  componentDidMount() {
    this.getGoals(false);
    this.getGoals(true);
  }

  columns = (isGroupGoal: boolean = false) => {
    return [
      {
        title: "Title",
        dataIndex: "title",
        key: "title",
        render: (
          text: any,
          goal: {
            key:
              | string
              | number
              | boolean
              | {}
              | React.ReactElement<
                  any,
                  string | React.JSXElementConstructor<any>
                >
              | React.ReactNodeArray
              | React.ReactPortal
              | null
              | undefined;
          }
        ) => (
          <Link
            to={
              (isGroupGoal ? `/${GoalTypes.Group}/` : `/${GoalTypes.Normal}/`) +
              goal.key
            }
          >
            {text}
          </Link>
        ),
      },
      {
        title: "Description",
        dataIndex: "description",
        key: "description",
      },
      {
        title: "Deadline",
        dataIndex: "deadline",
        key: "deadline",
      },
    ];
  };

  showModal = () => {
    this.setState({
      visible: true,
    });
  };

  handleOk = () => {
    this.setState({ loading: true });
    setTimeout(() => {
      this.setState({ loading: false, visible: false });
    }, 3000);
  };

  handleCancel = () => {
    this.setState({ visible: false });
  };

  openNotification = (
    type: "success" | "info" | "warning",
    message: string
  ) => {
    notification[type]({
      message,
      placement: "bottomRight",
    });
  };
  render() {
    const { isGroupsLoaded, goals, isGroupGoalsLoaded, groupGoals } =
      this.state;

    console.log("goals", goals);
    console.log("groupGoals", groupGoals);

    return (
      <Row>
        <Col span={12}>
          <Card
            title="Goals"
            loading={!isGroupsLoaded}
            extra={
              <Link to="/addGoal">
                <Button type="primary">
                  Create <PlusOutlined />
                </Button>
              </Link>
            }
          >
            <div>
              <Table columns={this.columns(false)} dataSource={goals} />
            </div>
          </Card>
        </Col>
        <Col span={12}>
          <Card
            title="Group Goals"
            loading={!isGroupGoalsLoaded}
            extra={
              <Row>
                <Col>
                  <Button type="default" onClick={this.showModal}>
                    Join
                  </Button>
                </Col>
                <Col>
                  <Link to="/addGroupGoal">
                    <Button type="primary">
                      Create
                      <PlusOutlined />
                    </Button>
                  </Link>
                </Col>
              </Row>
            }
          >
            <div>
              <Table columns={this.columns(true)} dataSource={groupGoals} />
            </div>
          </Card>
        </Col>
        <Col span={6}>
          <Modal
            visible={this.state.visible}
            onOk={this.handleOk}
            onCancel={this.handleCancel}
            width={"800px"}
            footer={[
              <Button key="back" onClick={this.handleCancel}>
                Return
              </Button>,
            ]}
          >
            <Form onFinish={this.joinGroup}>
              <h2>
                Please write down the token of group goal that you want to join:
              </h2>
              <Form.Item
                name="token"
                rules={[{ required: true, message: "Please input a token!" }]}
              >
                <Input placeholder="Token" />
              </Form.Item>
              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  loading={this.state.loading}
                  onClick={this.handleOk}
                >
                  Submit
                </Button>
              </Form.Item>
            </Form>
          </Modal>
        </Col>
      </Row>
    );
  }
}
