package org.example.state;

import org.example.model.Parameter;
import org.example.model.Request;
import org.example.storage.DBUtil;

import java.util.ArrayList;

public class State {
    private Request request;


    private final DBUtil db;

    public State(Integer key, DBUtil db) {
        this.db = db;
        loadData(key);
    }

    public void loadData(Integer key) {
        this.request = this.db.getRequestById(key);
        if (this.request == null) {
            this.request = new Request();
            Integer id = this.db.saveRequest(this.request);
            System.out.println(id);
            this.request.setId(id);
        }
    }

    public Request getRequest() {
        return this.request;
    }

    public void saveUrl(String url) {
        this.request.setUrl(url);
        db.saveRequest(this.request);
    }

    public void saveMethod(String method) {
        this.request.setMethod(method);
        this.db.saveRequest(request);
    }

    public String getMethod() {
        return this.request.getMethod();
    }

    public String getUrl() {
        return this.request.getUrl();
    }

    public ArrayList<Parameter> getState(String key) {
        return switch (key) {
            case "headers" -> this.request.getHeaders();
            case "queries" -> this.request.getQueries();
            case "body" -> this.request.getBody();
            default -> null;
        };
    }

    public void updateState(String key, Integer id, String name, String value) {

        if (key == null || id == null || name == null || value == null) {
            throw new IllegalArgumentException("Key, id, name, and value must not be null");
        }

        ArrayList<Parameter> parameters = getParameters(key);

        if (id >= parameters.size()) {
            parameters.add(new Parameter(name, value));
        } else {
            Parameter param = parameters.get(id);
            param.setKey(name);
            param.setValue(value);
        }
        this.db.saveRequest(this.request);

    }

    private ArrayList<Parameter> getParameters(String key) {
        return switch (key) {
            case "headers" -> this.request.getHeaders();
            case "queries" -> this.request.getQueries();
            case "body" -> this.request.getBody();
            default -> throw new IllegalArgumentException("Invalid key: " + key);
        };
    }

    public void removeData(String key, Integer id) {
        if (key == null || id == null) {
            throw new IllegalArgumentException("Key, id, name, and value must not be null");
        }
        ArrayList<Parameter> parameters = getParameters(key);
        parameters.remove((int)id);

        this.db.saveRequest(this.request);
    }
}
