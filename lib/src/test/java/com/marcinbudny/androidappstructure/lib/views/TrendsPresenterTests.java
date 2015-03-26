package com.marcinbudny.androidappstructure.lib.views;

import com.marcinbudny.androidappstructure.lib.Settings;
import com.marcinbudny.androidappstructure.lib.api.contract.Trend;
import com.marcinbudny.androidappstructure.lib.logic.AccessTokenStorage;
import com.marcinbudny.androidappstructure.lib.logic.Authenticator;
import com.marcinbudny.androidappstructure.lib.logic.TrendsDownloader;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.squareup.otto.Bus;

import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrendsPresenterTests {

    @Test
    public void should_start_authentication_if_no_token_is_present() throws Exception {
        // given
        TrendsPresenterFixture fixture = new TrendsPresenterFixture()
                .withNoToken()
                .configure();

        // when
        fixture.sut.onViewStarted();

        // then
        fixture.verifyAuthenticationStartedFor(Settings.CONSUMER_KEY, Settings.CONSUMER_SECRET);
    }


    @Test
    public void should_not_start_authentication_if_token_is_already_present() throws Exception {
        // given
        TrendsPresenterFixture fixture = new TrendsPresenterFixture()
                .withToken()
                .configure();

        // when
        fixture.sut.onViewStarted();

        // then
        fixture.verifyAuthenticationNotStarted();

    }

    @Test
    public void should_start_trends_download_when_token_was_already_present() {
        // given
        TrendsPresenterFixture fixture = new TrendsPresenterFixture()
                .withToken()
                .configure();

        // when
        fixture.sut.onViewStarted();

        // then
        fixture.verifyTrendsDownloadStarted();
    }

    @Test
    public void should_start_trends_download_when_authentication_completes() throws Exception {
        // given
        Authenticator.Success success = new Authenticator.Success();

        TrendsPresenterFixture fixture = new TrendsPresenterFixture()
                .withNoToken()
                .configure();

        // when
        fixture.sut.onAuthenticationSuccess(success);

        // then
        fixture.verifyTrendsDownloadStarted();
    }

    @Test
    public void should_display_error_when_authentication_fails() throws Exception {
        // given
        Authenticator.Failure failure = new Authenticator.Failure(new SpiceException(""));

        TrendsPresenterFixture fixture = new TrendsPresenterFixture()
                .configure();

        // when
        fixture.sut.onAuthenticationFailure(failure);

        // then
        fixture.verifyAuthenticationErrorDisplayed();

    }

    @Test
    public void should_display_trends_when_download_successful() throws Exception {
        // given
        Trend.List trends = new Trend.List();
        TrendsDownloader.Success success = new TrendsDownloader.Success(trends);

        TrendsPresenterFixture fixture = new TrendsPresenterFixture()
                .configure();

        // when
        fixture.sut.onTrendsDownloadSuccess(success);

        // then
        fixture.verifyTrendsDisplayed(trends);

    }

    @Test
    public void should_display_trends_download_error_when_download_fails() throws Exception {
        // given
        TrendsDownloader.Failure failure = new TrendsDownloader.Failure(new SpiceException(""));

        TrendsPresenterFixture fixture = new TrendsPresenterFixture()
                .configure();

        // when
        fixture.sut.onTrendsDownloadFailure(failure);

        // then
        fixture.verifyTrendsDownloadErrorDisplayed();
    }

    private static class TrendsPresenterFixture {

        private Authenticator authenticator = mock(Authenticator.class);
        private AccessTokenStorage accessTokenStorage = mock(AccessTokenStorage.class);
        private TrendsDownloader trendsDownloader = mock(TrendsDownloader.class);
        private Bus bus = mock(Bus.class);
        private TrendsView view = mock(TrendsView.class);

        public TrendsPresenter sut;

        public TrendsPresenterFixture withNoToken() {
            when(accessTokenStorage.hasAccessToken()).thenReturn(false);
            return this;
        }

        public TrendsPresenterFixture withToken() {
            when(accessTokenStorage.hasAccessToken()).thenReturn(true);
            when(accessTokenStorage.getAccessToken()).thenReturn("accessToken");
            return this;
        }

        public TrendsPresenterFixture configure() {
            sut = new TrendsPresenter(bus, authenticator, accessTokenStorage, trendsDownloader);
            sut.setView(view);
            return this;
        }

        public void verifyAuthenticationStartedFor(String consumerKey, String consumerSecret) {
            verify(authenticator).authenticateAndStoreToken(consumerKey, consumerSecret);
        }

        public void verifyAuthenticationNotStarted() {
            verify(authenticator, never()).authenticateAndStoreToken(anyString(), anyString());
        }

        public void verifyTrendsDownloadStarted() {
            verify(trendsDownloader).start();
        }

        public void verifyAuthenticationErrorDisplayed() {
            verify(view).displayAuthenticationError();
        }

        public void verifyTrendsDisplayed(Trend.List trends) {
            verify(view).displayTrends(trends);
        }

        public void verifyTrendsDownloadErrorDisplayed() {
            verify(view).displayTrendsDownloadError();
        }
    }
}
