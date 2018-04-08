package engine.quality.jobs;

import engine.api.AbstractContainerJob;
import engine.api.JDBCJob;
import engine.api.Job;
import engine.quality.results.StringResult;
import engine.quality.util.DatabaseUtils;
import engine.quality.util.SQLParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Job executing SQL query with parameters from it's children
 */
public class ParametrizedSQLJob extends AbstractContainerJob<StringResult, StringResult> implements JDBCJob<StringResult> {

    private final Connection connection;
    private final String sqlQuery;

    public ParametrizedSQLJob(UUID uuid, Connection connection, String sqlQuery) {
        super(uuid);
        this.connection = connection;
        this.sqlQuery = sqlQuery;
    }

    @Override
    protected StringResult doExecute() throws SQLException {
        StringResult result = new StringResult(this);

        List<String> childrenResults = new ArrayList<>();
        for (Job<StringResult> child : getChildren()) {
            if (child.getResult() != null) {
                childrenResults.add(child.getResult().getResultValue());
            }
            else {
                throw new IllegalArgumentException("Result cannot be created if all children results are not computed.");
            }
        }

        SQLParser parser = new SQLParser(sqlQuery);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(parser.parametrize(childrenResults));
        String resultValue = DatabaseUtils.getOneResult(resultSet);

        result.setResultValue(resultValue);
        return result;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
