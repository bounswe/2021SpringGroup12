import axios from "axios";
import { Dispatch } from "redux";
import { ActionType, Action } from "../actionTypes";

const token = localStorage.getItem("token");

export const getUser = (username: string | null) => {
  return (dispatch: Dispatch<Action>) => {
    axios
      .get(`/users/${username}`, {
        headers: { Authorization: `Bearer ${token}` },
        data: {},
      })
      .then((response) => {
        // check for error response
        if (response.status === 200) {
          return response.data;
        }
        throw response;
      })
      .then((data) => {
        dispatch({
          type: ActionType.GET_USER_CREDENTIAL,
          payload: data,
        });
      })
      .catch((err) => {
        console.log(err);
      });
  };
};

export const getAllGoals = (userId: number | null) => {
  return (dispatch: Dispatch<Action>) => {
    axios
      .get(`/goals/of_user/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
        data: {},
      })
      .then((response) => {
        // check for error response
        if (response.status === 200) {
          return response.data;
        }
        throw response;
      })
      .then((data) => {
        dispatch({
          type: ActionType.GET_ALL_GOALS,
          payload: data,
        });
      })
      .catch((err) => {
        console.log(err);
      });
  };
};
export const getAllEntities = (userId: number | null) => {
  return (dispatch: Dispatch<Action>) => {
    axios
      .get(`/entities/user/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
        data: {},
      })
      .then((response) => {
        // check for error response
        if (response.status === 200) {
          return response.data;
        }
        throw response;
      })
      .then((data) => {
        dispatch({
          type: ActionType.GET_ALL_ENTITIES,
          payload: data,
        });
      })
      .catch((err) => {
        console.log(err);
      });
  };
};
