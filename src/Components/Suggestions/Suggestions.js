import React, { Component } from 'react';
import "./Suggestions.css"
import { Avatar } from '@material-ui/core';
import imageSrc1 from '../../images/pp1.png'
import imageSrc2 from '../../images/pp2.png'
import imageSrc3 from '../../images/pp3.jpeg'

class Suggestions extends Component {   
    constructor(props) {
        super(props);
        this.state = {  }
    }
    render() { 
        return ( 
        <div>
            <div className="suggestions__container">
                <div className="suggestions__header">
                    <div>Suggestions For You</div>
                </div>
                <div className="suggestions__body">
                    <div className="suggestions__friends">
                        <Avatar src={imageSrc1} className="suggestions__image"/>
                        <div className="suggestions__username">Anubhav</div>
                    </div>
                    <div className="suggestions__friends">
                        <Avatar src={imageSrc2} className="suggestions__image"/>
                        <div className="suggestions__username">Mayank</div>
                    </div>
                    <div className="suggestions__friends">
                        <Avatar src={imageSrc3} className="suggestions__image"/>
                        <div className="suggestions__username">Anjali</div>
                    </div>
                    <div className="suggestions__friends">
                        <Avatar src={imageSrc2} className="suggestions__image"/>
                        <div className="suggestions__username">Kaushal</div>
                    </div>
                    <div className="suggestions__friends">
                        <Avatar src={imageSrc3} className="suggestions__image"/>
                        <div className="suggestions__username">Anirudh</div>
                    </div>
                </div>
            </div>
        </div> );
    }
}
 
export default Suggestions;