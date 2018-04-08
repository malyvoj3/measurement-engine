package engine.graph;

import engine.api.Job;

import java.util.concurrent.Callable;

/**
 * BasicNode
 */
public class BasicNode extends Node implements Callable<Void> {

    /**
     * Defines number of its independent set
     */
    private int depth;

    public BasicNode(Job job) {
        super(job);
    }

    @Override
    public Void call() throws Exception {
        job.execute();
        return null;
    }

    public void execute() {
        job.execute();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

}
