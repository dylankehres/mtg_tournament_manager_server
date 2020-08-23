import React, { Component } from "react";
import $ from "jquery";
import { Table } from "react-bootstrap";
import { Player } from "../dtos/player";

type PlayerListProps = {
  serverAddress: string;
  roomCode: string;
};

type PlayerListState = {
  playerList: Player[];
};

class PlayerList extends Component<PlayerListProps, PlayerListState> {
  state = {
    playerList: [],
  };

  componentDidMount() {
    this.getPlayerList();
  }

  async getPlayerList() {
    await $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress + "/playerList/" + this.props.roomCode,
      type: "GET",
      success: (playerList) => {
        if (playerList.length > 0) {
          this.setState({ playerList });
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error in getPlayerList", status);
      },
    });
  }

  render() {
    if (this.state.playerList.length > 0) {
      return (
        <div className="m-2" style={{ width: "300px" }}>
          <Table striped bordered hover size="sm">
            <thead>
              <tr>
                <th>#</th>
                <th>Player Name</th>
                <th>Deck Name</th>
              </tr>
            </thead>
            <tbody>
              {this.state.playerList.map((player: Player, index) => (
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
