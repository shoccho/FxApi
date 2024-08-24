package org.example.state;

import org.example.storage.Storage;
import org.json.JSONObject;

import java.util.HashMap;

public class State {
    private HashMap<String, JSONObject> data;

    private final Storage storage;

    public State(Storage storage) {

        this.storage = storage;
        loadData();

    }

    public void loadData(){
        this.data = new HashMap<>();

        data.put("headers", new JSONObject(storage.readJSON("headers")));
        data.put("queries", new JSONObject(storage.readJSON("queries")));
        data.put("body", new JSONObject(storage.readJSON("body")));
    }

    public JSONObject getState(String key) {
        return data.get(key);
    }

    public void updateState(String key, String name, String value) {
        JSONObject jsonObject = this.data.get(key);
        jsonObject.put(name, value);
        this.storage.saveJSON(jsonObject.toString(), key);
    }
}
