package engine.api;

import java.sql.Connection;

/**
 * Interface for a job, which needs connection for it's execution
 * @param <R>
 */
public interface JDBCJob<R extends Result> extends Job<R> {

    Connection getConnection();

}
