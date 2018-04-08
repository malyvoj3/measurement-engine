package engine.quality.jobs;

import engine.api.AbstractContainerJob;
import engine.api.Job;
import engine.quality.results.StringResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Job which computes weighted mean from it's children
 */
public class AggregateCheckJob extends AbstractContainerJob<StringResult, StringResult> {

    private final List<Long> weights = new ArrayList<>();

    public AggregateCheckJob(UUID uuid) {
        super(uuid);
    }

    public void addWeight(Long weight) {
        weights.add(weight);
    }

    @Override
    protected StringResult doExecute() {
        StringResult aggregateCheckResult = new StringResult(this);

        List<Job<StringResult>> children = getChildren();
        int size = children.size();
        Double weightedSum = 0.0;
        StringResult jobResult;

        for (int i = 0; i < size; i++) {
            jobResult = children.get(i).getResult();
            if (jobResult != null) {
                weightedSum += weights.get(i) * Double.valueOf(jobResult.getResultValue());
            } else {
                throw new IllegalArgumentException("Result cannot be created if all children results are not computed.");
            }
        }

        Double weightedMean = weightedSum / size;
        aggregateCheckResult.setResultValue(weightedMean.toString());
        return aggregateCheckResult;
    }

}
