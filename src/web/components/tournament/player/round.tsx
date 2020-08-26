import React, { Component } from "react";
import { Button, Table } from "react-bootstrap";
import Timer from "react-compound-timer";
import LoadingDiv from "../../loadingDiv";
import { MatchData } from "../../dtos/matchData";
import { Game } from "../../dtos/game";

type RoundProps = {
  serverAddress: string;
  match: {
    params: {
      playerID: string;
    };
  };
};

type RoundState = {
  roundNum: number;
  playerID: string;
  opponentID: string;
  currGameResultStatus: number;
  matchData: MatchData;
  winnersList: string[];
  timeRemaining: number;
};

class Round extends Component<RoundProps, RoundState> {
  state = {
    roundNum: 1,
    playerID: "",
    opponentID: "",
    currGameResultStatus: -1,
    matchData: new MatchData(),
    winnersList: [],
    timeRemaining: -1,
  };

  playerGameWin() {
    this.reportResults(this.state.playerID);
  }

  opponentGameWin() {
    this.reportResults(this.state.opponentID);
  }

  reportResults(winnerID: string) {
    console.log("Report results");
    console.log("Game List: ", this.state.matchData.gameList);

    let currentGameID = "-1";
    const currentGame = this.state.matchData.gameList.find(
      (game) => game.isActive,
      new Game()
    );

    if (currentGame !== undefined) {
      currentGameID = currentGame.id;
    }

    fetch(
      `${this.props.serverAddress}/match/gameResults/${this.props.match.params.playerID}/${winnerID}`,
      {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      }
    )
      .then((res) => res.json())
      .then((matchData: MatchData) => {
        const activeGame = matchData.gameList.find(
          (game: Game) => game.id === currentGameID,
          new Game()
        );

        if (activeGame !== undefined) {
          if (currentGameID === matchData.match.activeGameID) {
            if (activeGame.resultStatus === 1) {
              // Waiting on other votes
              console.log("Awaiting final results");
              this.setState({ currGameResultStatus: activeGame.resultStatus });
            } else {
              // Disputed results
              console.log("Results are being disputed");
              this.setState({ currGameResultStatus: activeGame.resultStatus });
            }
          } else {
            if (activeGame.resultStatus === 2) {
              // These are final results
              console.log("Results are final");
              this.setState({ matchData });
            }
          }
        }
      })
      .catch((err) =>
        console.log("Ajax Error in round.tsx reportResults", err)
      );
  }

  gameDraw() {
    console.log("Game Draw");
  }

  getMatchData(): void {
    const round = this;

    fetch(
      `${round.props.serverAddress}/match/${round.props.match.params.playerID}`,
      {
        method: "GET",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      }
    )
      .then((res) => res.json())
      .then((matchData: MatchData) => {
        round.setState({ matchData });
        round.setState({ timeRemaining: matchData.match.endTime - Date.now() });

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
        round.buildWinnersList();
      })
      .catch((err) => console.log("Ajax Error in round.tsx getMatchData", err));
  }

  buildWinnersList() {
    let winners = [];
    this.state.matchData.gameList.reverse();
    this.state.matchData.gameList.forEach((game) => {
      if (game !== null) {
        if (game.winningPlayerID === "-1") {
          winners.push("");
        } else if (game.winningPlayerID === this.state.matchData.player1.id) {
          winners.push(this.state.matchData.player1.name);
        } else if (game.winningPlayerID === this.state.matchData.player2.id) {
          winners.push(this.state.matchData.player2.name);
        } else {
          winners.push("Error!");
        }
      }
    });

    if (winners.length === 0) {
      winners.push("");
    }

    this.setState({ winnersList: winners });
  }

  componentDidMount() {
    this.getMatchData();
  }

  render() {
    if (this.state.timeRemaining === -1) {
      return <LoadingDiv />;
    }
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
                <Timer
                  initialTime={this.state.timeRemaining}
                  direction="backward"
                >
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
                    {this.state.winnersList.map((winner, index) => (
                      <tr key={index}>
                        <td>{index + 1}</td>
                        <td>{winner}</td>
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
                          type="submit"
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
                          type="submit"
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
