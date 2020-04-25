import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

import Home from "./components/home";
import Tournament from "./components/tournament/tournament";
import Pairings from "./components/tournament/pairings";
import JoinTmt from "./components/tournament/player/joinTmt";
import PlayerWaiting from "./components/tournament/player/playerWaiting";
import PlayerList from "./components/tournament/playerList";
import HostTmt from "./components/tournament/host/hostTmt";
import StartTmt from "./components/tournament/host/startTmt";

const Root = () => {
  return (
    <Router>
      <Switch>
        <Route exact path="/" component={Home} />
        <Route exact path="/join" component={JoinTmt} />
        <Route exact path="/tournament" component={Tournament} />
        <Route path="/join/waiting" component={PlayerWaiting} />
        <Route exact path="/host" component={HostTmt} />
        <Route path="/host/waiting" component={StartTmt} />
        <Route path="/host/pairings" component={Pairings} />
        <Route path="/playerlist" component={PlayerList} />
      </Switch>
    </Router>
  );
};

export default Root;
