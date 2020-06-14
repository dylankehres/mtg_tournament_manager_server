import React, { Component } from "react";
import { Button, Table } from "react-bootstrap";
import Timer from "react-compound-timer";
import $ from "jquery";

class Round extends Component {
  state = {
    roundNum: 1,
    playerID: "",
    opponentID: "",
    matchData: {},
    // results: [
    // { gameNum: "1", winner: "" },
    // { gameNum: "2", winner: "" },
    // { gameNum: "3", winner: "" },
    //   { gameNum: "1", winner: "Dylan" },
    //   { gameNum: "2", winner: "Matt" },
    //   { gameNum: "3", winner: "Dylan" },
    // ],
  };

  playerGameWin() {
    console.log("Player Game Win");

    this.reportResults(this.state.playerID);
  }

  opponentGameWin() {
    console.log("Opponent Game Win");

    this.reportResults(this.state.opponentID);
  }

  reportResults(winnerID) {
    console.log("Report results");

    let currentGameID = "-1";

    matchData.gameList.forEach((game) => {
      if (game.active) {
        currentGameKey = game.id;
      }
    });

    const round = this;

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url:
        this.props.serverAddress +
        "/match/gameResults/" +
        this.state.playerID +
        "/" +
        winnerID,
      type: "POST",
      success: (matchData) => {
        if (matchData === "") {
          alert("Something went wrong. Please try that again.");
        } else {
          if (currentGameKey === matchData.match.activeGameID) {
            if (matchData.resultStatus === 1) {
              // Waiting on other votes
            } else {
              // Disputed results
            }
          } else {
            if (matchData.resultStatus === 2) {
              // These are final results
              round.setState({ matchData });
              console.log("matchData: ", matchData);
            }
          }
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
    const round = this;

    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url:
        round.props.serverAddress +
        "/match/" +
        round.props.match.params.playerID,
      type: "GET",
      success: (matchData) => {
        if (matchData === "") {
          alert("Something went wrong. Please try that again.");
        } else {
          round.setState({ matchData });

          if (round.props.match.params.playerID === matchData.player1.id) {
            round.setState({
              playerID: matchData.player1.id,
              opponentID: matchData.player2.id,
            });
          } else {
            round.setState({
              playerID: matchData.player2.id,
              opponentID: matchData.player1.id,
            });
          }
          console.log("matchData: ", matchData);
        }
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error in getPlayerMatch", status);
      },
    });
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
                    {this.state.results.map((game, index) => (
                      <tr key={game.gameID}>
                        <td>{index + 1}</td>
                        <td>{game.winningPlayer.name}</td>
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
                          onClick={() => this.playerGameWin()}
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
                          onClick={() => this.opponentGameWin()}
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
                          onClick={() => this.gameDraw()}
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
