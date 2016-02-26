package com.github.totoCastaldi.integration.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by toto on 26/02/16.
 */
public abstract class GenericDatabase {

    public int rowCount(String tableName) throws ClassNotFoundException, SQLException {
        String sql = String.format("select count(*) as c from %s", tableName);
        System.out.println(sql);
        return executeQuery(sql, new QueryResultSetCallBack<Integer>() {

            @Override
            public Integer onResultSet(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return resultSet.getInt("c");
            }
        });
    }

    public void emptyTable(String tableName) throws SQLException, ClassNotFoundException {
        String sql = String.format("delete from %s", tableName);
        System.out.println(sql);
        executeQuery(sql, null);
    }

    private <T> T executeQuery(String sql, QueryResultSetCallBack<T> callBack) throws ClassNotFoundException, SQLException {
        final Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = null;

        try {
            if (callBack != null) {
                resultSet = statement.executeQuery(sql);
                return callBack.onResultSet(resultSet);
            } else {
                statement.executeUpdate(sql);
                return null;
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    protected abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}
