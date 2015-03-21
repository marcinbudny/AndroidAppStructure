package com.marcinbudny.androidappstructure.lib.views;

import com.marcinbudny.androidappstructure.lib.Settings;
import com.marcinbudny.androidappstructure.lib.logic.AccessTokenStorage;
import com.marcinbudny.androidappstructure.lib.logic.Authenticator;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrendingTagsPresenterTests {

    @Test
    public void should_start_authentication_if_no_token_is_already_present() throws Exception {
        // given
        TrendingTagsPresenterFixture fixture = new TrendingTagsPresenterFixture()
                .withNoToken()
                .configure();

        // when
        fixture.sut.onViewStarted();

        // then
        fixture.verifyAuthenticationStartedFor(Settings.CONSUMER_KEY, Settings.CONSUMER_SECRET);
    }


    private static class TrendingTagsPresenterFixture {

        private Authenticator authenticator = mock(Authenticator.class);
        private AccessTokenStorage accessTokenStorage = mock(AccessTokenStorage.class);

        public TrendingTagsPresenter sut;

        public TrendingTagsPresenterFixture withNoToken() {
            when(accessTokenStorage.hasAccessToken()).thenReturn(false);
            return this;
        }

        public TrendingTagsPresenterFixture configure() {
            sut = new TrendingTagsPresenter(authenticator, accessTokenStorage);
            return this;
        }

        public void verifyAuthenticationStartedFor(String consumerKey, String consumerSecret) {
            verify(authenticator).authenticateAndStoreToken(consumerKey, consumerSecret);
        }
    }
}
