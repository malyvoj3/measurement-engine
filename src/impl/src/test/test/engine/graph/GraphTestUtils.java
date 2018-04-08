package engine.graph;

import engine.quality.jobs.AggregateCheckJob;
import engine.quality.jobs.ParametrizedSQLJob;
import engine.quality.jobs.SQLJob;

import java.util.UUID;

public class GraphTestUtils {

    public static final UUID UUID1 = UUID.fromString("c29b1851-1868-11e7-93ae-92361f002671");
    public static final UUID UUID2 = UUID.fromString("c29b1852-1868-11e7-93ae-92361f002671");
    public static final UUID UUID3 = UUID.fromString("c29b1853-1868-11e7-93ae-92361f002671");
    public static final UUID UUID4 = UUID.fromString("c29b1854-1868-11e7-93ae-92361f002671");
    public static final UUID UUID5 = UUID.fromString("c29b1855-1868-11e7-93ae-92361f002671");
    public static final UUID UUID6 = UUID.fromString("c29b1856-1868-11e7-93ae-92361f002671");
    public static final UUID UUID7 = UUID.fromString("c29b1857-1868-11e7-93ae-92361f002671");
    public static final UUID UUID8 = UUID.fromString("c29b1858-1868-11e7-93ae-92361f002671");

    public AggregateCheckJob prepareJob() {
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

        return agr1;
    }

}
