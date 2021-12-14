import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {Button, Input, Space, Table, Tag} from "antd";


export function ResourcePage() {

    let {resource_id} = useParams<{resource_id: string }>();
    const [display, setImage] = useState()
    const [imgUrl, setImgUrl] = useState("");
    console.log("resource_id: " + resource_id)
    useEffect(() => {
        axios.get(`/resources/${resource_id}`,
            {
                headers: { Authorization: `Bearer`},
                data: {},
            })
            .then(response => {
                // check for error response
                console.log("response: " , response)
                if (response.status === 200) {
                    return response.data
                }
                throw response
            })
            .then(image => {
                setImage(image)
                console.log(image)
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }, []);


    return (
        <div>
            <img src={display} alt="" />
        </div>)
}

