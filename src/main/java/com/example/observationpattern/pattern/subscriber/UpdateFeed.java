package com.example.observationpattern.pattern.subscriber;

import com.example.observationpattern.pattern.interfaces.ShutdownThread;
import javafx.scene.control.TextArea;

public class UpdateFeed implements Runnable, ShutdownThread {

    private final TextArea ta;
    private final Subscriber s1;

    private volatile boolean shutdown = false;

    public UpdateFeed(TextArea ta, Subscriber s1) {
        this.ta = ta;
        this.s1 = s1;
    }

    @Override
    public void run() {
        while (!shutdown) {
            try {
                Thread.sleep(1000);
                ta.setText(s1.displayFeed());
                ta.appendText("");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }
}
