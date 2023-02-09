import React, { Component } from 'react';
import "./Post.css";
import { Avatar } from '@material-ui/core';
import postimage from "../../images/post.jpg"; 
import love from "../../images/love.svg"; 
import comment from "../../images/comment.svg"; 
import share from "../../images/share.svg"; 


class Post extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            commentList:[]
         }
    }

    componentDidMount(){
        this.getComments();
    }

    getComments=()=>{ 
        let data=[
            {
                "username": "Anubhav",
                "commentId":"1234",
                "timeStamp":"123456",
                "description":"Comment 1"
            },
            {
                "username": "anuragini",
                "commentId":"1234",
                "timeStamp":"123456",
                "description":"Comment 2"
            },
            {
                "username": "anirudh",
                "commentId":"1234",
                "timeStamp":"123456",
                "description":"Comment 3"
            }
        ];
                this.setState({commentList: data});
    }
  
    render() { 
        return ( 
        <div className="post__container">
            {/* Header */}
          <div className="post__header">
                <Avatar className="post__image" src="" />
                <div className="post__username">{this.props.userName}</div>
          </div>

          {/* Image */}
          <div>
              <img src={this.props.postImage} width="615px" /> 
          </div>

          {/* Analytics */}
          <div>
              <div style={{"marginLeft":"10px"}}>
                  <img src={love} className="post_reactimage"/>
                  <img src={comment} className="post_reactimage"/>
                  <img src={share} className="post_reactimage"/>
              </div>
              <div style={{ "fontWeight":"bold","marginLeft":"20px  "}}>
                  {this.props.likes} likes     
              </div>
          </div>

          {/* Comment Section */}
          <div>
              {
                  this.state.commentList.map((item,index)=>(
                        <div className="post_comment">{item.userName}: {item.description}</div>
                  ))
              }
              <input text="text" className="post__commentbox" placeholder="Add a comment..." />
          </div>
          
        </div> 
        );
    }
}
 
export default Post;