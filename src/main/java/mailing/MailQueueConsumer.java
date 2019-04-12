package mailing;

public interface MailQueueConsumer<T> extends Runnable {

    void consume(T value);
}
