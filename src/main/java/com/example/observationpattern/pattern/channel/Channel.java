package com.example.observationpattern.pattern.channel;

import com.example.observationpattern.pattern.interfaces.Subject;
import com.example.observationpattern.pattern.subscriber.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Channel implements Subject {

    /**
     * {@code subs} is a persisted list that is loaded from a .dat file on each new object creation.
     */
    public final List<Subscriber> subs = new ArrayList<>();

    /**
     * {@code channelName} is the name of the channel.
     */
    private String channelName;

    /**
     * Constructor: Channel <br>
     * <p>
     *     Description: Creates a new channel object.
     * </p>
     * @param channelName see definition above.
     */
    public Channel(String channelName) {
        this.channelName = channelName;
    }

    public Channel() {}

    /**
     * Method: subscribe
     * <p>
     *     Description: This method adds subscribers to the List of subscribers. This method is called from the
     *     loadSubscribers method.
     * </p>
     * @param sub a subscriber object.
     */
    @Override
    public void subscribe(Subscriber sub) {
        subs.add(sub);
    }

    @Override
    public void unSubscribe(Subscriber subscriber) {
        subs.remove(subscriber);
    }

    /**
     * Method: notifySubscribers
     * <p>
     *     Description: Sends the channel name, video title, and upload date. <br>
     *     his method should only be called by {@code upload} method.
     *     Ex. "Coding Class 101, "How to use Conditional Statements", "new Date()"
     * </p>
     * @param message
     */
    @Override
    public boolean notifySubscribers(String message) {
        for (Subscriber sub : subs) {
            sub.update(message);
        }
        return true;
    }

    /**
     * Method: upload
     * <p>
     *     Description: This method notifies all subscribers of a new video.
     * </p>
     * @param video name of the video uploaded.
     */
    @Override
    public boolean upload(String video) {
        return notifySubscribers(channelName + " - " + video + ", " + new Date());
    }

    /**
     * Method: updateSubsList
     * <p>
     *     Description: This method is called when there are subscribers unsubscribe.
     *      The subs list repopulates with the remaining subscribers.
     * </p>
     * @param tempUnsubscribeList this list is storing unsubscribe requests.
     */
    public void updateSubsList(List<Subscriber> tempUnsubscribeList) {
        // Prepare a union
        List<Subscriber> union = new ArrayList<>(tempUnsubscribeList);
        union.addAll(subs);

        // Prepare an intersection
        List<Subscriber> intersection = new ArrayList<>(tempUnsubscribeList);
        intersection.retainAll(subs);

        // Subtract the intersection from the union
        union.removeAll(intersection);

        subs.clear();
        subs.addAll(union);
    }

    public List<Subscriber> getSubs() {
        return subs;
    }
}
