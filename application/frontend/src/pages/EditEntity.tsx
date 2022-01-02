import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {EntityForm} from "../components/EntityForm";
import {Link} from "react-router-dom";
const token = localStorage.getItem("token")



export function EditEntity() {

    let urlElements = window.location.href.split('/')
    
    let page = urlElements[3];
    let parentType = urlElements[4];
    let entitiType2 = urlElements[5].toLowerCase();
    let parent_id = urlElements[6]; //it is entity_id if edit entity will used
    let entity_id2= urlElements[7]


    const onFinish = (values: any) => {
        console.log('Received values of form: ', values);
        axios.get(`/entities/${entitiType.toLowerCase()}/${entity_id}`,
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
    const {entitiType, entity_id} = useParams();
    const [resources, setResources] = useState("no resource")

    useEffect(() => {
        axios.get(`/entities/${entitiType.toLowerCase()}/${entity_id}`,
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
                setForm(EntityForm(onFinish, entity))
                setLoaded(true)
                setResources(entity.resources)
                console.log(resources)
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
            {parentType == "goal" &&
            <Link to={"/goals/" + parent_id} >
                <button type="button">
                    Return to Goal
                </button>
            </Link>
            }
            {parentType == "groupgoal" &&
            <Link to={"/groupgoals/" + parent_id} >
                <button type="button">
                    Return to Group-Goal
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
                    Return to {parentType}
                </button>
            </Link>
            }
            {message}
        </div>

    );
}

