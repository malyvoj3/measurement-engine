package engine;

import engine.api.Job;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Queue for jobs
 */
@Component
public class JobQueue {

    private BlockingQueue<Job> queue = new LinkedBlockingQueue<>();

    /**
     * Add job to queue for execution
     * @param job Job that should be executed
     */
    public void push(Job job) {
        try {
            queue.put(job);
        } catch (InterruptedException ex) {
            throw new IllegalStateException("Job queue error", ex);
        }
    }

    /**
     * Take a job from queue
     */
    public Job pop() {
        try {
            return queue.take();
        } catch (InterruptedException ex) {
            throw new IllegalStateException("Job queue error", ex);
        }
    }

    /**
     * Test queue if has some job
     * @return If queue is empty or not
     */
    boolean isEmpty() {
        return queue.isEmpty();
    }

}
