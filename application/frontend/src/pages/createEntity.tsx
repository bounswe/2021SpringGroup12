import { Row, Col, Card } from "antd";
import * as React from "react";
import { CreateEntityForm } from "../components/CreateEntityForm";
export interface ILoginProps {}

export function CreateEntity(props: ILoginProps) {
  return (
    <div className="site-card-wrapper">
    <Row justify="center">
      <Col lg={12} style={{minWidth:'%25'}}>
        <Card title="Create Entity" bordered>
          <CreateEntityForm />
        </Card>
      </Col>
    </Row>
  </div>
  );
}
