import React, { Component } from "react";
import { Button, Form } from "react-bootstrap";
import PlayerList from "../playerList";
import Pairings from "../pairings";
import { MatchData } from "../../dtos/matchData";
import { Tournament } from "../../dtos/tournament";

type StartTmtProps = {
  serverAddress: string;
  match: {
    params: {
      tmtID: string;
    };
  };
};

type StartTmtState = {
  roomCode: string;
  pairings: MatchData[];
};

class StartTmt extends Component<StartTmtProps, StartTmtState> {
  state = {
    roomCode: "",
    pairings: [],
  };

  handleCancelTmt() {
    let tmt = this;

    fetch(`${this.props.serverAddress}/host`, {
      method: "DELETE",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: tmt.props.match.params.tmtID,
    })
      .then((res) => res.json())
      .then(() => {})
      .catch((err) => console.log("Fetch Error in handleCancelTmt", err));
  }

  handleStartTmt() {
    let tmt = this;

    fetch(
      `${this.props.serverAddress}/host/pairings/${tmt.props.match.params.tmtID}`,
      {
        method: "GET",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      }
    )
      .then((res) => res.json())
      .then((pairings: MatchData[]) => {
        tmt.setState({ pairings });
      })
      .catch((err) => console.log("Fetch Error in handleStartTmt", err));
  }

  getPairings() {
    this.getTournamentData()
      .then((tournament: Tournament) => {
        if (tournament.roomCode === "") {
          alert("Something went wrong. Please try that again.");
        } else {
          this.setState({ roomCode: tournament.roomCode });
          fetch(`${this.props.serverAddress}/pairings/${this.state.roomCode}`, {
            method: "GET",
            headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
            },
          })
            .then((res) => res.json())
            .then((pairings: MatchData[]) => {
              this.setState({ pairings });
            })
            .catch((err) =>
              console.log("Fetch Error in getPairings for startTmt.jsx", err)
            );
        }
      })
      .catch((err) => console.log("Fetch Error in getTournamentData", err));
  }

  getTournamentData() {
    return fetch(
      `${this.props.serverAddress}/host/${this.props.match.params.tmtID}`,
      {
        method: "GET",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      }
    ).then((res) => res.json());
  }

  componentDidMount() {
    this.getPairings();
  }

  render() {
    if (this.state.roomCode === "") {
      return <h2>Loading...</h2>;
    } else if (this.state.pairings.length > 0) {
      return (
        <div className="m-2">
          <Pairings
            pairings={this.state.pairings}
            onGetPairings={this.getPairings}
            key={"pairings_" + this.state.roomCode}
          />
          <Form>
            <Button
              className="btn btn-danger m-2"
              onClick={() => this.handleCancelTmt()}
              href="/host"
            >
              Cancel Tournament
            </Button>
          </Form>
        </div>
      );
    } else {
      return (
        <div className="m-2">
          <PlayerList
            serverAddress={this.props.serverAddress}
            roomCode={this.state.roomCode}
            key={"playerList_" + this.state.roomCode}
          />
          <Form>
            <Button
              className="btn btn-primary m-2"
              onClick={() => this.handleStartTmt()}
            >
              Start Tournament
            </Button>
            <Button
              className="btn btn-danger m-2"
              onClick={() => this.handleCancelTmt()}
              href="/host"
            >
              Cancel Tournament
            </Button>
          </Form>
        </div>
      );
    }
  }
}

export default StartTmt;
