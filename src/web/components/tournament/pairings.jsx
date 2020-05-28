import React from "react";
import { Table } from "react-bootstrap";

const Pairings = (props) => {
  if (props.pairings.length > 0) {
    return (
      <div className="m-2" style={{ width: "450px" }}>
        {/* <div>
          <h4>Pairings</h4>
          <Button
            className="btn btn-primary m-2 "
            onClick={() => props.onGetPairings()}
          >
            Refresh
          </Button>
        </div> */}
        <Table striped bordered hover size="sm">
          <thead>
            <tr>
              <th>Table #</th>
              <th>Player Name</th>
              <th>Player Name</th>
              <th align="right" onClick={() => props.onGetPairings}>
                R
              </th>
            </tr>
          </thead>
          <tbody>
            {props.pairings.map((matchData, index) => (
              <tr key={matchData.match.id}>
                <td>{index + 1}</td>
                <td>{matchData.player1.name}</td>
                <td>{matchData.player2.name}</td>
                <td></td>
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
          <h4>Waiting for pairings to post</h4>
        </div>
      </div>
    );
  }
};

export default Pairings;
