import React from "react";
import "./App.css";
import Register from "./pages/Register";
import Layout, { Content, Footer, Header } from "antd/lib/layout/layout";
import { Menu } from "antd";
import Sidebar from "./components/Sidebar";
import { Link, Route, Router, Switch } from "react-router-dom";
import history from "./helpers/history";
import { Login } from "./pages/Login";
import { ForgotPassword } from "./pages/ForgotPassword";
import {GoalsPage} from "./pages/GoalsPage";
import {AddGoal} from "./pages/AddGoal";
import {GoalPage} from "./pages/GoalPage";
import axios from "axios";
import {EditGoal} from "./pages/EditGoal";

function App() {
  axios.defaults.baseURL = "http://localhost:8085"
  localStorage.setItem("user_id", "1")
  localStorage.setItem("jwt", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaXRpdHl1bSIsImV4cCI6MTYzNzMyNzc4MCwiaWF0IjoxNjM2ODk1NzgwfQ.IavSQyuERhiM58XmMt6udqh7vZISi7B6Bx1PY3zROLA")

  // @ts-ignore
  // @ts-ignore
  return (
    <>
      <Router history={history}>
        <Sidebar />
        <Layout className="site-layout" style={{ marginLeft: 200 }}>
          <Header className="site-layout-background" style={{ padding: 0 }}>
            <Menu theme="dark" mode="horizontal">
              <Menu.Item key="1">
                <Link to="/register">Register</Link>
              </Menu.Item>
              <Menu.Item key="2">
                <Link to="/">Home</Link>
              </Menu.Item>
              <Menu.Item key="3">
                <Link to="/dashboard">Dashboard</Link>
              </Menu.Item>
              <Menu.Item key="4">
                <Link to="/login">Login</Link>
              </Menu.Item>
              <Menu.Item key="5">
                <Link to="/goals">GoalsPage</Link>
              </Menu.Item>
            </Menu>
          </Header>
          <Content
            style={{
              margin: "24px 16px 0",
              overflow: "scroll",
              height:"100vh",
              minWidth: "400px"
            }}
          >
            <Switch>
              <Route exact path="/">
                Home
              </Route>
              <Route path="/register">
                <Register />
              </Route>
              <Route path="/dashboard">Dashboard</Route>
              <Route path="/login">
                <Login/>
              </Route>
              <Route path="/forgotpassword">
                <ForgotPassword/>
              </Route>
              <Route path="/goals">
                <GoalsPage/>
              </Route>
              <Route path="/addGoal">
                <AddGoal/>
              </Route>
              <Route path="/goal/:goal_id" children={<GoalPage />} />
              <Route path="/editGoal/:goal_id" children={<EditGoal />} />
            </Switch>
          </Content>
          <Footer style={{ textAlign: 'center' }}>Ant Design ©2018 Created by Ant UED</Footer>
        </Layout>
      </Router>
    </>
  );
}

export default App;
