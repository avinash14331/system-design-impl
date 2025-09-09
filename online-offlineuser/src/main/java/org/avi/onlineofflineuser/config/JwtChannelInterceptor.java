package org.avi.onlineofflineuser.config;

import org.avi.onlineofflineuser.util.JwtUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public JwtChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                String username = jwtUtil.validateTokenAndGetUsername(token.substring(7));

                if (username != null) {
                    accessor.setUser(new Principal() {
                        @Override
                        public String getName() {
                            return username;
                        }
                    });
                } else {
                    throw new IllegalArgumentException("Invalid JWT Token");
                }
            } else {
                throw new IllegalArgumentException("Missing Authorization header");
            }
        }
        return message;
    }
}
