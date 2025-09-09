package org.avi.onlineofflineuser.repository;

import org.avi.onlineofflineuser.entity.UserPresence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserPresenceRepository extends JpaRepository<UserPresence, Long> {
    Optional<UserPresence> findByUsername(String username);
}
