package com.github.shoccho.storage;

import com.github.shoccho.model.Request;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OpenTabsDao {
    private final DBConnection dbConnection;

    public OpenTabsDao(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public ArrayList<Request> getAllOpenTabs() {
        ArrayList<Request> allRequests = new ArrayList<>();
        try {
            Statement statement = this.dbConnection.getConnection().createStatement();
            String sql = "select * from OpenTabs;";

            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {

                Request request = new Request();
                request.setId(result.getInt(1));
                request.setUrl(result.getString(2));
                request.setMethod(result.getString(3));
                request.setTitle(result.getString(4));
                String headerString = result.getString(5);
                request.setParamString("header", headerString);

                String queryString = result.getString(6);
                request.setParamString("query", queryString);

                String bodyString = result.getString(7);
                request.setParamString("body", bodyString);

                allRequests.add(request);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allRequests;
    }

    public void removeOpenTab(Integer id) {
        try {
            Statement statement = this.dbConnection.getConnection().createStatement();
            String sql = "Delete from OpenTabs where id = " + id + ";";

            boolean result = statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Request getRequestById(Integer id) {
        if (id == null) return null;
        try {
            Statement statement = this.dbConnection.getConnection().createStatement();
            String sql = "select * from OpenTabs where id = " + id + ";";

            ResultSet result = statement.executeQuery(sql);
            Request request = new Request();
            request.setId(result.getInt(1));
            request.setUrl(result.getString(2));
            request.setMethod(result.getString(3));
            request.setTitle(result.getString(4));
            String headerString = result.getString(5);
            request.setParamString("header", headerString);

            String queryString = result.getString(6);
            request.setParamString("query", queryString);

            String bodyString = result.getString(7);
            request.setParamString("body", bodyString);

            return request;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer saveRequest(Request request) {
        Integer generatedId = null;

        try {
            Connection connection = this.dbConnection.getConnection();
            Statement statement = connection.createStatement();

            if (request.getId() == null) {
                String sql = "INSERT INTO OpenTabs (url, method, title, headers, queries, body) VALUES ('"
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
            } else {
                String sql = "INSERT OR REPLACE INTO OpenTabs (id, url, method, title, headers, queries, body) VALUES ("
                        + request.getId() + ",'"
                        + request.getUrl() + "','"
                        + request.getMethod() + "','"
                        + request.getTitle() + "','"
                        + request.getHeadersString() + "','"
                        + request.getQueriesString() + "','"
                        + request.getBodyString() + "');";
                statement.executeUpdate(sql);
                generatedId = request.getId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }
}
