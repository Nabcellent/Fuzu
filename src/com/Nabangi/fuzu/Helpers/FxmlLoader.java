package com.Nabangi.fuzu.Helpers;

import com.Nabangi.fuzu.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.net.URL;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class FxmlLoader {
    private Pane view;

    public Pane getPage(String fileName) {
        try {
            URL fileUrl = Main.class.getResource("Fxml/" + fileName + ".fxml");

            if(fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML file can't be found");
            }

            new FXMLLoader();
            view = FXMLLoader.load(fileUrl);
        } catch(Exception e) {
            System.out.println("No page " + fileName + " Please check Loader.");
        }

        return view;
    }
}