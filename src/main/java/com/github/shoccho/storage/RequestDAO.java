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

            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {

                Request request = new Request();
                request.setId(result.getInt(1));
                request.setUrl(result.getString(2));
                request.setMethod(result.getString(3));
                request.setTitle(result.getString(4));
                request.setBodyType(result.getString(5));
                String headerString = result.getString(6);
                request.setParamString("header", headerString);

                String queryString = result.getString(7);
                request.setParamString("query", queryString);

                String bodyString = result.getString(8);
                request.setParamString("body", bodyString);
                
                request.setRawBody(result.getString(9));

                allRequests.add(request);
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
            Request request = new Request();
            request.setId(result.getInt(1));
            request.setUrl(result.getString(2));
            request.setMethod(result.getString(3));
            request.setTitle(result.getString(4));
            request.setBodyType(result.getString(5));
            String headerString = result.getString(6);
            request.setParamString("header", headerString);

            String queryString = result.getString(7);
            request.setParamString("query", queryString);

            String bodyString = result.getString(8);
            request.setParamString("body", bodyString);

            request.setRawBody(result.getString(9));

            return request;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveRequest(Request request) {
        Integer generatedId = null;

        try {
            Connection connection = this.dbConnection.getConnection();
            Statement statement = connection.createStatement();

            String sql = "INSERT INTO History(url, method, title, body_type headers, queries, body) VALUES ('"
                    + request.getUrl() + "','"
                    + request.getMethod() + "','"
                    + request.getTitle() + "','"
                    + request.getHeadersString() + "','"
                    + request.getQueriesString() + "','"
                    + request.getBodyString() + "');";

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
