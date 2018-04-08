package engine.test;

import engine.quality.listeners.ProgressMonitoringListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Generator of graphs
 */
public class GraphGenerator {

    /**
     * Generate graph without listeners
     * @param n
     * @return graph with n^3+n^2+n+1 nodes
     */
    public static TestAggregateJob generateJob(int n) {
        int aggregateCount = n;
        List<TestAggregateJob> agr = new ArrayList<>();
        List<TestParametrizedJob> param = new ArrayList<>();
        List<TestSimpleJob> simple = new ArrayList<>();
        TestAggregateJob root = new TestAggregateJob(UUID.fromString("11111111-1868-11e7-93ae-92361f002671"));

        ProgressMonitoringListener listener = new ProgressMonitoringListener();

        int parametrizedCount = aggregateCount * aggregateCount;
        int simpleCount = parametrizedCount * aggregateCount;

        for (int i = 0; i < simpleCount; i++) {
            TestSimpleJob job = new TestSimpleJob(UUID.randomUUID());
            job.addJobListener(listener);
            simple.add(job);
        }

        int currentSimple = 0;
        for (int i = 0; i < parametrizedCount; i++) {
            TestParametrizedJob job = new TestParametrizedJob(UUID.randomUUID());
            job.addJobListener(listener);
            for (int k = currentSimple; k < currentSimple + aggregateCount; k++) {
                job.addChild(simple.get(k));
            }
            currentSimple += aggregateCount;
            param.add(job);
        }

        int currentParam = 0;
        for (int i = 0; i < aggregateCount; i++) {
            TestAggregateJob job = new TestAggregateJob(UUID.randomUUID());
            job.addJobListener(listener);
            for (int k = currentParam; k < currentParam + aggregateCount; k++) {
                job.addChild(param.get(k));
            }
            currentParam += aggregateCount;
            agr.add(job);
        }

        root.addChildren(agr);
        root.addJobListener(listener);

        return root;
    }

    /**
     * Generate graph without listeners
     * @param n
     * @return graph with n^3+n^2+n+1 nodes
     */
    public static TestAggregateJob generateJobWithoutListeners(int n) {
        int aggregateCount = n;
        List<TestAggregateJob> agr = new ArrayList<>();
        List<TestParametrizedJob> param = new ArrayList<>();
        List<TestSimpleJob> simple = new ArrayList<>();
        TestAggregateJob root = new TestAggregateJob(UUID.fromString("11111111-1868-11e7-93ae-92361f002671"));

        int parametrizedCount = aggregateCount * aggregateCount;
        int simpleCount = parametrizedCount * aggregateCount;

        for (int i = 0; i < simpleCount; i++) {
            TestSimpleJob job = new TestSimpleJob(UUID.randomUUID());
            simple.add(job);
        }

        int currentSimple = 0;
        for (int i = 0; i < parametrizedCount; i++) {
            TestParametrizedJob job = new TestParametrizedJob(UUID.randomUUID());
            for (int k = currentSimple; k < currentSimple + aggregateCount; k++) {
                job.addChild(simple.get(k));
            }
            currentSimple += aggregateCount;
            param.add(job);
        }

        int currentParam = 0;
        for (int i = 0; i < aggregateCount; i++) {
            TestAggregateJob job = new TestAggregateJob(UUID.randomUUID());
            for (int k = currentParam; k < currentParam + aggregateCount; k++) {
                job.addChild(param.get(k));
            }
            currentParam += aggregateCount;
            agr.add(job);
        }

        root.addChildren(agr);

        return root;
    }

    /**
     * Generate graph which is list
     * @param length
     * @return list graph with 2 + length nodes
     */
    public static TestAggregateJob generateList(int length) {
        TestAggregateJob root = new TestAggregateJob(UUID.fromString("11111111-1868-11e7-93ae-92361f002671"));
        TestSimpleJob leaf = new TestSimpleJob(UUID.randomUUID());

        if (length == 0) {
            root.addChild(leaf);
            return root;
        }

        TestParametrizedJob param = new TestParametrizedJob(UUID.randomUUID());
        root.addChild(param);

        if (length == 1) {
            param.addChild(leaf);
            return root;
        }

        List<TestParametrizedJob> params = new ArrayList<>();
        params.add(param);

        for (int i = 1; i < length; i++) {
            TestParametrizedJob tmp = new TestParametrizedJob(UUID.randomUUID());
            params.add(tmp);
            params.get(i - 1).addChild(tmp);
        }
        params.get(length - 1).addChild(leaf);

        return root;
    }

}
