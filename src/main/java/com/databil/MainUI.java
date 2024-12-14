package com.databil;

import com.databil.ui.MainControlPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class MainUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new MainControlPane(), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
