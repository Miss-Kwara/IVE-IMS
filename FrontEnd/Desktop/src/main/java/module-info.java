module com.rbnk.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.net.http;
    requires com.google.gson;

    opens com.rbnk.desktop to javafx.fxml;
    exports com.rbnk.desktop;
}