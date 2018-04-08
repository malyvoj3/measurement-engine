package engine.graph;

import engine.graph.ReactiveGraph;
import engine.graph.ReactiveNode;
import engine.quality.jobs.AggregateCheckJob;
import engine.quality.jobs.ParametrizedSQLJob;
import engine.quality.jobs.SQLJob;
import org.junit.Test;

import java.util.List;

import static engine.graph.GraphTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveGraphTest {

    @Test
    public void returnCorrectRoots() {
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

        ReactiveGraph graph = new ReactiveGraph(agr1);

        List<ReactiveNode> roots = graph.getRoots();
        assertThat(roots).hasSize(5);

        boolean assertion = true;
        for (ReactiveNode node : roots) {
            assertion &= node.getInDegree() == 0;
            assertion &= (UUID4.equals(node.getUuid()) || UUID5.equals(node.getUuid()) || UUID6.equals(node.getUuid())
                    || UUID7.equals(node.getUuid()) || UUID8.equals(node.getUuid()));
        }
        assertThat(assertion).isTrue();
    }

}
