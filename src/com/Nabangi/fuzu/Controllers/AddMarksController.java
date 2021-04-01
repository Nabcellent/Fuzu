package com.Nabangi.fuzu.Controllers;

import com.Nabangi.fuzu.Database.DatabaseLink;
import com.Nabangi.fuzu.Helpers.Helpers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.Nabangi.fuzu.Helpers.Helpers.isInRange;
import static com.Nabangi.fuzu.Helpers.Helpers.isNumeric;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/

public class AddMarksController extends LoginController implements Initializable {
    @FXML private Label lblHeader, lblStudents;
    @FXML private JFXComboBox<Item> cmbStudents;
    @FXML private JFXTextField txtMath, txtEng, txtSwa;
    @FXML private Label lblAlert, lblError;
    @FXML private JFXButton btnAddMarks;

    private boolean update;
    private int studentId;

    static class Item {
        final int id;
        final String text;

        public Item(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public int getId() {
            return id;
        }
    }

    static class ItemCell extends ListCell<Item> {

        @Override
        protected void updateItem(Item item, boolean empty) {
            super.updateItem(item, empty);

            setText(item == null ? "" : item.text);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JFXTextField[] textFields = {txtMath, txtEng, txtSwa};

        for(JFXTextField field : textFields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }

        Connection link = DatabaseLink.connectDb();

        String sql;
        if(sessionUserType.equals("Parent")) {
            sql = "SELECT user_id AS id, first_name, last_name FROM tbl_students " +
                    "INNER JOIN tbl_users ON tbl_students.user_id = tbl_users.id WHERE parent_id = " + sessionId;
        } else {
            sql = "SELECT id, first_name, last_name FROM tbl_users WHERE user_type = 'Student'";
        }

        try {
            PreparedStatement prepStmt = link.prepareStatement(sql);
            ResultSet result = prepStmt.executeQuery();

            while(result.next()) {
                int studentId = result.getInt("id");
                String studentName = result.getString("first_name") + " " + result.getString("last_name");
                cmbStudents.getItems().add(new Item(studentId, studentName));
                cmbStudents.setCellFactory(lv -> new ItemCell());
                cmbStudents.setButtonCell(new ItemCell());
            }

            prepStmt.close();
            result.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public void btnAddMarksOnClick() throws SQLException {
        if(isValid()) {
            lblError.setText("");
            if(update) {
                updateMarks();
            } else {
                insertMarks();
            }
        } else {
            Helpers.setMessageTimer(7, lblError);
        }
    }



    public boolean isValid() {
        Item student = cmbStudents.getValue();
        String mathematics = txtMath.getText().trim();
        String english = txtEng.getText().trim();
        String swahili = txtSwa.getText().trim();

        if(sessionId == 0) {
            lblError.setText("Please login first before adding marks!");
            return false;
        }
        if(!sessionUserType.equals("Admin")) {
            lblError.setText("Only a teacher can upload marks.");
            return false;
        }
        if(!update) {
            if(student == null) {
                lblError.setText("Please fill in all required fields!");
                return false;
            }
        }

        if(mathematics.isEmpty() || english.isEmpty() || swahili.isBlank()) {
            lblError.setText("Please fill in all required fields!");
            return false;
        }
        if(!isNumeric(mathematics) || !isNumeric(english) || !isNumeric(swahili)) {
            lblError.setText("All marks must be numeric. No characters allowed!");
            return false;
        }
        double mat = Double.parseDouble(mathematics);
        double eng = Double.parseDouble(english);
        double swa = Double.parseDouble(swahili);

        if(isInRange(mat) || isInRange(eng) || isInRange(swa)) {
            lblError.setText("All marks must between the range (0 - 100)");
            return false;
        }

        return true;
    }



    public void insertMarks() throws SQLException {
        Connection link = DatabaseLink.connectDb();

        final String SQL_INSERT = "INSERT INTO tbl_marks (student_id, mathematics, english, swahili) " +
                "VALUES (?,?,?,?)";

        PreparedStatement prepStatement = link.prepareStatement(SQL_INSERT,
                Statement.RETURN_GENERATED_KEYS);

        prepStatement.setInt(1, cmbStudents.valueProperty().get().getId());
        prepStatement.setString(2, txtMath.getText());
        prepStatement.setString(3, txtEng.getText());
        prepStatement.setString(4, txtSwa.getText());

        try {
            //==    Get affected rows
            int affectedRows = prepStatement.executeUpdate();

            if(affectedRows == 1) {
                clearForm();
                lblAlert.setText("Marks inserted Successfully!");
                Helpers.setMessageTimer(4, lblAlert);
            } else {
                lblError.setText("Error! Unable to add new mark");
                Helpers.setMessageTimer(7, lblAlert);
            }
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.getCause().printStackTrace();
        }
    }


    public void updateMarks() throws SQLException {
        Connection link = DatabaseLink.connectDb();

        final String SQL_UPDATE = "UPDATE tbl_marks SET mathematics = ?, english = ?, swahili = ? WHERE id = " + studentId;

        PreparedStatement prepStatement = link.prepareStatement(SQL_UPDATE,
                Statement.RETURN_GENERATED_KEYS);

        prepStatement.setString(1, txtMath.getText());
        prepStatement.setString(2, txtEng.getText());
        prepStatement.setString(3, txtSwa.getText());

        try {
            //==    Get affected rows
            int affectedRows = prepStatement.executeUpdate();

            if(affectedRows == 1) {
                clearForm();
                lblAlert.setText("Marks edit Successfully!");
                Helpers.setMessageTimer(4, lblAlert);
            } else {
                lblError.setText("Error! Unable to edit marks");
                Helpers.setMessageTimer(7, lblAlert);
            }
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.getCause().printStackTrace();
        }
    }

    private void clearForm() {
        cmbStudents.setValue(null);
        txtMath.setText("");
        txtEng.setText("");
        txtSwa.setText("");
    }

    public void setUpdate(boolean b) {
        this.update = b;
        btnAddMarks.setText("Update Marks");
        cmbStudents.setVisible(!b);
        lblStudents.setVisible(!b);
        lblHeader.setText("EDIT STUDENT MARKS");
    }

    public void setTextField(int studentId, float math, float eng, float swa) {
        this.studentId = studentId;
        txtMath.setText(String.valueOf(math));
        txtEng.setText(String.valueOf(eng));
        txtSwa.setText(String.valueOf(swa));
    }
}
