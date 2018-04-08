package engine.graph;

import engine.quality.jobs.AggregateCheckJob;
import engine.quality.jobs.ParametrizedSQLJob;
import engine.quality.jobs.SQLJob;
import org.junit.Test;

import java.util.List;

import static engine.graph.GraphTestUtils.*;
import static engine.graph.GraphTestUtils.UUID8;
import static org.assertj.core.api.Assertions.assertThat;

public class LatchGraphTest {

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

        LatchGraph graph = new LatchGraph(agr1);

        List<LatchNode> topSort = graph.topSort();
        assertThat(topSort).hasSize(8);
        assertThat(topSort.get(5))
                .hasFieldOrPropertyWithValue("job", pSql)
                .hasFieldOrPropertyWithValue("latch.count", 2L);
        assertThat(topSort.get(6))
                .hasFieldOrPropertyWithValue("job", agr2)
                .hasFieldOrPropertyWithValue("latch.count", 3L);
        assertThat(topSort.get(7))
                .hasFieldOrPropertyWithValue("job", agr1)
                .hasFieldOrPropertyWithValue("latch.count", 4L);
    }

}
