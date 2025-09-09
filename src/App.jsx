// import { useState } from 'react'
// import reactLogo from './assets/react.svg'
// import viteLogo from '/vite.svg'
// import './App.css'
//
// function App() {
//   const [count, setCount] = useState(0)
//
//   return (
//     <>
//       <div>
//         <a href="https://vite.dev" target="_blank">
//           <img src={viteLogo} className="logo" alt="Vite logo" />
//         </a>
//         <a href="https://react.dev" target="_blank">
//           <img src={reactLogo} className="logo react" alt="React logo" />
//         </a>
//       </div>
//       <h1>Vite + React</h1>
//       <div className="card">
//         <button onClick={() => setCount((count) => count + 1)}>
//           count is {count}
//         </button>
//         <p>
//           Edit <code>src/App.jsx</code> and save to test HMR
//         </p>
//       </div>
//       <p className="read-the-docs">
//         Click on the Vite and React logos to learn more
//       </p>
//     </>
//   )
// }
//
// export default App

import { useEffect, useState } from "react";
import { connectWebSocket, sendMessage } from "./ws";

function App() {
    const [token, setToken] = useState("");
    const [connected, setConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [onlineUsers, setOnlineUsers] = useState([]);
    const [input, setInput] = useState("");

    const handleConnect = () => {
        connectWebSocket(
            token,
            (msg) => setMessages((prev) => [...prev, msg]),
            (users) => setOnlineUsers(users)
        );
        setConnected(true);
    };

    const handleSend = () => {
        sendMessage(input);
        setInput("");
    };

    return (
        <div style={{ padding: 20 }}>
            {!connected ? (
                <div>
                    <h2>Enter JWT Token to Connect</h2>
                    <input
                        type="text"
                        placeholder="Paste JWT here"
                        value={token}
                        onChange={(e) => setToken(e.target.value)}
                        style={{ width: "400px" }}
                    />
                    <button onClick={handleConnect}>Connect</button>
                </div>
            ) : (
                <div>
                    <h2>Chat Room</h2>
                    <div>
                        <strong>Online Users:</strong>
                        <ul>
                            {onlineUsers.map((u) => (
                                <li key={u.id}>{u.username}</li>
                            ))}
                        </ul>
                    </div>

                    <div style={{ border: "1px solid gray", padding: 10, height: 200, overflowY: "scroll" }}>
                        {messages.map((m, i) => (
                            <div key={i}>{m}</div>
                        ))}
                    </div>

                    <input
                        type="text"
                        value={input}
                        onChange={(e) => setInput(e.target.value)}
                    />
                    <button onClick={handleSend}>Send</button>
                </div>
            )}
        </div>
    );
}

export default App;

