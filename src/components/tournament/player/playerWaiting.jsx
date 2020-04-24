import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import PlayerList from "../playerList";

class PlayerWaiting extends Component {
  state = {};
  render() {
    return (
      <div className="m-2">
        <PlayerList playerList={this.props.playerList} />
        <Form>
          <Button className="btn btn-danger m-2" href="/join">
            Leave Tournament
          </Button>
        </Form>
      </div>
    );
  }
}

export default PlayerWaiting;
