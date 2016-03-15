package com.github.totoCastaldi.integration.db;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by toto on 26/02/16.
 */
public abstract class GenericDatabase {

    protected abstract Connection getConnection() throws ClassNotFoundException, SQLException;

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

    public void executeUpdate(String sql) throws SQLException, ClassNotFoundException {
        System.out.println(sql);
        executeQuery(sql, null);
    }

    public Collection<Map<String, Object>> extractRow(String sql) throws SQLException, ClassNotFoundException {
        final List<Map<String, Object>> result = Lists.newArrayList();

        executeQuery(sql, new QueryResultSetCallBack<Object>() {
            @Override
            public Object onResultSet(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    Map<String, Object> row = Maps.newHashMap();
                    final ResultSetMetaData metaData = resultSet.getMetaData();
                    final int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount ; i++) {
                        final String columnName = metaData.getColumnName(i);
                        row.put(columnName, resultSet.getObject(i));
                    }
                    result.add(row);
                }
                return null;
            }
        });

        return result;
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


}
