package com.example.observationpattern.nopattern;

/**
 * Author: Brian Smithers <br>
 * Class: Channel <br>
 * Description: This class is used to instantiate a Subscriber object
 * to demonstrate the hardcoded version of the Observer Pattern.
 */
public class Subscriber {

    /**
     * The name of a {@link Subscriber} object.
     */
    private final String name;

    /**
     * The message received from a {@link Channel} object.
     */
    private String notification;

    /**
     * Author: Brian Smithers <br>
     * Constructor: Subscriber <br>
     * Description: This constructor assigns a name to the subscriber object.
     * @param name is the name of a subscriber object.
     */
    public Subscriber(String name) {
        this.name = name;
    }


    /**
     * Author: Brian Smithers <br>
     * Method: update <br>
     * Description: This method is called inside the notifyObservers method
     * from the {@link Channel} class. This method receives the uploaded video
     * and prints out the message with the name of the subscriber and message
     * that there is a new upload.
     * @param upload is a video upload from a channel object.
     */
    void update(String upload) {
        notification = upload;
        System.out.println(name + " received an upload notification: " +
                notification);
    }

    public String getName() {
        return name;
    }
}
