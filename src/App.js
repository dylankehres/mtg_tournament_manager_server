import React from "react";
import "./App.css";
import Root from "./web/root";
import NavBar from "./web/components/navbar";

function App() {
  return (
    <React.Fragment>
      <NavBar />
      <main className="container">
        {/* <Tournament /> */}
        {/* <Home /> */}
        <Root />
      </main>
    </React.Fragment>

    // <div className="App">
    //   <header className="App-header">
    //     <img src={logo} className="App-logo" alt="logo" />
    //     <p>
    //       Edit <code>src/App.js</code> and save to reload.
    //     </p>
    //     <a
    //       className="App-link"
    //       href="https://reactjs.org"
    //       target="_blank"
    //       rel="noopener noreferrer"
    //     >
    //       Learn React
    //     </a>
    //   </header>
    // </div>
  );
}

export default App;
