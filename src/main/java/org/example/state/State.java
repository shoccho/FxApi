package org.example.state;

import org.example.storage.Storage;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
    private HashMap<String, JSONObject> data;
    private ArrayList<JSONObject> data2;
    private String url;
    private String method;
    private final Storage storage;

    public State(Storage storage) {
        this.storage = storage;
        loadData();
    }

    public void loadData() {
        this.data = new HashMap<>();
        this.data2 = new ArrayList<JSONObject>();

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

    public void updateState(String key, String id, String name, String value) {
        JSONObject jsonObject = this.data.get(key);
        JSONObject child = new JSONObject();
        child.put(name, value);
        jsonObject.put(id, child.toString());
        this.storage.writeString(jsonObject.toString(), key + ".json");
    }

    public void removeData(String key, String id) {
        JSONObject jsonObject = this.data.get(key);
        jsonObject.remove(id);
        this.storage.writeString(jsonObject.toString(), key + ".json");
    }
}
