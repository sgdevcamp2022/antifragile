import React, { Component } from "react";
import './LoginPage.css'
import Grid from '@material-ui/core/Grid';
import inst_image from '../../images/9364675fb26a.svg';
import insta_logo from '../../images/logoinsta.png';
import fb from '../../images/fb.png';
import appstore from '../../images/app.png';
import playstore from '../../images/play.png';

class LoginPage extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }
    render() {
        return(
            <div>
                <Grid container>
                    <Grid item xs={3}>
                    </Grid>
                    <Grid item xs={6}>
                       <div className="loginpage__main">
                           <div>
                               <img src={inst_image} width="450px" />
                           </div>
                          <div>
                               <div className="loginpage_rightcomponent">
                                   <img className="loginpage__logo" src={insta_logo} />
                                   <div className="loginPage__signin">
                                        <input className="loginpage__text" type="text" placeholder="username"/>
                                        <input className="loginpage__text" type="password" placeholder="Password"/>
                                        <button className="login__button">Sign In</button>
{/* 
                                     {
                                         this.state.isLogin ? <SignIN/> : <SignUp/>
                                     } */}

                                        <div className="login__ordiv">
                                            <div className="login__dividor"></div>
                                            <div className="login__or">OR</div>
                                            <div className="login__dividor"></div>
                                        </div>

                                        <div className="login__fb">
                                            <img src={fb} width="15px" style={{ "marginRight":"5px" }} />Log in with Facebook
                                        </div>
                                        <div className="login_forgt"> Forgot password?</div>
                                   </div>
                               </div>

                                <div className="loginpage__signupoption">
                                    {
                                        this.state.isLogin ?
                                        <div className="loginPage__signin">
                                                 Don't have an account? <span onClick={this.changeLogin} style={{ "fontWeight":"bold", "color":"#0395F6"}}>Sign up</span>
                                        </div> :
                                        <div className="loginPage__signup">
                                                Have an account? <span onClick={this.changeLogin}  style={{ "fontWeight":"bold", "color":"#0395F6"}}>Sign in</span>
                                        </div>
                                    } 
                                </div>

                                <div className="loginPage__downloadSection">
                                    <div>
                                    Get the app.
                                    </div>
                                    <div className="loginPage__option">
                                        <img className="loginPage_dwimg" src={appstore} width="136px" />
                                        <img className="loginPage_dwimg" src={playstore} width="136px" />
                                    </div>
                                </div>
                           </div>
                       </div>
                    </Grid>
                    <Grid item xs={3}>
                    </Grid>
                </Grid>
            </div>
        )
    }
}
export default LoginPage
