import React, { Component } from "react";
import "./css/homepage.css";
// import $ from 'jquery'

import logo from "./images/logo.png";
import con1 from "./images/con1.png";
import con2 from "./images/con2.png";
import con3 from "./images/con3.png";
import android_logo from "./images/android_logo.png";
import web_logo from "./images/web_logo.png";
import ie_logo from "./images/ie_logo.png";
import ff_logo from "./images/ff_logo.png";
import ss_logo from "./images/ss_logo.png";

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loginPage: false,
      registerPage: false,
      username: '',
      password: '',
      password2: ''
    };
    this.handleLoginSubmit = this.handleLoginSubmit.bind(this);
    this.handleRegisterSubmit = this.handleRegisterSubmit.bind(this);
    this.passwordChange = this.passwordChange.bind(this);
    this.password2Change = this.password2Change.bind(this);
    this.usernameChange = this.usernameChange.bind(this);
    this.loginClicked = this.loginClicked.bind(this);
    this.registerClicked = this.registerClicked.bind(this);
    this.postLogin = this.postLogin.bind(this);
    this.cancelLogin = this.cancelLogin.bind(this);
    this.cancelRegister = this.cancelRegister.bind(this);
  }

  postLogin(username, password) {
    this.props.postLogin(username, password)
  }

  usernameChange(event) {
    this.setState({username: event.target.value})
  }

  passwordChange(event) {
    this.setState({password: event.target.value})
  }

  password2Change(event) {
    this.setState({password2: event.target.value})
  }

  handleLoginSubmit(event) {
    event.preventDefault();
    this.postLogin(this.state.username, this.state.password)
  }

  handleRegisterSubmit(event) {
    event.preventDefault();
    this.props.postRegister(this.state.username, this.state.password)
    
  }

  loginClicked() {
    this.setState({loginPage: true})
  }
  registerClicked() {
    this.setState({registerPage: true})
  }
  cancelLogin() {
    this.setState({loginPage: false})
  }
  cancelRegister() {
    this.setState({registerPage: false})
  }

  render() {
    return (
      <div>
        <div className="logo">
          <a href="/">
            <img src={logo} />
          </a>
        </div>
        <div id="loginbar">
          <h3>
            <div id="homepage">
              {" "}
              &nbsp;&nbsp;<a href="/">Home</a>&nbsp;&nbsp;&nbsp;&nbsp;{" "}
            </div>
            <div id="login">
              &nbsp;&nbsp;&nbsp;&nbsp; <a onClick={this.loginClicked}>Log In</a> &nbsp;&nbsp;&nbsp;&nbsp;
            </div>
            <div id="register">
              &nbsp;&nbsp;&nbsp;&nbsp; <a onClick={this.registerClicked}>Register</a> &nbsp;&nbsp;&nbsp;&nbsp;{" "}
            </div>
          </h3>
        </div>

        <div className="pic">
          <ul>
            <li id="li1">
              <img src={con1} />
              <div className="nav" />
              <div className="bar">
                <p />
              </div>
            </li>
            <li id="li2">
              <img src={con2} />
              <div className="nav" />
              <div className="bar">
                <p />
              </div>
            </li>
            <li id="li3">
              <img src={con3} />
              <div className="nav" />
              <div className="bar">
                <p />
              </div>
            </li>
          </ul>
        </div>
        <div id="login-dowload">
        <img className="android_logo" src={android_logo} />{" "}
        <img className="web_logo" src={web_logo} />{" "}
        <img className="ie_logo" src={ie_logo} />{" "}
        <img className="ff_logo" src={ff_logo} />{" "}
        <img className="ss_logo" src={ss_logo} />{" "}
        </div>
        <div id="tips">
          <p> One safe place for all your files.</p>
        </div>
        {this.state.loginPage == true &&
          <div id="loginForm">
            <form onSubmit={this.handleLoginSubmit}>
              <label>
                Username:
                <input type="text" value={this.state.username} onChange={this.usernameChange} required/>
              </label>
              <label>
                Password:
                <input type="password" value={this.state.password} onChange={this.passwordChange} required/>
              </label>
              <input type="submit" value="Submit" />
              <button className="cancelbtn" onClick={this.cancelLogin} type="button">Cancel</button>
            </form>
          </div>
        }

        {this.state.registerPage == true &&
          <div id="loginForm">
            <form onSubmit={this.handleRegisterSubmit}>
              <label>
                Username:
                <input type="text" value={this.state.username} onChange={this.usernameChange} required/>
              </label>
              <label>
                Password:
                <input type="password" value={this.state.password} onChange={this.passwordChange} required/>
              </label>
              <label>
                Input again:
                <input type="password" value={this.state.password2} onChange={this.password2Change} required/>
              </label>
              {this.state.password != this.state.password2 &&
                <p id="warn">Password not match</p>
              }
              <input type="submit" value="Submit" disabled={this.state.password != this.state.password2}/>
              <button className="cancelbtn" onClick={this.cancelRegister} type="button">Cancel</button>
            </form>
          </div>
        }
      </div>
    );
  }
}

export default Home;
