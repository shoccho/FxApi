package org.example.model;

import org.json.JSONObject;

import java.util.ArrayList;

public class Request {
    private int id;
    private String url;
    private String method;
    private ArrayList<Parameter> headers;
    private ArrayList<Parameter> queries;
    private ArrayList<Parameter> body;

    public Request() {
    }

    public Request(int id, String url, String method, ArrayList<Parameter> headers, ArrayList<Parameter> queries, ArrayList<Parameter> body) {
        this.id = id;
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.queries = queries;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ArrayList<Parameter> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<Parameter> headers) {
        this.headers = headers;
    }

    public ArrayList<Parameter> getQueries() {
        return queries;
    }

    public void setQueries(ArrayList<Parameter> queries) {
        this.queries = queries;
    }

    public ArrayList<Parameter> getBody() {
        return body;
    }

    public void setBody(ArrayList<Parameter> body) {
        this.body = body;
    }

    public String getJSON(){
        return JSONObject.valueToString(this);
    }
}
