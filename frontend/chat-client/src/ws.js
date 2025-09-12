import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;

// export const connectWebSocket = (token, onMessage, onUsersUpdate) => {
//     stompClient = new Client({
//
//         webSocketFactory: () => new SockJS("/ws"), // proxy will forward
//         connectHeaders: { Authorization: "Bearer " + token },
//         debug: (str) => console.log(str),
//         onConnect: () => {
//             console.log("Connected to WebSocket");
//
//             stompClient.subscribe("/topic/messages", (msg) => {
//                 onMessage(msg.body);
//             });
//
//             stompClient.subscribe("/topic/online", (msg) => {
//                 onUsersUpdate(JSON.parse(msg.body));
//             });
//         },
//         onStompError: (frame) => {
//             console.error("Broker error:", frame.headers["message"]);
//         },
//     });
//
//     stompClient.activate();
// };
//
// export const sendMessage = (msg) => {
//     if (stompClient && stompClient.connected) {
//         stompClient.publish({ destination: "/app/chat", body: msg });
//     }
// };

export const connectWebSocket = (token, onMessage, onUsersUpdate) => {
    console.log("Attempting to connect to WebSocket...");

    stompClient = new Client({
        webSocketFactory: () => {
            console.log("Creating SockJS connection to /ws");
            const socket = new SockJS("/ws");
            socket.onclose = (event) => {
                console.log("SockJS connection closed:", event);
            };
            socket.onerror = (error) => {
                console.error("SockJS error:", error);
            };
            return socket;
        },
        connectHeaders: { Authorization: "Bearer " + token },
        debug: (str) => console.log("STOMP Debug:", str),
        onConnect: () => {
            console.log("Successfully connected to STOMP");

            console.log("Subscribing to /topic/messages");
            stompClient.subscribe("/topic/messages", (msg) => {
                console.log("Received message:", msg);
                onMessage(msg.body);
            });

            console.log("Subscribing to /topic/online");
            stompClient.subscribe("/topic/online", (msg) => {
                console.log("Received online update:", msg);
                onUsersUpdate(JSON.parse(msg.body));
            });
        },
        onStompError: (frame) => {
            console.error("STOMP error:", frame);
        },
        onWebSocketError: (error) => {
            console.error("WebSocket error:", error);
        },
        onDisconnect: (frame) => {
            console.log("STOMP disconnected:", frame);
        }
    });

    try {
        console.log("Activating STOMP client...");
        stompClient.activate();
    } catch (error) {
        console.error("Error activating STOMP client:", error);
    }
};

export const sendMessage = (msg) => {
    if (stompClient && stompClient.connected) {
        console.log("Sending message to /app/chat:", msg);
        stompClient.publish({ destination: "/app/chat", body: msg });
    } else {
        console.warn("Cannot send message - STOMP client not connected");
    }
};