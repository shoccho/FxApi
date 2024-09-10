package com.github.shoccho.storage;

import com.github.shoccho.model.Request;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Util {
    public static Request requestFromResult(ResultSet result) throws SQLException {
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
    }
}
