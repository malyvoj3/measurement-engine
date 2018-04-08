package engine.quality.util;

import org.apache.commons.lang3.Validate;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Class supporting work with database
 */
public class DatabaseUtils {

    /**
     * Validate if SQL query result has just one result
     * @param resultSet Result of SQL query
     * @return One result from SQL query
     * @throws SQLException
     */
    public static String getOneResult(ResultSet resultSet) throws SQLException {
        String result;
        ResultSetMetaData metaData = resultSet.getMetaData();
        //only one column is allowed
        Validate.isTrue(metaData.getColumnCount() == 1);
        //set the cursor to the first row of result set, which has to exist
        Validate.isTrue(resultSet.next());
        result = resultSet.getString(1);
        //only one row is allowed
        Validate.isTrue(!resultSet.next());
        return result;
    }

}
