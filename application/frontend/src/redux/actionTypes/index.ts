import { Entity, Goal, User } from "../reducers/index";

export enum ActionType {
  GET_USER_CREDENTIAL = "GET_USER_CREDENTIAL",
  GET_ALL_GOALS = "GET_ALL_GOALS",
  GET_ALL_ENTITIES = "GET_ALL_ENTITIES",
}

interface userAction {
  type: ActionType.GET_USER_CREDENTIAL;
  payload: User;
}

interface getAllGoalsAction {
  type: ActionType.GET_ALL_GOALS;
  payload: Goal[];
}
interface getAllEntitiesAction {
  type: ActionType.GET_ALL_ENTITIES;
  payload: Entity[];
}
export type Action = userAction | getAllGoalsAction | getAllEntitiesAction;
