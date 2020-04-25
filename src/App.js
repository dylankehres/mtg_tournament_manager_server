import React from "react";
import "./App.css";
import Root from "./web/root";
import NavBar from "./web/components/navbar";

function App() {
  return (
    <React.Fragment>
      <NavBar />
      <main className="container">
        <Root />
      </main>
    </React.Fragment>
  );
}

export default App;
