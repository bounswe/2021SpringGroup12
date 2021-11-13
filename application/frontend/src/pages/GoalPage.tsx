import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";

export function GoalPage() {
    const [goal, setGoal] = useState(
        {
            name: 'Loading',
            description: 'Loading'
    })
    // @ts-ignore
    const {goal_name} = useParams();
    useEffect(() => {
        fetch(`http://localhost:5002/goal/${goal_name}`)
            .then(response => {
                // check for error response
                if (response.ok) {
                    return response.json()
                }
                throw response
            })
            .then(data => setGoal(data['data']))
            .catch(error => {
                console.error('There was an error!', error);
            })
    });
    return (<div>
                <h2>Name: {goal['name']}</h2>
                <h2>Description: {goal['description']}</h2>
            </div>)
}

