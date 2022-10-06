package com.example.observationpattern.pattern.interfaces;

public interface Observer {
    void update(String message);

    boolean subscribeChannel(String channel);
}
