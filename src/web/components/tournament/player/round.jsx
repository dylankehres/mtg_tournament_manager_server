import React, { Component } from "react";
import { Button, Table } from "react-bootstrap";
import Timer from "react-compound-timer";
import $ from "jquery";

class Round extends Component {
  state = {
    roundNum: 1,
    roundData: {},
    results: [
      { gameNum: "1", winner: "" },
      { gameNum: "2", winner: "" },
      { gameNum: "3", winner: "" },
      //   { gameNum: "1", winner: "Dylan" },
      //   { gameNum: "2", winner: "Matt" },
      //   { gameNum: "3", winner: "Dylan" },
    ],
  };

  playerGameWin() {
    console.log("Player Game Win");
    const round = this;

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url:
        this.props.serverAddress +
        "/join/round/gameResults/" +
        this.state.roundData.opponent.playerID,
      type: "POST",
      success: (roundData) => {
        if (roundData === "") {
          alert("Something went wrong. Please try that again.");
        } else {
          round.setState({ roundData });
          console.log("roundData: ", roundData);
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error in getPlayerMatch", status);
      },
    });
  }

  opponentGameWin() {
    console.log("Opponent Game Win");

    const round = this;

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url:
        this.props.serverAddress +
        "/join/round/gameResults/" +
        this.state.roundData.opponent.playerID,
      type: "GET",
      success: (roundData) => {
        if (roundData === "") {
          alert("Something went wrong. Please try that again.");
        } else {
          round.setState({ roundData });
          console.log("roundData: ", roundData);
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error in getPlayerMatch", status);
      },
    });
  }

  gameDraw() {
    console.log("Game Draw");
  }

  componentDidMount() {
    // const round = this;
    //
    // $.ajax({
    //     headers: {
    //       Accept: "application/json",
    //       "Content-Type": "application/json",
    //     },
    //     url:
    //       this.props.serverAddress +
    //       "/join/round/" +
    //       this.props.match.params.playerID,
    //     type: "GET",
    //     success: (roundData) => {
    //       if (roundData === "") {
    //         alert("Something went wrong. Please try that again.");
    //       } else {
    //         round.setState({ roundData });
    //         console.log("roundData: ", roundData);
    //       }
    //     },
    //     error: function (jqxhr, status) {
    //       console.log("Ajax Error in getPlayerMatch", status);
    //     },
    //   });
  }

  render() {
    return (
      <div className="m-2">
        <table>
          <thead>
            <tr>
              <th>
                <h3>{"Round " + this.state.roundNum}</h3>
              </th>
              <th></th>
              <th>
                <Timer initialTime={3000000} direction="backward">
                  {() => (
                    <React.Fragment>
                      <Timer.Minutes /> {"minutes "}
                      <Timer.Seconds /> {"seconds"}
                    </React.Fragment>
                  )}
                </Timer>
              </th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                <Table striped bordered hover size="sm">
                  <thead>
                    <tr>
                      <th>Game #</th>
                      <th>Winner</th>
                    </tr>
                  </thead>
                  <tbody>
                    {this.state.results.map((game) => (
                      <tr key={game.gameNum}>
                        <td>{game.gameNum}</td>
                        <td>{game.winner}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </td>
              <td>
                <div className="m-2"></div>
              </td>
              <td style={{ verticalAlign: "top" }}>
                <table>
                  <tbody>
                    <tr>
                      <td>
                        <Button
                          className="btn btn-success"
                          style={{ width: "136px" }}
                          onClick={this.playerGameWin}
                        >
                          I Won
                        </Button>
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <Button
                          className="btn btn-danger"
                          style={{ width: "136px" }}
                          onClick={this.opponentGameWin}
                        >
                          Opponent Won
                        </Button>
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <Button
                          className="btn btn-primary"
                          style={{ width: "136px" }}
                          onClick={this.gameDraw}
                        >
                          Draw
                        </Button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    );
  }
}

export default Round;
