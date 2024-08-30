package org.example.storage;

import org.example.model.Request;

import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private final DBConnection dbConnection;

    public DBUtil() {
        dbConnection = new DBConnection();
    }

    public void saveRequest(Request request) {
        try {
            Statement statement = this.dbConnection.getConnection().createStatement();
            String sql = "INSERT OR REPLACE INTO requests (id, url, method, headers, queries, body) values " +
                    "('" + request.getId() + "','" + request.getUrl() + "','" + request.getMethod() + "','"
                    + request.getHeadersString() + "','"
                    + request.getQueriesString() +"','"
                    + request.getBodyString() + "');";
            System.out.println(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
