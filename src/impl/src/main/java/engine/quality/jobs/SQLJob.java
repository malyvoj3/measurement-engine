package engine.quality.jobs;

import engine.api.AbstractJob;
import engine.api.JDBCJob;
import engine.quality.results.StringResult;
import engine.quality.util.DatabaseUtils;

import java.sql.*;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * Simple job with SQL query
 */
public class SQLJob extends AbstractJob<StringResult> implements JDBCJob<StringResult> {

    private final Connection connection;
    private final String sqlQuery;

    public SQLJob(UUID uuid, Connection connection, String sqlQuery) {
        super(uuid);
        this.connection = connection;
        this.sqlQuery = sqlQuery;
    }

    @Override
    protected StringResult doExecute() throws SQLException {
        StringResult result = new StringResult(this);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        String resultValue = DatabaseUtils.getOneResult(resultSet);

        result.setResultValue(resultValue);
        return result;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
