package org.example.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Request {
    private int id;
    private String url;
    private String method;
    private HashMap<Integer, Parameter> headers;
    private HashMap<Integer, Parameter> queries;
    private HashMap<Integer, Parameter> body;

    public Request(int id) {
        this.id = id;
        this.headers = new HashMap<>();
        this.queries = new HashMap<>();
        this.body = new HashMap<>();
    }

    public Request(int id, String url, String method, HashMap<Integer, Parameter> headers, HashMap<Integer, Parameter> queries, HashMap<Integer, Parameter> body) {
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

    public HashMap<Integer, Parameter> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<Integer, Parameter> headers) {
        this.headers = headers;
    }

    public HashMap<Integer, Parameter> getQueries() {
        return queries;
    }

    public void setQueries(HashMap<Integer, Parameter> queries) {
        this.queries = queries;
    }

    public HashMap<Integer, Parameter> getBody() {
        return body;
    }

    public void setBody(HashMap<Integer, Parameter> body) {
        this.body = body;
    }

    public String getJSON(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("id:").append(this.id).append(",");
        sb.append("url:\"").append(this.method).append("\",");
        sb.append("method:\"").append(this.url).append("\",");
        sb.append("queries:[");
        this.queries.forEach((key, val)->{
            sb.append("{");
            sb.append("key:\"").append(val.getKey()).append("\",");
            sb.append("value:\"").append(val.getValue()).append("\",");
            sb.append("},");
        });
        sb.append("],");
        sb.append("headers:[");
        this.headers.forEach((key, val)->{
            sb.append("{");
            sb.append("key:\"").append(val.getKey()).append("\",");
            sb.append("value:\"").append(val.getValue()).append("\",");
            sb.append("},");
        });
        sb.append("],");
        sb.append("body:[");
        this.body.forEach((key, val)->{
            sb.append("{");
            sb.append("key:\"").append(val.getKey()).append("\",");
            sb.append("value:\"").append(val.getValue()).append("\",");
            sb.append("}");
        });
        sb.append("]");
        sb.append("}");
        return  sb.toString();
    }
}
