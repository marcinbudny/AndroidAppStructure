package com.marcinbudny.androidappstructure.views.trends;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.marcinbudny.androidappstructure.R;
import com.marcinbudny.androidappstructure.infrastructure.ThisApplication;
import com.marcinbudny.androidappstructure.lib.api.contract.Trend;
import com.marcinbudny.androidappstructure.lib.views.trends.TrendsPresenter;
import com.marcinbudny.androidappstructure.lib.views.trends.TrendsView;
import com.marcinbudny.androidappstructure.views.statuses.StatusesActivity;

import javax.inject.Inject;


public class TrendsActivity extends ListActivity implements TrendsView {

    @Inject
    public TrendsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThisApplication.inject(this);

        presenter.setView(this);

        setupListeners();
    }

    private void setupListeners() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onTrendSelected(position);
            }
        });
    }

    private void onTrendSelected(int trendIndex) {
        presenter.onTrendSelected(trendIndex);
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
    public void displayAuthenticationError() {
        Toast.makeText(this, R.string.authentication_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayTrends(Trend.List trends) {
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trends));
    }

    @Override
    public void displayTrendsDownloadError() {
        Toast.makeText(this, R.string.trends_download_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToStatusesWithQuery(String query) {
        Intent intent = new Intent(this, StatusesActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }
}
