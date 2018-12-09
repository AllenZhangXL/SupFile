import React, { Component } from "react";
import "./App.css";

import Home from "./Home";
import Files from "./Files";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      user: null,
      login: false,
      loginPage: false,
      filePage: false,
      fileInfoPage: false
    };
    this.saveUser = this.saveUser.bind(this)
  }

  saveUser(user) {
    localStorage.setItem('user', this.state.user)
  }

  postRegister = (username, password) => (
    fetch("http://api.supfile.io/WebApi/rest/user/register", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        username: username,
        password: password
      })
    })
      .then(res => res.json())
      .then(data => {
        if (data.status == 200) {
          let user = {
            "id": data.id,
            "username": data.username,
            "availableSpace": data.availableSpace
          }
          this.setState({user: user, login: true}, this.saveUser(this.state.user))
        }
      })
  )

  postLogin = (username, password) => (
    fetch("http://api.supfile.io/WebApi/rest/user/login", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        username: username,
        password: password
      })
    })
      .then(res => res.json())
      .then(data => {
        if (data.status == 200) {
          let user = {
            "id": data.id,
            "username": data.username,
            "availableSpace": data.availableSpace
          }
          this.setState({user: user, login: true}, this.saveUser(this.state.user))
        }
      })
  )

  logout() {
    this.setState({login: false})
    this.setState({user: null})
  }

  componentWillMount() {
    let user = JSON.parse(localStorage.getItem('user'))
    if(user !== null && user !== "") {
      this.setState({user: user})
      this.setState({login: true})
    }
  }

  render() {
    return <div>
    {this.state.login ? <Files logout={this.logout.bind(this)} user={this.state.user}/> : <Home postRegister={this.postRegister.bind(this)} postLogin = {this.postLogin.bind(this)}/>}
    </div>;
  }
}

export default App;
