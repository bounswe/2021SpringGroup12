import { Row, Col, Card } from "antd";
import * as React from "react";
import { PasswordForm } from "../components/PasswordForm";
export interface IForgotPasswordProps {}

export function ForgotPassword(props: IForgotPasswordProps) {
  return (
    <div className="site-card-wrapper">
    <Row justify="center">
      <Col lg={12} style={{minWidth:'%25'}}>
        <Card title="Reset Password" bordered>
          <PasswordForm />
        </Card>
      </Col>
    </Row>
  </div>
  );
}
