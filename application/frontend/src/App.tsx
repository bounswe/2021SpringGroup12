import React from "react";
import "./App.css";
import Register from "./pages/Register";
import { Route, Router, Switch } from "react-router-dom";
import history from "./helpers/history";
import { Login } from "./pages/Login";
import { ForgotPassword } from "./pages/ForgotPassword";
import Home from "./pages/Home";
import NavBar from "./components/NavBar";
import {GoalsPage} from "./pages/GoalsPage";
import {AddGoal} from "./pages/AddGoal";
import {GoalPage} from "./pages/GoalPage";
import {EditGoal} from "./pages/EditGoal";
import {EntityPage} from "./pages/EntityPage";
import {AddEntity} from "./pages/AddEntity";
import {EditEntity} from "./pages/EditEntity"
import {LinkEntity} from "./pages/LinkEntity"
import { Content } from "antd/lib/layout/layout";
import {Layout} from "antd";
import Sidebar from "./components/Sidebar";
import UserCalendar from "./components/UserCalendar";



export interface IAppProps {}

export interface IAppState {
  username: string;
}
export default class App extends React.Component<IAppProps, IAppState> {
  constructor(props: IAppProps) {
    super(props);

    this.state = {
      username: "",
    };
  }
  componentDidMount() {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username");
    const user_id = localStorage.getItem("user_id");
    // TODO add jwt authorization

    // const config = {
    //   Headers: {
    //     Authorization: `Bearer${token}`,
    //   },
    // };
    // axios
    //   .get("/users", {
    //     params: {
    //       username,
    //       config,
    //     },
    //   })
    //   .then((res) => console.log(res))
    //   .catch((err) => console.log(err));
    if (token && username) {
      this.setState({
        username: username,
      });
    }
  }
  render() {
    return (
        <Router history={history}>
          <Sidebar user={this.state.username} />
          <Layout className="site-layout" style={{ marginLeft: 200 }}>
            <NavBar user={this.state.username}></NavBar>
            <Content
                style={{
                  margin: "24px 16px 0",
                  overflow: "scroll",
                  height: "100vh",
                  minWidth: "400px",
                }}
            >
              <Switch>
                <Route exact path="/dashboard">
                  Dashboard
                </Route>
                <Route exact path="/register">
                  <Register />
                </Route>
                <Route exact path="/login">
                  <Login />
                </Route>
                <Route exact path="/forgotpassword">
                  <ForgotPassword />
                </Route>
                <Route exact path="/">
                  <Home user={this.state.username} />
                </Route>
                <Route path="/goals">
                  <GoalsPage />
                </Route>
                <Route path="/addGoal">
                  <AddGoal />
                </Route>
                <Route path="/calendar">
                  <UserCalendar />
                </Route>
                <Route path="/goal/:goal_id" children={<GoalPage />} />
                <Route path="/editGoal/:goal_id" children={<EditGoal />} />
                <Route path="/entity/:entity_id" children={<EntityPage />} />
                <Route path="/addEntity/:goal_id" children={<AddEntity /> } />
                <Route path="/editEntity/:entity_id" children={<EditEntity /> } />
                <Route path="/linkEntityfrom/:entity_id" children={<LinkEntity /> } />
              </Switch>
            </Content>
          </Layout>
        </Router>
    );
  }
}
