package engine.strategies;

import engine.api.ExecutionStrategy;
import engine.api.Job;
import engine.api.JobListener;
import engine.test.TestAggregateJob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static engine.test.GraphGenerator.generateJob;
import static engine.test.GraphGenerator.generateJobWithoutListeners;
import static org.assertj.core.api.Assertions.assertThat;

public class PerformanceTests {

    private final int NODES_NUMBER = 2;
    private final int THREAD_POOL = 64;

    private TestAggregateJob graph;
    private ExecutorService executorService;
    private ExecutionStrategy strategy;

    private boolean finished[];
    private Date finishedTime[];

    @Before
    public void prepare() {
        graph = generateJobWithoutListeners(NODES_NUMBER);
        executorService = Executors.newFixedThreadPool(THREAD_POOL);

        JobListener jobListener = new JobListener() {
            public void jobStarted(Job<?> job) {

            }

            public void jobFinished(Job<?> job) {
                finished[0] = true;
                finished[1] = true;
                finishedTime[0] = new Date();
            }

            public void jobFailed(Job<?> job, Throwable t) {
                finished[0] = true;
                finishedTime[0] = new Date();
            }
        };
        graph.addJobListener(jobListener);
    }

    @After
    public void end() {
        executorService.shutdown();
    }

    @Test
    public void LayerStrategyTest() {
        finished = new boolean[] { false, false };
        finishedTime = new Date[1];
        strategy = new LayerExecutionStrategy(executorService);

        Date startTime = new Date();
        strategy.execute(graph);
        while (!finished[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(finishedTime[0].getTime() - startTime.getTime());
        assertThat(finished[1]).isTrue();
    }

    @Test
    public void TraversalStrategyTest() {
        finished = new boolean[] { false, false };
        finishedTime = new Date[1];
        strategy = new TraversalExecutionStrategy(executorService);

        Date startTime = new Date();
        strategy.execute(graph);
        while (!finished[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(finishedTime[0].getTime() - startTime.getTime());
        assertThat(finished[1]).isTrue();
    }

    @Test
    public void LatchStrategyTest() {
        finished = new boolean[] { false, false };
        finishedTime = new Date[1];
        strategy = new LatchExecutionStrategy(executorService);

        Date startTime = new Date();
        strategy.execute(graph);
        while (!finished[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(finishedTime[0].getTime() - startTime.getTime());
        assertThat(finished[1]).isTrue();
    }

    @Test
    public void ReactiveStrategyTest() {
        finished = new boolean[] { false, false };
        finishedTime = new Date[1];
        strategy = new ReactiveExecutionStrategy();

        Date startTime = new Date();
        strategy.execute(graph);
        while (!finished[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(finishedTime[0].getTime() - startTime.getTime());
        assertThat(finished[1]).isTrue();
    }

    @Test
    public void SequentialStrategyTest() {
        finished = new boolean[] { false, false };
        finishedTime = new Date[1];
        strategy = new SequentialExecutionStrategy();

        Date startTime = new Date();
        strategy.execute(graph);
        while (!finished[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(finishedTime[0].getTime() - startTime.getTime());
        assertThat(finished[1]).isTrue();
    }

}
