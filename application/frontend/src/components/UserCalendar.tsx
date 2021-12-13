import { Calendar, Tooltip } from "antd";
import { Moment } from "moment";
import { Goal } from "../redux/reducers";
import { AimOutlined } from "@ant-design/icons";

export interface IUserCalendarProps {
  goalList: Goal[];
}

export default function UserCalendar(props: IUserCalendarProps) {
  const getListData = (value: Moment) => {
    let listData = props.goalList.filter((item) => {
      return (
        item.deadline.getDay() === value.date() &&
        item.deadline.getMonth() === value.month()
      );
    });
    return listData;
  };

  const dateCellRender = (value: Moment) => {
    const listData = getListData(value);
    return (
      <ul>
        {listData.map((item) => (
          <Tooltip title={item.description}>
            <li key={item.id}>
              <AimOutlined /> {item.title}
            </li>
          </Tooltip>
        ))}
      </ul>
    );
  };

  const getMonthData = (value: Moment) => {
    const monthlyGoals = props.goalList.filter(
      (item) => item.deadline.getMonth() === value.month()
    );
    return monthlyGoals;
  };

  const monthCellRender = (value: Moment) => {
    //appears only "Year" display
    const monthData = getMonthData(value);
    return (
      <div>
        <ul>
          {monthData.map((item) => (
            <Tooltip title={item.description}>
              <li key={item.title}>
                <AimOutlined /> {item.title}
              </li>
            </Tooltip>
          ))}
          {}
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
