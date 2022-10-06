package com.example.observationpattern.pattern.subscriber;

import com.example.observationpattern.pattern.interfaces.Observer;
import com.example.observationpattern.pattern.interfaces.ShutdownThread;
import com.example.observationpattern.pattern.channel.Channel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Subscriber implements Runnable, Serializable, ShutdownThread, Observer {

    /**
     * {@code notificationBox} is the location of a text file with upload channel notifications
     */
    private final String notificationBox = "src/main/java/com/example/observationpattern/pattern/files/";

    /**
     * {@code notificationBoxUser} is the exact location of the subscriber's {@code notificationBox}.
     */
    private final String notificationBoxUser;

    /**
     * {@code feed} is the location of a text file with an archived list of channel notifications
     */
    private final String feed = "src/main/java/com/example/observationpattern/pattern/files/";

    /**
     * {@code feedUser} is the exact location of the subscriber's {@code feed}.
     */
    private final String feedUser;

    private String name;
    private final transient List<Channel> subscribedChannels = new ArrayList<>();
    private transient StringBuilder stringBuilder;

    private volatile boolean shutdown = false;

    /**
     * Author: Brian Smithers <br>
     * <p> Constructor: Subscriber </p>
     *
     * @param name the subscriber's name.
     * @param notificationBoxUser is the exact location of the subscriber's {@code notificationBox}.
     * @param feedUser is the exact location of the subscriber's {@code feed}.
     */
    public Subscriber(String name, String notificationBoxUser, String feedUser) {
        this.name = name;
        this.notificationBoxUser = notificationBoxUser;
        this.feedUser = feedUser;
    }

    /**
     * Method: update
     * <p>
     *     Writes the message to the Subscriber's Notification Box
     * </p>
     * @param message consists of a channel title, upload title, and date/time.
     */
    @Override
    public void update(String message) {
        // Add new string to notification box file
        updateNotificationBoxWithNewUpload("Video uploaded: " + message + "\n");
    }

    /**
     * Method: subscribeChannel
     * <p>
     *     This method allows the subscriber to sign up to a channel to receive notifications. The method
     *     sends a message to the channel's notification box that a new user wants to subscribe to their channel.
     * </p>
     * @param channel for this example, it should be any variation of "coding class 101".
     * @return true if channel was subscribed.
     */
    @Override
    public boolean subscribeChannel(String channel) {
        if (channel.equalsIgnoreCase("coding class 101")) {
            String temp = "src/main/java/com/example/observationpattern/pattern/files/CodingClass101SubscriberBox.dat";

            // Send a message to the channel's notification box.
            sendNewSubscriberNotification(temp, this);;
            return true;
        }
        return false;
    }

    // TODO implement this feature
    public boolean unSubscribeChannel(String channel) {
        // Send a message to the channel's unsubscribe notification box.
        // Model this method after subscribeChannel method in this class.
        if (channel.equalsIgnoreCase("coding class 101")) {
            String temp = "src/main/java/com/example/observationpattern/pattern/files/CodingClass101UnsubscribeBox.dat";

            // Send a message to the channel's notification box.
            sendNewUnsubscribeNotification(temp, this);
            return true;
        }
        return false;
    }

    /**
     * Method: sendNewSubscriberNotification
     * <p>
     *     Send a message to the subscriber that a user has subscribed.
     * </p>
     * @param temp the location of the channel's notification box on the "server."
     * @param subscriber the subscriber object.
     */
    private void sendNewSubscriberNotification(String temp, Subscriber subscriber) {
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(new FileOutputStream(temp, true));

            objectOutputStream.writeObject(subscriber);
            objectOutputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendNewUnsubscribeNotification(String temp, Subscriber subscriber) {
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(new FileOutputStream(temp, true));
            objectOutputStream.writeObject(subscriber);
            objectOutputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method: run <br>
     * <p>
     *     Description: Checks if there is a new notification in the {@code notificationBox}.
     *      If a new notification is available, empty the notificationBox,
     *      update the {@code feed} and finally, display the {@code feed}.
     * </p>
     *
     */
    @Override
    public void run() {
        while (!shutdown) {
            try {
                Thread.sleep(1000);

                boolean checkNotificationBox = checkNotificationBox();

                if (checkNotificationBox) {
                    emptyNotificationBox();
                    updateFeed();
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Method: checkNotificationBox
     * <p>
     *     Checks the subscriber's notification box to see if a new upload
     *     has been sent to their inbox via the {@code update()} method called
     *     by the channel class' {@code notifySubscribers} method. The data
     *     is saved in a StringBuilder object and called in the {@code updateFeed()} method.
     * </p>
     * @return {@code true} if the file is not empty.
     */
    private boolean checkNotificationBox() {
        File file = new File(notificationBox + notificationBoxUser);

        if (file.length() != 0) {
            try {
                Scanner readFile = new Scanner(file);
                while (readFile.hasNext()) {
                    getStringBuilder().setLength(0);
                    getStringBuilder().append(readFile.nextLine()).append("\n");
                }
                readFile.close();
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    /**
     * Method: emptyNotificationBox
     * <p>
     *     Empties the notification box.
     * </p>
     */
    private void emptyNotificationBox() {
        try {
            new FileWriter(notificationBox + notificationBoxUser, false).close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method: updateFeed
     * <p>
     *     This method updates the subscriber's feed. This method is used in the run() method.
     * </p>
     */
    private void updateFeed() {
        try {
            FileWriter fileWriter = new FileWriter(feed + feedUser, true);
            fileWriter.write(getStringBuilder().toString());
            fileWriter.close();

            getStringBuilder().setLength(0);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateFeedForSubscription(String message) {
        try {
            FileWriter fileWriter = new FileWriter(feed + feedUser, true);
            fileWriter.write(message);
            fileWriter.close();

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method: displayFeed
     * <p>
     *     This method is used to print the subscriber's feed to the console. A feed consists of
     *     new or past updates from a channel.
     * </p>
     */
    public String displayFeed() {
        File file = new File(feed + feedUser);
        try {
            if (file.length() != 0) {
                Scanner readFile = new Scanner(file);

                getStringBuilder().setLength(0);

                while (readFile.hasNext()) {
                    getStringBuilder().append(readFile.nextLine()).append("\n");
                }

                readFile.close();
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return getStringBuilder().toString();
    }

    private StringBuilder getStringBuilder() {
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
        }
        return stringBuilder;
    }

    /**
     * Method: updateNotificationBoxWithNewUpload
     * <p>
     *      This method is called by the update() method in this class to write a new message to the subscriber's
     *      notification box. This method is called when a channel uploads a new video.
     * </p>
     * @param message consists of a channel title, upload title, and date/time.
     */
    private void updateNotificationBoxWithNewUpload(String message) {
        try {
            FileWriter fileWriter = new FileWriter(notificationBox + notificationBoxUser, true);
            fileWriter.write(message);
            fileWriter.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscriber that = (Subscriber) o;

        if (!notificationBox.equals(that.notificationBox)) return false;
        if (!notificationBoxUser.equals(that.notificationBoxUser)) return false;
        if (!feed.equals(that.feed)) return false;
        if (!feedUser.equals(that.feedUser)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = notificationBox.hashCode();
        result = 31 * result + notificationBoxUser.hashCode();
        result = 31 * result + feed.hashCode();
        result = 31 * result + feedUser.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }
}
