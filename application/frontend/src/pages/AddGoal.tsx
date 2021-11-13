import * as React from "react";
import { Link } from 'react-router-dom';
import {GoalForm} from '../components/GoalForm'

export interface ILoginProps {}

export class AddGoal extends React.Component {
    state = { isAdded: false};

    onFinish = (values: any) => {
        console.log('Received values of form: ', values);
        const requestOptions = {
            method: 'POST',
            headers: { 'Accept': 'application/json',
                'Content-Type': 'application/json' },
            body: JSON.stringify(values)
        };
        fetch('http://localhost:5002/add_goal', requestOptions)
            .then(response => response.json())
            .then(() => this.setState({ isAdded: true }));
    };
    render() {
        const form = GoalForm(this.onFinish)
        let message;
        if (this.state.isAdded) {
            message = <h2>Goal Added Successfully!</h2>
        }
        return (
            <div>
                {form}
                <Link to="/goals">
                    <button type="button">
                        Return to Goals
                    </button>
                </Link>
                {message}
            </div>

        );
    }
}
