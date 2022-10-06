package com.example.observationpattern.pattern.channel;

import com.example.observationpattern.pattern.interfaces.ShutdownThread;
import com.example.observationpattern.pattern.subscriber.Subscriber;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChannelServices implements Runnable, ShutdownThread {
    private volatile boolean shutdown = false;

    /**
     * {@code subscriberInboxLocation} is the location on the "server" where new subscriber notifications are stored.
     * This .dat file is emptied as it is filled and transferred to the persisted list.
     */
    private final String subscriberInboxLocation;

    /**
     * {@code subscribersPersistedList} is the location on the "server" where all subscribers are located. <br>
     * The data is stored on a .dat file and loaded into the {@code subs} {@code List} object on each new creation.
     */
    private final String subscribersPersistedList;

    private final String unSubscribeInboxLocation;

    private final Channel channel;

    public ChannelServices(Channel channel, String subscriberInboxLocation, String subscribersPersistedList,
                           String unSubscribeInboxLocation) {
        this.channel = channel;
        this.subscriberInboxLocation = subscriberInboxLocation;
        this.subscribersPersistedList = subscribersPersistedList;
        this.unSubscribeInboxLocation = unSubscribeInboxLocation;
        init();
    }

    /**
     * Method: init
     * <p>
     *     Description: On creation of a ChannelServices object, this method checks for new subscribers, loads subscribers, and then empties the subscriber inbox.
     * </p>
     */
    private void init() {
        boolean checkNewSubscriberBox = checkForNewSubscribers();

        if (checkNewSubscriberBox) {
            loadSubscribers();
            emptyInboxLocation(subscriberInboxLocation);
        }

        loadSubscribers();
    }

    /**
     * Method: checkForNewSubscribers
     * <P>
     *     Description: This method checks the .dat file that contains new subscribers for the channel and transfers
     *      them to the persisted list .dat file for the channel to store.
     * </P>
     */
    private boolean checkForNewSubscribers() {
        List<Subscriber> tempList = new ArrayList<>();

        File file = new File(subscriberInboxLocation);

        if (file.length() != 0) {
            // Get the new subscribers and place them into a temporary list to be written into
            // the .dat file for the persisted list of subscribers.
            try {
                ObjectInputStream objectInputStream =
                        new ObjectInputStream(new FileInputStream(subscriberInboxLocation));

                boolean flag = true;
                while (flag) {
                    try {
                        Subscriber subscriber = (Subscriber) objectInputStream.readObject();
                        tempList.add(subscriber);
                    }
                    catch (EOFException exception) {
                        flag = false;
                    }
                }

                objectInputStream.close();
            }
            catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // Write the new subscribers to the subscribers persisted list .dat file.
            try {
                // This code was added from stack overflow
                FileOutputStream fileOutputStream = new FileOutputStream(subscribersPersistedList, false);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

                for (Subscriber subscriber : tempList) {
                    objectOutputStream.writeObject(subscriber);
                }
                objectOutputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    /**
     * Method: emptyInboxLocation
     * <p>
     *     Description: This method empties removes a files contents.
     * </p>
     */
    private void emptyInboxLocation(String location) {
        try {
            new FileWriter(location, false).close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method: loadSubscribers
     * <p>
     *    Description: Reads in the {@code subscriber.java} objects that are saved in the subscriber's inbox .dat file.
     * </p>
     */
    private void loadSubscribers() {
        File file = new File(subscribersPersistedList);

        if (file.length() != 0) {
            try {
                FileInputStream fileInputStream = new FileInputStream(subscribersPersistedList);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

                boolean flag = true;
                while (flag) {
                    try {
                        Subscriber subscriber = (Subscriber) objectInputStream.readObject();
                        channel.subscribe(subscriber);
                    }
                    catch (EOFException exception) {
                        flag = false;
                    }
                }

                // Read in objects from .dat file and add them to the subscriber list.
                objectInputStream.close();
            }
            catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private boolean checkForUnSubscriptions() {
        File file = new File(unSubscribeInboxLocation);

        return file.length() > 0;
    }

    private List<Subscriber> getListOfUnSubscribers() {
        System.out.println("getListOfUnSubscribers called");
        List<Subscriber> tempUnsubscribeList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(unSubscribeInboxLocation);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

            boolean flag = true;
            while (flag) {
                try {
                    Subscriber subscriber = (Subscriber) objectInputStream.readObject();
                    tempUnsubscribeList.add(subscriber);
                }
                catch (EOFException exception) {
                    flag = false;
                }
                catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            // Read in objects from .dat file and add them to the subscriber list.
            objectInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempUnsubscribeList;
    }

    private void deleteChannelSubscribersDatFile() {
        File file = new File(subscribersPersistedList);

        if (file.delete()) {
            System.out.println("File deleted");
        }
        else {
            System.out.println("dat file not deleted.");
        }
    }

    private void rewriteChannelSubscribersDatFile() {
        if (!channel.getSubs().isEmpty()) {
            try {
                File file = new File(subscribersPersistedList);

                if (file.createNewFile()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(subscribersPersistedList);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

                    for (Subscriber sub : channel.getSubs()) {
                        objectOutputStream.writeObject(sub);
                    }
                    objectOutputStream.close();
                }
                else {
                    System.out.println("File could not be created.");
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            File file = new File(subscribersPersistedList);
            try {
                if (file.createNewFile()) {
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }

    /**
     * Method: run
     * <p>
     *     Description: This method is checking to see if there are new subscribers. If there are new subscribers
     *     the program adds the subscriber's to the subscribers list location, the List object is updated, and
     *     the new subscriber inbox is emptied.
     * </p>
     */
    @Override
    public void run() {
        while (!shutdown) {
            // Ping server every 5 seconds to allow files to be written.
            try {
                Thread.sleep(5000);

                boolean checkNewSubscriberBox = checkForNewSubscribers();
                boolean checkForUnSubscriptions = checkForUnSubscriptions();

                if (checkNewSubscriberBox) {
                    loadSubscribers();
                    emptyInboxLocation(subscriberInboxLocation);
                }

                if (checkForUnSubscriptions) {
                    List<Subscriber> tempUnsubscribeList = getListOfUnSubscribers();
                    channel.updateSubsList(tempUnsubscribeList);

                    deleteChannelSubscribersDatFile();
                    rewriteChannelSubscribersDatFile();
                    emptyInboxLocation(unSubscribeInboxLocation);
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
