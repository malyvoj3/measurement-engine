package engine.api;

import java.util.List;
import java.util.UUID;

/**
 * Basic interface for a job
 * @param <R> Result of this job
 */
public interface Job<R extends Result> {

    UUID getUuid();

    R getResult();

    List<? extends Job> getChildren();

    /**
     * Execute job
     */
    void execute();

    /**
     * Add listener
     * @param listener
     */
    void addJobListener(JobListener listener);

    /**
     * Remove listener
     * @param listener
     */
    void removeJobListener(JobListener listener);

}
