package com.Nabangi.fuzu.Controllers;

import com.Nabangi.fuzu.Helpers.Frames;
import com.Nabangi.fuzu.Helpers.FxmlLoader;
import com.Nabangi.fuzu.Helpers.Helpers;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class IndexController extends LoginController implements Initializable {
    @FXML private VBox navButtons, navIcons, navIcons1;
    @FXML private Label indexTitle;
    @FXML private ImageView imgMenuBtn;
    @FXML private ImageView imgHome, imgStudents, imgProfile, imgLogout, imgReport;
    @FXML private ImageView imgHome1, imgStudents1, imgProfile1, imgLogout1, imgReport1;
    @FXML private JFXButton btnLogout, btnStudents, btnReport;
    @FXML private BorderPane mainPane;
    @FXML private AnchorPane navList, navOverlay;
    @FXML private FontAwesomeIconView imgExitBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navOverlay.setVisible(false);
        navOverlay.setOnMouseClicked(event -> closeNav());
        imgExitBtn.setOnMouseClicked(event -> System.exit(0));

        if(sessionUserType != null) {
            indexTitle.setText(sessionUserType);

            if(sessionUserType.equals("Parent")) {
                btnStudents.setText("My Students");
                btnReport.setText("Results");
            } else if(sessionUserType.equals("Student")) {
                btnReport.setText("View Marks");
                navIcons.getChildren().remove(imgStudents);
                navIcons1.getChildren().remove(imgStudents1);
                navButtons.getChildren().remove(btnStudents);
            }
        }

        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("home");
        mainPane.setCenter(view);

        setImages();
        navTransition();
        navIconOnClick();
    }


    @FXML private void btnHomeOnClick() {
        showPane("home");
    }
    @FXML private void btnStudentsOnClick() {
        showPane("studentCrud");
    }

    @FXML private void btnReportOnClick() { showPane("reportCrud"); }
    @FXML private void btnProfileOnClick() {
        showPane("profile");
    }
    @FXML private void btnLogoutOnClick() {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();
        Frames.loginFrame();
    }



    private void navTransition() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(.5), navOverlay);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(.5), navList);
        translateTransition.setByX(-600);
        translateTransition.play();

        imgMenuBtn.setOnMouseClicked(event -> {
            navOverlay.setVisible(true);

            if(navList.getBoundsInParent().getMinX() == -560.0) {
                FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(.5), navOverlay);
                fadeTransition1.setFromValue(0);
                fadeTransition1.setToValue(0.15);
                fadeTransition1.play();
                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(.5), navList);
                translateTransition1.setByX(+600);
                translateTransition1.play();
            } else if(navList.getBoundsInParent().getMinX() == 40) {
                closeNav();
            }
        });
    }

    private void showPane(String page) {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage(page);
        mainPane.setCenter(view);

        if(navList.getBoundsInParent().getMinX() == 40) {
            closeNav();
        }
    }

    private void closeNav() {
        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(.5), navOverlay);
        fadeTransition1.setFromValue(.15);
        fadeTransition1.setToValue(0);
        fadeTransition1.play();

        fadeTransition1.setOnFinished(event1 -> navOverlay.setVisible(false));

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(.5), navList);
        translateTransition1.setByX(-600);
        translateTransition1.play();
    }

    public void navIconOnClick() {
        imgHome1.setOnMouseClicked(event -> showPane("home"));
        imgStudents1.setOnMouseClicked(event -> showPane("studentCrud"));
        imgReport1.setOnMouseClicked(event -> showPane("reportCrud"));
        imgProfile1.setOnMouseClicked(event -> showPane("profile"));
        imgLogout1.setOnMouseClicked(event -> {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.close();
            Frames.loginFrame();
        });
    }

    public void setImages() {
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-menu-48.png", imgMenuBtn);

        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-home-48.png", imgHome);
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-students-48.png", imgStudents);
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-combo-chart.gif", imgReport);
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-life-cycle.gif", imgProfile);
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-power-off-button.gif", imgLogout);

        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-home-48.png", imgHome1);
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-students-48.png", imgStudents1);
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-combo-chart.gif", imgReport1);
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-life-cycle.gif", imgProfile1);
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/Nav/icons8-power-off-button.gif", imgLogout1);
    }
}