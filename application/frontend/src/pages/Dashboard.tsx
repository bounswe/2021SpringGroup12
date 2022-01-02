import * as React from "react";
import { Card, Col, Row, Statistic } from "antd";
import { AimOutlined, StockOutlined } from "@ant-design/icons";
import axios from "axios";

const user_id = localStorage.getItem("user_id");
const token = localStorage.getItem("token");

type UserAnalytics = {
  activeGoalCount: number;
  averageCompletionTimeOfGoals: number;
  averageExtensionCount: number;
  averageRating: number;
  completedGoalCount: number;
  bestGoal: {
    description: string;
    id: number;
    title: string;
  } | null;
  longestGoal: {
    description: string;
    id: number;
    title: string;
  } | null;
  shortestGoal: {
    description: string;
    id: number;
    title: string;
  } | null;
  worstGoal: {
    description: string;
    id: number;
    title: string;
  } | null;
};

const initialAnalytics: UserAnalytics = {
  activeGoalCount: 0,
  averageCompletionTimeOfGoals: 0,
  averageExtensionCount: 0,
  averageRating: 0,
  bestGoal: null,
  completedGoalCount: 0,
  longestGoal: null,
  shortestGoal: null,
  worstGoal: null,
};
export interface IDashboardProps {}

export function Dashboard(props: IDashboardProps) {
  const { Meta } = Card;
  const [analyticsData, setAnalyticsData] = React.useState(initialAnalytics);

  React.useEffect(() => {
    axios
      .get(`/users/analytics/${user_id}`, {
        headers: { Authorization: `Bearer ${token}` },
        data: {},
      })
      .then((response) => {
        // check for error response
        if (response.status === 200) {
          setAnalyticsData(response.data);
        }
      })
      .catch((error) => {
        console.error("There was an error!", error);
      });
  }, [analyticsData]);
  return (
    <div className="site-card-wrapper">
      <Row gutter={[16, 32]}>
        <Col span={24} style={{ textAlign: "center" }}>
          <Card bordered={false} title="Completed Goals">
            <Statistic value={analyticsData.completedGoalCount} />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<AimOutlined />}
              title="Best Goal"
              description={
                analyticsData.bestGoal ? (
                  <div>
                    <h3>{analyticsData.bestGoal.title}</h3>
                    <p>{analyticsData.bestGoal.description}</p>
                  </div>
                ) : (
                  "N/A"
                )
              }
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<AimOutlined />}
              title="Longest Goal"
              description={
                analyticsData.longestGoal ? (
                  <div>
                    <h3>{analyticsData.longestGoal.title}</h3>
                    <p>{analyticsData.longestGoal.description}</p>
                  </div>
                ) : (
                  "N/A"
                )
              }
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<AimOutlined />}
              title="Shortest Goal"
              description={
                analyticsData.shortestGoal ? (
                  <div>
                    <h3>{analyticsData.shortestGoal.title}</h3>
                    <p>{analyticsData.shortestGoal.description}</p>
                  </div>
                ) : (
                  "N/A"
                )
              }
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<AimOutlined />}
              title="Worst Goal"
              description={
                analyticsData.worstGoal ? (
                  <div>
                    <h3>{analyticsData.worstGoal.title}</h3>
                    <p>{analyticsData.worstGoal.description}</p>
                  </div>
                ) : (
                  "N/A"
                )
              }
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<StockOutlined />}
              title="Average Completion Time"
              description={analyticsData.averageCompletionTimeOfGoals ? analyticsData.averageCompletionTimeOfGoals : 'N/A'}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<StockOutlined />}
              title="Active Goals"
              description={analyticsData.activeGoalCount ? analyticsData.activeGoalCount : 'N/A'}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<StockOutlined />}
              title="Average Rating"
              description={analyticsData.averageRating ? analyticsData.averageRating : 'N/A'}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card bordered={false}>
            <Meta
              avatar={<StockOutlined />}
              title="Average Extension Count"
              description={analyticsData.averageExtensionCount ? analyticsData.averageExtensionCount : 'N/A'}
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
}
