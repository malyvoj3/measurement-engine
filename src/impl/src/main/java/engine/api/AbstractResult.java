package engine.api;

/**
 * AbstractResult
 */
public abstract class AbstractResult implements Result {

    private final Job<? extends AbstractResult> job;

    public AbstractResult(Job<? extends AbstractResult> job) {
        this.job = job;
    }

    public Job<? extends AbstractResult> getJob() {
        return job;
    }
}
