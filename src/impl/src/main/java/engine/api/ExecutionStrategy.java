package engine.api;

/**
 * Interface which executes job
 */
public interface ExecutionStrategy {

    /**
     * Execute the job
     * @param job Job which would be execute
     */
    void execute(Job job);

}
