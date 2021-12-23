import * as React from "react";
import { Card, Col, Row, Statistic } from "antd";
import { AimOutlined, StockOutlined } from "@ant-design/icons";

const user_id = localStorage.getItem("user_id");
export interface IDashboardProps {}

export function Dashboard(props: IDashboardProps) {
  const { Meta } = Card;
  return (
    <div className="site-card-wrapper">
      <Row gutter={[16,32]}>
        <Col span={24} style={{ textAlign: "center" }}>
          <Card bordered={false} title="Completed Goals">
            <Statistic value={112893} />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<AimOutlined />}
              title="Best Goal"
              description="This is the description"
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<AimOutlined />}
              title="Longest Goal"
              description="This is the description"
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<AimOutlined />}
              title="Shortest Goal"
              description="This is the description"
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<AimOutlined />}
              title="Worst Goal"
              description="This is the description"
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<StockOutlined />}
              title="Average Completion Time"
              description="This is the description"
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<StockOutlined />}
              title="Active Goals"
              description="This is the description"
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<StockOutlined />}
              title="Average Rating"
              description="This is the description"
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<StockOutlined />}
              title="Average Extension Count"
              description="This is the description"
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
}
