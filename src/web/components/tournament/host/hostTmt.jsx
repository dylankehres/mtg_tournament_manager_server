import React, { Component } from "react";
// import XMLHttpRequest from "xmlhttprequest";
import Form from "react-bootstrap/Form";
import Dropdown from "react-bootstrap/Dropdown";
import Button from "react-bootstrap/Button";

class HostTmt extends Component {
  state = {
    id: 1,
    tmtName: "",
    roomCode: "",
    selectedFormat: "Select Format",
  };

  formats = [
    { name: "Standard", id: 1 },
    { name: "Modern", id: 2 },
    { name: "Legacy", id: 3 },
    { name: "Commander", id: 4 },
  ];

  XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
  xhttp = new XMLHttpRequest();

  serverAddress = "localhost:8080/api/v1/tournament/";

  handleNameChange = this.handleNameChange.bind(this);
  handleRoomChange = this.handleRoomChange.bind(this);
  handleFormatSelect = this.handleFormatSelect.bind(this);

  handleNameChange(event) {
    this.setState({ tmtName: event.target.value });
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
            value={this.state.tmtName}
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
          onClick={() => this.handleOpenTmt()}
          href="/host/waiting"
        >
          Open Tournament
        </Button>
      </Form>
    );
  }

  handleOpenTmt() {
    debugger;
    console.log("Open tournament");
    this.xhttp.onreadystatechange = () => {
      if (this.readyState === 4 && this.status === 200) {
        //Why did they do this to select an element?
        // document.getElementById("demo").innerHTML = this.responseText;
        console.log("Ready for AJAX Request");
      }
    };

    this.xhttp.open("POST", this.serverAddress, true);
    this.xhttp.send('{"name": "' + this.state.tmtName + '"}');
  }
}

export default HostTmt;
