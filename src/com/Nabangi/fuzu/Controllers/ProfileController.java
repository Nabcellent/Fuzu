package com.Nabangi.fuzu.Controllers;

import com.Nabangi.fuzu.Database.DatabaseLink;
import com.Nabangi.fuzu.Helpers.Helpers;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/

public class ProfileController extends LoginController implements Initializable {
    @FXML private Label lblTitle, lblProfileMessage, lblProfileError, lblEmail, lblPhone;
    @FXML private JFXTextField txtFirstName, txtGender, txtLastName, txtPhone, txtProfileEmail;
    @FXML private JFXPasswordField txtNewPassword, txtCurrentPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtFirstName.setText(sessionFirstName);
        txtLastName.setText(sessionLastName);
        txtGender.setText(sessionGender);

        if(sessionUserType.equals("Admin")) {
            lblTitle.setText("ADMIN PROFILE");

            txtProfileEmail.setText(sessionEmail);
            txtPhone.setText(sessionPhone);
        } else if(sessionUserType.equalsIgnoreCase("Parent")) {
            lblTitle.setText("PARENT PROFILE");

            txtProfileEmail.setText(sessionEmail);
            txtPhone.setText(sessionPhone);
        } else if(sessionUserType.equalsIgnoreCase("Student")) {
            txtPhone.setDisable(true);

            lblTitle.setText("STUDENT PROFILE");
            lblEmail.setText("Admission Number");
            lblPhone.setText("Date of Birth");

            txtProfileEmail.setText(String.valueOf(sessionAdmNo));
            txtPhone.setText(String.valueOf(sessionDOB));
        }

        if(!(sessionGender == null)) {
            txtGender.setDisable(true);
        }
    }

    public void btnUpdateProfileOnClick() {
        if(isValidProfile()) {
            updateProfile();
        } else {
            Helpers.setMessageTimer(7, lblProfileError);
        }
    }

    public void btnChangePasswordOnClick() {
        if(isValidPassword()) {
            changePassword();
        } else {
            Helpers.setMessageTimer(7, lblProfileError);
        }
    }

    private boolean isValidProfile() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtFirstName.getText().trim();
        String phone = txtPhone.getText();
        String gender = txtGender.getText();

        lblProfileMessage.setText("");
        if(firstName.isEmpty() || lastName.isEmpty()) {
            lblProfileError.setText("Please fill in all required fields!");
            return false;
        }

        if(!sessionUserType.equals("Student")) {
            if (Helpers.validatePhone(sessionId, phone, lblProfileError)) return false;
        }

        if(!txtGender.isDisable()) {
            if(!(gender == null)) {
                if(!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")) {
                    lblProfileError.setText("Gender must be 'Male' or 'Female'");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isValidPassword() {
        String currentPassword = txtCurrentPassword.getText();
        String newPassword = txtNewPassword.getText();

        lblProfileMessage.setText("");
        if(currentPassword.isEmpty() || newPassword.isEmpty()) {
            lblProfileError.setText("Both password fields must be filled!");
            return false;
        }

        if(!currentPassword.equals(sessionPassword)) {
            lblProfileError.setText("Current password is incorrect");
            return false;
        }

        return true;
    }

    private void updateProfile() {
        Connection link = DatabaseLink.connectDb();

        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String gender = txtGender.getText();
        String phoneNo = txtPhone.getText();

        final String SQL_INSERT;
        if(sessionUserType.equals("Student")) {

            SQL_INSERT = "UPDATE tbl_users SET " +
                    "first_name = '" + firstName + "', " +
                    "last_name = '" + lastName + "' " +
                    "WHERE id = " + sessionId;
        } else {
            if(phoneNo == null) {
                SQL_INSERT = "UPDATE tbl_users SET " +
                        "first_name = '" + firstName + "', " +
                        "last_name = '" + lastName + "', " +
                        "gender = '" + gender + "' WHERE id = " + sessionId;
            } else {
                int phone = Integer.parseInt(phoneNo);
                SQL_INSERT = "UPDATE tbl_users SET " +
                        "first_name = '" + firstName + "', " +
                        "last_name = '" + lastName + "', " +
                        "gender = '" + gender + "', " +
                        "phone = " + phone + " WHERE id = " + sessionId;
            }
        }

        try {
            Statement statement = link.createStatement();
            int result = statement.executeUpdate(SQL_INSERT);

            if(result == 1) {
                lblProfileMessage.setText("Profile updated");
                lblProfileError.setText("");
                Helpers.setMessageTimer(4, lblProfileMessage);
            } else {
                lblProfileError.setText("Unable to update Profile");
                Helpers.setMessageTimer(7, lblProfileError);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void changePassword() {
        Connection link = DatabaseLink.connectDb();

        final String SQL_INSERT = "UPDATE tbl_users SET password = ? WHERE id = " + sessionId;

        try {
            PreparedStatement prepStatement = link.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            prepStatement.setString(1, txtNewPassword.getText());

            if(prepStatement.executeUpdate() == 1) {
                lblProfileMessage.setText("Password updated.");
                txtCurrentPassword.setText("");
                txtNewPassword.setText("");
                lblProfileError.setText("");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
