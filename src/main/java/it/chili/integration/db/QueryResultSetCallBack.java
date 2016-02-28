package com.github.totoCastaldi.integration.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by toto on 17/02/16.
 */
public interface QueryResultSetCallBack<T> {

    public T onResultSet(ResultSet resultSet) throws SQLException;
}
