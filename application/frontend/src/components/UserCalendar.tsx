import { Calendar, Tooltip } from "antd";
import { Moment } from "moment";
import { Entity, Goal } from "../redux/reducers";
import { AimOutlined, CarryOutOutlined } from "@ant-design/icons";

export interface IUserCalendarProps {
  goalList: Goal[];
  entityList: Entity[];
}

export default function UserCalendar(props: IUserCalendarProps) {
  const getGoalListData = (value: Moment) => {
    let listData = props.goalList.filter((item) => {
      return (
        item.deadline.getDay() === value.date() &&
        item.deadline.getMonth() === value.month()
      );
    });
    return listData;
  };
  const getEntityListData = (value: Moment) => {
    let listData = props.entityList.filter((item) => {
      return (
        item.deadline.getDay() === value.date() &&
        item.deadline.getMonth() === value.month()
      );
    });
    return listData;
  };

  const dateCellRender = (value: Moment) => {
    const goalListData = getGoalListData(value);
    const entityListData = getEntityListData(value);

    return (
      <ul>
        {goalListData.map((item) => (
          <Tooltip title={item.description}>
            <li key={item.id}>
              <AimOutlined /> {item.title}
            </li>
          </Tooltip>
        ))}
        {entityListData.map((item) => (
          <Tooltip title={item.description}>
            <li key={item.id}>
              <CarryOutOutlined /> {`${item.entitiType} ${item.title}`}
            </li>
          </Tooltip>
        ))}
      </ul>
    );
  };

  const getMonthGoalsData = (value: Moment) => {
    const monthlyGoals = props.goalList.filter(
      (item) => item.deadline.getMonth() === value.month()
    );
    return monthlyGoals;
  };

  const getMonthEntityData = (value: Moment) => {
    const monthlyEntities = props.entityList.filter(
      (item) => item.deadline.getMonth() === value.month()
    );
    return monthlyEntities;
  };

  const monthCellRender = (value: Moment) => {
    //appears only "Year" display
    const goalData = getMonthGoalsData(value);
    const entityData = getMonthEntityData(value);
    return (
      <div>
        <ul>
          {goalData.map((item) => (
            <Tooltip title={item.description}>
              <li key={item.title}>
                <AimOutlined /> {item.title}
              </li>
            </Tooltip>
          ))}
          {entityData.map((item) => (
            <Tooltip title={item.description}>
              <li key={item.title}>
                <CarryOutOutlined /> {`${item.entitiType} ${item.title}`}
              </li>
            </Tooltip>
          ))}
        </ul>
      </div>
    );
  };
  return (
    <div>
      <Calendar
        dateCellRender={dateCellRender}
        monthCellRender={monthCellRender}
      />
    </div>
  );
}
