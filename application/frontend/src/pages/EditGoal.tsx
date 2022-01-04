import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {GoalForm} from "../components/GoalForm";
import {Link} from "react-router-dom";
import {GoalTypes} from "../helpers/GoalTypes";

const token = localStorage.getItem("token");

export function EditGoal(params :{goalType: any}) {
    const goalType = params.goalType
    const [goal, setGoal] = useState({} as any)
    const [isAdded, setAdded] = useState(false)
    const [isLoaded, setLoaded] = useState(false)
    const [returnLink, setReturnLink] = useState("/goalsPage")
    // @ts-ignore
    const {goal_id} = useParams();

    const onFinish = (values: any) => {
        let put_obj = {} as any
        for (let property in goal) {
            put_obj[property] = goal[property]
        }
        for (let property in values) {
            put_obj[property] = values[property]
        }
        console.log('Received values of form: ', values);
        console.log('Sending object of form: ', put_obj);
        axios.put(`/${goalType}/`, put_obj, {
            headers: { Authorization: `Bearer ${token}`},
        })
            .then(() => setAdded(true))
    };
    const [form, setForm] = useState(GoalForm(onFinish))

    useEffect(() => {
        axios.get(`/${goalType}/${goal_id}`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            })
            .then(response => {
                // check for error response
                if (response.status === 200) {
                    return response.data
                }
                throw response
            })
            .then(goal_info => {
                setForm(GoalForm(onFinish, goal_info))
                setGoal(goal_info)
                if (goal_info.main_goal_id != null) {
                    setReturnLink(`/${GoalTypes.Normal}/${goal_info.main_goal_id}`)
                } else if (goal_info.main_groupgoal_id != null) {
                    setReturnLink(`/${GoalTypes.Group}/${goal_info.main_groupgoal_id}`)
                } else if (goal_info.parent_subgoal_id != null) {
                    setReturnLink(`/${GoalTypes.Sub}/${goal_info.parent_subgoal_id}`)
                }
                setLoaded(true)
                console.log('Burada!')
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }, [goal_id, goalType])

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
            <Link to={returnLink}>
                <button type="button">
                    Return
                </button>
            </Link>
            {message}
        </div>

    );
}

