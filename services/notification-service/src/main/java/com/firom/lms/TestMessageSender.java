package com.firom.lms;

import com.firom.lms.producers.AuthNotificationProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestMessageSender implements CommandLineRunner {

    private final AuthNotificationProducer authProducer;

    public TestMessageSender(AuthNotificationProducer authProducer) {
        this.authProducer = authProducer;
    }

    @Override
    public void run(String... args) {
        authProducer.sendAuthNotification("<-------------------- User X logged in -------------------->");
    }
}
