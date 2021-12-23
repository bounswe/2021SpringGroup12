import React from "react";
import "./App.css";
import Register from "./pages/Register";
import { Route, Router, Switch } from "react-router-dom";
import history from "./helpers/history";
import { Login } from "./pages/Login";
import { ForgotPassword } from "./pages/ForgotPassword";
import Home from "./pages/Home";
import NavBar from "./components/NavBar";
import { GoalsPage } from "./pages/GoalsPage";
import { AddGoal } from "./pages/AddGoal";
import { GoalPage } from "./pages/GoalPage";
import { EditGoal } from "./pages/EditGoal";
import { EntityPage } from "./pages/EntityPage";
import { AddEntity } from "./pages/AddEntity";
import { EditEntity } from "./pages/EditEntity";
import { LinkEntity } from "./pages/LinkEntity";
import { Content } from "antd/lib/layout/layout";
import { Layout } from "antd";
import Sidebar from "./components/Sidebar";
import { ResourcePage } from "./pages/ResourcePage";
import { GoalTypes } from "./helpers/GoalTypes";
import { CalendarPage } from "./pages/CalendarPage";
import { Dashboard } from "./pages/Dashboard";

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
                <Dashboard />
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
              <Route exact path="/goalsPage">
                <GoalsPage />
              </Route>
              <Route exact path="/addGoal">
                <AddGoal goalType={GoalTypes.Normal} parentType={undefined} />
              </Route>
              <Route exact path="/addGroupGoal">
                <AddGoal goalType={GoalTypes.Group} parentType={undefined} />
              </Route>
              <Route exact path="/addSubToSub/:parent_id">
                <AddGoal goalType={GoalTypes.Sub} parentType={GoalTypes.Sub} />
              </Route>
              <Route exact path="/addSubToNormal/:parent_id">
                <AddGoal
                  goalType={GoalTypes.Sub}
                  parentType={GoalTypes.Normal}
                />
              </Route>
              <Route exact path="/addSubToGroup/:parent_id">
                <AddGoal
                  goalType={GoalTypes.Sub}
                  parentType={GoalTypes.Group}
                />
              </Route>
              <Route exact path="/calendar">
                <CalendarPage />
              </Route>
              <Route
                path="/addEntity/:parentType/:entitiType/:parent_id"
                children={<AddEntity />}
              />
              <Route
                path="/editEntity/:entitiType/:entity_id"
                children={<EditEntity />}
              />
              <Route
                path="/linkEntityfrom/:goal_id/:entity_id"
                children={<LinkEntity />}
              />
              <Route
                path="/entity/:entitiType/:entity_id"
                children={<EntityPage />}
              />
              <Route
                path="/resources/:resource_id"
                children={<ResourcePage />}
              />
              <Route
                exact
                path="/goals/:goal_id"
                children={<GoalPage goalType={GoalTypes.Normal} />}
              />
              <Route
                exact
                path="/subgoals/:goal_id"
                children={<GoalPage goalType={GoalTypes.Sub} />}
              />
              <Route
                exact
                path="/groupgoals/:goal_id"
                children={<GoalPage goalType={GoalTypes.Group} />}
              />
              <Route
                exact
                path="/editGoal/:goal_id"
                children={<EditGoal goalType={GoalTypes.Normal} />}
              />
              <Route
                exact
                path="/editSubgoal/:goal_id"
                children={<EditGoal goalType={GoalTypes.Sub} />}
              />
              <Route
                exact
                path="/editGroupgoal/:goal_id"
                children={<EditGoal goalType={GoalTypes.Group} />}
              />
            </Switch>
          </Content>
        </Layout>
      </Router>
    );
  }
}
