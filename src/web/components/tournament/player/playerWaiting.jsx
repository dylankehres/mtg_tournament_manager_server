import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import PlayerList from "../playerList";
import $ from "jquery";
import { wait } from "@testing-library/react";
import Pairings from "../pairings";

class PlayerWaiting extends Component {
  state = {
    roomCode: "",
    pairings: [],
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

  waitForPairings() {
    // Send request to have server wait until pairings are posted to send response
    let waiting = this;
    console.log("Waiting for pairings", waiting);

    if (wait.state.roomCode !== "") {
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
        success: (data) => {
          waiting.setState({ pairings: data });
        },
        error: function (jqxhr, status) {
          console.log("Ajax Error", status);
        },
      });
    }
  }

  getPairings() {
    let waiting = this;

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress + "/pairings/" + waiting.state.roomCode,
      type: "GET",
      success: (data) => {
        waiting.setState({ pairings: data });
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error in getPairings for playerWaiting.jsx", status);
      },
    });
  }

  componentDidMount() {
    $.ajax({
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
          this.setState({ roomCode: player.roomCode });
          this.getPairings();
        }
      },
      error: function (jqxhr, status) {
        console.log(
          "Ajax Error in componentDidMount for playerWaiting.jsx",
          status
        );
      },
    });
  }

  render() {
    if (this.state.roomCode === "") {
      return <h2>Loading...</h2>;
    } else if (this.state.pairings.length > 0) {
      return (
        <Pairings
          pairings={this.state.pairings}
          onGetPairings={this.getPairings()}
        />
      );
    } else {
      return (
        <div className="m-2">
          <PlayerList
            serverAddress={this.props.serverAddress}
            roomCode={this.state.roomCode}
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
