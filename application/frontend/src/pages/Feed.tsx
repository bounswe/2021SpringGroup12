import * as React from "react";
import axios from "axios";
import { List, Typography, Divider, Avatar } from 'antd';
import { Link } from "react-router-dom";

export function Feed() {

    const user_id = localStorage.getItem("user_id")
    const token = localStorage.getItem("token")


    type FeedElement = {
        actor: {
            type: string,
            name: string
        },
        createdAt: string,
        object: string | null,
        objectschema: {
            content: string,
            id: number,
            name: string,
            type: string,
            url: string
        } | null,
        origin: {
            id: number,
            name: string,
            type: string
        } | null,
        summary: string,
        target: {
            content: string,
            id: 0,
            name: string,
            type: string,
            url: string
        } | null,
        type: string
    }

    type FeedElement_short = {
        summary: string,
        actor_name: string,
        url: string,
        target_id: number,
        target_name:string
    }

    const initialFeedData: FeedElement_short[] = []

    const [feedData, setFeedData] = React.useState(initialFeedData);
    const [summaries, setSummary] = React.useState([""])
    const [actor_names, setActor_Names] = React.useState([""])
    const [urls, setUrls] = React.useState([""])
    //const [feedData, setFeedData] = React.useState([])

    React.useEffect(() => {
        axios
            .get(`/activitystreams/${user_id}`, {
                headers: { Authorization: `Bearer ${token}` },
                data: {},
            })
            .then((response) => {
                // check for error response
                if (response.status === 200) {
                    let data = response.data

                    let tmp_Feed_Data = []

                    for (let i = 0; i < data.length; i++) {
                        var tmp_Feed_Datum = { summary: "", actor_name: "", url: "", target_id: -1 ,target_name: ""}
                        tmp_Feed_Datum.summary = data[i].summary
                        tmp_Feed_Datum.actor_name = data[i].actor.name
                        tmp_Feed_Datum.url = data[i].objectschema.url
                        tmp_Feed_Datum.target_id = data[i].objectschema.id
                        tmp_Feed_Datum.target_name= data[i].objectschema.type
                        tmp_Feed_Data.push(tmp_Feed_Datum)

                        /*tmp_actor_names.push(data[i].actor.name)
                        tmp_summaries.push(data[i].summary)
                        tmp_urls.push(data[i].objectschema.url)
                        tmp_target_ids.push(data[i].objectschema.id)*/
                    }


                    setFeedData(tmp_Feed_Data)
                    console.log(feedData)
                }
            })
            .catch((error) => {
                console.error("There was an error!", error);
            });
    }, []);

    return (

        <List
            itemLayout="horizontal"
            dataSource={feedData}
            renderItem={item => (
                <List.Item>
                    <List.Item.Meta
                        title={<a>{item.actor_name}</a>}
                        description={
                            <div>
                                <b>{item.summary}</b>
                                <Link to={item.url.substring(3)}>Go to {item.target_name}</Link>
                            </div>
                        }
                    />
                </List.Item>
            )}
        />

    )

}