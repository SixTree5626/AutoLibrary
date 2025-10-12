module com.caden.autolibrary.autolibrary {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.graphics;
    requires org.jsoup;
    requires java.logging;


    opens com.caden.autolibrary.autolibrary to javafx.fxml, com.google.gson, org.jsoup;
    exports com.caden.autolibrary.autolibrary;
}