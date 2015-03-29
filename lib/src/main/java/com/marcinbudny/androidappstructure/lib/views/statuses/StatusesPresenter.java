package com.marcinbudny.androidappstructure.lib.views.statuses;

import com.marcinbudny.androidappstructure.lib.logic.StatusesDownloader;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class StatusesPresenter {

    private StatusesView view;
    private Bus bus;
    private StatusesDownloader statusesDownloader;
    private String query;

    public StatusesPresenter(Bus bus, StatusesDownloader statusesDownloader) {
        this.bus = bus;
        this.statusesDownloader = statusesDownloader;
    }

    public void setView(StatusesView view) {
        this.view = view;
    }

    public void onViewStarted() {
        bus.register(this);
    }

    public void onViewStopped() {
        bus.unregister(this);
    }

    public void onReceivedQuery(String query) {
        this.query = query;
        statusesDownloader.start(query);
    }

    @Subscribe
    public void onDownloadSuccess(StatusesDownloader.Success success) {
        view.displayStatuses(success.getStatuses());
    }

    @Subscribe
    public void onFailure(StatusesDownloader.Failure failure) {
        view.displayDownloadErrorText();
    }

    public void onRefresh() {
        statusesDownloader.start(query);
    }
}
