import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import PlayerList from "../playerList";
import $ from "jquery";
import { Redirect } from "react-router-dom";

class StartTmt extends Component {
  state = {
    roomCode: "",
    pairings: [],
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
      success: (data) => {
        console.log("Ajax success", data);
        tmt.setState({ pairings: data });
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
        debugger;
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
    } else if (this.state.pairings.length > 0) {
      debugger;
      return (
        <Redirect to={`/host/pairings/${this.props.match.params.tmtID}`} />
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
              className="btn btn-primary m-2"
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
