import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Form from "react-bootstrap/Form";
import NavDropdown from "react-bootstrap/NavDropdown";
import Button from "react-bootstrap/Button";
import FormControl from "react-bootstrap/FormControl";

const NavBar = () => {
  return (
    <Navbar bg="light" expand="lg">
      <Navbar.Brand href="/">MTG Tournament Manager</Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto">
          <NavDropdown title="Tournaments" id="basic-nav-dropdown">
            <NavDropdown.Item href="/join">Join</NavDropdown.Item>
            <NavDropdown.Item href="/host">Host</NavDropdown.Item>
          </NavDropdown>
          <Nav.Link href="#link">Stats</Nav.Link>
          <NavDropdown title="Decks" id="basic-nav-dropdown">
            <NavDropdown.Item href="#action/3.1">Temur Flash</NavDropdown.Item>
            <NavDropdown.Item href="#action/3.2">
              Bant Midrange
            </NavDropdown.Item>
            <NavDropdown.Item href="#action/3.3">RB Sacrafice</NavDropdown.Item>
            <NavDropdown.Divider />
            <NavDropdown.Item href="#action/3.4">More decks</NavDropdown.Item>
          </NavDropdown>
        </Nav>
        <Form inline>
          <FormControl type="text" placeholder="Search" className="mr-sm-2" />
          <Button variant="outline-success">Search</Button>
        </Form>
      </Navbar.Collapse>
    </Navbar>
  );
};

export default NavBar;
