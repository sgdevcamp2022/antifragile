import { Avatar } from '@material-ui/core';
import React, { Component } from 'react';
import "./StatusBar.css";
import statusimg from "../../images/pp1.png"
import uploadImage from "../../images/statusadd.png";


class StatusBar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            statusList: []
        }
    }
    componentDidMount() {
        this.getData();
    }

    getData = () => {
        let data = [
            {
                "username": "KyuminLee",
                "imageURL": "../../images/pp1.png"
            },
            {
                "username": "testing",
                "imageURL": "../../images/pp1.png"
            },
            {
                "username": "testing",
                "imageURL": "../../images/pp1.png"
            },
            {
                "username": "testing",
                "imageURL": "../../images/pp1.png"
            },
            {
                "username": "testing",
                "imageURL": "../../images/pp1.png"
            },
            {
                "username": "testing",
                "imageURL": "../../images/pp1.png"
            },
            {
                "username": "testing",
                "imageURL": "../../images/pp1.png"
            },
            {
                "username": "testing",
                "imageURL": "../../images/pp1.png"
            },
            {
                "username": "testing",
                "imageURL": "../../images/pp1.png"
            },
            {
                "username": "testing",
                "imageURL": "../../images/pp1.png"
            }
        ]
        this.setState({ statusList: data });
    }

    render() {
        return (
            <div>
                <div className='statusbar__container'>
                    <img src={uploadImage} className='statusbar__upload' width="55px" height="55px" />
                    {
                        this.state.statusList.map((item, index) => (
                            <div className='status'>
                                <Avatar className='statusbar__status' src={statusimg} />
                                <div className='statusbar__text'>{item.username}</div>
                            </div>
                        ))
                    }
                </div>
            </div>
        );
    }
}

export default StatusBar;