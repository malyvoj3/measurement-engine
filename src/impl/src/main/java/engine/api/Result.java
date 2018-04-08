package engine.api;

/**
 * Interface for job's results
 */
public interface Result {

    Job<? extends Result> getJob();

}
