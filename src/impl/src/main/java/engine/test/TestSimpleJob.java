package engine.test;

import engine.api.AbstractJob;

import java.util.Date;
import java.util.UUID;

/**
 * Job for testing with 300 ms execution
 */
public class TestSimpleJob extends AbstractJob<TestResult> {

    public TestSimpleJob(UUID uuid) {
        super(uuid);
    }

    @Override
    protected TestResult doExecute() throws Exception {
        TestResult result = new TestResult(this);
        sleep(300);
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
