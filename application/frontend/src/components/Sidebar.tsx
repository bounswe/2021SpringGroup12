import * as React from 'react';
import Sider from 'antd/lib/layout/Sider';
import {
  AppstoreOutlined,
  BarChartOutlined,
  CloudOutlined,
  ShopOutlined,
  TeamOutlined,
  UserOutlined,
  UploadOutlined,
  VideoCameraOutlined,
} from '@ant-design/icons';
import { Menu } from 'antd';


export interface ISidebarProps {
}

export default function Sidebar (props: ISidebarProps) {
  return (
    <Sider
        style={{
          overflow: "auto",
          height: "100vh",
          position: "fixed",
          left: 0,
        }}
      >
        <div style={{width:'120px', height:'120px', backgroundColor:'aqua',textAlign:'center'}}> LOGO</div>
        <Menu theme="dark" mode="inline" defaultSelectedKeys={["4"]}>
          <Menu.Item key="1" icon={<UserOutlined />}>
            nav 1
          </Menu.Item>
          <Menu.Item key="2" icon={<VideoCameraOutlined />}>
            nav 2
          </Menu.Item>
          <Menu.Item key="3" icon={<UploadOutlined />}>
            nav 3
          </Menu.Item>
          <Menu.Item key="4" icon={<BarChartOutlined />}>
            nav 4
          </Menu.Item>
          <Menu.Item key="5" icon={<CloudOutlined />}>
            nav 5
          </Menu.Item>
          <Menu.Item key="6" icon={<AppstoreOutlined />}>
            nav 6
          </Menu.Item>
          <Menu.Item key="7" icon={<TeamOutlined />}>
            nav 7
          </Menu.Item>
          <Menu.Item key="8" icon={<ShopOutlined />}>
            nav 8
          </Menu.Item>
        </Menu>
      </Sider>
  );
}
