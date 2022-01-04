import * as React from "react";
import { Col, Row } from "antd/lib/grid";
import { Card, Input, List } from "antd";
import axios from "axios";

export interface ISearchPageProps {}

export type prototypeResponse = {
  description: string;
  download_count: number;
  entities: [
    {
      description: string;
      entitiType: string;
      id: number;
      title: string;
    }
  ];
  id: number;
  reference_goal_id: number;
  subgoals: [
    {
      description: string;
      id: number;
      title: string;
    }
  ];
  tags: [string];
  title: string;
  username: string;
};

export function SearchPage(props: ISearchPageProps) {
  const { Search } = Input;
  const token = localStorage.getItem("token");
  const initialValue: prototypeResponse[] = [
    {
      description: "",
      download_count: 0,
      entities: [
        {
          description: "",
          entitiType: "",
          id: 0,
          title: "",
        },
      ],
      id: 0,
      reference_goal_id: 0,
      subgoals: [
        {
          description: "",
          id: 0,
          title: "",
        },
      ],
      tags: [""],
      title: "",
      username: "",
    },
  ];

  const [searchResponse, setSearchResponse] = React.useState(initialValue);

  const [visible, setVisible] = React.useState(false);

  const onSearch = (value: string) => {
    axios
      .get(`/prototypes/search/?query=${value}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => {
        // check for error response
        if (response.status === 200) {
          setSearchResponse(response.data);
          setVisible(true);
          console.log(searchResponse);
        }
      })
      .catch((error) => {
        console.error("There was an error!", error);
      });
  };
  return (
    <>
      <Row>
        <Col offset={6} span={12}>
          <Search
            placeholder="Search"
            allowClear
            enterButton="Search"
            size="large"
            onSearch={onSearch}
          />
        </Col>
      </Row>
      {visible && (
        <Row>
          <Col span={24}>
            <Card>
              <List
                size="large"
                header={<h2>Search Results</h2>}
                dataSource={searchResponse}
                renderItem={(item) => (
                  <List.Item>
                    <h3>{item.title}</h3>
                    <p>{item.description}</p>
                  </List.Item>
                )}
              />
            </Card>
          </Col>
        </Row>
      )}
    </>
  );
}
