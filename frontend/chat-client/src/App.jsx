import { useState } from "react";
import { connectWebSocket, sendMessage } from "./ws.js";

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