package com.Nabangi.fuzu.Controllers;

import com.Nabangi.fuzu.Helpers.*;
import com.Nabangi.fuzu.Database.DatabaseLink;
import com.Nabangi.fuzu.Models.Student;
import de.jensd.fx.glyphs.fontawesome.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/

public class StudentCrudController extends LoginController implements Initializable {
    @FXML private Label lblStudentsError;
    @FXML private TableView<Student> tblStudents;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colFname;
    @FXML private TableColumn<Student, String> colLname;
    @FXML private TableColumn<Student, String> colGender;
    @FXML private TableColumn<Student, String> colDob;
    @FXML private TableColumn<Student, String> colParent;
    @FXML private TableColumn<Student, String> colAction;

    String query = null;
    Connection connection = DatabaseLink.connectDb();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Student student = null ;

    ObservableList<Student> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(sessionUserType != null) {
            if(sessionUserType.equals("Parent")) {
                tblStudents.setTableMenuButtonVisible(false);
                tblStudents.setPrefHeight(200);
                colParent.setVisible(false);
            }
        }

        refreshTable();
        loadData();
    }

    @FXML private void getAddView() {
        if(sessionUserType != null) {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("../Fxml/addStudent.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(DatabaseLink.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            lblStudentsError.setText("Please login to add a student.");
        }
    }

    @FXML private void refreshTable() {
        try {
            studentList.clear();

            if(sessionUserType.equals("Parent")) {
                query = "SELECT stu.id, stu.first_name, stu.last_name, stu.gender, DOB, par.first_name, par.last_name " +
                        "FROM tbl_students " +
                        "INNER JOIN tbl_users stu ON tbl_students.user_id = stu.id " +
                        "INNER JOIN tbl_users par ON tbl_students.parent_id = par.id " +
                        "WHERE parent_id = " + sessionId;
            } else {
                query = "SELECT stu.id, stu.first_name, stu.last_name, stu.gender, DOB, par.first_name, par.last_name " +
                        "FROM tbl_students " +
                        "INNER JOIN tbl_users stu ON tbl_students.user_id = stu.id " +
                        "INNER JOIN tbl_users par ON tbl_students.parent_id = par.id ";
            }

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                studentList.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("stu.first_name"),
                        resultSet.getString("stu.last_name"),
                        resultSet.getString("gender"),
                        resultSet.getString("par.first_name") +
                                " " + resultSet.getString("par.last_name"),
                        resultSet.getDate("DOB")
                ));
                tblStudents.setItems(studentList);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData() {
        Helpers.loadTableData(colId, colFname, colLname, colGender, colDob);
        colParent.setCellValueFactory(new PropertyValueFactory<>("parentName"));

        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFactory = (TableColumn<Student, String> param) -> {
            // make cell containing buttons

            return new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows

                    if (empty) {
                        setGraphic(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                student = tblStudents.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `tbl_users` WHERE id  ="+student.getId();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();

                            } catch (SQLException ex) {
                                Logger.getLogger(StudentCrudController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            student = tblStudents.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("../Fxml/addStudent.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(StudentCrudController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            Helpers.getAddStudentSceneController(loader, student);
                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                    }
                    setText(null);
                }

            };
        };
        colAction.setCellFactory(cellFactory);
        tblStudents.setItems(studentList);
    }
}
