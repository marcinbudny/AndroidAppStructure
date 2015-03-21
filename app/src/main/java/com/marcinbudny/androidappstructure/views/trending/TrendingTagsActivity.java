package com.marcinbudny.androidappstructure.views.trending;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.marcinbudny.androidappstructure.R;
import com.marcinbudny.androidappstructure.infrastructure.ThisApplication;
import com.marcinbudny.androidappstructure.lib.views.TrendingTagsPresenter;
import com.marcinbudny.androidappstructure.lib.views.TrendingTagsView;

import javax.inject.Inject;


public class TrendingTagsActivity extends ActionBarActivity implements TrendingTagsView {

    @Inject
    public TrendingTagsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_tags);

        ThisApplication.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewStarted();
    }
}
