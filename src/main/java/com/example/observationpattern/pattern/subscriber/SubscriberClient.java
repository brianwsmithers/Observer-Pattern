package com.example.observationpattern.pattern.subscriber;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Date;

public class SubscriberClient extends Application {

    private TextArea ta;

    private Subscriber s1;

    private Button btnSubscribe;

    private TextField tf;

    private Button btnUnsubscribe;

    private UpdateFeed updateFeed;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane paneForTextField = new BorderPane();
        VBox vBoxForButtons = new VBox();
        vBoxForButtons.setPadding(new Insets(5, 5, 5, 5));

        btnSubscribe = new Button("Subscribe");
        addSubscribeListener();

        btnUnsubscribe = new Button("Unsubscribe");
        addUnsubscribeListener();

        btnSubscribe.setMaxWidth(Double.MAX_VALUE);
        btnUnsubscribe.setMaxWidth(Double.MAX_VALUE);

        vBoxForButtons.getChildren().addAll(btnSubscribe, btnUnsubscribe);

        paneForTextField.setLeft(vBoxForButtons);

        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");

        tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);

        BorderPane mainPane = new BorderPane();
        ta = new TextArea();
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.setPrefWidth(500);

        ta.textProperty().addListener((observableValue, s, t1) -> {
            Platform.runLater(() -> {
                ta.setScrollTop(Double.MAX_VALUE);
            });
        });

        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);

        Scene scene = new Scene(mainPane, 500, 200);
        primaryStage.setTitle("Client 1");
        primaryStage.setScene(scene);
        primaryStage.show();

        initializeClient();

        updateFeed = new UpdateFeed(ta, s1);

        updateFeed();

        primaryStage.setOnCloseRequest(windowEvent -> {
            s1.shutdown();
            updateFeed.shutdown();
        });
    }

    public void initializeClient() {
        s1 = new Subscriber("Brian", "Subscriber1NotificationBox",
                "Subscriber1Feed");

        Thread thread = new Thread(s1);
        thread.start();
    }

    private void addSubscribeListener() {
        btnSubscribe.setOnAction(actionEvent -> {
            String text = tf.getText().trim(); // this must be coding class 101

            if (text.length() != 0) {
                if (s1.subscribeChannel(text)) {
                    s1.updateFeedForSubscription("Subscribed to " + text + " " + new Date() + "\n");
                }
                else {
                    s1.updateFeedForSubscription("Unable to subscribe to " + text + "\n");
                }
                tf.setText("");
            }
        });
    }

    public void addUnsubscribeListener() {
        btnUnsubscribe.setOnAction(actionEvent -> {
            String text = tf.getText().trim(); // this must be coding class 101

            if (text.length() != 0) {
                if (s1.unSubscribeChannel(text)) {
                    s1.updateFeedForSubscription("Unsubscribed from " + text + " " + new Date() + "\n");
                }
                else {
                    s1.updateFeedForSubscription("Unable to unsubscribe from " + text + "\n");
                }
                tf.setText("");
            }
        });
    }

    private void updateFeed() {
        Thread thread = new Thread(updateFeed);
        thread.start();
    }
}
