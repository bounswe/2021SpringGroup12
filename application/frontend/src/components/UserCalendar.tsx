import { Badge, Calendar } from "antd";
import { Moment } from "moment";
import * as React from "react";

export interface IUserCalendatProps {}

type CalendarItemType = {
  type: "warning" | "success" | "error" | "default" | "processing" | undefined;
  content: string;
};

export default function UserCralendat(props: IUserCalendatProps) {
  const getListData = (value: Moment) => {
    let listData: CalendarItemType[] = [];

    switch (value.date()) {
      case 8:
        listData = [
          { type: "warning", content: "This is warning event." },
          { type: "success", content: "This is usual event." },
        ];
        break;
      case 10:
        listData = [
          { type: "warning", content: "This is warning event." },
          { type: "success", content: "This is usual event." },
          { type: "error", content: "This is error event." },
        ];
        break;
      case 15:
        listData = [
          { type: "warning", content: "This is warning event" },
          { type: "success", content: "This is very long usual event。。...." },
          { type: "error", content: "This is error event 1." },
          { type: "error", content: "This is error event 2." },
          { type: "error", content: "This is error event 3." },
          { type: "error", content: "This is error event 4." },
        ];
        break;
      default:
    }
    return listData;
  };

  const dateCellRender = (value: Moment) => {
    const listData = getListData(value);
    return (
      <ul className="events">
        {listData.map((item) => (
          <li key={item.content}>{item.content}</li>
        ))}
      </ul>
    );
  };

  const getMonthData = (value: Moment) => {
    if (value.month() === 8) {
      return 1394;
    }
  };

  const monthCellRender = (value: Moment) => {
    //appears only "Year" display
    const num = getMonthData(value);
    return num ? (
      <div className="notes-month">
        <section>{num}</section>
        <span>Backlog number</span>
      </div>
    ) : null;
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
