import React, { Component } from "react";
import $ from "jquery";
import { Form, Dropdown, Button } from "react-bootstrap";
import { Redirect, withRouter } from "react-router-dom";

class HostTmt extends Component {
  state = {
    id: "",
    tmtName: "",
    roomCode: "",
    format: "Select Format",
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
    this.setState({ tmtName: event.target.value });
  }

  handleRoomChange(event) {
    this.setState({ roomCode: event.target.value });
  }

  handleFormatSelect(eventKey) {
    this.setState({ format: eventKey });
  }

  getOpenDisabled() {
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
      this.state.format !== "Select Format"
    ) {
      return true;
    } else {
      return false;
    }
  }

  handleOpenTmt() {
    console.log("Open tournament");

    if (this.formIsValid()) {
      $.ajax({
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        url: this.props.serverAddress + "/host",
        type: "POST",
        data: JSON.stringify(this.state),
        success: (id) => {
          if (id === "") {
            alert("Room code is not unique. Please try a differnt code.");
          } else {
            console.log("Opened tournament");
            this.setState({ id });
          }
        },
        error: function (jqxhr, status) {
          console.log("Ajax Error", status);
        },
      });
    }
  }

  render() {
    if (this.state.id === "") {
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
            className="btn btn-primary m-2 "
            onClick={() => this.handleOpenTmt()}
            disabled={this.getOpenDisabled()}
            // href="/host/waiting/"
          >
            Open Tournament
          </Button>
        </Form>
      );
    } else {
      return <Redirect to={`/host/${this.state.id}`} />;
    }
  }
}

export default withRouter(HostTmt);
