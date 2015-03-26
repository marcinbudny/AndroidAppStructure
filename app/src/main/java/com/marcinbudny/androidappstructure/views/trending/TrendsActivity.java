package com.marcinbudny.androidappstructure.views.trending;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.marcinbudny.androidappstructure.R;
import com.marcinbudny.androidappstructure.infrastructure.ThisApplication;
import com.marcinbudny.androidappstructure.lib.api.contract.Trend;
import com.marcinbudny.androidappstructure.lib.views.TrendsPresenter;
import com.marcinbudny.androidappstructure.lib.views.TrendsView;

import javax.inject.Inject;


public class TrendsActivity extends ListActivity implements TrendsView {

    @Inject
    public TrendsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_trends);
        //setContentView(android.R.layout.);

        ThisApplication.inject(this);

        presenter.setView(this);
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
}
