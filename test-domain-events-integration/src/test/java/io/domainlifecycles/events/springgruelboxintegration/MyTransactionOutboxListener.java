package io.domainlifecycles.events.springgruelboxintegration;

import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
import com.gruelbox.transactionoutbox.TransactionOutboxListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyTransactionOutboxListener implements TransactionOutboxListener {

    public List<TransactionOutboxEntry> successfulEntries = new ArrayList<>();
    public List<TransactionOutboxEntry> blockedEntries = new ArrayList<>();

    @Override
    public void success(TransactionOutboxEntry entry) {
        log.info("Entry '{}' processed successfully!", entry);
        successfulEntries.add(entry);
    }

    @Override
    public void failure(TransactionOutboxEntry entry, Throwable cause) {
        log.error("Entry '{}' failed!", entry, cause);
    }

    @Override
    public void blocked(TransactionOutboxEntry entry, Throwable cause) {
        log.error("Entry '{}' blocked!", entry, cause);
        blockedEntries.add(entry);
    }
}
