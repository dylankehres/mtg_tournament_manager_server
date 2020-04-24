import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import Tournament from "./tournament/tournament";

class Home extends Component {
  state = {};
  render() {
    return (
      <React.Fragment>
        <div className="m-2">
          <h2>Welcome to MTG Tournament Manager!</h2>
          <Form>
            <Button className="btn btn-primary m-2" /*href="/tournament*/>
              Tournaments
            </Button>
          </Form>
        </div>
        {/* <Router>
          <Switch>
            <Route exact path="/tournament" component={Tournament} />
          </Switch>
        </Router> */}
      </React.Fragment>
    );
  }
}

export default Home;
