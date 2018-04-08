package engine.quality.listeners;

import engine.api.Job;
import engine.api.JobListener;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Schedulers;

/**
 * Reactive listener using {@link Mono} for printing info
 */
public class ReactiveMessageListener implements JobListener {

    static final Logger log = LoggerFactory.getLogger(ReactiveMessageListener.class);
    private final JobStartedSubscriber subscriber = new JobStartedSubscriber();

    @Override
    public void jobStarted(Job<?> job) {
        Mono.just(job.getUuid())
                //enable asynchronous events
                .subscribeOn(Schedulers.elastic())
                .subscribe(subscriber);
    }

    @Override
    public void jobFinished(Job<?> job) {
        Mono.just(job.getResult())
                .subscribeOn(Schedulers.elastic())
                .subscribe(System.out::println);
    }

    @Override
    public void jobFailed(Job<?> job, Throwable t) {
        Mono.just(job.getUuid())
                .subscribeOn(Schedulers.elastic())
                .subscribe(uuid -> System.err.println("Failed:" + uuid));
    }
}
