package engine;

import engine.api.ExecutionStrategy;
import engine.api.Job;
import engine.strategies.LatchExecutionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;

/**
 * Component which takes in infinite cycle jobs from queue
 */
@Component
public class LaunchService extends Thread {

    private final JobQueue jobQueue;

    /**
     * ExecutionStrategy for launching jobs
     */
    private ExecutionStrategy executionStrategy = new LatchExecutionStrategy(Executors.newFixedThreadPool(200));

    @Autowired
    public LaunchService(JobQueue jobQueue) {
        Assert.notNull(jobQueue, "Job Queue must not be null");
        this.jobQueue = jobQueue;
    }

    @PostConstruct
    public void init() {
        start();
    }

    @Override
    public void run() {
        while (true) {
            Job job = jobQueue.pop();
            executionStrategy.execute(job);
        }
    }


}
