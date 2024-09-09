package com.github.shoccho.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Request {
    private Integer id;
    private String url;
    private String method;
    private ArrayList<Parameter> headers;
    private ArrayList<Parameter> queries;
    private ArrayList<Parameter> body;
    private String title;
    private String bodyType;
    private String rawBody;


    public Request() {
        this.headers = new ArrayList<>();
        this.queries = new ArrayList<>();
        this.body = new ArrayList<>();

        this.bodyType = "raw";
        this.method = "GET";
        this.url = "http://";
        this.title = "";
        this.headers.add(new Parameter("accept","application/json"));
        this.headers.add(new Parameter("content-type","application/json"));
    }

    public String getTitle() {
        return title;
    }

    public String getTitleForUI() {
        if (Objects.equals(this.title, "")) {
            return this.getMethod() + ":" + this.getUrl();
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Request(Integer id, String url, String method, String title, ArrayList<Parameter> headers, ArrayList<Parameter> queries, ArrayList<Parameter> body) {
        this.id = id;
        this.url = url;
        this.method = method;
        this.title = title;
        this.headers = headers;
        this.queries = queries;
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getHeadersString() {
        return new JSONArray(headers).toString();
    }

    public void setHeaders(ArrayList<Parameter> headers) {
        this.headers = headers;
    }

    public ArrayList<Parameter> getQueries() {
        return queries;
    }

    public String getQueriesString() {
        return new JSONArray(queries).toString();
    }

    public void setQueries(ArrayList<Parameter> queries) {
        this.queries = queries;
    }

    public ArrayList<Parameter> getBody() {
        return body;
    }

    public String getBodyString() {
        return new JSONArray(body).toString();
    }

    public void setBody(ArrayList<Parameter> body) {
        this.body = body;
    }

    public void setBodyType(String bodyType){
        this.bodyType = bodyType;
    }
    public String getBodyType(){
        return this.bodyType;
    }
    public void setRawBody(String rawBody){
        this.rawBody = rawBody;
    }
    public String getRawBody(){
        return this.rawBody;
    }

    public void setParamString(String type, String jsonString) {
        ArrayList<Parameter> params = new ArrayList<>();
        if (jsonString == null) return;
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            params.add(i, new Parameter(jsonObject.getString("key"), jsonObject.getString("value")));
        }
        switch (type) {
            case "header":
                this.setHeaders(params);
                break;
            case "query":
                this.setQueries(params);
                break;
            case "body":
                this.setBody(params);
                break;
        }
    }

    public String getJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("id:").append(this.id).append(",");
        sb.append("url:\"").append(this.method).append("\",");
        sb.append("method:\"").append(this.url).append("\",");
        sb.append("queries:[");
        this.queries.forEach((val) -> {
            sb.append("{");
            sb.append("key:\"").append(val.getKey()).append("\",");
            sb.append("value:\"").append(val.getValue()).append("\",");
            sb.append("},");
        });
        sb.append("],");
        sb.append("headers:[");
        this.headers.forEach((val) -> {
            sb.append("{");
            sb.append("key:\"").append(val.getKey()).append("\",");
            sb.append("value:\"").append(val.getValue()).append("\",");
            sb.append("},");
        });
        sb.append("],");
        sb.append("body:[");
        this.body.forEach((val) -> {
            sb.append("{");
            sb.append("key:\"").append(val.getKey()).append("\",");
            sb.append("value:\"").append(val.getValue()).append("\",");
            sb.append("}");
        });
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
