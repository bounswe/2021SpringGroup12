import { Goal, User } from '../reducers/index';

export enum ActionType {
    GET_USER_CREDENTIAL = 'GET_USER_CREDENTIAL',
    GET_ALL_GOALS = 'GET_ALL_GOALS'
}

interface userAction {
    type: ActionType.GET_USER_CREDENTIAL;
    payload: User;
}

interface getAllGoalsAction {
    type: ActionType.GET_ALL_GOALS;
    payload: Goal[] ;
}

export type Action = userAction | getAllGoalsAction