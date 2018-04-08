package engine.strategies;

import engine.api.ExecutionStrategy;
import engine.api.Job;
import engine.graph.BasicGraph;
import engine.graph.BasicNode;

import java.util.List;
import java.util.concurrent.*;

/**
 * Strategy using {@link BasicGraph} for parallel execution of independent sets
 */
public class LayerExecutionStrategy implements ExecutionStrategy {

    private final CompletionService<Void> completionService;

    public LayerExecutionStrategy() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        this.completionService = new ExecutorCompletionService<>(executorService);
    }

    public LayerExecutionStrategy(ExecutorService executorService) {
        this.completionService = new ExecutorCompletionService<>(executorService);
    }

    @Override
    public void execute(Job job) {
        BasicGraph graph = new BasicGraph(job);
        List<List<BasicNode>> layers = graph.getLayers();
        for (List<BasicNode> layer : layers) {
            int done = 0;
            layer.forEach(completionService::submit);
            while(done < layer.size()) {
                try {
                    completionService.take();
                    done++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
