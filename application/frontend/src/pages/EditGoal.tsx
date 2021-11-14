import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {GoalForm} from "../components/GoalForm";
import {Link} from "react-router-dom";

const jwt = localStorage.getItem("jwt")

export function EditGoal() {
    const onFinish = (values: any) => {
        console.log('Received values of form: ', values);
        values['id'] = goal_id
        axios.post(`/goals/`, values, {
            headers: { Authorization: `Bearer ${jwt}`},
        })
            .then(() => setAdded(true))

    };

    const [isAdded, setAdded] = useState(false)
    const [isLoaded, setLoaded] = useState(false)
    const [form, setForm] = useState(GoalForm(onFinish))
    // @ts-ignore
    const {goal_id} = useParams();


    useEffect(() => {
        axios.get(`/goals/${goal_id}`,
            {
                headers: { Authorization: `Bearer ${jwt}`},
                data: {}
            })
            .then(response => {
                // check for error response
                if (response.status === 200) {
                    return response.data
                }
                throw response
            })
            .then(goal => {
                setForm(GoalForm(onFinish, goal))
                setLoaded(true)
                console.log('Burada!')
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }, [goal_id, onFinish])

    let message;
    if (!isLoaded) {
        return <h2>Loading...</h2>
    }
    if (isAdded) {
        message = <h2>Goal Edited Successfully!</h2>
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

