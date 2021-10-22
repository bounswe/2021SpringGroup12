import React from "react";
import "./App.css";
import  Register  from "./pages/Register";
import Layout, { Content, Footer, Header } from "antd/lib/layout/layout";
import { Menu } from "antd";
import Sidebar from "./components/Sidebar";

function App() {
  return (
    <>
      <Sidebar />
      <Layout className="site-layout" style={{ marginLeft: 200 }}>
        <Header className="site-layout-background" style={{ padding: 0 }}>
          <Menu theme="dark" mode="horizontal">
            <Menu.Item key="1">nav 1</Menu.Item>
            <Menu.Item key="2">nav 2</Menu.Item>
            <Menu.Item key="3">nav 3</Menu.Item>
          </Menu>
        </Header>
        <Content style={{ margin: "24px 16px 0", overflow: "initial" }}>
          <Register />
        </Content>
        <Footer></Footer>
      </Layout>
    </>
  );
}

export default App;
