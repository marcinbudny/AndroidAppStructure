package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.lib.api.contract.Status;
import com.marcinbudny.androidappstructure.lib.api.contract.StatusQueryResponse;
import com.marcinbudny.androidappstructure.lib.api.requests.SearchStatusesRequest;
import com.marcinbudny.androidappstructure.testutils.MockBus;
import com.marcinbudny.androidappstructure.testutils.MockRequestRunner;
import com.marcinbudny.androidappstructure.testutils.StatusQueryResponseBuilder;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class StatusesDownloaderTests {

    @Test
    public void can_send_request_on_download_start() throws Exception {
        // given
        StatusesDownloaderFixture fixture = new StatusesDownloaderFixture()
                .withSkipResponse()
                .configure();

        // when
        fixture.sut.start("query");

        // then
        fixture.verifyRequestSentFor("query");
    }

    @Test
    public void should_notify_by_event_when_download_successful() throws Exception {
        // given
        StatusQueryResponse response = new StatusQueryResponseBuilder().build();

        StatusesDownloaderFixture fixture = new StatusesDownloaderFixture()
                .withSuccessResponse(response)
                .configure();

        // when
        fixture.sut.start("query");

        // then
        fixture.verifySuccessEventSent();

    }

    @Test
    public void can_extract_statuses_list_from_response() throws Exception {
        // given
        StatusQueryResponse response = new StatusQueryResponseBuilder()
                .withStatus("marcin", "hello")
                .withStatus("someone", "something")
                .build();

        StatusesDownloaderFixture fixture = new StatusesDownloaderFixture()
                .withSuccessResponse(response)
                .configure();

        // when
        fixture.sut.start("query");

        // then
        fixture.verifySuccessEventContains(response.statuses);
    }

    @Test
    public void should_notify_by_event_when_download_fails() throws Exception {
        // given
        StatusesDownloaderFixture fixture = new StatusesDownloaderFixture()
                .withGenericFailureResponse()
                .configure();

        // when
        fixture.sut.start("query");

        // then
        fixture.verifyFailureEventSent();
    }

    private class StatusesDownloaderFixture {

        public StatusesDownloaderImpl sut;

        public MockRequestRunner requestRunner = new MockRequestRunner();
        public MockBus bus = new MockBus();

        public StatusesDownloaderFixture configure() {
            sut = new StatusesDownloaderImpl(requestRunner, bus);
            return this;
        }

        public StatusesDownloaderFixture withSkipResponse() {
            requestRunner.setSkipResponse(true);
            return this;
        }

        public StatusesDownloaderFixture withSuccessResponse(StatusQueryResponse response) {
            requestRunner.enqueueSuccess(SearchStatusesRequest.class, response);
            return this;
        }

        public void verifyRequestSentFor(String expectedQuery) {
            Object request = requestRunner.dequeueSentRequest();
            assertThat(request, instanceOf(SearchStatusesRequest.class));

            SearchStatusesRequest searchStatusesRequest = (SearchStatusesRequest) request;
            assertThat(searchStatusesRequest.getQuery(), equalTo(expectedQuery));

        }

        public void verifySuccessEventSent() {
            Object event = bus.getNextEventOrThrow();
            assertThat(event, instanceOf(StatusesDownloader.Success.class));

        }

        public void verifySuccessEventContains(Status.List expectedStatuses) {
            StatusesDownloader.Success event = (StatusesDownloader.Success) bus.getNextEventOrThrow();
            assertThat(event.getStatuses(), equalTo(expectedStatuses));
        }

        public StatusesDownloaderFixture withGenericFailureResponse() {
            requestRunner.enqueueFailure(SearchStatusesRequest.class, 500, "Internal Server Error");
            return this;
        }

        public void verifyFailureEventSent() {
            Object event = bus.getNextEventOrThrow();
            assertThat(event, instanceOf(StatusesDownloader.Failure.class));
        }
    }
}
