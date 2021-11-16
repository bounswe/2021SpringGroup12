import * as React from "react";
import Sider from "antd/lib/layout/Sider";
import {
  AppstoreOutlined,
  BarChartOutlined,
  CloudOutlined,
  ShopOutlined,
  TeamOutlined,
  UserOutlined,
  UploadOutlined,
  VideoCameraOutlined,
} from "@ant-design/icons";
import { Menu } from "antd";
import { Link } from "react-router-dom";
import logo from './logo192.png';

export interface ISidebarProps {
  user: string;
}

const img_size = "200px"

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
          <div style={{width:img_size, height:img_size, backgroundColor:'aqua',textAlign:'center'}}>
            <img src={logo} alt="Logo" width={img_size} height={img_size}/>
          </div>
          <Menu theme="dark" mode="inline" defaultSelectedKeys={["4"]}>
            <Menu.Item key="1" icon={<UserOutlined />}>
              <Link to="/sampleURL">sampleURL 1</Link>
            </Menu.Item>
            <Menu.Item key="2" icon={<VideoCameraOutlined />}>
              <Link to="/sampleURL2">sampleURL 2</Link>
            </Menu.Item>
            <Menu.Item key="3" icon={<UploadOutlined />}>
              <Link to="/sampleURL3">sampleURL 3</Link>
            </Menu.Item>
            <Menu.Item key="4" icon={<BarChartOutlined />}>
              <Link to="/sampleURL4">sampleURL 4</Link>
            </Menu.Item>
            <Menu.Item key="5" icon={<CloudOutlined />}>
              <Link to="/sampleURL5">sampleURL 5</Link>
            </Menu.Item>
            <Menu.Item key="6" icon={<AppstoreOutlined />}>
              <Link to="/sampleURL6">sampleURL 6</Link>
            </Menu.Item>
            <Menu.Item key="7" icon={<TeamOutlined />}>
              <Link to="/sampleURL7">sampleURL 7</Link>
            </Menu.Item>
            <Menu.Item key="8" icon={<ShopOutlined />}>
              <Link to="/sampleURL8">sampleURL 8</Link>
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
          <div style={{width:img_size, height:img_size, backgroundColor:'aqua',textAlign:'center'}}>
            <img src={logo} alt="Logo" width={img_size} height={img_size}/>
          </div>

        </Sider>
    );
  }
}