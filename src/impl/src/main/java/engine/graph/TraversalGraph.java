package engine.graph;

import engine.api.Job;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Graph of jobs
 */
public class TraversalGraph {

    private HashSet<TraversalNode> set = new HashSet<>();
    private HashMap<UUID, TraversalNode> nodeHelper = new HashMap<>();
    private final ExecutorService executorService;

    public TraversalGraph(Job<?> job, ExecutorService executorService) {
        this.executorService = executorService;
        buildGraph(job);
    }

    private void buildGraph(Job<?> currentJob) {
        TraversalNode currentNode = nodeHelper.get(currentJob.getUuid());
        if (currentNode == null) {
            currentNode = new TraversalNode(currentJob, executorService);
            nodeHelper.put(currentNode.getUuid(), currentNode);
        }

        if (!set.contains(currentNode)) {
            currentNode.setInDegree(currentJob.getChildren().size());
            for (Job job : currentJob.getChildren()) {
                TraversalNode tmp = nodeHelper.get(job.getUuid());
                if (tmp == null) {
                    tmp = new TraversalNode(job, executorService);
                    nodeHelper.put(tmp.getUuid(), tmp);
                }
                tmp.addSuccessor(currentNode);
            }
            set.add(currentNode);
            currentJob.getChildren().forEach(this::buildGraph);
        }
    }

    public List<TraversalNode> getRoots() {
        return set.stream()
                .filter(forkNode -> forkNode.getInDegree() == 0)
                .collect(Collectors.toList());
    }


}
