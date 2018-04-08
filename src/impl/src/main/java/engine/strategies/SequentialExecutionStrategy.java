package engine.strategies;

import engine.api.ExecutionStrategy;
import engine.api.Job;
import engine.graph.BasicGraph;
import engine.graph.BasicNode;

/**
 * Strategy using {@link BasicGraph} for sequential execution of topological sort
 */
public class SequentialExecutionStrategy implements ExecutionStrategy {

    public void execute(Job job) {
        BasicGraph graph = new BasicGraph(job);
        graph.topSort().forEach(BasicNode::execute);
    }
}
