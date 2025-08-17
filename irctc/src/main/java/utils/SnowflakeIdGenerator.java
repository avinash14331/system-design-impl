package utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdGenerator {

    private static final long CUSTOM_EPOCH = 1659312000000L; // Set your custom epoch

    @Value("${snowflake.node-id}")
    private long nodeId; // Injected from env or config

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    @PostConstruct
    public void init() {
        if (nodeId < 0 || nodeId > 1023) {
            throw new IllegalArgumentException("NodeId must be between 0 and 1023");
        }
    }

    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & 0xFFF; // 12 bits
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - CUSTOM_EPOCH) << 22) | (nodeId << 12) | sequence;
    }

    private long waitNextMillis(long timestamp) {
        while (timestamp == lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}

