import React, {ReactElement} from 'react';
import Enzyme, {render} from 'enzyme';
import {GoalsPage} from "./GoalsPage";
import {Route, Router, BrowserRouter} from 'react-router-dom';
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

test('Check location is correct', async () => {
    const { history} = renderWithRouter(
        <BrowserRouter>
            <Route exact path="/goalsPage">
                <GoalsPage/>
            </Route>
        </BrowserRouter>,
        {
            route: "/goalsPage",
        });
    expect(history.location.pathname).toEqual(`/goalsPage`);
});

