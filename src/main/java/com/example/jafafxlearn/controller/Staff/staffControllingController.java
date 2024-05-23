package com.example.jafafxlearn.controller.Staff;

import com.example.jafafxlearn.RuntimeConfiguration;
import com.example.jafafxlearn.database.DatabaseConnection;
import com.example.jafafxlearn.functionalityInterface.BasicIntegrationDB;
import com.example.jafafxlearn.model.KPIVM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class staffControllingController implements Initializable, BasicIntegrationDB {
    DatabaseConnection db = new DatabaseConnection();
    Connection connection = db.getConnection();

    @FXML
    TableView<KPIVM> tableView;
    @FXML
    private TableColumn<KPIVM,Integer> colNo;
    @FXML
    private TableColumn<KPIVM,String> colNama;
    @FXML
    private TableColumn<KPIVM,String> colTask;
    @FXML
    private TableColumn<KPIVM,String> colTipe;
    @FXML
    private TableColumn<KPIVM,String> colTarget;
    @FXML
    private TableColumn<KPIVM,String> colActual;
    @FXML
    private TableColumn<KPIVM,Void> colAction;

    ObservableList<KPIVM> mylist = FXCollections.observableArrayList();


    @Override
    public boolean add() {
        return false;
    }

    @Override
    public boolean update(int id) {
        return false;
    }


    @Override
    public boolean read() {

        mylist.clear();
        String query = "select a.*,b.jobdesc,c.fullname from kpi a " +
                "left outer join mst_jobdesc b on a.taskID = b.id "+
                "left outer join users c on a.userID = c.id " +
                "where a.userID = "+ RuntimeConfiguration.getLoginId();
        try {
            Statement statement =  connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()){
                if(output.getString("tipe").equals("Target Value")){
                    mylist.add(new KPIVM(output.getInt("id"),output.getString("jobdesc"),output.getString("tipe"),output.getString("fullname"),
                            output.getString("valueTarget"),output.getString("valueActual")));
                }else{
                    mylist.add(new KPIVM(output.getInt("id"),output.getString("jobdesc"),output.getString("tipe"),output.getString("fullname"),
                            output.getString("timeTarget"),output.getString("timeActual")));
                }

            }
            colNo.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (this.getTableRow() != null) {
                        int index = this.getTableRow().getIndex();
                        setText(empty ? "" : String.valueOf(index + 1));
                    }
                }
            });
            colNama.setCellValueFactory(new PropertyValueFactory<>("user"));
            colTask.setCellValueFactory(new PropertyValueFactory<>("task"));
            colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
            colActual.setCellValueFactory(new PropertyValueFactory<>("actual"));
            colTarget.setCellValueFactory(new PropertyValueFactory<>("target"));
            colAction.setCellFactory(col -> {
                Button button = new Button("Add Actual");
                button.setStyle("-fx-background-color: #E8DD47;");
                button.setTextFill(Color.WHITE);
                VBox vBox = new VBox(button);
                TableCell<KPIVM, Void> cell = new TableCell<KPIVM, Void>() {
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(vBox);
                        }
                    }
                };
                button.setOnAction(event -> {
                    KPIVM item = cell.getTableView().getItems().get(cell.getIndex());
                    controll(item.getID(),item.getTipe());

                });
                return cell;
            });
            tableView.setItems(mylist);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void controll(int id, String tipe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/staff/ControllingModal.fxml"));
            Parent root = loader.load();
            staffControllingModalController myc = loader.getController();
            myc.setTipe(tipe);
            myc.setId(id);
            Stage stage = new Stage();
            stage.setTitle("Add Actual");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 300, 200));
            stage.setOnHidden(windowEvent -> {
                read();
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean delete(int id) {
        return false;
    }
    public void loadDataKaryawanJobdesc(){

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         read();
    }


}
