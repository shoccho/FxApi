package org.example.state;

import org.example.model.Request;
import org.example.storage.OpenTabsDao;
import org.example.storage.RequestDAO;

import java.util.ArrayList;

public class ApplicationState {
    private ArrayList<State> openTabs;
    private RequestDAO requestDao;
    private OpenTabsDao openTabsDao;

    public ApplicationState(RequestDAO requestDao, OpenTabsDao openTabsDao) {
        this.openTabs = new ArrayList<>();
        this.requestDao = requestDao;
        this.openTabsDao = openTabsDao;
        loadAllOpenTabs();
    }

    private void loadAllOpenTabs() {
        ArrayList<Request> allOpenRequests = this.openTabsDao.getAllOpenTabs();
        this.openTabs.addAll(allOpenRequests.stream().map(e -> new State(e, openTabsDao, requestDao)).toList());
    }

    public ArrayList<State> getOpenTabs() {
        return openTabs;
    }

    public void openTab(Integer id) {
        if (id == null) {
            this.openTabs.add(new State((Integer) null, this.openTabsDao, requestDao));
        } else {
            this.openTabs.add(new State(id, this.openTabsDao, requestDao));
        }
    }

}
