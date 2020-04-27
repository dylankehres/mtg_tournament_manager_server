import React, { Component } from "react";
import Table from "react-bootstrap/table";
import $ from "jquery";

class TmtList extends Component {
  state = {
    tmtList: [],
  };

  componentDidMount() {
    $.ajax({
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      url: this.props.serverAddress,
      type: "GET",
      // data: JSON.stringify(this.state),
      success: (tmtList) => {
        console.log("Ajax success", tmtList);
        this.setState({ tmtList });
      },
      error: function (jqxhr, status) {
        console.log("Ajax Error", status);
      },
    });
  }

  render() {
    if (this.state.tmtList.length > 0) {
      return (
        <div style={{ width: "450px" }}>
          <Table striped bordered hover size="sm">
            <thead>
              <tr>
                <th>Tournament Name</th>
                <th>Format</th>
                <th>Room Code</th>
              </tr>
            </thead>
            <tbody>
              {this.state.tmtList.map((tmt) => (
                <tr key={tmt.id}>
                  <td>{tmt.name}</td>
                  <td>{tmt.format}</td>
                  <td>{tmt.roomCode}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      );
    } else {
      return (
        <div style={{ width: "450px" }}>
          <h2>No tournaments available</h2>
        </div>
      );
    }
  }
}

export default TmtList;
