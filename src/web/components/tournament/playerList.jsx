import React, { Component } from "react";
import $ from "jquery";
import Table from "react-bootstrap/table";

class PlayerList extends Component {
  state = {
    playerList: [
      {
        id: 1,
        userName: "Dylan",
        roomCode: "TitanEDH",
        selectedFormat: "Commander",
      },
      {
        id: 2,
        userName: "Matt",
        roomCode: "TitanEDH",
        selectedFormat: "Commander",
      },
      {
        id: 3,
        userName: "Preston",
        roomCode: "TitanEDH",
        selectedFormat: "Commander",
      },
    ],
  };

  componentDidMount() {
    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress + "/playerList/" + this.props.roomCode,
      type: "GET",
      // data: JSON.stringify(this.props.roomCode),
      success: (playerList) => {
        console.log("Ajax success", playerList);
        this.setState({ playerList });
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  render() {
    return (
      <div className="m-2" style={{ width: "300px" }}>
        <div className="m-2">
          <h4>Waiting for event to start</h4>
        </div>
        <Table striped bordered hover size="sm">
          <thead>
            <tr>
              <th>#</th>
              <th>Player Name</th>
              <th>Deck Name</th>
            </tr>
          </thead>
          <tbody>
            {this.state.playerList.map((player, index) => (
              <tr key={player.id}>
                <td>{index + 1}</td>
                <td>{player.name}</td>
                <td>{player.deckName}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    );
  }
}

export default PlayerList;
