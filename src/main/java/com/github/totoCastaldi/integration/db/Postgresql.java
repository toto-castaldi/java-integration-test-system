package com.github.totoCastaldi.integration.db;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by toto on 26/02/16.
 */
@AllArgsConstructor
public class Postgresql extends GenericDatabase {

    private final String host;
    private final String db;
    private final String username;
    private final String password;
    private final int port;

    @Override
    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(String.format("jdbc:postgresql://%s:" + port + "/%s", host, db), username, password);
    }
}

