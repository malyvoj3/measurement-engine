package engine.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Abstract job, which needs other jobs for it's execution
 * @param <R> Result type of job
 * @param <R2> Result type of children
 */
public abstract class AbstractContainerJob<R extends Result, R2 extends Result> extends AbstractJob<R> {

    public AbstractContainerJob(UUID uuid) {
        super(uuid);
    }

    private final List<Job<R2>> children = new ArrayList<Job<R2>>();

    @Override
    public List<Job<R2>> getChildren() {
        return children;
    }

    /**
     * Add child to current job
     * @param child
     */
    public void addChild(Job<R2> child) {
        children.add(child);
    }

    /**
     * Add children to current job
     * @param child
     */
    public void addChildren(Collection<? extends Job<R2>> child) {
        children.addAll(child);
    }


}
