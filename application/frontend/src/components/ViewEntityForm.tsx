import { Typography, Divider, Button, Radio } from 'antd';
import React from 'react';
import ReactDOM from 'react-dom';
const { Title, Paragraph, Text, Link } = Typography;
  

export const ViewEntityForm = () => {

  return (
    <Typography>
    <Title>Entity Name</Title> 
    <Title level={3}>Description</Title>
    <Paragraph>        
      Description will be here 
    </Paragraph>
    
    <Button type="dashed" size = "large">
        Create Sub-Goal
    </Button>
    <br/>
    <br/>
    <Button type="dashed" size = "large">
        Create Task
    </Button>
    <br/>
    <br/>
    <Button type="dashed" size = "large">
        Create Routine
    </Button>
    <br/>
    </Typography>
  );
};
