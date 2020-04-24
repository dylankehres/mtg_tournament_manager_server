import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import JoinTmt from "./player/joinTmt";
import PlayerWaiting from "./player/playerWaiting";
import PlayerList from "./playerList";
import HostTmt from "./host/hostTmt";
import StartTmt from "./host/startTmt";
import Home from "../home";

class Tournament extends Component {
  state = {
    path: "",
    playerList: [],
  };

  constructor() {
    super();
    this.state.path = "/join";
    this.state.playerList = [
      {
        id: 1,
        userName: "Dylan",
        roomCode: "TitanEDH",
        selectedFormat: "Commander",
      },
      {
        id: 2,
        userName: "Matt",
        roomCode: "TitanEDH",
        selectedFormat: "Commander",
      },
      {
        id: 3,
        userName: "Preston",
        roomCode: "TitanEDH",
        selectedFormat: "Commander",
      },
    ];
  }

  handleJoinTmt = (form) => {
    form.id = this.state.playerList.length;
    const playerList = this.state.playerList.push(form);
    this.setState({ playerList });
    console.log("Player list: ", this.state.playerList);
  };

  handleOpenTmt = (form) => {
    console.log("Open tournament", form);
  };

  render() {
    return (
      <React.Fragment>
        {/* <Form>
          <Button className="btn btn-primary m-2" href="/join">
            Join Tournament
          </Button>
          <Button className="btn btn-primary m-2" href="/host">
            Host Tournament
          </Button>
        </Form> */}
        <Router>
          <Switch>
            <Route exact path="/" component={Home} />
            <Route exact path="/join">
              <JoinTmt
                onJoinTmt={this.handleJoinTmt}
                playerList={this.state.playerList}
              />
            </Route>
            <Route path="/join/waiting">
              <PlayerWaiting playerList={this.state.playerList} />
            </Route>
            <Route exact path="/host">
              <HostTmt onOpenTmt={this.handleOpenTmt} />
            </Route>
            <Route path="/host/waiting">
              <StartTmt playerList={this.state.playerList} />
            </Route>
            <Route path="/playerlist">
              <PlayerList playerList={this.state.playerList} />
            </Route>
          </Switch>
        </Router>
      </React.Fragment>
    );
  }
}

export default Tournament;
