package io.domainlifecycles.events.springgruelbox;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class GruelboxBackgroundProcessor {

    private final TransactionOutbox outbox;

    GruelboxBackgroundProcessor(TransactionOutbox outbox) {
        this.outbox = outbox;
    }

    @Scheduled(initialDelayString = "PT3S", fixedRateString = "PT1S")
    void poll() {
        try {
            do {
                log.info("Flushing");
            } while (outbox.flush());
        } catch (Throwable t) {
            log.error("Error flushing transaction outbox. Pausing", t);
        }
    }
}
