package com.marcinbudny.androidappstructure.lib.views.trends;

import com.marcinbudny.androidappstructure.lib.api.contract.Trend;

public interface TrendsView {
    void displayAuthenticationError();

    void displayTrends(Trend.List trends);

    void displayTrendsDownloadError();

    void navigateToStatusesWithQuery(String query);
}
