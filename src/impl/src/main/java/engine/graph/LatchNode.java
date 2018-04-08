package engine.graph;

import engine.api.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Node for {@link LatchGraph}
 */
public class LatchNode extends Node implements Runnable {

    private final CountDownLatch latch;
    private List<CountDownLatch> decrements = new ArrayList<>();

    public LatchNode(Job job, CountDownLatch latch) {
        super(job);
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            latch.await();
            job.execute();
            decrements.forEach(CountDownLatch::countDown);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public List<CountDownLatch> getDecrements() {
        return decrements;
    }

    public void addDecrement(CountDownLatch decrement) {
        this.decrements.add(decrement);
    }

}
