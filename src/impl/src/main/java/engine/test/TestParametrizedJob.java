package engine.test;

import engine.api.AbstractContainerJob;
import engine.api.Job;

import java.util.Date;
import java.util.UUID;

/**
 * Job for testing with 500 ms execution
 */
public class TestParametrizedJob extends AbstractContainerJob<TestResult, TestResult> {

    public TestParametrizedJob(UUID uuid) {
        super(uuid);
    }

    @Override
    protected TestResult doExecute() throws Exception {
        TestResult result = new TestResult(this);
        for (Job<TestResult> child : getChildren()) {
            if (child.getResult() == null) {
                throw new IllegalArgumentException("Result cannot be created if all children results are not computed.");
            }
        }
        sleep(500);
        result.setResultValue(getUuid().toString());
        return result;
    }

    /**
     * Blocking function as database query
     * @param duration How long should be the thread blocked
     */
    private void sleep(long duration) {
        boolean sleep = true;
        long startTime = new Date().getTime();
        while (sleep) {
            if ((new Date().getTime()) - startTime >= duration) {
                sleep = false;
            }
        }
    }

}
