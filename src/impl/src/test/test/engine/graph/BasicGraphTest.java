package engine.graph;

import engine.quality.jobs.AggregateCheckJob;
import engine.quality.jobs.ParametrizedSQLJob;
import engine.quality.jobs.SQLJob;
import org.junit.Test;

import java.util.List;

import static engine.graph.GraphTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BasicGraphTest {

    @Test
    public void returnCorrectTopologicalSort() {
        AggregateCheckJob agr1 = new AggregateCheckJob(UUID1);
        AggregateCheckJob agr2 = new AggregateCheckJob(UUID2);
        ParametrizedSQLJob pSql = new ParametrizedSQLJob(UUID3, null, null);
        SQLJob sql1 = new SQLJob(UUID4, null, null);
        SQLJob sql2 = new SQLJob(UUID5, null, null);
        SQLJob sql3 = new SQLJob(UUID6, null, null);
        SQLJob sql4 = new SQLJob(UUID7, null, null);
        SQLJob sql5 = new SQLJob(UUID8, null, null);

        agr1.addChild(pSql);
        agr1.addChild(agr2);
        agr1.addChild(sql1);
        agr1.addChild(sql2);

        agr2.addChild(pSql);
        agr2.addChild(sql5);
        agr2.addChild(sql2);

        pSql.addChild(sql3);
        pSql.addChild(sql4);

        BasicGraph graph = new BasicGraph(agr1);

        List<BasicNode> topSort = graph.topSort();
        assertThat(topSort).hasSize(8);
        //first five can have different orders
        assertThat(topSort.get(5))
                .hasFieldOrPropertyWithValue("job", pSql);
        assertThat(topSort.get(6))
                .hasFieldOrPropertyWithValue("job", agr2);
        assertThat(topSort.get(7))
                .hasFieldOrPropertyWithValue("job", agr1);
    }

    @Test
    public void returnCorrectDepthSet() {
        AggregateCheckJob agr1 = new AggregateCheckJob(UUID1);
        AggregateCheckJob agr2 = new AggregateCheckJob(UUID2);
        ParametrizedSQLJob pSql = new ParametrizedSQLJob(UUID3, null, null);
        SQLJob sql1 = new SQLJob(UUID4, null, null);
        SQLJob sql2 = new SQLJob(UUID5, null, null);
        SQLJob sql3 = new SQLJob(UUID6, null, null);
        SQLJob sql4 = new SQLJob(UUID7, null, null);
        SQLJob sql5 = new SQLJob(UUID8, null, null);

        agr1.addChild(pSql);
        agr1.addChild(agr2);
        agr1.addChild(sql1);
        agr1.addChild(sql2);

        agr2.addChild(pSql);
        agr2.addChild(sql5);
        agr2.addChild(sql2);

        pSql.addChild(sql3);
        pSql.addChild(sql4);

        BasicGraph graph = new BasicGraph(agr1);

        List<List<BasicNode>> topSort = graph.getLayers();
        assertThat(topSort).hasSize(4);
        assertThat(topSort.get(0))
                .hasSize(5);
        assertThat(topSort.get(1))
                .hasSize(1);
        assertThat(topSort.get(1).get(0))
                .hasFieldOrPropertyWithValue("job", pSql);
        assertThat(topSort.get(2))
                .hasSize(1);
        assertThat(topSort.get(2).get(0))
                .hasFieldOrPropertyWithValue("job", agr2);
        assertThat(topSort.get(3))
                .hasSize(1);
        assertThat(topSort.get(3).get(0))
                .hasFieldOrPropertyWithValue("job", agr1);
    }

}
