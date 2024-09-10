package com.github.shoccho.storage;

import com.github.shoccho.model.Request;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RequestDAO {
    private final DBConnection dbConnection;

    public RequestDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public ArrayList<Request> getHistory() {
        ArrayList<Request> allRequests = new ArrayList<>();
        try {
            Statement statement = this.dbConnection.getConnection().createStatement();
            String sql = "select * from History ORDER BY id DESC;";

            // TODO: don't get result just title and Id,
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                allRequests.add(Util.requestFromResult(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allRequests;
    }

    public Request getRequestById(Integer id) {
        if (id == null) return null;
        try {
            Statement statement = this.dbConnection.getConnection().createStatement();
            String sql = "select * from History where id = " + id + ";";

            ResultSet result = statement.executeQuery(sql);
            return Util.requestFromResult(result);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveRequest(Request request) {
        Integer generatedId = null;

        try {
            Connection connection = this.dbConnection.getConnection();
            Statement statement = connection.createStatement();

            String sql = "INSERT INTO History(url, method, title, body_type, headers, queries, body, raw_body) VALUES ('"
                    + request.getUrl() + "','"
                    + request.getMethod() + "','"
                    + request.getTitle() + "','"
                    + request.getBodyType() + "','"
                    + request.getHeadersString() + "','"
                    + request.getQueriesString() + "','"
                    + request.getBodyString() + "','"
                    + request.getRawBody() + "');";

            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
