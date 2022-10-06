package com.example.observationpattern.pattern.interfaces;

import com.example.observationpattern.pattern.subscriber.Subscriber;

public interface Subject {
    void subscribe(Subscriber sub);

    void unSubscribe(Subscriber subscriber);

    boolean notifySubscribers(String message);

    boolean upload(String video);
}
