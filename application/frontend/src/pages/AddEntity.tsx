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
    const {parentType,entitiType,parent_id} = useParams();

    const onFinish = (values: any) => {
        console.log('Received values of form: ', parent_id);
        values['parent_id'] = parseInt(parent_id) //for now, it adds entity to a goal
        values['parentType'] = parentType.toUpperCase()
        values["deadline"]=values["deadline"].toDate()
        console.log("he: "+ JSON.stringify(values))
        axios.post(`/entities/${entitiType}`, values,  {
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
            {parentType == "goal" &&
            <Link to={"/goal/" + parent_id} >
                <button type="button">
                    Return to Goal
                </button>
            </Link>
            }
            {parentType == "subgoal" &&
            <Link to={"/subgoal/" + parent_id} >
                <button type="button">
                    Return to SubGoal
                </button>
            </Link>
            }
            {(parentType == "question" || parentType == "reflection"
             || parentType == "task" || parentType == "routine") &&
            <Link to={`/entity/${parentType}/${parent_id}/` + parent_id} >
                <button type="button">
                    Return to SubGoal
                </button>
            </Link>
            }
            {message}
        </div>

    );
    
    if (isSubmitted) {
        return <h2>...</h2>
    }
}

