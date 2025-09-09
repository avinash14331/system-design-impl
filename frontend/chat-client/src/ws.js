import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;

export const connectWebSocket = (token, onMessage, onUsersUpdate) => {
    stompClient = new Client({

        webSocketFactory: () => new SockJS("/ws"), // proxy will forward
        connectHeaders: { Authorization: "Bearer " + token },
        debug: (str) => console.log(str),
        onConnect: () => {
            console.log("Connected to WebSocket");

            stompClient.subscribe("/topic/messages", (msg) => {
                onMessage(msg.body);
            });

            stompClient.subscribe("/topic/online", (msg) => {
                onUsersUpdate(JSON.parse(msg.body));
            });
        },
        onStompError: (frame) => {
            console.error("Broker error:", frame.headers["message"]);
        },
    });

    stompClient.activate();
};

export const sendMessage = (msg) => {
    if (stompClient && stompClient.connected) {
        stompClient.publish({ destination: "/app/chat", body: msg });
    }
};
