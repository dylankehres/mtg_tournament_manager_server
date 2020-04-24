import React, { Component } from "react";
import { Form, Dropdown, Button } from "react-bootstrap";

class JoinTmt extends Component {
  state = {
    id: 1,
    userName: "",
    roomCode: "",
    selectedFormat: "Select Format",
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
    this.setState({ userName: event.target.value });
  }

  handleRoomChange(event) {
    this.setState({ roomCode: event.target.value });
  }

  handleFormatSelect(eventKey) {
    this.setState({ selectedFormat: eventKey });
  }

  handleDeckChange(event) {
    this.setState({ deckName: event.target.value });
  }

  render() {
    return (
      <React.Fragment>
        <Form>
          <Form.Group className="m-2" style={{ width: "300px" }}>
            <Form.Label>Player Name</Form.Label>
            <Form.Control
              type="text"
              placeholder="Player name"
              value={this.state.userName}
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
          <Dropdown className="m-2" onSelect={this.handleFormatSelect}>
            <Dropdown.Toggle variant="success" id="dropdown-basic">
              {this.state.selectedFormat}
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
            onClick={() => this.props.onJoinTmt(this.state)}
            href="/join/waiting"
          >
            Join Tournament
          </Button>
        </Form>
      </React.Fragment>
    );
  }
}

export default JoinTmt;
