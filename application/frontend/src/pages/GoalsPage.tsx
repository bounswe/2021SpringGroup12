import {Button, Space, Table} from 'antd';
import * as React from "react";
import { Link } from 'react-router-dom';
export class GoalsPage extends React.Component {
    state = {
        isLoaded: false,
        goals: [],
        deletedCount: 0
    };

    deleteGoal = (record: { name: any; }) => {
        console.log('Received values of delete: ', record);
        const requestOptions = {
            method: 'POST',
            headers: { 'Accept': 'application/json',
                'Content-Type': 'application/json' },
            body: JSON.stringify({'name': record.name})
        };
        fetch('http://localhost:5002/delete_goal', requestOptions).then(() => {this.getGoals()})

    };

    getGoals() {
        fetch('http://localhost:5002/goals')
            .then(response => {
                // check for error response
                if (response.ok) {
                    return response.json()
                }
                throw response
            })
            .then(data => {
                let tmp = []
                for (let i = 0; i < data['data'].length; i++) {
                    tmp.push({
                        key: i.toString(),
                        name: data['data'][i]['name'],
                        description: data['data'][i]['description']
                    })
                }
                this.setState({
                    isLoaded: true,
                    goals: tmp
                })

                console.log('Success!')
            })
            .catch(error => {
                console.error('There was an error!', error);
            });
    }

    componentDidMount() {
        this.getGoals()
    }

    columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            render: (text: any) => <Link to={"/goal/" + text}> {text} </Link>
            ,
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Action',
            key: 'action',
            render: (text: any,
                     record: { name: string | number | boolean | {} | React.ReactElement<any, string | React.JSXElementConstructor<any>> | React.ReactNodeArray | React.ReactPortal | null | undefined; }) =>
                (<Space size="middle">
                        <Button type="primary" onClick={() => this.deleteGoal(record)}>
                            Delete
                        </Button>
                    </Space>
                ),
        },
    ];

    render() {
        const { isLoaded,
            goals,
        } = this.state;

        if (!isLoaded) {
            return <div>Loading...</div>;
        }

        console.log('goals', goals)

        return (
            <div>
                <Table columns={this.columns} dataSource={[...goals]} />
                <Link to="/addGoal">
                    <button type="button">
                        Add Goal
                    </button>
                </Link>
            </div>

        )

    }
}
