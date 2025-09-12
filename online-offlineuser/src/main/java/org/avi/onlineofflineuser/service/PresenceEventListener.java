package org.avi.onlineofflineuser.service;

import lombok.extern.slf4j.Slf4j;
import org.avi.onlineofflineuser.entity.UserPresence;
import org.avi.onlineofflineuser.repository.UserPresenceRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class PresenceEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();
    private final UserPresenceRepository repository;

    public PresenceEventListener(SimpMessagingTemplate messagingTemplate,
                                 UserPresenceRepository repository) {
        this.messagingTemplate = messagingTemplate;
        this.repository = repository;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = accessor.getNativeHeader("username").get(0); // client sends username
        String username = event.getUser().getName(); // from JWT principal

        onlineUsers.add(username);

        // upsert to DB
        repository.findByUsername(username).ifPresentOrElse(user -> {
            user.setOnline(true);
            user.setLastSeen(LocalDateTime.now());
            repository.save(user);
        }, () -> {
            UserPresence user = new UserPresence();
            user.setUsername(username);
            user.setOnline(true);
            user.setLastSeen(LocalDateTime.now());
            repository.save(user);
        });

        // broadcast updated list
        messagingTemplate.convertAndSend("/topic/online", onlineUsers);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) accessor.getSessionAttributes().get("username");
        String sessionId = accessor.getSessionId();
        CloseStatus closeStatus = event.getCloseStatus();

        if (closeStatus != null) {
            switch (closeStatus.getCode()) {
                case 1001:  // Browser tab/window closed normally
                    log.info("Client closed browser - Session ID: {}", sessionId);
                    break;
                case 1006:  // Abnormal close (browser crash or network issues)
                    log.warn("Abnormal browser closure detected - Session ID: {}", sessionId);
                    break;
            }
        }

        username = event.getUser() != null ? event.getUser().getName() : username;

        if (username != null) {
            onlineUsers.remove(username);
            repository.findByUsername(username).ifPresent(user -> {
                user.setOnline(false);
                user.setLastSeen(LocalDateTime.now());
                repository.save(user);
            });
            messagingTemplate.convertAndSend("/topic/online", onlineUsers);
        }
    }
}
