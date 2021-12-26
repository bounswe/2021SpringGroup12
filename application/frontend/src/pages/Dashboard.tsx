import * as React from "react";
import { Card, Col, Row, Statistic } from "antd";
import { AimOutlined, StockOutlined } from "@ant-design/icons";
import axios from "axios";

const user_id = localStorage.getItem("user_id");
const token = localStorage.getItem("token");

type UserAnalytics = {
  activeGoalCount: number;
  averageCompletionTimeOfGoalsInMiliseconds: number;
  averageExtensionCount: number;
  averageRating: number;
  bestGoal: {
    deadline: string;
    description: string;
    id: number;
    title: string;
  };
  completedGoalCount: number;
  longestGoal: {
    deadline: string;
    description: string;
    id: number;
    title: string;
  };
  shortestGoal: {
    deadline: string;
    description: string;
    id: number;
    title: string;
  };
  worstGoal: {
    deadline: string;
    description: string;
    id: number;
    title: string;
  };
};
export interface IDashboardProps {}

export function Dashboard(props: IDashboardProps) {
  const { Meta } = Card;
  const [isLoaded, setIsLoaded] = React.useState(false);

  React.useEffect(() => {
    const analyticsData = axios
      .get(`/users/analytics/${user_id}`, {
        headers: { Authorization: `Bearer ${token}` },
        data: {},
      })
      .then((response) => {
        // check for error response
        if (response.status === 200) {
          setIsLoaded(true);
          console.log(`dashboard ${response}`);
          return response.data as UserAnalytics;
        }
        throw response;
      })
      .catch((error) => {
        console.error("There was an error!", error);
      });
  });
  return (
    <div className="site-card-wrapper">
      <Row gutter={[16, 32]}>
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
