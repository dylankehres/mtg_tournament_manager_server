import React, { Component } from "react";
import Form from "react-bootstrap/Form";
import Dropdown from "react-bootstrap/Dropdown";
import Button from "react-bootstrap/Button";

class HostTmt extends Component {
  state = {
    id: 1,
    userName: "",
    roomCode: "",
    selectedFormat: "Select Format",
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

  handleNameChange(event) {
    this.setState({ userName: event.target.value });
  }

  handleRoomChange(event) {
    this.setState({ roomCode: event.target.value });
  }

  handleFormatSelect(eventKey) {
    this.setState({ selectedFormat: eventKey });
  }

  render() {
    return (
      <Form>
        <Form.Group className="m-2" style={{ width: "300px" }}>
          <Form.Label>Tournament Name</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter name"
            value={this.state.userName}
            onChange={this.handleNameChange}
          ></Form.Control>
        </Form.Group>
        <Form.Group className="m-2" style={{ width: "300px" }}>
          <Form.Label>Room Code</Form.Label>
          <Form.Control
            type="text"
            placeholder="Room Code"
            value={this.state.roomCode}
            onChange={this.handleRoomChange}
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
          onClick={() => this.props.onOpenTmt(this.state)}
          href="/host/waiting"
        >
          Open Tournament
        </Button>
      </Form>
    );
  }
}

export default HostTmt;
