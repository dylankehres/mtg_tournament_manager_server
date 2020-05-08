import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import $ from "jquery";
import Pairings from "../pairings";
import PlayerList from "../playerList";
import PlayerMatch from "./playerMatch";

class PlayerWaiting extends Component {
  state = {
    pairings: [],
    match: {},
    player: {},
  };

  handleLeaveTmt() {
    console.log("Leave tournament");

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress + "/join",
      type: "DELETE",
      data: JSON.stringify(this.props.match.params.playerID),
      success: () => {},
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  async getPlayerData() {
    let waiting = this;
    await $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url:
        this.props.serverAddress + "/join/" + this.props.match.params.playerID,
      type: "GET",
      success: (player) => {
        if (player.roomCode === "") {
          alert("Something went wrong. Please try that again.");
        } else {
          waiting.setState({ player });
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error in getPlayerMatch", status);
      },
    });
  }

  getPairings() {
    let waiting = this;
    console.log("Try getPairings()");
    if (waiting.state.player.roomCode !== "") {
      $.ajax({
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        url:
          this.props.serverAddress +
          "/pairings/" +
          waiting.state.player.roomCode,
        type: "GET",
        success: (data) => {
          waiting.setState({ pairings: data });
          // console.log("Pairings: ", waiting.state.pairings);
        },
        error: function (jqxhr, status) {
          console.log(
            "Ajax Error in getPairings for playerWaiting.jsx",
            status
          );
        },
      });
    }
  }

  getPlayerMatch() {
    let waiting = this;
    console.log("Try getPlayerMatch()");
    if (this.props.match.params.playerID !== "") {
      $.ajax({
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        url:
          this.props.serverAddress +
          "/join/pairings/" +
          this.props.match.params.playerID,
        type: "GET",
        success: (match) => {
          if (match === "") {
            alert("Something went wrong. Please try that again.");
          } else {
            waiting.setState({ match });
            console.log("Match", waiting.state.match);
          }
        },
        error: function (jqxhr, status) {
          console.log("Ajax Error in getPlayerMatch", status);
        },
      });
    }
  }

  componentDidMount() {
    this.getPlayerData().then(() => this.getPairings());
    // this.getPairings();
    this.getPlayerMatch();
  }

  render() {
    if (this.state.player.roomCode === "") {
      return <h2>Loading...</h2>;
    } else if (this.state.pairings.length > 0) {
      return (
        <React.Fragment>
          <PlayerMatch
            currPlayer={this.state.player}
            match={this.state.match}
          />
          <Pairings
            pairings={this.state.pairings}
            onGetPairings={this.getPairings()}
          />
        </React.Fragment>
      );
    } else {
      return (
        <div className="m-2">
          <PlayerList
            serverAddress={this.props.serverAddress}
            roomCode={this.state.player.roomCode}
          />
          <Form>
            <Button
              className="btn btn-danger m-2"
              href="/join"
              onClick={() => this.handleLeaveTmt()}
            >
              Leave Tournament
            </Button>
          </Form>
        </div>
      );
    }
  }
}

export default PlayerWaiting;
