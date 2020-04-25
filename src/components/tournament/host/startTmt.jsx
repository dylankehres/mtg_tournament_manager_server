import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import PlayerList from "../playerList";

class StartTmt extends Component {
  state = {};
  render() {
    return (
      <div className="m-2">
        <PlayerList />
        <Form>
          <Button className="btn btn-primary m-2" href="/host/pairings">
            Start Tournament
          </Button>
          <Button className="btn btn-danger m-2" href="/host">
            Cancel Tournament
          </Button>
        </Form>
      </div>
    );
  }
}

export default StartTmt;
