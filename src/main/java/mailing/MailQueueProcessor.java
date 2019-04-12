package mailing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailQueueProcessor {

    private static final Logger log = LoggerFactory.getLogger(MailQueueProcessor.class);
    private static final int MAX_WORKERS = 4;

    private MailWorker[] mailWorkers;
    private int current = 0;

    public MailQueueProcessor() {
        log.info("Initialized {}", this.getClass().getSimpleName());
    }

    public void start() {
        log.info("Initializing workers ({})", MAX_WORKERS);
        initializeWorkers();
    }

    private void initializeWorkers() {
        mailWorkers = new MailWorker[MAX_WORKERS];
        for (int i = 0; i < MAX_WORKERS; i++) {
            mailWorkers[i] = new MailWorker(1000);
            mailWorkers[i].start();
        }
    }

    public void add(MailTemplate mailTemplate) {
        mailWorkers[current++].add(mailTemplate);
        current = current % MAX_WORKERS;
    }

    public void stop() {
        for (int i = 0; i < mailWorkers.length; i++) {
            try {
                log.info("Stopping worker ({})", i);
                mailWorkers[i].stop();
            } catch (InterruptedException e) {
                log.warn("[{}] Thread interrupted", Thread.currentThread().getName());
            }
        }
    }
}
