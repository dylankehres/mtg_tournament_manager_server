import React, { Component } from "react";
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
            </tr>
          </thead>
          <tbody>
            {this.state.playerList.map((player) => (
              <tr key={player.id}>
                <td>{player.id}</td>
                <td>{player.userName}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    );
  }
}

export default PlayerList;
