import * as React from "react";
import { Link } from 'react-router-dom';
import {GoalForm} from '../components/GoalForm'
import axios from "axios";
import {useState} from "react";
import {GoalTypes} from "../helpers/GoalTypes";
import {useParams} from "react-router";

const token = localStorage.getItem("token");
const user_id = localStorage.getItem("user_id")

export function AddGoal(params : {goalType: any, parentType: any}) {
    const [isAdded, setAdded] = useState(false)
    const goalType = params.goalType
    const parentType = params.parentType
    console.log('goalType ', goalType)

    // @ts-ignore
    let {parent_id} = useParams()

    let url = ""
    let values_parent = ""
    let returnLink = ""
    if (goalType === GoalTypes.Sub) {
        url = `/${parentType}`
        returnLink = `/${parentType}/${parent_id}`
        if (parentType === GoalTypes.Sub) {
            values_parent = "parent_subgoal_id"
        } else if (parentType === GoalTypes.Normal) {
            url += "/subgoal"
            values_parent = "main_goal_id"
        } else if (parentType === GoalTypes.Group) {
            url += "/subgoal"
            values_parent = "main_groupgoal_id"
        }
    } else {
        url = `/${goalType}/${user_id}`
        returnLink = "/goalsPage"
    }

    const onFinish = (values: any) => {
        console.log('Received values of form: ', values);
        if (goalType === GoalTypes.Sub) {
            values[values_parent] = parent_id
        }

        axios.post(url, values, {
            headers: { Authorization: `Bearer ${token}`},
        }).then(() => setAdded(true))
    };

    const form = GoalForm(onFinish)

    return (
        <div>
            {form}
            <Link to={returnLink}>
                <button type="button">
                    Return
                </button>
            </Link>
            {isAdded && <h2>Goal Added Successfully!</h2>}
        </div>

    );

}
