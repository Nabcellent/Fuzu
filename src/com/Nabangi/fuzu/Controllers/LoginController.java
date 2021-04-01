package com.Nabangi.fuzu.Controllers;

import javafx.fxml.FXML;
import com.Nabangi.fuzu.Database.DatabaseLink;
import com.Nabangi.fuzu.Helpers.Frames;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.Nabangi.fuzu.Helpers.*;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class LoginController implements Initializable {
    @FXML private Label lblErrorMessage, lblLoginMessage;
    @FXML private Button btnLogin, btnCancel;
    @FXML private ImageView brandingImageView, lockImageView;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    static int sessionId, sessionAdmNo;
    static String sessionFirstName, sessionLastName, sessionGender, sessionEmail, sessionPhone, sessionUserType, sessionPassword;
    static Date sessionDOB;


    //========================================    Initialize Frame
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/160-1606471_logo-java.png", brandingImageView);
        Helpers.loadImage("src/com/Nabangi/fuzu/Public/Images/e56b841924ac729935e858cb59535fb7.png", lockImageView);

        this.controlsFxValidation();
    }



    //========================================    Event Handlers
    @FXML
    private void btnLoginOnAction() {
        if(isValidForm()) {
            authenticateLogin();
        } else {
            Helpers.setMessageTimer(7, lblErrorMessage);
        }
    }

    @FXML
    private void btnCancelOnAction() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void linkRegisterOnAction() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
        Frames.registerForm();
    }



    //========================================    Form Actions
    private boolean isValidForm() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if(email.isEmpty() || password.isEmpty()) {
            lblErrorMessage.setText("Please fill in all fields!");
            return false;
        }

        return true;
    }

    private void authenticateLogin() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        BigInteger phoneAdm = new BigInteger(email, 35);

        Connection link = DatabaseLink.connectDb();

        if(Helpers.isNumeric(email)) {
            phoneAdm = BigInteger.valueOf(Long.parseLong(email));
        }

        String verifyLogin = "SELECT * FROM tbl_users " +
                "LEFT JOIN tbl_students ON tbl_users.id = tbl_students.user_id " +
                "WHERE (email = '" + email + "' OR phone = " + phoneAdm + " OR adm_no = " + phoneAdm + ") " +
                "AND password = '" + password + "' LIMIT 1";

        try{
            Statement statement = link.createStatement();
            ResultSet qryResult = statement.executeQuery(verifyLogin);

            if(qryResult.next()) {
                sessionId = qryResult.getInt("id");
                sessionFirstName = qryResult.getString("first_name");
                sessionLastName = qryResult.getString("last_name");
                sessionGender = qryResult.getString("gender");
                sessionPhone = qryResult.getString("phone");
                sessionEmail = qryResult.getString("email");
                sessionPassword = qryResult.getString("password");
                sessionUserType = qryResult.getString("user_type");

                sessionAdmNo = qryResult.getInt("adm_no");
                sessionDOB = qryResult.getDate("dob");

                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.close();

                Frames.indexFrame();
            } else {
                lblErrorMessage.setText("Invalid Credentials");
            }
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
        }
    }



    private void controlsFxValidation() {
        ValidationSupport validation = new ValidationSupport();

        validation.registerValidator(txtEmail,
                Validator.createEmptyValidator("Email is required!", Severity.ERROR));
        validation.registerValidator(txtPassword,
                Validator.createEmptyValidator("Password is required!", Severity.ERROR));
    }


    /*  SETTER  */
    public void setLblLoginMessage(String message) {
        lblLoginMessage.setText(message);
        Helpers.setMessageTimer(4, lblLoginMessage);
    }
}