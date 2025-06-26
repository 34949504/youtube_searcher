module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    requires com.github.kwhat.jnativehook;


    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}