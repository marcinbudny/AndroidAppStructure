package com.marcinbudny.androidappstructure.testutils;

import com.marcinbudny.androidappstructure.lib.api.contract.Trend;
import com.marcinbudny.androidappstructure.lib.api.contract.TrendQueryResult;

public class TrendQueryResultListBuilder {

    private Trend.List trends = new Trend.List();

    public TrendQueryResultListBuilder withTrend(String name, String query) {
        Trend trend = new Trend();
        trend.name = name;
        trend.query = query;
        trends.add(trend);

        return this;
    }

    public TrendQueryResult.List build() {
        TrendQueryResult trendQueryResult = new TrendQueryResult();
        trendQueryResult.trends = trends;

        TrendQueryResult.List result = new TrendQueryResult.List();
        result.add(trendQueryResult);
        return result;
    }
}
