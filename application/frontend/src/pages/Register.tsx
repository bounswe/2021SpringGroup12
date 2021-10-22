import * as React from "react";
import RegistrationForm from "../components/RegistrationForm";
import { Row, Col, Card } from "antd";

interface IRegisterProps {}

export default function Register(props: IRegisterProps) {
  return (
    <div className="site-card-wrapper">
      <Row justify="center">
        <Col span={8}>
          <Card title="Register" bordered>
            <RegistrationForm />
          </Card>
        </Col>
      </Row>
    </div>
  );
}
