package com.example.observationpattern.pattern.channel;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChannelClient extends Application {

    private Button btnUpload;

    private Channel codingClass101;

    private TextField tf;

    private TextArea ta;

    private ChannelServices channelServices;

    private final String SUBSCRIBER_INBOX_LOCATION =
            "src/main/java/com/example/observationpattern/pattern/files/CodingClass101SubscriberBox.dat";

    private final String SUBSCRIBERS_PERSISTED_LIST =
            "src/main/java/com/example/observationpattern/pattern/files/CodingClass101Subscribers.dat";

    private final String UN_SUBSCRIBERS_INBOX_LOCATION =
            "src/main/java/com/example/observationpattern/pattern/files/CodingClass101UnsubscribeBox.dat";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane paneForTextField = new BorderPane();
        VBox vBoxForButtons = new VBox();
        vBoxForButtons.setPadding(new Insets(5, 5, 5, 5));

        btnUpload = new Button("Upload");
        addUploadListener();

        btnUpload.setMaxWidth(Double.MAX_VALUE);

        vBoxForButtons.getChildren().addAll(btnUpload);

        paneForTextField.setLeft(vBoxForButtons);

        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: red");

        tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);

        BorderPane mainPane = new BorderPane();
        ta = new TextArea();
        ta.setEditable(false);
        ta.setPrefWidth(500);

        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);

        Scene scene = new Scene(mainPane, 500, 200);
        primaryStage.setTitle("Coding Class 101");
        primaryStage.setScene(scene);
        primaryStage.show();
        initializeChannel();
        initializeChannelServices();

        primaryStage.setOnCloseRequest(windowEvent -> channelServices.shutdown());
    }

    public void initializeChannel() {
        codingClass101 = new Channel("Coding Class 101");
    }

    public void initializeChannelServices() {
        channelServices = new ChannelServices(codingClass101, SUBSCRIBER_INBOX_LOCATION,
                SUBSCRIBERS_PERSISTED_LIST, UN_SUBSCRIBERS_INBOX_LOCATION);

        Thread thread = new Thread(channelServices);
        thread.start();
    }

    private void addUploadListener() {
        btnUpload.setOnAction(actionEvent -> {
            String text = tf.getText().trim(); // this must be coding class 101

            if (text.length() != 0) {
                System.out.println(codingClass101.subs.size());
                if (codingClass101.upload(text)) {
                    ta.appendText("Uploaded new video: " + text + "\n");
                }
                else {
                    ta.appendText("Unable to upload new video: " + text + "\n");
                }
            }
            tf.setText("");
        });
    }
}
