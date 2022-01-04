import React, {ReactElement} from 'react';
import Enzyme, {render} from 'enzyme';
import {Route, Router, BrowserRouter} from 'react-router-dom';
import {GoalPage} from "./GoalPage";
import {GoalTypes} from "../helpers/GoalTypes";
import {createMemoryHistory} from "history";

import Adapter from 'enzyme-adapter-react-16';


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

test('Check location is correct 1', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/goals/:goal_id">
                <GoalPage goalType={GoalTypes.Normal}/>
            </Route>
        </BrowserRouter>,
        {
            route: "/goals/1",
        });
    expect(history.location.pathname).toEqual(`/goals/1`);
});

test('Check location is correct 2', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/goals/:goal_id">
                <GoalPage goalType={GoalTypes.Normal}/>
            </Route>
        </BrowserRouter>,
        {
            route: "/goals/2",
        });
    expect(history.location.pathname).toEqual(`/goals/2`);
});

