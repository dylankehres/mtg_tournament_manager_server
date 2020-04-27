import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

import Home from "./components/home";
import Tournament from "./components/tournament/tournament";
import Pairings from "./components/tournament/pairings";
import JoinTmt from "./components/tournament/player/joinTmt";
import PlayerWaiting from "./components/tournament/player/playerWaiting";
import PlayerList from "./components/tournament/playerList";
import HostTmt from "./components/tournament/host/hostTmt";
import StartTmt from "./components/tournament/host/startTmt";

class Root extends Component {
  state = { serverAddress: "http://localhost:8080/api/v1/tournament" };
  render() {
    return (
      <Router>
        <Switch>
          <Route exact path="/" component={Home} />
          <Route exact path="/join">
            <JoinTmt serverAddress={this.state.serverAddress} />
          </Route>
          <Route exact path="/tournament" component={Tournament} />
          <Route path="/join/waiting" component={PlayerWaiting} />
          <Route exact path="/host">
            <HostTmt serverAddress={this.state.serverAddress} />
          </Route>
          <Route path="/host/waiting" component={StartTmt} />
          <Route path="/host/pairings" component={Pairings} />
          <Route path="/playerlist" component={PlayerList} />
        </Switch>
      </Router>
    );
  }
}

export default Root;
