import React, { Component } from "react";
import { Form, Button } from "react-bootstrap";
import { withRouter } from "react-router-dom";

class Tournament extends Component {
  state = {
    path: "",
    playerList: [],
  };

  constructor() {
    super();
    this.state.path = "/join";
  }

  render() {
    return (
      <React.Fragment>
        <Form>
          <Button className="btn btn-primary m-2" href="/join">
            Join Tournament
          </Button>
          <Button className="btn btn-primary m-2" href="/host">
            Host Tournament
          </Button>
        </Form>
      </React.Fragment>
    );
  }
}

export default withRouter(Tournament);
