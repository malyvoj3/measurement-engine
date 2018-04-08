package engine.graph;

import engine.api.Job;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Graph of jobs containing {@link CountDownLatch}
 */
public class LatchGraph {

    private HashMap<LatchNode, List<LatchNode>> map = new HashMap<>();
    private HashMap<UUID, LatchNode> nodeHelper = new HashMap<>();

    public LatchGraph(Job<?> job) {
        buildReversedGraph(job);
    }

    /**
     * Build inverted graph with nodes containing {@link CountDownLatch}
     * @param currentJob
     */
    private void buildReversedGraph(Job<?> currentJob) {
        LatchNode currentNode = nodeHelper.get(currentJob.getUuid());
        CountDownLatch countDownLatch;
        if (currentNode == null) {
            int childrenSize = currentJob.getChildren().size();
            countDownLatch = new CountDownLatch(childrenSize);
            currentNode = new LatchNode(currentJob, countDownLatch);
            currentNode.setInDegree(childrenSize);
            nodeHelper.put(currentNode.getUuid(), currentNode);
        }
        else {
            countDownLatch = currentNode.getLatch();
        }
        currentJob.getChildren().forEach(this::buildReversedGraph);
        if (!map.containsKey(currentNode)) {
            for (Job job : currentJob.getChildren()) {
                LatchNode tmp = nodeHelper.get(job.getUuid());
                tmp.addDecrement(countDownLatch);
                List<LatchNode> list = map.get(tmp);
                list.add(currentNode);
            }
            map.put(currentNode, new ArrayList<>());
        }
    }

    /**
     * Topologicall sort
     * @return Topologically sorted nodes
     */
    public List<LatchNode> topSort() {
        List<LatchNode> topSort = new ArrayList<>();
        Set<LatchNode> keyNodes = map.keySet();
        Queue<LatchNode> nodes = new LinkedList<>();

        for (LatchNode node : keyNodes) {
            if (node.getInDegree() == 0) {
                nodes.add(node);
                topSort.add(node);
            }
        }

        while(!nodes.isEmpty()) {
            LatchNode tmp = nodes.remove();
            map.get(tmp).forEach(node -> {
                node.setInDegree(node.getInDegree() - 1);
                if (node.getInDegree() == 0) {
                    nodes.add(node);
                    topSort.add(node);
                }
            });
        }
        return topSort;
    }

}
