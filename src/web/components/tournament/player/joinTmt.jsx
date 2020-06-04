import React, { Component } from "react";
import { Form, Dropdown, Button, Table } from "react-bootstrap";
import { Redirect } from "react-router-dom";
import $ from "jquery";
import TmtList from "../tmtList";

class JoinTmt extends Component {
  state = {
    id: "",
    name: "",
    roomCode: "",
    format: "Select Format",
    deckName: "",
  };

  formats = [
    { name: "Standard", id: 1 },
    { name: "Modern", id: 2 },
    { name: "Legacy", id: 3 },
    { name: "Commander", id: 4 },
  ];

  handleNameChange = this.handleNameChange.bind(this);
  handleRoomChange = this.handleRoomChange.bind(this);
  handleFormatSelect = this.handleFormatSelect.bind(this);
  handleDeckChange = this.handleDeckChange.bind(this);

  handleNameChange(event) {
    this.setState({ name: event.target.value });
  }

  handleRoomChange(event) {
    this.setState({ roomCode: event.target.value });
  }

  handleFormatSelect(eventKey) {
    this.setState({ format: eventKey });
  }

  handleDeckChange(event) {
    this.setState({ deckName: event.target.value });
  }

  handleJoinTmt() {
    if (this.formIsValid) {
      console.log("Joining tournament", this.state);
      $.ajax({
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        url: this.props.serverAddress + "/join",
        type: "POST",
        dataType: "text",
        data: JSON.stringify(this.state),
        success: (id) => {
          if (id === "") {
            alert("Invalid room code");
          } else {
            console.log("ID: ", id);
            this.setState({ id });
          }
        },
        error: function (jqxhr, status) {
          console.log("Ajax Error in handleJoinTmt", status);
        },
      });
    }
  }

  getJoinDisabled() {
    if (this.formIsValid()) {
      return false;
    } else {
      return true;
    }
  }

  formIsValid() {
    if (
      this.state.name !== "" &&
      this.state.roomCode !== "" &&
      this.state.format !== "Select Format" &&
      this.state.deckName !== ""
    ) {
      return true;
    } else {
      return false;
    }
  }

  render() {
    if (this.state.id === "") {
      return (
        <React.Fragment>
          <Table>
            <tbody>
              <tr>
                <td>
                  <Form>
                    <Form.Group className="m-2" style={{ width: "300px" }}>
                      <Form.Label>Player Name</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Player name"
                        value={this.state.name}
                        onChange={this.handleNameChange}
                      ></Form.Control>
                      <Form.Label>Room Code</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Room code"
                        value={this.state.roomCode}
                        onChange={this.handleRoomChange}
                      ></Form.Control>
                      <Form.Label>Deck Name</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Deck name"
                        value={this.state.deckName}
                        onChange={this.handleDeckChange}
                      ></Form.Control>
                    </Form.Group>
                    <Dropdown
                      className="m-2"
                      onSelect={this.handleFormatSelect}
                    >
                      <Dropdown.Toggle variant="success" id="dropdown-basic">
                        {this.state.format}
                      </Dropdown.Toggle>
                      <Dropdown.Menu>
                        {this.formats.map((format) => (
                          <Dropdown.Item
                            key={format.id}
                            value={format.name}
                            eventKey={format.name}
                          >
                            {format.name}
                          </Dropdown.Item>
                        ))}
                      </Dropdown.Menu>
                    </Dropdown>
                    <Button
                      className="btn btn-primary m-2"
                      disabled={this.getJoinDisabled}
                      onClick={() => this.handleJoinTmt}
                    >
                      Join Tournament
                    </Button>
                  </Form>
                </td>
                <td>
                  <TmtList serverAddress={this.props.serverAddress} />
                </td>
              </tr>
            </tbody>
          </Table>
        </React.Fragment>
      );
    } else {
      return <Redirect to={`/join/${this.state.id}`} />;
    }
  }
}

export default JoinTmt;
