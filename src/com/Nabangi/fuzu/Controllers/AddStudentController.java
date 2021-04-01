package com.Nabangi.fuzu.Controllers;

import com.Nabangi.fuzu.Database.DatabaseLink;
import com.Nabangi.fuzu.Helpers.Helpers;
import com.Nabangi.fuzu.Models.Student;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class AddStudentController extends LoginController implements Initializable {
    @FXML private Label lblAddStudentMessage, lblErrorMessage;
    @FXML private JFXButton btnAddStudent;
    @FXML private JFXTextField txtFirstName, txtLastName;
    @FXML private JFXRadioButton radioMale, radioFemale;
    @FXML private JFXComboBox<String> cmbClass;
    @FXML private JFXDatePicker dateDOB;

    private String selectedGender;
    private int studentUserId;
    private boolean update;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i = 1; i < 9; i ++) {
            cmbClass.getItems().add("Class " + i);
        }
    }

    public void btnAddStudentOnClick() {
        if(isValid()) {
            lblErrorMessage.setText("");
            if(update) {
                updateStudent();
            } else {
                registerStudent();
            }
        } else {
            Helpers.setMessageTimer(7, lblErrorMessage);
        }
    }



    public boolean isValid() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtFirstName.getText().trim();

        if(sessionId == 0) {
            lblErrorMessage.setText("You must be Signed In to add a student.");
            return false;
        }

        if(firstName.isEmpty() || lastName.isEmpty()) {
            lblErrorMessage.setText("Please fill in all required fields!");
            return false;
        }

        if(!radioMale.isSelected() && !radioFemale.isSelected()) {
            lblErrorMessage.setText("Please choose a gender.");
            return false;
        }

        this.selectedGender = (radioMale.isSelected()) ? radioMale.getText() : radioFemale.getText();

        return true;
    }


    public void registerStudent() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String gender = this.selectedGender;
        Date dob = Date.valueOf(dateDOB.getValue());

        Student student = new Student(sessionId, firstName, lastName, gender, dob, "FUZU-7");

        if(student.createStudent() == 1) {
            lblAddStudentMessage.setText("New Student added Successfully!");
            Helpers.setMessageTimer(4, lblAddStudentMessage);
            clearForm();
        } else {
            lblErrorMessage.setText("Error! Unable to add new student.");
        }
    }


    private void updateStudent() {
        Connection link = DatabaseLink.connectDb();
        Date dateOfBirth = Date.valueOf(dateDOB.getValue());
        final String SQL_UPDATE = "UPDATE tbl_users SET first_name = ?, last_name = ?, gender = ? WHERE id = " + studentUserId;
        final String SQL_UPDATE1 = "UPDATE tbl_students SET dob = '" + dateOfBirth + "' WHERE user_id = " + studentUserId;

        try {
            assert link != null;
            PreparedStatement prepStatement = link.prepareStatement(SQL_UPDATE,
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement prepStatement1 = link.prepareStatement(SQL_UPDATE1,
                    Statement.RETURN_GENERATED_KEYS);

            prepStatement.setString(1, txtFirstName.getText());
            prepStatement.setString(2, txtLastName.getText());
            prepStatement.setString(3, this.selectedGender);

            int affectedRows;
            affectedRows = prepStatement.executeUpdate();

            if(affectedRows == 1) {
                //Insert Data Into Students Table
                try {
                    affectedRows = prepStatement1.executeUpdate();

                    if(affectedRows == 1) {
                        lblAddStudentMessage.setText("Student Updated Successfully!");
                        Helpers.setMessageTimer(4, lblAddStudentMessage);
                    } else {
                        lblErrorMessage.setText("Error! Unable to update Student.");
                        Helpers.setMessageTimer(5, lblErrorMessage);
                    }
                } catch(SQLException se) {
                    se.printStackTrace();
                } catch(Exception e) {
                    e.getCause().printStackTrace();
                }
            }
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.getCause().printStackTrace();
        }
    }


    private void clearForm() {
        txtFirstName.setText("");
        txtLastName.setText("");
        cmbClass.setValue("");
        dateDOB.setValue(null);
        radioMale.setSelected(false);
        radioFemale.setSelected(false);
    }

    public void setUpdate(boolean b) {
        this.update = b;
        btnAddStudent.setText("Update Student");
    }

    public void setTextField(int id, String firstName, String lastName, LocalDate DOB, String gender) {
        studentUserId = id;
        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);
        dateDOB.setValue(DOB);
        radioMale.setSelected(gender.equals("Male"));
        radioFemale.setSelected(gender.equals("Female"));
    }
}
