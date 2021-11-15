import * as React from "react";

export interface IHomeProps {
    user: string
}

export interface IHomeState {
}

export default class Home extends React.Component<IHomeProps, IHomeState> {
  constructor(props: IHomeProps) {
    super(props);

    this.state = {
    };
  }

  

  public render() {
      if(this.props.user){
          return <h1>You logged in user: {this.props.user} !!</h1>
      }else{
        return <h1>No user home page</h1>;
      }
    
  }
}
