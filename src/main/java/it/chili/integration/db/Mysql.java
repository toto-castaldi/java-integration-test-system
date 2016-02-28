package com.github.totoCastaldi.integration.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by toto on 12/02/16.
 */
@AllArgsConstructor
public class Mysql extends GenericDatabase {

    private final String host;
    private final String db;
    private final String username;
    private final String password;

    @Override
    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", host, db, username, password));
    }

}
