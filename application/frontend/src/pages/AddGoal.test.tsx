import React, {ReactElement} from 'react';
import Enzyme, {render} from 'enzyme';
import {Route, Router, BrowserRouter} from 'react-router-dom';
import {createMemoryHistory} from "history";
import Adapter from 'enzyme-adapter-react-16';
import {AddGoal} from "./AddGoal";
import {GoalTypes} from "../helpers/GoalTypes";


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
            <Route exact path="/addGoal">
                <AddGoal goalType={GoalTypes.Normal} parentType={undefined} />
            </Route>
        </BrowserRouter>,
        {
            route: "/addGoal",
        });
    expect(history.location.pathname).toEqual(`/addGoal`);
});

test('Check location is correct group goal', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/addGroupGoal">
                <AddGoal goalType={GoalTypes.Normal} parentType={undefined} />
            </Route>
        </BrowserRouter>,
        {
            route: "/addGroupGoal",
        });
    expect(history.location.pathname).toEqual(`/addGroupGoal`);
});

test('Check location is correct sub to sub', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/addSubToSub/1">
                <AddGoal goalType={GoalTypes.Sub} parentType={GoalTypes.Sub} />
            </Route>
        </BrowserRouter>,
        {
            route: "/addSubToSub/1",
        });
    expect(history.location.pathname).toEqual(`/addSubToSub/1`);
});

test('Check location is correct sub to goal', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/addSubToNormal/1">
                <AddGoal goalType={GoalTypes.Sub} parentType={GoalTypes.Normal} />
            </Route>
        </BrowserRouter>,
        {
            route: "/addSubToNormal/1",
        });
    expect(history.location.pathname).toEqual(`/addSubToNormal/1`);
});

test('Check location is correct sub to group', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/addSubToGroup/1">
                <AddGoal goalType={GoalTypes.Sub} parentType={GoalTypes.Group} />
            </Route>
        </BrowserRouter>,
        {
            route: "/addSubToGroup/1",
        });
    expect(history.location.pathname).toEqual(`/addSubToGroup/1`);
});

