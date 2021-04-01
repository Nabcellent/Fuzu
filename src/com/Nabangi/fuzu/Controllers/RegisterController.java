package com.Nabangi.fuzu.Controllers;

import com.Nabangi.fuzu.Database.DatabaseLink;
import com.Nabangi.fuzu.Helpers.Frames;
import com.Nabangi.fuzu.Helpers.Helpers;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.validation.*;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class RegisterController implements Initializable {
    @FXML private Label lblErrorMessage;
    @FXML private TextField txtLastName, txtFirstName, txtEmail, txtPhone;
    @FXML private PasswordField txtPassword, txtPasswordConfirm;
    @FXML private JFXRadioButton radioAdmin, radioParent;
    @FXML private Button btnClose, btnRegister;
    @FXML private ImageView registerImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File registerFile = new File("src/com/Nabangi/fuzu/Public/Images/icons8-authentication-48.png");
        Image registerImage = new Image(registerFile.toURI().toString());
        registerImageView.setImage(registerImage);

        controlsFxValidation();
    }



    //=================================================    Action Handlers
    @FXML
    private void btnRegisterOnAction() throws SQLException {
        if(isValid()) {
            lblErrorMessage.setText("");
            registerUser();
        } else {
            Helpers.setMessageTimer(7, lblErrorMessage);
        }
    }

    @FXML
    private void closeButtonOnAction() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    @FXML
    private void linkLoginOnAction() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
        Frames.loginFrame();
    }


    @FXML
    private boolean isValid() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtFirstName.getText().trim();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String passwordConfirm = txtPasswordConfirm.getText();

        if(!radioAdmin.isSelected() && !radioParent.isSelected()) {
            lblErrorMessage.setText("Please choose a user type.");
            return false;
        }

        if(firstName.isEmpty() || lastName.isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || passwordConfirm.trim().isEmpty()) {
            lblErrorMessage.setText("Please fill in all required fields!");
            return false;
        }

        if(!Helpers.isEmail(email)) {
            lblErrorMessage.setText("Invalid email!");
            return false;
        }

        if(Helpers.emailExists(email)) {
            lblErrorMessage.setText("Email already in use.");
            return false;
        }

        if (Helpers.validatePhone(0, phone, lblErrorMessage)) return false;

        if(!txtPasswordConfirm.getText().equals(txtPassword.getText())) {
            lblErrorMessage.setText("Passwords do not match");
            return false;
        }

        return true;
    }


    //=================================================    Form Action
    @FXML
    private void registerUser() throws SQLException {
        Connection link = DatabaseLink.connectDb();

        String currentUserType = (radioAdmin.isSelected()) ? radioAdmin.getText() : radioParent.getText();

        String SQL_INSERT;
        if(txtPhone.getText().trim().isEmpty()) {
            SQL_INSERT = "INSERT INTO tbl_users (first_name, last_name, email, user_type, password) " +
                    "VALUES (?,?,?,?,?)";
        } else {
            SQL_INSERT = "INSERT INTO tbl_users (first_name, last_name, phone, email, user_type, password) " +
                    "VALUES (?,?,?,?,?,?)";
        }

        PreparedStatement prepStatement = link.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS);

        if(txtPhone.getText().trim().isEmpty()) {
            prepStatement.setString(1, txtFirstName.getText());
            prepStatement.setString(2, txtLastName.getText());
            prepStatement.setString(3, txtEmail.getText());
            prepStatement.setString(4, currentUserType);
            prepStatement.setString(5, txtPassword.getText());
        } else {
            prepStatement.setString(1, txtFirstName.getText());
            prepStatement.setString(2, txtLastName.getText());
            prepStatement.setInt(3, Integer.parseInt(txtPhone.getText()));
            prepStatement.setString(4, txtEmail.getText());
            prepStatement.setString(5, currentUserType);
            prepStatement.setString(6, txtPassword.getText());
        }

        try {
            //==    Get affected rows
            if(prepStatement.executeUpdate() == 1) {
                FXMLLoader loader = new FXMLLoader ();
                loader.setLocation(getClass().getResource("../Fxml/login.fxml"));

                try {
                    loader.load();
                } catch (IOException ex) {
                    Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                }

                LoginController loginController = loader.getController();
                loginController.setLblLoginMessage("Registration successful! You may log in.");

                Stage registerStage = (Stage) btnRegister.getScene().getWindow();
                registerStage.close();

                Parent parent = loader.getRoot();
                Stage loginStage = new Stage();
                loginStage.setScene(new Scene(parent));
                loginStage.initStyle(StageStyle.UNDECORATED);
                loginStage.show();
            }
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.getCause().printStackTrace();
        }
    }



    private void controlsFxValidation() {
        ValidationSupport validation = new ValidationSupport();

        validation.registerValidator(txtFirstName,
                Validator.createEmptyValidator("First name is required!", Severity.ERROR));
        validation.registerValidator(txtLastName,
                Validator.createEmptyValidator("Last name is required!", Severity.ERROR));
        validation.registerValidator(txtEmail,
                Validator.createEmptyValidator("Email name is required!", Severity.ERROR));
        validation.registerValidator(txtPassword,
                Validator.createEmptyValidator("Password name is required!", Severity.ERROR));
        validation.registerValidator(txtPasswordConfirm,
                Validator.createEmptyValidator("Password Confirmation name is required!", Severity.ERROR));
    }
}
