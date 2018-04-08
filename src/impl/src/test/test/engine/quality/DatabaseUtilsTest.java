package engine.quality;

import engine.quality.util.DatabaseUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.assertj.core.api.Assertions.*;

public class DatabaseUtilsTest {

    public static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    public static final String USERNAME = "malyvoj3";
    public static final String PASS = "123456789";
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
    public void whenSQLQueryIsCorrect_thenReturnNumberResult() throws SQLException {
        String sql = "select count(*) from DEMO_CUSTOMERS";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String resultValue = DatabaseUtils.getOneResult(resultSet);
        assertThat(resultValue).isEqualTo("7");
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenSQLQueryReturnMoreColumns_thenThrowException() throws SQLException {
        String sql = "select * from DEMO_CUSTOMERS";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String resultValue = DatabaseUtils.getOneResult(resultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenSQLQueryReturnMoreRows_thenThrowException() throws SQLException {
        String sql = "select CUSTOMER_ID from DEMO_CUSTOMERS";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String resultValue = DatabaseUtils.getOneResult(resultSet);
    }

}
