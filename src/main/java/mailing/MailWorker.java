package mailing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class MailWorker implements MailQueueConsumer<MailTemplate> {

    private static final int DEFAULT_TIMEOUT = 0;

    private final LinkedBlockingQueue<MailTemplate> workerQueue = new LinkedBlockingQueue<>();
    private final Logger log = LoggerFactory.getLogger(MailWorker.class);

    private Thread thread;
    private boolean isRunning;
    private int sleepTimeout;

    MailWorker() {
        this.sleepTimeout = DEFAULT_TIMEOUT;
    }

    MailWorker(int sleepTimeout) {
        this.isRunning = true;
        this.sleepTimeout = sleepTimeout;
    }

    public void start() {
        this.isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void add(MailTemplate mailTemplate) {
        workerQueue.add(mailTemplate);
    }

    @Override
    public void run() {
        while (isRunning)
        {
            try {
                MailTemplate mailTemplate = workerQueue.take();
                consume(mailTemplate);

                if (sleepTimeout > 0) {
                    Thread.sleep(sleepTimeout);
                }
            } catch (InterruptedException e) {
                log.info("[{}] Thread interrupted", Thread.currentThread().getName());
            }
        }
    }

    @Override
    public void consume(MailTemplate mailTemplate) {
        log.info("[{}] Sending email (key: {})", Thread.currentThread().getName(), mailTemplate.getKey());
    }

    public void stop() throws InterruptedException {
        isRunning = false;
        thread.interrupt();
        thread.join();
    }
}
