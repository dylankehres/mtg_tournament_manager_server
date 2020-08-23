class Game {
  id: string;
  matchID: string;
  tournamentID: string;
  player1Wins: number;
  player2Wins: number;
  player1Ready: boolean;
  player2Ready: boolean;
  draw: number;
  player1Voted: boolean;
  player2Voted: boolean;
  isActive: boolean;
  resultStatus: number;
  winningPlayerID: string;

  constructor() {
    this.id = "";
    this.matchID = "";
    this.tournamentID = "";
    this.player1Wins = 0;
    this.player2Wins = 0;
    this.player1Ready = false;
    this.player2Ready = false;
    this.draw = 0;
    this.player1Voted = false;
    this.player2Voted = false;
    this.isActive = false;
    this.resultStatus = 0;
    this.winningPlayerID = "-1";
  }
}

export { Game };
