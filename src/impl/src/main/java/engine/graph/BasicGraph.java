package engine.graph;

import engine.api.Job;

import java.util.*;

/**
 * Basic graph of jobs
 */
public class BasicGraph {

    private HashMap<BasicNode, List<BasicNode>> map = new HashMap<>();
    private HashMap<UUID, BasicNode> nodeHelper = new HashMap<>();

    public BasicGraph(Job<?> job) {
        buildReversedGraph(job);
    }

    /**
     * Build inverted graph from job
     * @param currentJob
     */
    private void buildReversedGraph(Job<?> currentJob) {
        BasicNode currentNode = nodeHelper.get(currentJob.getUuid());
        if (currentNode == null) {
            currentNode = new BasicNode(currentJob);
            currentNode.setInDegree(currentJob.getChildren().size());
            nodeHelper.put(currentNode.getUuid(), currentNode);
        }
        currentJob.getChildren().forEach(this::buildReversedGraph);
        if (!map.containsKey(currentNode)) {
            for (Job job : currentJob.getChildren()) {
                List<BasicNode> list = map.get(nodeHelper.get(job.getUuid()));
                list.add(currentNode);
            }
            map.put(currentNode, new ArrayList<>());
        }
    }

    /**
     * Topological sort via removing roots
     * @return Topologically sorted nodes
     */
    public List<BasicNode> topSort() {
        List<BasicNode> topSort = new ArrayList<>();
        Set<BasicNode> keyNodes = map.keySet();
        Queue<BasicNode> nodes = new LinkedList<>();

        for (BasicNode node : keyNodes) {
            if (node.getInDegree() == 0) {
                nodes.add(node);
                topSort.add(node);
            }
        }

        while(!nodes.isEmpty()) {
            BasicNode tmp = nodes.remove();
            map.get(tmp).forEach(node -> {
                node.setInDegree(node.getInDegree() - 1);
                if (node.getInDegree() == 0) {
                    node.setDepth(tmp.getDepth() + 1);
                    nodes.add(node);
                    topSort.add(node);
                }
            });
        }
        return topSort;
    }

    /**
     * Creating independent sets
     * @return Independent sets
     */
    public List<List<BasicNode>> getLayers() {
        List<List<BasicNode>> layers = new ArrayList<>();
        List<BasicNode> topSort = topSort();

        int maxDepth = 0;
        for (BasicNode node : topSort) {
            if (maxDepth < node.getDepth()) {
                maxDepth = node.getDepth();
            }
        }

        for (int i = 0; i <= maxDepth; i++) {
            layers.add(new ArrayList<>());
        }

        for (BasicNode node : topSort) {
            layers.get(node.getDepth()).add(node);
        }

        return layers;
    }

}
