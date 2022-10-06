package com.example.observationpattern.nopattern;

/**
 * Author: Brian Smithers <br>
 * Class: Channel <br>
 * Description: This class is used to instantiate a Subscriber object
 * to demonstrate the hardcoded version of the Observer Pattern.
 */
public class Channel {

    /**
     * The channel's name
     */
    private String name;

    /**
     * The message sent to a {@link Subscriber} object.
     */
    private String upload;

    /**
     * Two hardcoded instances of a {@link Subscriber} object.
     */
    private Subscriber subscriber1;
    private Subscriber subscriber2;

    public Channel(String name) {
        this.name = name;
    }

    /**
     * Author: Brian Smithers <br>
     * Method: upload <br>
     * Description: This method updates the string upload and
     * calls the notifyObservers method.
     * @param value
     */
    void upload(String value) {
        upload = value;
        notifyObservers();
    }

    /**
     * Author: Brian Smithers <br>
     * Method: notifyObservers <br>
     * Description: This method calls each subscriber object's method update.
     * The update method sends the upload String to the subscriber object.
     */
    public void notifyObservers() {
        if (subscriber1 != null & subscriber2 != null) {
            subscriber1.update(upload);
            subscriber2.update(upload);
        }
        else if (subscriber1 != null) {
            subscriber1.update(upload);
        }
        else if (subscriber2 != null) {
            subscriber2.update(upload);
        }
    }

    /**
     * Author: Brian Smithers <br>
     * Method: subscribe <br>
     * Description: This method subscribes a {@link Subscriber} object to one of the channel's two
     * slots for a subscriber. A message is printed when the subscriber is successfully subscribed.
     * @param subscriber a new channel subscriber.
     */
    public void subscribe(Subscriber subscriber) {
        if (subscriber1 == null) {
            setSubscriber1(subscriber);
            System.out.println(subscriber.getName() + " has successfully subscribed");
        }
        else if (subscriber2 == null) {
            setSubscriber2(subscriber);
            System.out.println(subscriber.getName() + " has successfully subscribed");
        }
        else {
            System.err.println("An error occurred while subscribing " + subscriber.getName() + ".");
        }
    }

    /**
     * Author: Brian Smithers <br>
     * Method: unSubscribe <br>
     * Description: Unsubscribes a subscriber from the channel.
     */
    public void unSubscribe(Subscriber subscriber) {
        if (subscriber1 != null && subscriber1.equals(subscriber)) {
            System.out.println(subscriber.getName() + " has successfully unsubscribed");
            setSubscriber1(null);
        }
        else if (subscriber2 != null && subscriber2.equals(subscriber)) {
            System.out.println(subscriber.getName() + " has successfully unsubscribed");
            setSubscriber2(null);
        }
        else {
            //System.err.println("An error occurred while unsubscribing " + subscriber.getName() + ".");
        }
    }

    public void setSubscriber1(Subscriber subscriber1) {
        this.subscriber1 = subscriber1;
    }

    public void setSubscriber2(Subscriber subscriber2) {
        this.subscriber2 = subscriber2;
    }
}
