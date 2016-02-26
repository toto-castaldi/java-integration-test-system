package com.github.totoCastaldi.integration.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * Created by toto on 12/02/16.
 */
@Slf4j
public class Mysql {

    private final String host;
    private final String db;
    private final String username;
    private final String password;

    public Mysql(String host, String db, String username, String password) {
        this.host = host;
        this.db = db;
        this.username = username;
        this.password = password;
    }

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
        Class.forName("com.mysql.jdbc.Driver");
        Connection connect = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", host, db, username, password));
        Statement statement = connect.createStatement();

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
            if (connect != null) connect.close();
        }
    }


}
