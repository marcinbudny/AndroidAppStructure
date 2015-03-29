package com.marcinbudny.androidappstructure.views.statuses;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.marcinbudny.androidappstructure.R;
import com.marcinbudny.androidappstructure.infrastructure.ThisApplication;
import com.marcinbudny.androidappstructure.lib.api.contract.Status;
import com.marcinbudny.androidappstructure.lib.views.statuses.StatusesPresenter;
import com.marcinbudny.androidappstructure.lib.views.statuses.StatusesView;

import javax.inject.Inject;

public class StatusesActivity extends ListActivity implements StatusesView {

    @Inject
    StatusesPresenter presenter;

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThisApplication.inject(this);

        setupLayout();
        setupListeners();

        presenter.setView(this);

        startDownload();
    }

    private void setupListeners() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });
    }

    private void setupLayout() {
        setContentView(R.layout.activity_statuses);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.statuses_refresh_layout);
    }


    private void startDownload() {
        String query = getQueryFromIntent();
        presenter.onReceivedQuery(query);
    }

    public String getQueryFromIntent() {
        Intent intent = getIntent();
        return intent.getStringExtra("query");
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewStarted();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewStopped();
    }

    @Override
    public void displayStatuses(Status.List statuses) {
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statuses));
        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    @Override
    public void displayDownloadErrorText() {
        Toast.makeText(this, R.string.statuses_download_error, Toast.LENGTH_LONG).show();
    }
}
