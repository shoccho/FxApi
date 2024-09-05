package org.example.state;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.model.Request;
import org.example.storage.OpenTabsDao;
import org.example.storage.RequestDAO;
import org.example.ui.components.actions.UpdateHistory;

import java.util.ArrayList;

public class ApplicationState {
    private ObservableList<State> openTabs;
    private RequestDAO requestDao;
    private OpenTabsDao openTabsDao;
    private ObservableList<Request> history;
    private UpdateHistory refreshHistory;

    public ApplicationState(RequestDAO requestDao, OpenTabsDao openTabsDao) {
        this.openTabs = FXCollections.observableArrayList();
        this.requestDao = requestDao;
        this.openTabsDao = openTabsDao;

        refreshHistory = new UpdateHistory() {
            @Override
            public void execute() {
                history.clear();
                history.addAll(requestDao.getHistory());
            }
        };

        loadAllOpenTabs();
        history = FXCollections.observableArrayList();
        this.history.addAll(this.requestDao.getHistory());

    }

    private void loadAllOpenTabs() {
        ArrayList<Request> allOpenRequests = this.openTabsDao.getAllOpenTabs();
        this.openTabs.addAll(allOpenRequests.stream().map(e -> new State(e, openTabsDao, requestDao, refreshHistory)).toList());
    }

    public ObservableList<State> getOpenTabs() {
        return openTabs;
    }

    public void removeOpenTab(Integer reqId) {
        this.openTabs.removeIf(e -> {
            return e.getRequest().getId() == reqId;
        });
        this.openTabsDao.removeOpenTab(reqId);
    }

    public ObservableList<Request> getHistory() {
        return this.history;
    }

    public void openTab(Integer id) {
        if (id == null) {
            this.openTabs.add(new State((Integer) null, this.openTabsDao, requestDao, refreshHistory));
        } else {
            this.openTabs.add(new State(id, this.openTabsDao, requestDao, refreshHistory));
        }
    }

}
