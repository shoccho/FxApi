package org.example.state;

import org.example.model.Parameter;
import org.example.model.Request;
import org.example.storage.Storage;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class State {
   private Request request;

    private HashMap<String, JSONObject> data;
    private String url;
    private String method;
    private final Storage storage;

    public State(Storage storage, int key) {
        this.storage = storage;
        loadData(key);
    }

    public void loadData(int key) {
        this.request = storage.getRequest(key);
        if(this.request== null){
            this.request = new Request(key);
        }
        this.data = new HashMap<>();

        data.put("headers", new JSONObject(storage.readString("headers.json")));
        data.put("queries", new JSONObject(storage.readString("queries.json")));
        data.put("body", new JSONObject(storage.readString("body.json")));
        this.url = storage.readString("url");
        this.method = storage.readString("method");
    }

    public Request getRequest(){
        return this.request;
    }

    public void saveUrl(String url) {
        this.url = url;
        this.request.setUrl(url);
        storage.writeString(url, "url");
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

    public JSONObject getState(String key) {
        return data.get(key);
    }

    public void updateState(String key, String id, String name, String value) {
        JSONObject jsonObject = this.data.get(key);
        JSONObject child = new JSONObject();
        child.put(name, value);
        jsonObject.put(id, child.toString());
        if(key == "headers"){
            HashMap<Integer, Parameter> headers = this.request.getHeaders();
            Parameter param = headers.get(Integer.parseInt(id));
            if(param == null){
                headers.put(Integer.parseInt(id), new Parameter(name, value));
            }else{
                param.setKey(name);
                param.setValue(value);
            }
        }else if(key == "queries"){
            HashMap<Integer, Parameter> queries = this.request.getQueries();
            Parameter param = queries.get(Integer.parseInt(id));
            if(param == null){
                queries.put(Integer.parseInt(id), new Parameter(name, value));
            }else{
                param.setKey(name);
                param.setValue(value);
                queries.put(Integer.parseInt(id), param);
            }
        }
        else if(key == "body"){
            HashMap<Integer, Parameter> queries = this.request.getQueries();
            Parameter param = queries.get(Integer.parseInt(id));
            if(param == null){
                queries.put(Integer.parseInt(id), new Parameter(name, value));
            }else{
                param.setKey(name);
                param.setValue(value);
                queries.put(Integer.parseInt(id), param);
            }
        }
        System.out.println(this.request.getJSON());

        this.storage.writeString(this.request.getJSON(), String.valueOf(this.request.getId()));
        this.storage.writeString(jsonObject.toString(), key );
    }

    public void removeData(String key, String id) {
        JSONObject jsonObject = this.data.get(key);
        jsonObject.remove(id);
        this.storage.writeString(jsonObject.toString(), key + ".json");
    }
}
