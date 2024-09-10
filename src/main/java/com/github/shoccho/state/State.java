package com.github.shoccho.state;

import com.github.shoccho.model.Parameter;
import com.github.shoccho.model.Request;
import com.github.shoccho.storage.OpenTabsDao;
import com.github.shoccho.storage.RequestDAO;
import com.github.shoccho.ui.components.actions.UpdateHistory;

import java.util.ArrayList;

public class State {
    private Request request;

    private final RequestDAO requestDAO;
    private final OpenTabsDao openTabsDao;
    private final UpdateHistory updateHistory;

    public State(Integer key, OpenTabsDao db, RequestDAO requestDAO, UpdateHistory refreshHistory) {
        this.openTabsDao = db;
        this.requestDAO = requestDAO;
        loadData(key);
        updateHistory = refreshHistory;
    }

    public State(Request request, OpenTabsDao db, RequestDAO requestDAO, UpdateHistory refreshHistory) {
        this.openTabsDao = db;
        this.request = request;
        this.requestDAO = requestDAO;
        updateHistory = refreshHistory;
    }

    public void loadData(Integer key) {
        this.request = this.requestDAO.getRequestById(key);
        if (this.request == null) {
            this.request = new Request();
        }
        this.request.setId(null);
        this.request.setId(this.openTabsDao.saveRequest(this.request));
    }

    public Request getRequest() {
        return this.request;
    }

    public void saveUrl(String url) {
        this.request.setUrl(url);
        openTabsDao.saveRequest(this.request);
    }

    public void saveMethod(String method) {
        this.request.setMethod(method);
        this.openTabsDao.saveRequest(request);
    }

    public String getMethod() {
        return this.request.getMethod();
    }

    public String getUrl() {
        return this.request.getUrl();
    }

    public String getTitle() {
        return this.request.getTitle();
    }

    public void setTitle(String title) {
        this.request.setTitle(title);
        this.openTabsDao.saveRequest(request);
    }

    public void setBodyType(String type) {
        this.request.setBodyType(type);
    }

    public String getBodyType() {
        return this.request.getBodyType();
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
        this.openTabsDao.saveRequest(this.request);

    }

    public void saveToHistory() {
        this.requestDAO.saveRequest(this.request);
        updateHistory.execute();
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
        parameters.remove((int) id);

        this.openTabsDao.saveRequest(this.request);
    }

    public String getRawBody() {
        return this.request.getRawBody();
    }

    public void setRawBody(String rawBody) {
        this.request.setRawBody(rawBody);
    }
}
