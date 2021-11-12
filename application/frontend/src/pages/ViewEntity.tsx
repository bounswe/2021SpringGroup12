import { Row, Col, Card } from "antd";
import * as React from "react";
import { ViewEntityForm } from "../components/ViewEntityForm";
export interface ILoginProps {}

export function ViewEntity(props: ILoginProps) {
  return (
    <div className="site-card-wrapper">
    <Row justify="center">
      <Col lg={12} style={{minWidth:'%25'}}>
        <Card title="View Entity" bordered>
          <ViewEntityForm />
        </Card>
      </Col>
    </Row>
  </div>
  );
}