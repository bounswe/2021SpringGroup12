import React, {ReactElement} from 'react';
import Enzyme, {render} from 'enzyme';
import {Route, Router, BrowserRouter} from 'react-router-dom';
import {GoalPage} from "./GoalPage";
import {GoalTypes} from "../helpers/GoalTypes";
import {createMemoryHistory} from "history";

import Adapter from 'enzyme-adapter-react-16';
import {EditGoal} from "./EditGoal";


Enzyme.configure({adapter: new Adapter()});

function renderWithRouter(ui: ReactElement, {
                              route = '/',
                              history = createMemoryHistory({initialEntries: [route]}),
                          }: any = {}
) {
    const Wrapper: React.FC = ({children}) => (
        <Router history={history}>{children}</Router>
    );
    return {
        ...render(ui, {wrapper: Wrapper}), history,
    };
}

test('Check location is correct goal', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/editGoal/:goal_id">
                <EditGoal goalType={GoalTypes.Normal}/>
            </Route>
        </BrowserRouter>,
        {
            route: "/editGoal/1",
        });
    expect(history.location.pathname).toEqual(`/editGoal/1`);
});

test('Check location is correct subgoal', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/editSubgoal/:goal_id">
                <EditGoal goalType={GoalTypes.Sub}/>
            </Route>
        </BrowserRouter>,
        {
            route: "/editSubgoal/1",
        });
    expect(history.location.pathname).toEqual(`/editSubgoal/1`);
});

test('Check location is correct groupgoal', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/editGroupgoal/:goal_id">
                <EditGoal goalType={GoalTypes.Sub}/>
            </Route>
        </BrowserRouter>,
        {
            route: "/editGroupgoal/1",
        });
    expect(history.location.pathname).toEqual(`/editGroupgoal/1`);
});

