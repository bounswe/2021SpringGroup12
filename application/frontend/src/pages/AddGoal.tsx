import * as React from "react";
import { Link } from 'react-router-dom';
import {GoalForm} from '../components/GoalForm'
import axios from "axios";


export class AddGoal extends React.Component {
    state = { isAdded: false};
    user_id = localStorage.getItem("user_id")
    token = localStorage.getItem("token");

    onFinish = (values: any) => {
        console.log('Received values of form: ', values);
        values['goalType'] = 'GOAL'
        axios.post(`/goals/${this.user_id}`, values, {
            headers: { Authorization: `Bearer ${this.token}`},
        }).then(() => this.setState({ isAdded: true }))
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
