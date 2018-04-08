package engine.quality.listeners;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.UUID;

/**
 * Example of subscriber for reactive listener
 */
public class JobStartedSubscriber implements Subscriber<UUID> {

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(UUID uuid) {
        //can be done something more complicated
        System.out.println(uuid);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
