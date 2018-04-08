package engine.graph;

import engine.api.Job;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Graph of jobs using Reactor
 */
public class ReactiveGraph {

    private HashSet<ReactiveNode> set = new HashSet<>();
    private HashMap<UUID, ReactiveNode> nodeHelper = new HashMap<>();

    public ReactiveGraph(Job<?> job) {
        buildGraph(job);
    }

    private void buildGraph(Job<?> currentJob) {
        ReactiveNode currentNode = nodeHelper.get(currentJob.getUuid());
        if (currentNode == null) {
            currentNode = new ReactiveNode(currentJob);
            nodeHelper.put(currentNode.getUuid(), currentNode);
        }

        if (!set.contains(currentNode)) {
            currentNode.setInDegree(currentJob.getChildren().size());
            for (Job job : currentJob.getChildren()) {
                ReactiveNode tmp = nodeHelper.get(job.getUuid());
                if (tmp == null) {
                    tmp = new ReactiveNode(job);
                    nodeHelper.put(tmp.getUuid(), tmp);
                }
                tmp.addSuccessor(currentNode);
            }
            set.add(currentNode);
            currentJob.getChildren().forEach(this::buildGraph);
        }
    }

    public List<ReactiveNode> getRoots() {
        return set.stream()
                .filter(forkNode -> forkNode.getInDegree() == 0)
                .collect(Collectors.toList());
    }


}
