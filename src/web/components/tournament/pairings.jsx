import React, { Component } from "react";
import { Button, Table } from "react-bootstrap";
import $ from "jquery";

class Pairings extends Component {
  state = {
    pairings: [],
  };

  componentDidMount() {
    this.getPairings();
  }

  getPairings() {
    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url:
        this.props.serverAddress +
        "/host/pairings/" +
        this.props.match.params.tmtID,
      type: "GET",
      success: (pairings) => {
        console.log("Ajax success", pairings);
        if (pairings.length > 0) {
          this.setState({ pairings });
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  render() {
    if (this.state.pairings.length > 0) {
      return (
        <div className="m-2" style={{ width: "450px" }}>
          <div className="m-2">
            <h4>Pairings</h4>
            <Button
              className="btn btn-primary m-2 "
              onClick={() => this.getPairings()}
            >
              Refresh
            </Button>
          </div>
          <Table striped bordered hover size="sm">
            <thead>
              <tr>
                <th>Table #</th>
                <th>Player Name</th>
                <th>Player Name</th>
                <th align="right" onClick={() => this.getPairings()}>
                  R
                </th>
              </tr>
            </thead>
            <tbody>
              {this.state.pairings.map((match, index) => (
                <tr key={match.id}>
                  <td>{index + 1}</td>
                  <td>{match.player1.name}</td>
                  <td>{match.player2.name}</td>
                  <td></td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      );
    } else {
      return (
        <div className="m-2" style={{ width: "300px" }}>
          <div className="m-2">
            <h4>No pairings?</h4>
          </div>
        </div>
      );
    }
  }
}

export default Pairings;
