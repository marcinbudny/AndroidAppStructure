package com.marcinbudny.androidappstructure.lib.views.statuses;

import com.marcinbudny.androidappstructure.lib.api.contract.Status;
import com.marcinbudny.androidappstructure.lib.logic.StatusesDownloader;
import com.marcinbudny.androidappstructure.testutils.MockBus;
import com.octo.android.robospice.persistence.exception.SpiceException;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StatusesPresenterTests {

    @Test
    public void should_start_download_when_view_created() throws Exception {
        // given
        StatusesPresenterFixture fixture = new StatusesPresenterFixture()
                .configure();

        // when
        fixture.sut.onReceivedQuery("query");

        // then
        fixture.verifyDownloadStartedFor("query");

    }

    @Test
    public void should_display_statuses_when_download_successful() throws Exception {
        // given
        StatusesPresenterFixture fixture = new StatusesPresenterFixture()
                .configure();

        Status.List statuses = new Status.List();
        StatusesDownloader.Success success = new StatusesDownloader.Success(statuses);

        // when
        fixture.sut.onDownloadSuccess(success);

        // then
        fixture.verifyStatusesDisplayed(statuses);

    }

    @Test
    public void should_display_error_when_download_fails() throws Exception {
        // given
        StatusesPresenterFixture fixture = new StatusesPresenterFixture()
                .configure();

        StatusesDownloader.Failure failure = new StatusesDownloader.Failure(new SpiceException(""));

        // when
        fixture.sut.onFailure(failure);

        // then
        fixture.verifyErrorTextDisplayed();

    }

    @Test
    public void should_start_download_on_refresh() throws Exception {
        // given
        StatusesPresenterFixture fixture = new StatusesPresenterFixture()
                .configure();

        // when
        fixture.sut.onReceivedQuery("query");
        fixture.sut.onRefresh();

        // then
        fixture.verifyDownloadStartedTwiceFor("query");
    }

    private static class StatusesPresenterFixture {
        public StatusesPresenter sut;

        private StatusesView view = mock(StatusesView.class);
        private StatusesDownloader statusesDownloader = mock(StatusesDownloader.class);
        private MockBus bus = new MockBus();

        public StatusesPresenterFixture configure() {
            sut = new StatusesPresenter(bus, statusesDownloader);
            sut.setView(view);
            return this;
        }

        public void verifyDownloadStartedFor(String expectedQuery) {
            verify(statusesDownloader).start(expectedQuery);
        }

        public void verifyStatusesDisplayed(Status.List statuses) {
            verify(view).displayStatuses(statuses);
        }

        public void verifyErrorTextDisplayed() {
            verify(view).displayDownloadErrorText();
        }

        public void verifyDownloadStartedTwiceFor(String expectedQuery) {
            verify(statusesDownloader, times(2)).start(expectedQuery);
        }
    }
}
