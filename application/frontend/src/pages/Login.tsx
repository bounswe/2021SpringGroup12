import { Row, Col, Card } from "antd";
import * as React from "react";
import { LoginForm } from "../components/LoginForm";
export interface ILoginProps {}

export function Login(props: ILoginProps) {
  return (
    <div className="site-card-wrapper">
      <Row justify="center">
        <Col lg={12} style={{ minWidth: "%25" }}>
          <Card title="Login" bordered>
            <LoginForm />
          </Card>
        </Col>
      </Row>
    </div>
  );
}
