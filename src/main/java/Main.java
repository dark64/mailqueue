import mailing.MailQueueProcessor;
import mailing.MailTemplate;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        MailQueueProcessor mailQueueProcessor = new MailQueueProcessor();
        mailQueueProcessor.start();

        for (int i = 0; i < 10; i++) {
            mailQueueProcessor.add(new MailTemplate(String.valueOf(i)));
        }

        // sleep for a bit
        Thread.sleep(5000);

        mailQueueProcessor.stop();
    }
}
