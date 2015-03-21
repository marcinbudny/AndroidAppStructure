package com.marcinbudny.androidappstructure.testutils;

import com.marcinbudny.androidappstructure.lib.infrastructure.RequestRunner;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;


public class MockRequestRunner implements RequestRunner {

    private HashMap<Class, Queue<QueuedResponse>> queuedResponses = new HashMap<>();

    private Queue<SpiceRequest> requestsRun = new LinkedList<>();

    public void setSkipResponse(boolean skipResponse) {
        this.skipResponse = skipResponse;
    }

    private boolean skipResponse;

    @Override
    public <T> void run(SpiceRequest<T> request, RequestListener<T> requestListener) {

        requestsRun.add(request);

        if(skipResponse) return;

        QueuedResponse response = getQueuedResponseOrThrow(request);

        simulateResponse(requestListener, response);
    }

    private <T> void simulateResponse(RequestListener<T> requestListener, QueuedResponse response) {
        if(response.successResponse != null) {
            requestListener.onRequestSuccess((T)response.successResponse);
        } else {
            requestListener.onRequestFailure(response.failedResponse);
        }
    }

    private <T> QueuedResponse getQueuedResponseOrThrow(SpiceRequest<T> request) {
        if(!queuedResponses.containsKey(request.getClass()))
            throw new IllegalStateException("No queued responses for request of type " + request.getClass().getSimpleName());

        Queue<QueuedResponse> responses = queuedResponses.get(request.getClass());
        if(responses.isEmpty())
            throw new IllegalStateException("No queued responses for request of type " + request.getClass().getSimpleName());

        return responses.remove();
    }

    public MockRequestRunner enqueueSuccess(Class requestClass, Object response) {
        QueuedResponse queuedResponse = new QueuedResponse();
        queuedResponse.successResponse = response;

        addToQueue(requestClass, queuedResponse);

        return this;
    }

    public MockRequestRunner enqueueFailure(Class requestClass, int statusCode, String reason) {
        QueuedResponse queuedResponse = new QueuedResponse();
        queuedResponse.failedResponse = createSpiceException(statusCode, reason);

        addToQueue(requestClass, queuedResponse);

        return this;
    }

    private SpiceException createSpiceException(int statusCode, String reason) {
        Response response = new Response("http://placeholder", statusCode, reason, new ArrayList<Header>(), null);
        RetrofitError retrofitError = RetrofitError.httpError("http://placeholder", response, null, null);
        return new SpiceException("request failed", retrofitError);
    }

    private void addToQueue(Class requestClass, QueuedResponse queuedResponse) {
        if(!queuedResponses.containsKey(requestClass)) {
            queuedResponses.put(requestClass, new LinkedList<QueuedResponse>());
        }

        queuedResponses.get(requestClass).add(queuedResponse);
    }

    public SpiceRequest dequeueSentRequest() {
        if(requestsRun.isEmpty())
            throw new IllegalStateException("The requests run queue is empty");
        return requestsRun.remove();
    }

    private class QueuedResponse {
        public Object successResponse;
        public SpiceException failedResponse;
    }
}