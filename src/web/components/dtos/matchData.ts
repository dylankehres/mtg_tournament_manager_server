import { Player } from "./player";
import { Match } from "./match";
import { Game } from "./game";

class MatchData {
  player1: Player;
  player2: Player;
  match: Match;
  gameList: Game[];

  constructor() {
    this.player1 = new Player();
    this.player2 = new Player();
    this.match = new Match();
    this.gameList = [];
  }
}

export { MatchData };
