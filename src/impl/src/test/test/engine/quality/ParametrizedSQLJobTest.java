package engine.quality;

import engine.quality.jobs.ParametrizedSQLJob;
import engine.quality.jobs.SQLJob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static engine.quality.DatabaseUtilsTest.PASS;
import static engine.quality.DatabaseUtilsTest.URL;
import static engine.quality.DatabaseUtilsTest.USERNAME;
import static org.assertj.core.api.Assertions.*;

public class ParametrizedSQLJobTest {

    private Connection connection;

    @Before
    public void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        connection = DriverManager.getConnection(URL, USERNAME, PASS);
    }

    @After
    public void closeDB() throws SQLException {
        connection.close();
    }

    @Test
    public void whenEverythingIsOk_thenJobHasCorrectResult() {
        SQLJob sqlJob1;
        SQLJob sqlJob2;
        sqlJob1 = new SQLJob(UUID.randomUUID(), connection, "select avg(LIST_PRICE) from DEMO_PRODUCT_INFO");
        sqlJob2 = new SQLJob(UUID.randomUUID(), connection, "select max(LIST_PRICE) from DEMO_PRODUCT_INFO");
        sqlJob1.execute();
        sqlJob2.execute();

        ParametrizedSQLJob job = new ParametrizedSQLJob(UUID.randomUUID(), connection, "select count(*) from DEMO_PRODUCT_INFO t where (t.LIST_PRICE > {0} and t.LIST_PRICE < {1})");
        job.addChild(sqlJob1);
        job.addChild(sqlJob2);
        job.execute();
        assertThat(job.getResult())
                .isNotNull()
                .hasFieldOrPropertyWithValue("resultValue", "3");
    }

    @Test
    public void whenSomeChildHasNotResult_thenJobHasNotResult() {
        SQLJob sqlJob1;
        SQLJob sqlJob2;
        sqlJob1 = new SQLJob(UUID.randomUUID(), connection, "select avg(LIST_PRICE) from DEMO_PRODUCT_INFO");
        sqlJob2 = new SQLJob(UUID.randomUUID(), connection, "select max(LIST_PRICE) from DEMO_PRODUCT_INFO");
        sqlJob1.execute();

        ParametrizedSQLJob job = new ParametrizedSQLJob(UUID.randomUUID(), connection, "select count(*) from DEMO_PRODUCT_INFO t where (t.LIST_PRICE > {0} and t.LIST_PRICE < {1})");
        job.addChild(sqlJob1);
        job.addChild(sqlJob2);
        job.execute();
        assertThat(job.getResult())
                .isNull();
    }

}
