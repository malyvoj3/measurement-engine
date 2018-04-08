package engine.strategies;

import engine.api.ExecutionStrategy;
import engine.api.Job;
import engine.graph.LatchGraph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Strategy using {@link LatchGraph} for parallel execution of topological sort
 */
public class LatchExecutionStrategy implements ExecutionStrategy {

    private final ExecutorService executorService;

    public LatchExecutionStrategy() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    public LatchExecutionStrategy(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void execute(Job job) {
        LatchGraph graph = new LatchGraph(job);
        graph.topSort().forEach(executorService::submit);
    }


}
