package com.Nabangi.fuzu;

import com.Nabangi.fuzu.Helpers.Frames;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Frames.loginFrame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}