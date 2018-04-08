package engine.strategies;

import engine.api.ExecutionStrategy;
import engine.api.Job;
import engine.graph.TraversalGraph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Strategy using {@link TraversalGraph} for parallel graph traversal
 */
public class TraversalExecutionStrategy implements ExecutionStrategy {

    private final ExecutorService executorService;

    public TraversalExecutionStrategy() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    public TraversalExecutionStrategy(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void execute(Job job) {
        TraversalGraph graph = new TraversalGraph(job, executorService);
        graph.getRoots().forEach(executorService::submit);
    }
}
