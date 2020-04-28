import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import PlayerList from "../playerList";
import $ from "jquery";

class StartTmt extends Component {
  state = {};

  handleCancelTmt() {
    let tmt = this;
    console.log("Cancel tournament", tmt);

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: tmt.props.serverAddress,
      type: "DELETE",
      data: JSON.stringify(tmt.props.tmt.id),
      success: (data) => {
        console.log("Ajax success", data);
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  render() {
    return (
      <div className="m-2">
        <PlayerList
          serverAddress={this.props.serverAddress}
          roomCode={this.props.tmt.roomCode}
        />
        <Form>
          <Button className="btn btn-primary m-2" href="/host/pairings">
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

export default StartTmt;
