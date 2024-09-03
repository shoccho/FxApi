package org.example.storage;

import org.example.model.Request;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RequestDAO {
    private final DBConnection dbConnection;

    public RequestDAO() {
        dbConnection = new DBConnection();
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
            String headerString = result.getString(4);
            request.setParamString("header", headerString);

            String queryString = result.getString(5);
            request.setParamString("query", queryString);

            String bodyString = result.getString(6);
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

            String sql = "INSERT INTO History(url, method, title, headers, queries, body) VALUES ('"
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

        return generatedId;
    }
}
