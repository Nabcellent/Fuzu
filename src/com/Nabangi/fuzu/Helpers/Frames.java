package com.Nabangi.fuzu.Helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class Frames {
    //==========================================    Login Frame
    public Frames(){}

    public static void loginFrame() {
        try {
            Parent root = FXMLLoader.load(Frames.class.getResource("../Fxml/login.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 620, 500));
            registerStage.show();
        } catch(Exception e) {
            e.getCause();
            e.printStackTrace();
        }
    }


    //==========================================    Register Frame
    public static void registerForm() {
        try {
            Parent root = FXMLLoader.load(Frames.class.getResource("../Fxml/register.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 620, 500));
            registerStage.show();
        } catch(Exception e) {
            e.getCause();
            e.printStackTrace();
        }
    }



    //==========================================    Index Frame
    public static void indexFrame() {
        try {
            Parent root = FXMLLoader.load(Frames.class.getResource("../Fxml/index.fxml"));
            Stage indexStage = new Stage();
            indexStage.initStyle(StageStyle.UNDECORATED);
            indexStage.setScene(new Scene(root, 1000, 600));
            indexStage.show();
        } catch(Exception e) {
            e.getCause();
            e.printStackTrace();
        }
    }
}
