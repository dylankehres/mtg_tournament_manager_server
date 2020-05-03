import React, { Component } from "react";
import $ from "jquery";
import { Table, Button } from "react-bootstrap";

class PlayerList extends Component {
  state = {
    playerList: [
      // {
      //   id: 1,
      //   userName: "Dylan",
      //   roomCode: "TitanEDH",
      //   selectedFormat: "Commander",
      // },
      // {
      //   id: 2,
      //   userName: "Matt",
      //   roomCode: "TitanEDH",
      //   selectedFormat: "Commander",
      // },
      // {
      //   id: 3,
      //   userName: "Preston",
      //   roomCode: "TitanEDH",
      //   selectedFormat: "Commander",
      // },
    ],
  };

  componentDidMount() {
    this.getPlayerList();
  }

  getPlayerList() {
    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress + "/playerList/" + this.props.roomCode,
      type: "GET",
      success: (playerList) => {
        console.log("Ajax success", playerList);
        if (playerList.length > 0) {
          this.setState({ playerList });
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  render() {
    if (this.state.playerList.length > 0) {
      return (
        <div className="m-2" style={{ width: "300px" }}>
          <div className="m-2">
            <h4>Waiting for event to start</h4>
            <Button
              className="btn btn-primary m-2 "
              onClick={() => this.getPlayerList()}
            >
              Refresh
            </Button>
          </div>
          <Table striped bordered hover size="sm">
            <thead>
              <tr>
                <th>#</th>
                <th>Player Name</th>
                <th>Deck Name</th>
                <th align="right" onClick={() => this.getPlayerList()}>
                  R
                </th>
              </tr>
            </thead>
            <tbody>
              {this.state.playerList.map((player, index) => (
                <tr key={player.id}>
                  <td>{index + 1}</td>
                  <td>{player.name}</td>
                  <td>{player.deckName}</td>
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
            <h4>This event is empty</h4>
          </div>
        </div>
      );
    }
  }
}

export default PlayerList;
