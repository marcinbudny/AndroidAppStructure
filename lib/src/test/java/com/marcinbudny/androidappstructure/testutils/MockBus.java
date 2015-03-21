package com.marcinbudny.androidappstructure.testutils;

import com.squareup.otto.Bus;

import java.util.LinkedList;
import java.util.Queue;

public class MockBus extends Bus {

    private Queue<Object> events = new LinkedList<>();

    @Override
    public void post(Object event) {
        events.add(event);
    }

    public Object getNextEventOrThrow() {
        if(events.isEmpty())
            throw new IllegalStateException("The bus mock queue is empty");

        return events.remove();
    }
}
