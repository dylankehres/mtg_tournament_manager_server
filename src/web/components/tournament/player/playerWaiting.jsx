import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import $ from "jquery";
import Pairings from "../pairings";
import PlayerList from "../playerList";
import PlayerMatch from "./playerMatch";

class PlayerWaiting extends Component {
  state = {
    pairings: [],
    matchData: {},
    currPlayer: {
      roomCode: "",
    },
  };

  handleLeaveTmt() {
    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress + "/join",
      type: "DELETE",
      data: this.props.match.params.playerID,
      success: () => {},
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  // async getPlayerData(getPairings) {
  getPlayerData() {
    let waiting = this;

    return $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url:
        this.props.serverAddress + "/join/" + this.props.match.params.playerID,
      type: "GET",
      success: (currPlayer) => {
        if (currPlayer.roomCode === "") {
          alert("Something went wrong. Please try that again.");
        } else {
          waiting.setState({ currPlayer });
          // getPairings();
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error in getPlayerData", status);
      },
    });
  }

  async getPairings() {
    let waiting = this;

    await this.getPlayerData();

    if (waiting.state.currPlayer.roomCode !== "") {
      $.ajax({
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        url:
          this.props.serverAddress +
          "/pairings/" +
          waiting.state.currPlayer.roomCode,
        type: "GET",
        success: (pairings) => {
          waiting.setState({ pairings });
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

  async getPlayerMatch() {
    let waiting = this;
    console.log("Try getPlayerMatch()");
    if (this.props.match.params.playerID !== "") {
      await $.ajax({
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        url:
          this.props.serverAddress +
          "/join/pairings/" +
          this.props.match.params.playerID,
        type: "GET",
        success: (matchData) => {
          if (matchData === "") {
            alert("Something went wrong. Please try that again.");
          } else {
            waiting.setState({ matchData });
            console.log("matchData: ", matchData);
          }
        },
        error: function (jqxhr, status) {
          console.log("Ajax Error in getPlayerMatch", status);
        },
      });
    }
  }

  componentDidMount() {
    // this.getPlayerData(() => this.getPairings());
    this.getPairings();
    this.getPlayerMatch();
  }

  render() {
    if (this.state.pairings.length > 0) {
      return (
        <React.Fragment>
          <PlayerMatch
            currPlayer={this.state.currPlayer}
            matchData={this.state.matchData}
            player1={this.state.player1}
            player2={this.state.player2}
            serverAddress={this.props.serverAddress}
          />
          <Pairings
            pairings={this.state.pairings}
            onGetPairings={this.getPairings}
          />
          <Button
            className="btn btn-success m-2"
            href={`/round/${this.state.currPlayer.id}`}
          >
            Ready
          </Button>
        </React.Fragment>
      );
    } else if (this.state.currPlayer.roomCode !== "") {
      console.log("Room code: ", this.state.currPlayer.roomCode);
      return (
        <div className="m-2">
          <PlayerList
            serverAddress={this.props.serverAddress}
            roomCode={this.state.currPlayer.roomCode}
          />
          <Form>
            <Button
              className="btn btn-danger m-2"
              href="/join"
              onClick={() => this.handleLeaveTmt}
            >
              Leave Tournament
            </Button>
          </Form>
        </div>
      );
    } else {
      return <h2>Loading...</h2>;
    }
  }
}

export default PlayerWaiting;
