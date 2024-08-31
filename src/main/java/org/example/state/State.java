package org.example.state;

import org.example.model.Parameter;
import org.example.model.Request;
import org.example.storage.DBUtil;
import org.example.storage.Storage;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
    private Request request;

    private HashMap<String, JSONObject> data;
    private String url;
    private String method;
    private final Storage storage;
    private final DBUtil db;

    public State(Storage storage, int key, DBUtil db) {
        this.storage = storage;
        this.db = db;
        loadData(key);
    }

    public void loadData(Integer key) {
        this.request = this.db.getRequestById(key);
        if (this.request == null) {
            this.request = new Request();
        }
        System.out.println(this.request.getJSON());
    }

    public Request getRequest() {
        return this.request;
    }

    public void saveUrl(String url) {
        this.url = url;
        this.request.setUrl(url);
//        storage.writeString(url, "url");
    }

    public void saveMethod(String method) {
        this.method = method;
        this.request.setMethod(method);
        storage.writeString(method, "method");
    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public ArrayList<Parameter> getState(String key) {
        switch (key) {
            case "headers":
                return this.request.getHeaders();
            case "queries":
                return this.request.getQueries();
            case "body":
                return this.request.getBody();
        }
        return null;
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
        switch (key) {
            case "headers":
                return this.request.getHeaders();
            case "queries":
                return this.request.getQueries();
            case "body":
                return this.request.getBody();
            default:
                throw new IllegalArgumentException("Invalid key: " + key);
        }
    }

    public void removeData(String key, Integer id) {

    }
}
