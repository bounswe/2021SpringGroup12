import * as React from "react";
import { useEffect } from "react";
import UserCalendar from "../components/UserCalendar";
import { getUser, getAllGoals } from "../redux/actionCreators";
import { useDispatch } from "react-redux";
import { useTypedSelector } from "../hooks/useTypeSelector";
import moment from "moment";

const username = localStorage.getItem("username");

export function CalendarPage() {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getUser(username));
  }, [username]);

  const userId = useTypedSelector((state) => state.root.user?.user_id);
  useEffect(() => {
    userId && dispatch(getAllGoals(userId));
  }, [userId, dispatch]);

  const goalList = useTypedSelector((state) => state.root.goals);
  const convertedGoalList = goalList.map((item) => {
    return {
      ...item,
      deadline: moment(item.deadline, "YYYY-MM-DD").toDate(),
    };
  });
  return (
    <>
      <div>
        <UserCalendar goalList={convertedGoalList} />
      </div>
    </>
  );
}
