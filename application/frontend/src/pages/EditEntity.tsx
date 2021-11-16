import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {EntityForm} from "../components/EntityForm";
import {Link} from "react-router-dom";

const token = localStorage.getItem("token")



export function EditEntity() {



    const onFinish = (values: any) => {
        console.log('Received values of form: ', values);
        axios.get(`/entities/entiti/${entity_id}`,
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
        .then(entity => {
            entity['title']=values['title']
            entity['description']=values['description']
            console.log(entity)
            axios.put(`/entities/${entity['entitiType'].toLowerCase()}/${entity_id}`, entity, {
                headers: { Authorization: `Bearer ${token}`},
            })
                .then(() => setAdded(true)) 
        })
  //id lazım

    };

    const [isAdded, setAdded] = useState(false)
    const [isLoaded, setLoaded] = useState(false)
    const [form, setForm] = useState(EntityForm(onFinish))
    // @ts-ignore
    const {entity_id} = useParams();


    useEffect(() => {
        axios.get(`/goals/${entity_id}`,
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
            .then(entity => {
                setForm(EntityForm(onFinish, entity, true))
                setLoaded(true)
                console.log('Burada!')
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }, [])



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

