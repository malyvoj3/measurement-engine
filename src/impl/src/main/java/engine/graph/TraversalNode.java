package engine.graph;

import engine.api.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Graph node for {@link TraversalGraph} using parallel traversal execution
 */
public class TraversalNode extends Node implements Runnable {

    private int finishedPredecessors;
    private final List<TraversalNode> successors = new ArrayList<>();
    private final ExecutorService executorService;

    public TraversalNode(Job job, ExecutorService executorService) {
        super(job);
        this.executorService = executorService;
    }

    @Override
    public void run() {
        synchronized (this) {
            if (++finishedPredecessors < inDegree) {
                return;
            }
        }
        job.execute();
        successors.forEach(executorService::submit);
    }

    public int getFinishedPredecessors() {
        return finishedPredecessors;
    }

    public void setFinishedPredecessors(int finishedPredecessors) {
        this.finishedPredecessors = finishedPredecessors;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void addSuccessor(TraversalNode node) {
        successors.add(node);
    }

}
