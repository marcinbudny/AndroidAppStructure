package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.testutils.MockSharedPreferencesOperations;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AccessTokenStorageTests {

    @Test
    public void can_save_and_retrieve_access_token() throws Exception {

        // given
        AccessTokenStorageFixture fixture = new AccessTokenStorageFixture().configure();

        // when
        fixture.sut.setAccessToken("access_token");
        String accessToken = fixture.sut.getAccessToken();

        // then
        assertThat(accessToken, equalTo("access_token"));
    }

    @Test
    public void hasAccessToken_should_return_false_if_access_token_not_set() throws Exception {

        // given
        AccessTokenStorageFixture fixture = new AccessTokenStorageFixture().configure();

        // when
        boolean hasAccessToken = fixture.sut.hasAccessToken();

        // then
        assertThat(hasAccessToken, equalTo(false));

    }

    @Test
    public void hasAccessToken_should_return_true_if_access_token_was_set() throws Exception {

        // given
        AccessTokenStorageFixture fixture = new AccessTokenStorageFixture().configure();

        // when
        fixture.sut.setAccessToken("ignored");
        boolean hasAccessToken = fixture.sut.hasAccessToken();

        // then
        assertThat(hasAccessToken, equalTo(true));

    }




    private static class AccessTokenStorageFixture {

        public AccessTokenStorageImpl sut;

        private MockSharedPreferencesOperations sharedPreferencesOperations = new MockSharedPreferencesOperations();

        public AccessTokenStorageFixture configure() {
            sut = new AccessTokenStorageImpl(sharedPreferencesOperations);
            return this;
        }
    }
}
