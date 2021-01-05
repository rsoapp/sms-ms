package rsoapp.smsms.config;

import com.twilio.Twilio;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioInitializer {

    public TwilioInitializer() {
        Twilio.init("ACf8add95276ff13b228253820c6adaa1d", "228935f476f6a2ecd7003b8433e43097");
    }
}
