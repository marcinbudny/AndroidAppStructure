package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.lib.api.contract.Trend;
import com.marcinbudny.androidappstructure.lib.api.contract.TrendQueryResult;
import com.marcinbudny.androidappstructure.lib.api.requests.GetTrendsRequest;
import com.marcinbudny.androidappstructure.testutils.MockBus;
import com.marcinbudny.androidappstructure.testutils.MockRequestRunner;
import com.marcinbudny.androidappstructure.testutils.TrendQueryResultListBuilder;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class TrendsDownloaderTests {

    private final static int EXPECTED_LOCATION_ID = 1;

    @Test
    public void can_send_download_trending_tags_request() throws Exception {

        // given
        TrendsDownloaderFixture fixture = new TrendsDownloaderFixture()
                .withSkipResponse()
                .configure();

        // when
        fixture.sut.start();

        // then
        fixture.verifyDownloadRequestSent(EXPECTED_LOCATION_ID);
    }

    @Test
    public void should_notify_by_event_when_download_successful() throws Exception {
        // given
        TrendQueryResult.List response = new TrendQueryResultListBuilder().build();

        TrendsDownloaderFixture fixture = new TrendsDownloaderFixture()
                .withSuccessResponse(response)
                .configure();

        // when
        fixture.sut.start();

        // then
        fixture.verifySuccessEventSent();
    }

    @Test
    public void can_extract_first_trend_list_from_response() throws Exception {
        // given
        TrendQueryResult.List response = new TrendQueryResultListBuilder()
                .withTrend("trend1", "%23trend1")
                .withTrend("trend2", "%23trend2")
                .build();


        TrendsDownloaderFixture fixture = new TrendsDownloaderFixture()
                .withSuccessResponse(response)
                .configure();

        // when
        fixture.sut.start();

        // then
        Trend.List expectedTrends = response.get(0).trends;
        fixture.verifySuccessEventContains(expectedTrends);
    }

    @Test
    public void should_return_empty_trend_list_if_response_contains_no_trends() throws Exception {
        // given
        TrendQueryResult.List response = new TrendQueryResult.List();


        TrendsDownloaderFixture fixture = new TrendsDownloaderFixture()
                .withSuccessResponse(response)
                .configure();

        // when
        fixture.sut.start();

        // then
        fixture.verifySuccessEventContainsEmptyTrendList();
    }

    @Test
    public void should_notify_by_event_about_failure() throws Exception {
        // given
        TrendsDownloaderFixture fixture = new TrendsDownloaderFixture()
                .withGenericFailureResponse()
                .configure();

        // when
        fixture.sut.start();

        // then
        fixture.verifyFailureEventSent();

    }

    private static class TrendsDownloaderFixture {


        private MockRequestRunner requestRunner = new MockRequestRunner();
        private MockBus bus = new MockBus();

        public TrendsDownloaderImpl sut;

        public TrendsDownloaderFixture withSkipResponse() {
            requestRunner.setSkipResponse(true);
            return this;
        }

        public TrendsDownloaderFixture withSuccessResponse(TrendQueryResult.List response) {
            requestRunner.enqueueSuccess(GetTrendsRequest.class, response);
            return this;
        }

        public TrendsDownloaderFixture withGenericFailureResponse() {
            requestRunner.enqueueFailure(GetTrendsRequest.class, 500, "Internal Server Error");
            return this;
        }


        public TrendsDownloaderFixture configure() {
            sut = new TrendsDownloaderImpl(requestRunner, bus);
            return this;
        }

        public void verifyDownloadRequestSent(int expectedLocationId) {
            Object request = requestRunner.dequeueSentRequest();
            assertThat(request, instanceOf(GetTrendsRequest.class));

            GetTrendsRequest getTrendsRequest = (GetTrendsRequest) request;
            assertThat(getTrendsRequest.getLocationId(), equalTo(expectedLocationId));
        }

        public void verifySuccessEventSent() {
            Object event = bus.getNextEventOrThrow();
            assertThat(event, CoreMatchers.instanceOf(TrendsDownloader.Success.class));
        }

        public void verifySuccessEventContains(Trend.List expectedTrends) {
            TrendsDownloader.Success event = (TrendsDownloader.Success) bus.getNextEventOrThrow();
            assertThat(event.getTrends(), equalTo(expectedTrends));
        }

        public void verifySuccessEventContainsEmptyTrendList() {
            TrendsDownloader.Success event = (TrendsDownloader.Success) bus.getNextEventOrThrow();
            assertThat(event.getTrends(), not(equalTo(null)));
            assertThat(event.getTrends().size(), equalTo(0));

        }

        public void verifyFailureEventSent() {
            Object event = bus.getNextEventOrThrow();
            assertThat(event, CoreMatchers.instanceOf(TrendsDownloader.Failure.class));
        }
    }
}
