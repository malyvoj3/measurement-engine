package engine.api;

/**
 * Interface for JobListeners
 */
public interface JobListener {

    /**
     * Is triggered before execution of job
     * @param job Started job
     */
    void jobStarted(Job<?> job);

    /**
     * Is triggered when job is successfully finished
     * @param job Finished job
     */
    void jobFinished(Job<?> job);

    /**
     * Is triggered if there is some problem during execution
     * @param job Failed job
     * @param t Throwable with root cause
     */
    void jobFailed(Job<?> job, Throwable t);

}
