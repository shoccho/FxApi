package org.example.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private Connection connection;

    public DBConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("isValid:" + connection.isValid(0));
            initTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTable() {
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate("create table if not exists Requests(    id identity primary key,    url text,    method text,    headers text,    queries text,    body text);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
