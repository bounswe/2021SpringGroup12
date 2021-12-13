import { Action, ActionType } from "../actionTypes/index";

export interface User {
  email: string;
  password: string;
  user_id: number;
  username: string;
}

export interface Goal {
  deadline: Date;
  description: string;
  id: number;
  title: string;
}

export interface AppState {
  user: User | null;
  goals: Goal[];
}

const initialState: AppState = {
  user: null,
  goals: [],
};

export const rootReducer = (
  state: AppState = initialState,
  action: Action
): AppState => {
  switch (action.type) {
    case ActionType.GET_USER_CREDENTIAL:
      return {
        ...state,
        user: action.payload,
      };
    case ActionType.GET_ALL_GOALS:
      return {
        ...state,
        goals: action.payload,
      };

    default:
      return state;
  }
};
