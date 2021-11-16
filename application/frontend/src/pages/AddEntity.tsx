import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Space, Table, Tag} from "antd";
import { EntityForm } from "../components/EntityForm";

const token = localStorage.getItem("token")

export function AddEntity() {

    const [isSubmitted, setSubmitted] = useState(false)
    // @ts-ignore
    const {goal_id} = useParams();

    const onFinish = (values: any) => {
        console.log('Received values of form: ', values);
        values['mainGoal_id'] = goal_id
        console.log(values)
        axios.post(`/entities/${values['entityType'].toLowerCase()}`, values,  {
            headers: { Authorization: `Bearer ${token}`},
        }).then(() => setSubmitted(true))
    };

    const form = EntityForm(onFinish)
    let message;
    if (isSubmitted) {
        message = <h2>Entity Added Successfully!</h2>
    }
    return (
        <div>
            {form}
            <Link to={"/goal/" + goal_id} >
                <button type="button">
                    Return to Goal
                </button>
            </Link>
            {message}
        </div>

    );
    
    if (isSubmitted) {
        return <h2>...</h2>
    }
}

