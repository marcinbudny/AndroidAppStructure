package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.lib.api.contract.BearerToken;
import com.marcinbudny.androidappstructure.lib.api.requests.GetBearerTokenRequest;
import com.marcinbudny.androidappstructure.lib.infrastructure.RequestRunner;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.otto.Bus;

public class AuthenticatorImpl implements Authenticator {

    private RequestRunner requestRunner;
    private Bus bus;
    private AccessTokenStorage storage;

    public AuthenticatorImpl(
            RequestRunner requestRunner,
            Bus bus,
            AccessTokenStorage storage) {

        this.requestRunner = requestRunner;
        this.bus = bus;
        this.storage = storage;
    }

    @Override
    public void authenticateAndStoreToken(String consumerKey, String consumerSecret) {

        GetBearerTokenRequest request = new GetBearerTokenRequest(consumerKey, consumerSecret);

        requestRunner.run(request, new RequestListener<BearerToken>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                bus.post(new Failure(spiceException));
            }

            @Override
            public void onRequestSuccess(BearerToken bearerToken) {
                storage.setAccessToken(bearerToken.accessToken);
                bus.post(new Success());
            }
        });
    }

}
