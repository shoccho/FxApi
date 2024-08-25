package org.example.state;

import org.example.storage.Storage;
import org.json.JSONObject;

import java.util.HashMap;

public class State {
    private HashMap<String, JSONObject> data;
    private String url;
    private String method;
    private final Storage storage;

    public State(Storage storage) {
        this.storage = storage;
        loadData();
    }

    public void loadData() {
        this.data = new HashMap<>();

        data.put("headers", new JSONObject(storage.readString("headers.json")));
        data.put("queries", new JSONObject(storage.readString("queries.json")));
        data.put("body", new JSONObject(storage.readString("body.json")));
        this.url = storage.readString("url");
        this.method = storage.readString("method");
    }

    public void saveUrl(String url) {
        this.url = url;
        storage.writeString(url, "url");
    }

    public void saveMethod(String method) {
        this.method = method;
        storage.writeString(method, "method");
    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public JSONObject getState(String key) {
        return data.get(key);
    }

    public void updateState(String key, String name, String value) {
        JSONObject jsonObject = this.data.get(key);
        jsonObject.put(name, value);
        this.storage.writeString(jsonObject.toString(), key + ".json");
    }

    public void removeData(String key, String fieldName) {
        JSONObject jsonObject = this.data.get(key);
        jsonObject.remove(fieldName);
        this.storage.writeString(jsonObject.toString(), key + ".json");
    }
}
