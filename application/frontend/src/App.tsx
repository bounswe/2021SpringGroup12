import React from "react";
import "./App.css";
import Register from "./pages/Register";
import Layout, { Content, Footer, Header } from "antd/lib/layout/layout";
import { Menu } from "antd";
import Sidebar from "./components/Sidebar";
import { Link, Route, Router, Switch } from "react-router-dom";
import history from "./helpers/history";

function App() {
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
            </Menu>
          </Header>
          <Content style={{ margin: "24px 16px 0", overflow: "initial", minWidth:"300px"}}>
            <Switch>
              <Route exact path="/">
                Home
              </Route>
              <Route path="/register">
                <Register />
              </Route>
              <Route path="/dashboard">Dashboard</Route>
            </Switch>
          </Content>
          <Footer></Footer>
        </Layout>
      </Router>
    </>
  );
}

export default App;
