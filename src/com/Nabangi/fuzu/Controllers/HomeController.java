package com.Nabangi.fuzu.Controllers;

import com.Nabangi.fuzu.Helpers.Helpers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class HomeController extends LoginController implements Initializable {
    @FXML private ImageView homePaneImg;
    @FXML private Label lblGreeting;

    LocalTime currentTime = LocalTime.now();
    LocalTime morningStart = LocalTime.of(0, 1);
    LocalTime afternoonStart = LocalTime.of(12, 1);
    LocalTime eveningStart = LocalTime.of(18, 1);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/blackboard1.jpg", homePaneImg);

        lblGreeting.setText(getWelcomeMessage());
    }

    private String getWelcomeMessage() {
        StringBuilder message = new StringBuilder();

        message.append("Good ").append(timePeriod()).append(" ");

        if(sessionGender == null) {
            message.append("!");
        } else {
            message.append(salutation());
            message.append(capitalize(sessionLastName));
        }

        return String.valueOf(message);
    }

    private String salutation() {
        String salute = "";
        if(!sessionUserType.equals("Student")) {
            if(sessionGender.equals("Female")) {
                salute = "Mrs. ";
            } else if(sessionGender.equals("Male")) {
                salute = "Mr. ";
            }
        }

        return salute;
    }

    private String timePeriod() {
        if(currentTime.isAfter(morningStart) && currentTime.isBefore(afternoonStart)) {
            return "Morning";
        } else if(currentTime.isAfter(afternoonStart) && currentTime.isBefore(eveningStart)) {
            return "Afternoon";
        }

        return "Evening";
    }

    private String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
