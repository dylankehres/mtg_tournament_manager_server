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
              <th align="right" onClick={() => props.onGetPairings()}>
                R
              </th>
            </tr>
          </thead>
          <tbody>
            {props.pairings.map((match, index) => (
              <tr key={match.id}>
                <td>{index + 1}</td>
                <td>{match.player1.name}</td>
                <td>{match.player2.name}</td>
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

// class Pairings extends Component {
// state = {
// pairings: [],
// };

// componentDidMount() {
//   this.props.onGetPairings();
// }

// getPairings() {
//   $.ajax({
//     headers: {
//       Accept: "application/json",
//       "Content-Type": "application/json",
//     },
//     url: this.props.serverAddress + "/pairings/" + this.props.roomCode,
//     type: "GET",
//     success: (pairings) => {
//       if (pairings.length > 0) {
//         this.setState({ pairings });
//       }
//     },
//     error: function (jqxhr, status) {
//       console.log("Ajax Error", status);
//     },
//   });
// }

//   render() {
//     if (this.props.pairings.length > 0) {
//       return (
//         <div className="m-2" style={{ width: "450px" }}>
//           <div className="m-2">
//             <h4>Pairings</h4>
//             <Button
//               className="btn btn-primary m-2 "
//               onClick={() => this.props.onGetPairings()}
//             >
//               Refresh
//             </Button>
//           </div>
//           <Table striped bordered hover size="sm">
//             <thead>
//               <tr>
//                 <th>Table #</th>
//                 <th>Player Name</th>
//                 <th>Player Name</th>
//                 <th align="right" onClick={() => this.props.onGetPairings()}>
//                   R
//                 </th>
//               </tr>
//             </thead>
//             <tbody>
//               {this.props.pairings.map((match, index) => (
//                 <tr key={match.id}>
//                   <td>{index + 1}</td>
//                   <td>{match.player1.name}</td>
//                   <td>{match.player2.name}</td>
//                   <td></td>
//                 </tr>
//               ))}
//             </tbody>
//           </Table>
//         </div>
//       );
//     } else {
//       return (
//         <div className="m-2" style={{ width: "300px" }}>
//           <div className="m-2">
//             <h4>Waiting for pairings to post</h4>
//           </div>
//         </div>
//       );
//     }
//   }
// }

// export default Pairings;
