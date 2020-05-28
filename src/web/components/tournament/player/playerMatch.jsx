import React from "react";

const playerMatch = (props) => {
  let oppName =
    props.matchData.player1.id === props.currPlayer.id
      ? props.matchData.player2.name
      : props.matchData.player1.name;

  return (
    <div className="m-2">
      <h4>{"Opponent: " + oppName}</h4>
      <h4>{"Table #" + props.matchData.match.tableNum}</h4>
    </div>
  );
};

export default playerMatch;
