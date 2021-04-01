package com.Nabangi.fuzu.Helpers;

import com.Nabangi.fuzu.Controllers.*;
import com.Nabangi.fuzu.Database.DatabaseLink;
import com.Nabangi.fuzu.Models.*;
import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.File;
import java.sql.*;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class Helpers {
    //++++++++++    VARIABLES
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_PATTERN = "((^0[17]+)|(^[17]+)).*";



    /**
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    METHODS
     * */
    public static void setMessageTimer(int durationInSeconds, Label label) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(durationInSeconds), ev -> label.setText("")));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static boolean isEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isPhoneNumber(String phone) {
        return phone.matches(PHONE_PATTERN);
    }

    public static void loadImage(String path, ImageView imageView) {
        File imageFile = new File(path);
        Image image = new Image(imageFile.toURI().toString());
        imageView.setImage(image);
    }

    public static boolean isInRange(double i) {
        return !(i >= 0) || !(i <= 100);
    }

    public static boolean emailExists(String email) {
        String query;
        Connection connection = DatabaseLink.connectDb();
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        query = "SELECT email FROM tbl_users where email = '" + email + "' LIMIT 1";
        try {
            assert false;
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public static boolean phoneExists(int phone, int id) {
        Connection connection = DatabaseLink.connectDb();
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        String query;

        if(id == 0) {
            query = "SELECT phone FROM tbl_users where phone = '" + phone + "' LIMIT 1";
        } else {
            query = "SELECT id, phone FROM tbl_users where phone = '" + phone + "' AND id != " + id + " LIMIT 1";
        }

        try {
            assert false;
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }


    public static boolean validatePhone(int userId, String phone, Label lblProfileError) {
        if(phone != null && !phone.trim().isEmpty()) {
            if(!isNumeric(phone)) {
                lblProfileError.setText("Phone number has to be an integer!");
                return true;
            }

            if(!isPhoneNumber(phone)) {
                lblProfileError.setText("Invalid phone number");
                return true;
            }

            if(phone.length() < 9) {
                lblProfileError.setText("Phone must be at least 9 digits");
                return true;
            } else if(phone.length() > 10) {
                lblProfileError.setText("Phone must be at most 10 digits");
                return true;
            }

            if(phoneExists(Integer.parseInt(phone), userId)) {
                lblProfileError.setText("Phone number already in use.");
                return true;
            }
        }
        return false;
    }


    public static void loadTableData(TableColumn<Student, String> colId, TableColumn<Student, String> colFname, TableColumn<Student, String> colLname, TableColumn<Student, String> colGender, TableColumn<Student, String> colDob) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("DOB"));
    }

    public static void getAddStudentSceneController(FXMLLoader loader, Student student) {
        AddStudentController addStudentController = loader.getController();
        addStudentController.setUpdate(true);
        addStudentController.setTextField(student.getId(), student.getFirstName(), student.getLastName(),
                student.getDOB().toLocalDate(), student.getGender());
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    public static void getAddMarksSceneController(FXMLLoader loader, Mark student) {
        AddMarksController addMarksController = loader.getController();
        addMarksController.setUpdate(true);
        addMarksController.setTextField(student.getId(), student.getMath(),
                student.getEng(), student.getSwa());
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
}
