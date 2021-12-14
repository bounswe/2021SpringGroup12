import * as React from "react";
import Sider from "antd/lib/layout/Sider";
import {
  UserOutlined,
  CalendarOutlined,
  AimOutlined,
} from "@ant-design/icons";
import { Menu } from "antd";
import { Link } from "react-router-dom";
import logo from "./logo192.png";

export interface ISidebarProps {
  user: string;
}

const img_size = "200px";

export default function Sidebar(props: ISidebarProps) {
  if (props.user) {
    return (
      <Sider
        style={{
          overflow: "auto",
          height: "100vh",
          position: "fixed",
          left: 0,
        }}
      >
        <div
          style={{
            width: img_size,
            height: img_size,
            backgroundColor: "aqua",
            textAlign: "center",
          }}
        >
          <img src={logo} alt="Logo" width={img_size} height={img_size} />
        </div>
        <Menu theme="dark" mode="inline" defaultSelectedKeys={["4"]}>
          <Menu.Item key="1" icon={<UserOutlined />}>
            <Link to="/dashboard">Dashboard</Link>
          </Menu.Item>
          <Menu.Item key="2" icon={<AimOutlined />}>
            <Link to="/goals">Goals</Link>
          </Menu.Item>
          <Menu.Item key="3" icon={<CalendarOutlined />}>
            <Link to="/calendar">Calendar</Link>
          </Menu.Item>
        </Menu>
      </Sider>
    );
  } else {
    return (
      <Sider
        style={{
          overflow: "auto",
          height: "100vh",
          position: "fixed",
          left: 0,
        }}
      >
        <div
          style={{
            width: img_size,
            height: img_size,
            backgroundColor: "aqua",
            textAlign: "center",
          }}
        >
          <img src={logo} alt="Logo" width={img_size} height={img_size} />
        </div>
      </Sider>
    );
  }
}
