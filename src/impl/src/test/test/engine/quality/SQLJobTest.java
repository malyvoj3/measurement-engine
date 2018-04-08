package engine.quality;

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

public class SQLJobTest {

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
        SQLJob job = new SQLJob(UUID.randomUUID(), connection, "select count(*) from DEMO_ORDERS");
        job.execute();
        assertThat(job.getResult())
                .isNotNull()
                .hasFieldOrPropertyWithValue("resultValue", "10");
    }

    @Test
    public void whenSomeErrorDuringExecution_thenJobHasNotResult() {
        SQLJob job = new SQLJob(UUID.randomUUID(), connection, "select error-count(*)-error from DEMO_ORDERS");
        job.execute();
        assertThat(job.getResult())
                .isNull();
    }

}
