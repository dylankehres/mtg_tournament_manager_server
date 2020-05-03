import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import PlayerList from "../playerList";
import $ from "jquery";

class StartTmt extends Component {
  state = {
    roomCode: "",
  };

  handleCancelTmt() {
    let tmt = this;
    console.log("Cancel tournament", tmt);

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress + "/host",
      type: "DELETE",
      data: JSON.stringify(tmt.props.match.params.tmtID),
      success: (data) => {
        console.log("Ajax success", data);
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  handleStartTmt() {
    let tmt = this;
    console.log("Start tournament", tmt);
    debugger;

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url:
        this.props.serverAddress +
        "/host/pairings/" +
        tmt.props.match.params.tmtID,
      type: "GET",
      // data: JSON.stringify(tmt.props.match.params.tmtID),
      success: (data) => {
        console.log("Ajax success", data);
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  componentDidMount() {
    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress + "/host/" + this.props.match.params.tmtID,
      type: "GET",
      success: (tmt) => {
        if (tmt.roomCode === "") {
          alert("Something went wrong. Please try that again.");
        } else {
          console.log("Redirected to start tournament page");
          this.setState({ roomCode: tmt.roomCode });
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
            <Button
              className="btn btn-primary m-2"
              //href="/host/pairings"
              onClick={() => this.handleStartTmt()}
            >
              Start Tournament
            </Button>
            <Button
              className="btn btn-danger m-2"
              onClick={() => this.handleCancelTmt()}
              href="/host"
            >
              Cancel Tournament
            </Button>
          </Form>
        </div>
      );
    }
  }
}

export default StartTmt;
