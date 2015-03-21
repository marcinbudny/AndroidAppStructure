package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.lib.api.contract.BearerToken;
import com.marcinbudny.androidappstructure.lib.api.requests.GetBearerTokenRequest;
import com.marcinbudny.androidappstructure.testutils.MockBus;
import com.marcinbudny.androidappstructure.testutils.MockRequestRunner;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuthenticatorTests {

    @Test
    public void can_send_authentication_request() throws Exception {

        // given
        AuthenticatorFixture fixture = new AuthenticatorFixture()
                .withSkipResponse()
                .configure();

        // when
        fixture.sut.authenticateAndStoreToken("consumerKey", "consumerSecret");

        // then
        fixture.verifyAuthenticationRequestSent("consumerKey", "consumerSecret");
    }

    @Test
    public void should_notify_by_event_when_authentication_successful() {

        // given
        AuthenticatorFixture fixture = new AuthenticatorFixture()
                .withSuccessResponse()
                .configure();

        // when
        fixture.sut.authenticateAndStoreToken("consumerKey", "consumerSecret");

        // then
        fixture.verifySuccessEventSent();

    }

    @Test
    public void should_store_access_token_when_authentication_successful() {

        // given
        AuthenticatorFixture fixture = new AuthenticatorFixture()
                .withSuccessResponse("accessToken")
                .configure();

        // when
        fixture.sut.authenticateAndStoreToken("consumerKey", "consumerSecret");

        // then
        fixture.verifyTokenStored("accessToken");

    }

    @Test
    public void should_notify_by_event_when_authentication_fails() {

        // given
        AuthenticatorFixture fixture = new AuthenticatorFixture()
                .withGenericFailureResponse()
                .configure();

        // when
        fixture.sut.authenticateAndStoreToken("consumerKey", "consumerSecret");

        // then
        fixture.verifyFailureEventSent();
    }

    private static class AuthenticatorFixture {

        public AuthenticatorImpl sut;

        private MockRequestRunner requestRunner = new MockRequestRunner();
        private MockBus bus = new MockBus();
        private AccessTokenStorage storage = mock(AccessTokenStorage.class);

        private AuthenticatorFixture withSkipResponse() {
            requestRunner.setSkipResponse(true);
            return this;
        }

        public AuthenticatorFixture withSuccessResponse() {
            return withSuccessResponse("ignoredToken");
        }

        public AuthenticatorFixture withSuccessResponse(String accessToken) {

            BearerToken bearerToken = new BearerToken();
            bearerToken.accessToken = accessToken;

            requestRunner.enqueueSuccess(GetBearerTokenRequest.class, bearerToken);
            return this;
        }

        public AuthenticatorFixture withGenericFailureResponse() {
            requestRunner.enqueueFailure(GetBearerTokenRequest.class, 500, "Internal server error");
            return this;
        }

        public AuthenticatorFixture configure() {
            sut = new AuthenticatorImpl(requestRunner, bus, storage);
            return this;
        }

        public void verifyAuthenticationRequestSent(String consumerKey, String consumerSecret) {
            Object request = requestRunner.dequeueSentRequest();
            assertThat(request, instanceOf(GetBearerTokenRequest.class));

            GetBearerTokenRequest getBearerTokenRequest = (GetBearerTokenRequest) request;
            assertThat(getBearerTokenRequest.getConsumerKey(), equalTo(consumerKey));
            assertThat(getBearerTokenRequest.getConsumerSecret(), equalTo(consumerSecret));
        }

        public void verifySuccessEventSent() {
            Object event = bus.getNextEventOrThrow();
            assertThat(event, instanceOf(Authenticator.Success.class));
        }

        public void verifyFailureEventSent() {
            Object event = bus.getNextEventOrThrow();
            assertThat(event, instanceOf(Authenticator.Failure.class));
        }

        public void verifyTokenStored(String accessToken) {
            verify(storage).setAccessToken(accessToken);
        }
    }
}
