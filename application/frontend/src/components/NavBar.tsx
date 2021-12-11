import { Menu } from "antd";
import { Header } from "antd/lib/layout/layout";
import * as React from "react";
import { Link } from "react-router-dom";
import { SignOutButton } from "./SignOutButton";

export interface INavBarProps {
  user: string;
}

export interface INavBarState {}

export default class NavBar extends React.Component<
  INavBarProps,
  INavBarState
> {
  constructor(props: INavBarProps) {
    super(props);

    this.state = {};
  }

  public render() {
    if (this.props.user) {
      return (
        <Header className="site-layout-background" style={{ padding: 0 }}>
          <Menu theme="dark" mode="horizontal">
            <Menu.Item key="1">
              <Link to="/">Home</Link>
            </Menu.Item>
            <Menu.Item key="2">
              <Link to="/goalsPage">Goals</Link>
            </Menu.Item>
            <Menu.Item key="4">
              <SignOutButton />
            </Menu.Item>
          </Menu>
        </Header>
      );
    } else {
      return (
        <Header className="site-layout-background" style={{ padding: 0 }}>
          <Menu theme="dark" mode="horizontal">
            <Menu.Item key="1">
              <Link to="/">Home</Link>
            </Menu.Item>
            <Menu.Item key="2">
              <Link to={`/login`}>Login</Link>
            </Menu.Item>
            <Menu.Item key="3">
              <Link to={`/register`}>Register</Link>
            </Menu.Item>
          </Menu>
        </Header>
      );
    }
  }
}
