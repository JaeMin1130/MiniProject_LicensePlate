package plate.back.lib;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class RateLimiter {
    private final int rateLimit = 5;
    private int tokens = rateLimit;
    private long lastRefillTimestamp = System.currentTimeMillis();;

    public synchronized boolean allowRequest() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            return true; // Request allowed
        } else {
            return false; // Request denied
        }
    }

    private void refillTokens() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastRefillTimestamp;
        int tokensToAdd = (int) (elapsedTime * rateLimit / (60 * 1000));
        if (tokensToAdd > 0) {
            tokens = Math.min(tokens + tokensToAdd, rateLimit);
            lastRefillTimestamp = currentTime;
        }
    }
}
