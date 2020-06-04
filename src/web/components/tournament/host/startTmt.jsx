import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import PlayerList from "../playerList";
import $ from "jquery";
import Pairings from "../pairings";

class StartTmt extends Component {
  state = {
    roomCode: "",
    pairings: [],
  };

  handleCancelTmt() {
    let tmt = this;

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress + "/host",
      type: "DELETE",
      data: tmt.props.match.params.tmtID,
      success: (data) => {},
      error: function (jqxhr, status) {
        console.log("Ajax Error in handleCancelTmt", status);
      },
    });
  }

  handleStartTmt() {
    let tmt = this;

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
      success: (pairings) => {
        tmt.setState({ pairings });
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error in handleStartTmt", status);
      },
    });
  }

  async getPairings() {
    let tmt = this;

    await this.getTournamentData();

    if (tmt.state.roomCode !== "") {
      $.ajax({
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        url: tmt.props.serverAddress + "/pairings/" + tmt.state.roomCode,
        type: "GET",
        success: (pairings) => {
          tmt.setState({ pairings });
        },
        error: function (jqxhr, status) {
          console.log("Ajax Error in getPairings for startTmt.jsx", tmt);
        },
      });
    }
  }

  getTournamentData() {
    return $.ajax({
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
          this.setState({ roomCode: tmt.roomCode });
          // getPairings();
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  componentDidMount() {
    this.getPairings();
  }

  render() {
    if (this.state.roomCode === "") {
      return <h2>Loading...</h2>;
    } else if (this.state.pairings.length > 0) {
      return (
        <div className="m-2">
          <Pairings
            pairings={this.state.pairings}
            onGetPairings={this.getPairings}
            key={"pairings_" + this.state.roomCode}
          />
          <Form>
            <Button
              className="btn btn-danger m-2"
              onClick={() => this.handleCancelTmt}
              href="/host"
            >
              Cancel Tournament
            </Button>
          </Form>
        </div>
      );
    } else {
      return (
        <div className="m-2">
          <PlayerList
            serverAddress={this.props.serverAddress}
            roomCode={this.state.roomCode}
            key={"playerList_" + this.state.roomCode}
          />
          <Form>
            <Button
              className="btn btn-primary m-2"
              onClick={() => this.handleStartTmt}
            >
              Start Tournament
            </Button>
            <Button
              className="btn btn-danger m-2"
              onClick={() => this.handleCancelTmt}
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
