package rsoapp.smsms.health;

import lombok.Setter;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@Setter
public class CustomHealthCheck implements HealthIndicator {

    private String state = "UP";

    @Override
    public Health health() {
        return state.equals("UP") ? Health.up().build() : Health.down().build();
    }
}
