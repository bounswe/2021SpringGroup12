import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Space, Table, Tag} from "antd";
import { LinkEntityForm } from "../components/LinkEntityForm";

const token = localStorage.getItem("token")

export function LinkEntity() {


    const [isSubmitted, setSubmitted] = useState(false)
    // @ts-ignore
    const {entity_id} = useParams();

    const onFinish = (values: any) => {
        console.log('Received values of form: ', values);
        values['entity_id'] = entity_id
        axios.post(`/entities/${values['entityType'].toLowerCase()}`, values,  {
            headers: { Authorization: `Bearer ${token}`},
        }).then(() => setSubmitted(true))
    };

    const form = LinkEntityForm(onFinish)
    let message;
    if (isSubmitted) {
        message = <h2>Entity Added Successfully!</h2>
    }
    return (
        <div>
            {form}
            <Link to={"/entity/" + entity_id} >
                <button type="button">
                    Return to Entity
                </button>
            </Link>
            {message}
        </div>

    );
    
    if (isSubmitted) {
        return <h2>...</h2>
    }
}

