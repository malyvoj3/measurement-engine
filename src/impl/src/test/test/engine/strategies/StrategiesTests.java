package engine.strategies;

import engine.api.ExecutionStrategy;
import engine.api.Job;
import engine.api.JobListener;
import engine.quality.jobs.AggregateCheckJob;
import engine.quality.jobs.ParametrizedSQLJob;
import engine.quality.jobs.SQLJob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class StrategiesTests {

    private final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private final String USERNAME = "malyvoj3";
    private final String PASS = "123456789";
    private Connection connection;
    private boolean finished[];
    private JobListener listener;


    @Before
    public void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        connection = DriverManager.getConnection(URL, USERNAME, PASS);

        listener = new JobListener() {
            public void jobStarted(Job<?> job) {

            }

            public void jobFinished(Job<?> job) {
                finished[0] = true;
                finished[1] = true;
            }

            public void jobFailed(Job<?> job, Throwable t) {
                finished[0] = true;
            }
        };
    }

    @After
    public void closeDB() throws SQLException {
        connection.close();
    }

    private AggregateCheckJob generateSimpleJob() {
        AggregateCheckJob job = new AggregateCheckJob(UUID.randomUUID());

        SQLJob sqlJob1;
        SQLJob sqlJob2;
        sqlJob1 = new SQLJob(UUID.randomUUID(), connection, "select avg(LIST_PRICE) from DEMO_PRODUCT_INFO");
        sqlJob2 = new SQLJob(UUID.randomUUID(), connection, "select max(LIST_PRICE) from DEMO_PRODUCT_INFO");

        ParametrizedSQLJob parametrizedSQLJob = new ParametrizedSQLJob(UUID.randomUUID(), connection, "select count(*) from DEMO_PRODUCT_INFO t where (t.LIST_PRICE > {0} and t.LIST_PRICE < {1})");
        parametrizedSQLJob.addChild(sqlJob1);
        parametrizedSQLJob.addChild(sqlJob2);

        job.addChild(parametrizedSQLJob);
        job.addWeight(20L);
        job.addChild(sqlJob2);
        job.addWeight(2L);

        job.addJobListener(listener);

        return job;
    }

    private Job generateComplexJob() {
        AggregateCheckJob agr1 = new AggregateCheckJob(UUID.randomUUID());
        AggregateCheckJob agr2 = new AggregateCheckJob(UUID.randomUUID());
        AggregateCheckJob agr3 = new AggregateCheckJob(UUID.randomUUID());

        SQLJob sqlJob1 = new SQLJob(UUID.randomUUID(), connection, "select avg(LIST_PRICE) from DEMO_PRODUCT_INFO");
        SQLJob sqlJob2 = new SQLJob(UUID.randomUUID(), connection, "select max(LIST_PRICE) from DEMO_PRODUCT_INFO");
        ParametrizedSQLJob parametrizedSQLJob = new ParametrizedSQLJob(UUID.randomUUID(), connection, "select count(*) from DEMO_PRODUCT_INFO t where (t.LIST_PRICE > {0} and t.LIST_PRICE < {1})");
        parametrizedSQLJob.addChild(sqlJob1);
        parametrizedSQLJob.addChild(sqlJob2);
        agr1.addChild(parametrizedSQLJob);
        agr1.addWeight(20L);
        agr1.addChild(sqlJob2);
        agr1.addWeight(2L);


        SQLJob sqlJob3 = new SQLJob(UUID.randomUUID(), connection, "select count(*) from DEMO_ORDERS");
        SQLJob sqlJob4 = new SQLJob(UUID.randomUUID(), connection, "select count(*) from DEMO_USERS");
        agr2.addChild(sqlJob2);
        agr2.addWeight(1L);
        agr2.addChild(sqlJob3);
        agr2.addWeight(2L);
        agr2.addChild(sqlJob4);
        agr2.addWeight(5L);

        agr3.addChild(parametrizedSQLJob);
        agr3.addWeight(10L);
        agr3.addChild(agr1);
        agr3.addWeight(1L);
        agr3.addChild(agr2);
        agr3.addWeight(1L);

        agr3.addJobListener(listener);

        return agr3;
    }

    @Parameterized.Parameter(value = 0)
    public ExecutionStrategy strategy;

    @Parameterized.Parameter(value = 1)
    public String strategyName;

    @Parameterized.Parameters(name = "{index}: Testing {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new LayerExecutionStrategy(), "LayerExecutionStrategy"},
                {new TraversalExecutionStrategy(), "TraversalExecutionStrategy"},
                {new LatchExecutionStrategy(), "LatchExecutionStrategy"},
                {new ReactiveExecutionStrategy(), "ReactiveExecutionStrategy"},
                {new SequentialExecutionStrategy(), "SequentialExecutionStrategy"},
        });
    }

    @Test
    public void simpleTest() {
        finished = new boolean[] { false, false };

        Job job = generateSimpleJob();
        strategy.execute(job);
        while (!finished[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertThat(job.getResult())
                .isNotNull()
                .hasFieldOrPropertyWithValue("resultValue", "180.0");
    }

    @Test
    public void complexTest() {
        finished = new boolean[] { false, false };

        Job job = generateComplexJob();
        strategy.execute(job);
        while (!finished[0]) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertThat(job.getResult())
                .isNotNull()
                .hasFieldOrPropertyWithValue("resultValue", "90.0");
    }

}
