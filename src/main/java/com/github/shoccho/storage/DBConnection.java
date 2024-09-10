package com.github.shoccho.storage;

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
            statement.executeUpdate("create table if not exists History( id INTEGER PRIMARY KEY AUTOINCREMENT, url text, method text, title text, body_type text, headers text, queries text, body text, raw_body text);");
            statement.executeUpdate("create table if not exists OpenTabs( id INTEGER PRIMARY KEY AUTOINCREMENT, url text, method text, title text, body_type text, headers text, queries text, body text, raw_body text);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
