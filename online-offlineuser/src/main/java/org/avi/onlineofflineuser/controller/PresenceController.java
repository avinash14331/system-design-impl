package org.avi.onlineofflineuser.controller;

import org.avi.onlineofflineuser.entity.UserPresence;
import org.avi.onlineofflineuser.repository.UserPresenceRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/presence")
public class PresenceController {

    private final UserPresenceRepository repository;

    public PresenceController(UserPresenceRepository repository) {
        this.repository = repository;
    }

//    @GetMapping("/online")
//    public List<UserPresence> getOnlineUsers() {
//        return repository.findAll()
//                .stream()
//                .filter(UserPresence::isOnline)
//                .toList();
//    }
    @GetMapping("/online")
    public List<UserPresence> getOnlineUsers(@AuthenticationPrincipal Principal principal) {
        System.out.println("Request made by: " + principal.getName());
        return repository.findAll()
                .stream()
                .filter(UserPresence::isOnline)
                .toList();
    }

    @GetMapping
    public List<UserPresence> getAllUsers() {
        return repository.findAll();
    }
}

