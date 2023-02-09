import React, { Component } from 'react';
import "./MainPage.css";
import Post from '../Post/Post';


class MainPage extends Component {
    constructor(props) {
        super(props); 
        this.state = { 
            postArray:[]
         }
    }

    componentDidMount(){
        this.getPost();
    }

    getPost=()=>{
        let data=[
            {
                "postId":"123456",
                "userName":"anubhav",
                "postImageURL":"https://irixlens.com/new/wp-content/uploads/2018/11/IRX_5473.jpg",
                "timeStamp":"12345",
                "likes":"1234"
            },
            {
                "postId":"123456",
                "userName":"anuragini",
                "postImageURL":"https://irixlens.com/new/wp-content/uploads/2018/11/IRX_5473.jpg",
                "timeStamp":"12345",
                "likes":"1234"
            },
            {
                "postId":"123456",
                "userName":"anirudh",
                "postImageURL":"https://irixlens.com/new/wp-content/uploads/2018/11/IRX_5473.jpg",
                "timeStamp":"12345",
                "likes":"1234"
            }
        ];
                this.setState({postArray: data});
    }

    render() { 
        return ( 
            <div>{
                    this.state.postArray.map((item,index)=>(
                        <Post id={item.postId} userName={item.userName} postImage={item.postImageURL} likes={item.likes} />
                    ))
                }
            </div>
         );
    }
}
 
export default MainPage;