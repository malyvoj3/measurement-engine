package engine.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Abstract implementation of job
 * @param <R> Type of result
 */
public abstract class AbstractJob<R extends Result> implements Job<R> {

    private final UUID uuid;
    private R result;
    private final List<JobListener> listeners = new ArrayList<JobListener>();

    public AbstractJob(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public R getResult() {
        return result;
    }

    /**
     *
     * @return Empty list
     */
    public List<? extends Job> getChildren() {
        return Collections.emptyList();
    }

    public void addJobListener(JobListener listener) {
        listeners.add(listener);
    }

    public void removeJobListener(JobListener listener) {
        listeners.remove(listener);
    }

    protected void fireStarted() {
        for (JobListener l : listeners) {
            l.jobStarted(this);
        }
    }

    protected void fireFinished() {
        for (JobListener l : listeners) {
            l.jobFinished(this);
        }
    }

    protected void fireFailed(Throwable t) {
        for (JobListener l : listeners) {
            l.jobFailed(this, t);
        }
    }

    /**
     * Execution of job
     */
    public void execute() {
        fireStarted();
        try {
            result = doExecute();
            fireFinished();
        } catch (Throwable t) {
            fireFailed(t);
        }
    }

    /**
     * Abstract method which should contains job execution logic
     * @return
     * @throws Exception
     */
    abstract protected R doExecute() throws Exception;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractJob<?> that = (AbstractJob<?>) o;

        return uuid.equals(that.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
