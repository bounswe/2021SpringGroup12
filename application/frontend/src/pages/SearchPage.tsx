import * as React from "react";
import { Col, Row } from "antd/lib/grid";
import { Card, Input, Table, Tag, Tooltip } from "antd";
import axios from "axios";
import Column from "antd/lib/table/Column";
import { Link } from "react-router-dom";

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

type entityType = {
  description: string;
  entitiType: string;
  id: number;
  title: string;
};

type subGoalType = {
  description: string;
  id: number;
  title: string;
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
            <Card title="Search Results">
              <Table dataSource={searchResponse}>
                <Column title="Title" dataIndex="title" key="title" />
                <Column
                  title="Description"
                  dataIndex="description"
                  key="description"
                />

                <Column
                  title="Download Count"
                  dataIndex="download_count"
                  key="download_count"
                />
                <Column
                  title="Entities"
                  dataIndex="entities"
                  key="entities"
                  render={(entity: entityType[]) => {
                    return (
                      <>
                        {entity.map((item) => {
                          <Tooltip title={item.description}>
                            <Link to={`/entity/${item.entitiType}/${item.id}"`}>
                              <Tag color="green" key={item.id}>
                                {item.title.toUpperCase()}
                              </Tag>
                            </Link>
                          </Tooltip>;
                        })}
                      </>
                    );
                  }}
                />
                <Column
                  title="Sub Goals"
                  dataIndex="subgoals"
                  key="subgoals"
                  render={(subgoal: subGoalType[]) => (
                    <>
                      {subgoal.map((item) => {
                        <Tooltip title={item.description}>
                          <Link to={`/goals/${item.id}`}>
                            <Tag color="green" key={item.id}>
                              {item.title}
                            </Tag>
                          </Link>
                        </Tooltip>;
                      })}
                    </>
                  )}
                />
                <Column
                  title="Tags"
                  dataIndex="tags"
                  key="tags"
                  render={(tags: string[]) => (
                    <>
                      {tags.map((tag) => (
                        <Tag color="blue" key={tag}>
                          {tag}
                        </Tag>
                      ))}
                    </>
                  )}
                />
              </Table>
            </Card>
          </Col>
        </Row>
      )}
    </>
  );
}
