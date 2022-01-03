import * as React from "react";
import {useParams} from "react-router";
import {useEffect, useState} from "react";
import axios from "axios";
import {Button} from "antd";

const token = localStorage.getItem("token");
const user_id = localStorage.getItem("user_id")


export function ProfilePage() {

    const [target_info, setTarget] = useState({
        followerCount: 0,
        followingCount: 0,
        username: 'Loading...'
    })
    const [isFollowing, setFollowing] = useState(false)
    // @ts-ignore
    const {target_id} = useParams();

    useEffect(() => {
        axios.get(`/users/get/${target_id}/`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            })
            .then(response => {
                if (response.status === 200) {
                    return response.data
                }
                throw response
            })
            .then(result => {
                setTarget({
                    followerCount: result.followerCount,
                    followingCount: result.followingCount,
                    username: result.userCredentials.username
                })
            })

        axios.get(`/users/${user_id}/followings`,
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            })
            .then(response => {
                if (response.status === 200) {
                    return response.data
                }
                throw response
            })
            .then(result => {
                setFollowing(result.map((user_object: any) => user_object.userCredentials.user_id).includes(user_id))
            })
    }, [target_id]);

    const follow = () => {
        axios.post(`/users/${user_id}/follow/${target_id}`,
            {},
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then((res) => {
            if (res && res.status === 200) {
                console.log(res.data)
                window.alert("User is followed successfully.");
                setFollowing(true)
            }
        })
            .catch((error) => {
                window.alert(`A problem occurred while trying to follow the user! \n ${error}`);
            });
    };

    const unfollow = () => {
        axios.post(`/users/${user_id}/unfollow/${target_id}`,
            {},
            {
                headers: { Authorization: `Bearer ${token}`},
                data: {}
            }).then((res) => {
            if (res && res.status === 200) {
                console.log(res.data)
                window.alert("User is unfollowed successfully.");
                setFollowing(false)
            }
        })
            .catch((error) => {
                window.alert(`A problem occurred while trying to unfollow the user! \n ${error}`);
            });
    };


    return (<div>
        <h2>{target_info['username']}</h2>
        <h3>{target_info.followerCount} followers</h3>
        <h3>{target_info.followingCount} followings</h3>
        {user_id !== target_id && !isFollowing &&
        <Button
            type="primary"
            onClick={follow}
        >
            Follow
        </Button>}
        {user_id !== target_id && isFollowing &&
        <Button
            type="primary"
            onClick={unfollow}
        >
            Unfollow
        </Button>}

    </div>)
}



