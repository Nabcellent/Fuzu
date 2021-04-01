package com.Nabangi.fuzu.Controllers;

import com.Nabangi.fuzu.Database.DatabaseLink;
import com.Nabangi.fuzu.Helpers.Helpers;
import com.Nabangi.fuzu.Models.Mark;
import com.jfoenix.controls.JFXTextField;
import java.awt.Component;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.swing.JOptionPane;

public class ReportCrudController extends LoginController implements Initializable {
    public JFXTextField txtMath;
    public JFXTextField txtEng;
    public JFXTextField txtSwa;
    public JFXTextField txtAvg;
    public JFXTextField txtGrade;
    public AnchorPane anchMarks;
    @FXML
    private HBox hboxButtons;
    @FXML
    private Label lblError;
    @FXML
    private TableView<Mark> tblReport;
    @FXML
    private TableColumn<Mark, String> colId;
    @FXML
    private TableColumn<Mark, String> colName;
    @FXML
    private TableColumn<Mark, String> colMath;
    @FXML
    private TableColumn<Mark, String> colEng;
    @FXML
    private TableColumn<Mark, String> colSwa;
    @FXML
    private TableColumn<Mark, String> colAvg;
    @FXML
    private TableColumn<Mark, String> colGrade;
    @FXML
    private TableColumn<Mark, String> colAction;
    String query = null;
    Connection connection = DatabaseLink.connectDb();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Mark mark = null;
    ObservableList<Mark> listMarks = FXCollections.observableArrayList();

    public ReportCrudController() {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.txtAvg.setStyle("-fx-text-inner-color: red");
        this.txtGrade.setStyle("-fx-text-inner-color: green");
        if (!sessionUserType.equalsIgnoreCase("Student")) {
            this.refreshTable();
            this.loadData();
            this.anchMarks.setVisible(false);
            if (sessionUserType != null && sessionUserType.equals("Parent")) {
                this.colAction.setVisible(false);
                this.tblReport.setTableMenuButtonVisible(false);
                this.hboxButtons.setVisible(false);
            }
        } else {
            this.tblReport.setVisible(false);
            this.hboxButtons.setVisible(false);
            Mark mark = new Mark();
            mark.getMarksById(sessionId, this.lblError);
            if (this.lblError.getText().equals("")) {
                this.txtMath.setText(String.valueOf(mark.getMath()));
                this.txtEng.setText(String.valueOf(mark.getEng()));
                this.txtSwa.setText(String.valueOf(mark.getSwa()));
                this.txtAvg.setText(String.valueOf(mark.calculateAverage()));
                this.txtGrade.setText(String.valueOf(mark.calculateGrade(mark.calculateAverage())));
            }
        }

    }

    public void getAddView() {
        if (sessionUserType != null) {
            if (sessionUserType.equals("Admin")) {
                try {
                    Parent parent = (Parent)FXMLLoader.load(this.getClass().getResource("../Fxml/addMarks.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UTILITY);
                    stage.show();
                } catch (IOException var4) {
                    Logger.getLogger(DatabaseLink.class.getName()).log(Level.SEVERE, (String)null, var4);
                }
            } else {
                JOptionPane.showMessageDialog((Component)null, "Only an admin can add a mark");
            }
        } else {
            this.lblError.setText("Please login as an administrator first.");
        }

    }

    @FXML
    private void refreshTable() {
        try {
            this.listMarks.clear();
            if (sessionUserType.equals("Parent")) {
                this.query = "SELECT tbl_marks.id as id, first_name, last_name, mathematics, english, swahili FROM tbl_marks INNER JOIN tbl_users ON tbl_marks.student_id = tbl_users.id INNER JOIN tbl_students ON tbl_marks.student_id = tbl_students.user_id WHERE parent_id = " + sessionId;
            } else {
                this.query = "SELECT tbl_marks.id as id, first_name, last_name, mathematics, english, swahili FROM tbl_marks INNER JOIN tbl_users ON tbl_marks.student_id = tbl_users.id";
            }

            this.preparedStatement = this.connection.prepareStatement(this.query);
            this.resultSet = this.preparedStatement.executeQuery();

            while(this.resultSet.next()) {
                float math = this.resultSet.getFloat("mathematics");
                float eng = this.resultSet.getFloat("english");
                float swa = this.resultSet.getFloat("swahili");
                this.listMarks.add(new Mark(this.resultSet.getInt("id"), this.resultSet.getString("first_name") + " " + this.resultSet.getString("last_name"), math, eng, swa));
                this.tblReport.setItems(this.listMarks);
            }
        } catch (SQLException var4) {
            Logger.getLogger(DatabaseLink.class.getName()).log(Level.SEVERE, (String)null, var4);
        }

    }

    private void loadData() {
        this.colId.setCellValueFactory(new PropertyValueFactory("id"));
        this.colName.setCellValueFactory(new PropertyValueFactory("name"));
        this.colMath.setCellValueFactory(new PropertyValueFactory("math"));
        this.colEng.setCellValueFactory(new PropertyValueFactory("eng"));
        this.colSwa.setCellValueFactory(new PropertyValueFactory("swa"));
        this.colAvg.setCellValueFactory(new PropertyValueFactory("avg"));
        this.colGrade.setCellValueFactory(new PropertyValueFactory("grade"));

        Callback<TableColumn<Mark, String>, TableCell<Mark, String>> cellFactory = (TableColumn<Mark, String> param) -> {

            final TableCell<Mark, String> cell = new TableCell<Mark, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        this.setGraphic((Node) null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                        deleteIcon.setStyle(" -fx-cursor: hand ;-glyph-size:28px;-fx-fill:#ff1744;");
                        editIcon.setStyle(" -fx-cursor: hand ;-glyph-size:28px;-fx-fill:#00E676;");

                        deleteIcon.setOnMouseClicked((event) -> {
                            try {
                                mark = tblReport.getSelectionModel().getSelectedItem();
                                query = "DELETE FROM `tbl_marks` WHERE id = " + mark.getId();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.execute();
                                refreshTable();
                            } catch (SQLException var3) {
                                Logger.getLogger(ReportCrudController.class.getName()).log(Level.SEVERE, (String) null, var3);
                            }

                        });
                        editIcon.setOnMouseClicked((event) -> {
                            mark = (Mark) tblReport.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../Fxml/addMarks.fxml"));

                            try {
                                loader.load();
                            } catch (IOException var4) {
                                Logger.getLogger(ReportCrudController.class.getName()).log(Level.SEVERE, (String) null, var4);
                            }

                            Helpers.getAddMarksSceneController(loader, mark);
                        });
                        HBox actionCol = new HBox(new Node[]{editIcon, deleteIcon});
                        actionCol.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2.0D, 2.0D, 0.0D, 3.0D));
                        HBox.setMargin(editIcon, new Insets(2.0D, 3.0D, 0.0D, 2.0D));
                        this.setGraphic(actionCol);
                    }

                    setText((String) null);
                }
            };
            return cell;
        };
        colAction.setCellFactory(cellFactory);
        tblReport.setItems(this.listMarks);
    }
}
