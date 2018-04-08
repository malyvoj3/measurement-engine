package engine.graph;

import engine.api.Job;

import java.util.UUID;

/**
 * Abstract wrapper for job
 */
public abstract class Node {

    protected final UUID uuid;
    protected final Job job;
    protected int inDegree;

    public Node(Job job) {
        this.job = job;
        this.uuid = job.getUuid();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Job getJob() {
        return job;
    }

    public int getInDegree() {
        return inDegree;
    }

    public void setInDegree(int inDegree) {
        this.inDegree = inDegree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return uuid.equals(node.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}
