import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import PlayerList from "../playerList";
import $ from "jquery";

class PlayerWaiting extends Component {
  state = {
    roomCode: "",
  };

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
          console.log("Redirected to player waiting page");
          this.setState({ roomCode: player.roomCode });
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  render() {
    if (this.state.roomCode === "") {
      return <h2>Loading...</h2>;
    } else {
      return (
        <div className="m-2">
          <PlayerList
            serverAddress={this.props.serverAddress}
            roomCode={this.state.roomCode}
          />
          <Form>
            <Button className="btn btn-danger m-2" href="/join">
              Leave Tournament
            </Button>
          </Form>
        </div>
      );
    }
  }
}

export default PlayerWaiting;
