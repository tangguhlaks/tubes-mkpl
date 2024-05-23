package com.example.jafafxlearn.controller.Staff;

import com.example.jafafxlearn.controller.Login;
import com.example.jafafxlearn.database.DatabaseConnection;
import com.example.jafafxlearn.functionalityInterface.BasicIntegrationDB;
import com.example.jafafxlearn.model.Jobdesc;
import com.example.jafafxlearn.model.KPIVM;
import com.example.jafafxlearn.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class staffKPIController implements Initializable, BasicIntegrationDB {
    DatabaseConnection db = new DatabaseConnection();
    Connection connection = db.getConnection();
    ArrayList<User> listUser = new ArrayList<>();
    ArrayList<String> listUserVM  = new ArrayList<>();
    ArrayList<Jobdesc> listJobdesc = new ArrayList<>();
    ArrayList<String> listJobdescVM = new ArrayList<>();
    @FXML
    ChoiceBox cbKarywan;
    @FXML
    ChoiceBox cbJobdesc;
    @FXML
    ChoiceBox cbTipe;
    @FXML
    TextField tfTargetValue;
    @FXML
    DatePicker tfTargetDate;
    @FXML
    TableView<KPIVM> tableView;
    @FXML
    private TableColumn<KPIVM,Integer> colNo;
    @FXML
    private TableColumn<KPIVM,String> colTipe;
    @FXML
    private TableColumn<KPIVM,String> colTarget;
    @FXML
    private TableColumn<KPIVM,String> colActual;
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
                "left outer join users c on a.userID = c.id";
        String selectedDepartemen = Login.getLoggedInUser().getDepartemen();
        String jobdescQuery = "SELECT * FROM mst_jobdesc WHERE departemen  = '" + selectedDepartemen + "'";
        try {
            Statement statement =  connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            Statement jobdescStatement = connection.createStatement();
            ResultSet jobdescResult = jobdescStatement.executeQuery(jobdescQuery);
            listJobdesc.clear();
            listJobdescVM.clear();

            while (jobdescResult.next()) {
                // Ambil data jobdesc dan tambahkan ke listJobdesc
                Jobdesc jobdesc = new Jobdesc(jobdescResult.getInt("id"), jobdescResult.getString("jobdesc"), selectedDepartemen);
                listJobdesc.add(jobdesc);

                // Ambil data jobdesc dan tambahkan ke listJobdescVM (misalnya nama jobdesc sebagai nilai yang ditampilkan)
                listJobdescVM.add(jobdesc.getJobdesc());
            }
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
            cbJobdesc.setItems(FXCollections.observableArrayList(listJobdescVM));
            colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
            colActual.setCellValueFactory(new PropertyValueFactory<>("actual"));
            colTarget.setCellValueFactory(new PropertyValueFactory<>("target"));
            tableView.setItems(mylist);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        read();
    }
}
