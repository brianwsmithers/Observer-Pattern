module com.example.observationpattern {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;

    exports com.example.observationpattern.pattern.channel;
    opens com.example.observationpattern.pattern.channel to javafx.fxml;

    exports com.example.observationpattern.pattern.subscriber;
    opens com.example.observationpattern.pattern.subscriber to javafx.fxml;

    exports com.example.observationpattern.pattern.interfaces;
    opens com.example.observationpattern.pattern.interfaces to javafx.fxml;
}