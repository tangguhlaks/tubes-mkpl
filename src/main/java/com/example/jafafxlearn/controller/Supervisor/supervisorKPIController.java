package com.example.jafafxlearn.controller.Supervisor;

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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class supervisorKPIController  implements Initializable, BasicIntegrationDB {
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
        String query = "INSERT INTO kpi " +
                "(taskID, userID, valueTarget, valueActual, timeTarget, timeActual,tipe) " +
                "values (?,?,?,?,?,?,?)";
        try{
            User u =listUser.stream().filter(x-> x.getUsername().equals(cbKarywan.getSelectionModel().getSelectedItem().toString())).findFirst().orElse(null);
            String departemen = u.getDepartemen();
            Jobdesc j = listJobdesc.stream()
                    .filter(x -> x.getJobdesc().equals(cbJobdesc.getSelectionModel().getSelectedItem()))
                    .filter(x -> x.getDepartemen().equals(departemen))
                    .findFirst()
                    .orElse(null);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,j.getID());
            statement.setInt(2,u.getID());
            if(cbTipe.getSelectionModel().getSelectedItem().toString().equals("Target Value")){
                statement.setString(3,tfTargetValue.getText().toString());
                statement.setString(5,"-");
            }else if (cbTipe.getSelectionModel().getSelectedItem().toString().equals("Target Date")){
                statement.setString(5,tfTargetDate.getValue().toString());
                statement.setString(3,"-");
            }
            statement.setString(4,"-");
            statement.setString(6,"-");
            statement.setString(7,cbTipe.getSelectionModel().getSelectedItem().toString());
            statement.executeUpdate();
            read();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
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
                "where c.role = 0";
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
                Button button = new Button("hapus");
                button.setStyle("-fx-background-color: #ff6666;");
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
                    delete(item.getID());
                });
                return cell;
            });
            tableView.setItems(mylist);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean delete(int id) {
        String query = "delete from kpi where id="+id;
        try {
            Statement statement = connection.createStatement();
            int res = statement.executeUpdate(query);
            if(res > 0){
                read();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public void loadDataKaryawanJobdesc(){
        cbKarywan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Ketika karyawan dipilih, perbarui daftar jobdesc sesuai dengan departemen karyawan
            User selectedUser = listUser.stream()
                    .filter(x -> x.getUsername().equals(newValue))
                    .findFirst()
                    .orElse(null);
            if (selectedUser != null) {
                String selectedDepartemen = selectedUser.getDepartemen();
                List<String> filteredJobdescs = listJobdesc.stream()
                        .filter(j -> j.getDepartemen().equals(selectedDepartemen))
                        .map(Jobdesc::getJobdesc)
                        .collect(Collectors.toList());
                cbJobdesc.setItems(FXCollections.observableArrayList(filteredJobdescs));
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listUser = getListUser();
        tfTargetDate.hide();
        for (User u: listUser) {
            listUserVM.add(u.getUsername());
        }
        cbKarywan.setItems(FXCollections.observableArrayList(listUserVM));

        listJobdesc = getListJobdesc();
        for (Jobdesc j:listJobdesc){
            listJobdescVM.add(j.getJobdesc());
        }
        cbJobdesc.setItems(FXCollections.observableArrayList(listJobdescVM));
        cbTipe.setItems(FXCollections.observableArrayList("Target Value","Target Date"));
        read();
        loadDataKaryawanJobdesc();
    }

    private ArrayList<Jobdesc> getListJobdesc() {
        ArrayList<Jobdesc> ress = new ArrayList<>();
        String query = "select * from mst_jobdesc";
        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(query);
            while (res.next()){
                ress.add(new Jobdesc(res.getInt("id"),res.getString("jobdesc"),res.getString("departemen")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ress;
    }

    private ArrayList<User> getListUser() {
        ArrayList<User> res = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 0";
        try {
            Statement statement = connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()) {
                String myjabatan = "";
                if(output.getInt("role") == 2){
                    myjabatan = "Admin";
                }else if (output.getInt("role") == 1){
                    myjabatan = "Supervisor";
                }else {
                    myjabatan = "Staff";
                }
                res.add(new User(output.getInt("id"), output.getString("fullname"), output.getString("username"), output.getString("jenis_kelamin"), output.getString("departemen"), myjabatan));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
