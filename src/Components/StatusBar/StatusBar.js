import React, { Component } from 'react';
import "./StatusBar.css";
import { Avatar } from '@material-ui/core';
import statusimg from "../../images/dp6.png";

class StatusBar extends Component{
    constructor(props) {
        super(props);
        this.state ={ }
    }
    render() {
        return (
            <div>
            <div className="statusbar__container">
                {
                    this.state.statusList.map((item,index)=>(
                        <div className="status">
                            <Avatar className="statusbar__status" src={statusimg} />
                            <div className="statusbar__text">{item.userName}</div>
                        </div>
                    ))
                }
            </div>
        </div>
        );
    }
}
export default StatusBar;