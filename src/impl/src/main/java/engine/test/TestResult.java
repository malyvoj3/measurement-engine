package engine.test;

import engine.api.AbstractResult;
import engine.api.Job;

/**
 * Test result containing string value
 */
public class TestResult extends AbstractResult {

    private String resultValue;

    public TestResult(Job<? extends AbstractResult> job) {
        super(job);
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "resultValue='" + resultValue + '\'' +
                '}';
    }
}
