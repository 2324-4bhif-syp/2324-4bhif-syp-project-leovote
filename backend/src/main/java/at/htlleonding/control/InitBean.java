package at.htlleonding.control;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

public class InitBean {
    @Transactional
    void startUp(@Observes StartupEvent event) {
        Log.info("It is working!");
    }
}
