package engine.quality.results;

import engine.api.AbstractResult;
import engine.api.Job;

/**
 * Result which has just one string
 */
public class StringResult extends AbstractResult {

    private String resultValue;

    public StringResult(Job<? extends AbstractResult> job) {
        super(job);
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }
}
