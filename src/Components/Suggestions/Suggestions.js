import { Avatar } from '@material-ui/core';
import React, { Component } from 'react';
import "./Suggestions.css";
import imageSrc from "../../images/pp1.png"

class Sugesstions extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }
    render() {
        return (
            <div>
                <div className='suggestions__container'>
                    <div className='suggestions__header'>
                        <div>Suggestions For You</div>
                    </div>
                    <div className='suggestions__body'>
                        <div className='suggestions__friends'>
                            <Avatar src={imageSrc} className='suggestions__image' />
                            <div className='suggestions__username'>Friends 1</div>
                        </div>
                        <div className='suggestions__friends'>
                            <Avatar src={imageSrc} className='suggestions__image' />
                            <div className='suggestions__username'>Friends 1</div>
                        </div>
                        <div className='suggestions__friends'>
                            <Avatar src={imageSrc} className='suggestions__image' />
                            <div className='suggestions__username'>Friends 1</div>
                        </div>
                        <div className='suggestions__friends'>
                            <Avatar src={imageSrc} className='suggestions__image' />
                            <div className='suggestions__username'>Friends 1</div>
                        </div>
                        <div className='suggestions__friends'>
                            <Avatar src={imageSrc} className='suggestions__image' />
                            <div className='suggestions__username'>Friends 1</div>
                        </div>
                        <div className='suggestions__friends'>
                            <Avatar src={imageSrc} className='suggestions__image' />
                            <div className='suggestions__username'>Friends 1</div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Sugesstions;