package com.example.jafafxlearn.controller.Supervisor;

import com.example.jafafxlearn.database.DatabaseConnection;
import com.example.jafafxlearn.functionalityInterface.BasicIntegrationDB;
import com.example.jafafxlearn.model.Staff;
import com.example.jafafxlearn.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class supervisorUserController implements Initializable, BasicIntegrationDB {

    @FXML
    private ChoiceBox cbJabatan,cbDepartemen,cbJK;
    @FXML
    private Button btnTambah;
    @FXML
    private TextField tfNama,tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label alert;

    @FXML
    private TableView<Staff> staffTableView;
    @FXML
    private TableColumn<Staff,Integer> colNo;
    @FXML
    private TableColumn<Staff,String> colNama;
    @FXML
    private TableColumn<Staff,String> colUsername;
    @FXML
    private TableColumn<Staff,String> colJK;
    @FXML
    private TableColumn<Staff,String> colDepartemen;
    @FXML
    private TableColumn<Staff,String> colJabatan;
    @FXML
    private TableColumn<Staff,Void> colAction;

    ObservableList<Staff> mylist = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User u = new User();
        cbDepartemen.setItems(FXCollections.observableArrayList(
                "SDM","KEUANGAN","PRODUKSI"
        ));
        cbJabatan.setItems(FXCollections.observableArrayList(
                "Staff"
        ));
        cbJK.setItems(FXCollections.observableArrayList(
                "Laki-laki","Perempuan"
        ));
        read();
    }

    public void tambahUser(ActionEvent event) {
        alert.setTextFill(Paint.valueOf("#FF0000"));
        if(tfNama.getText().equals("")){
            alert.setText("Nama Lengkap harus diisi");
        }else if (tfUsername.getText().equals("")){
            alert.setText("Username harus diisi");
        }else if (pfPassword.getText().equals("")){
            alert.setText("Password harus diisi");
        }else if (cbJK.getSelectionModel().getSelectedItem().toString().equals("")){
            alert.setText("Jenis Kelamin harus diisi");
        }else if(cbDepartemen.getSelectionModel().getSelectedItem().toString().equals("")){
            alert.setText("Departemen harus dipilih");
        }else if(cbJabatan.getSelectionModel().getSelectedItem().toString().equals("")){
            alert.setText("Jabatan harus dipilih");
        }else{
            add();
        }
    }

    @Override
    public boolean add() {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        String query = "INSERT INTO users " +
                "(fullname, username, password, role, is_delete, created_at, departemen, jenis_kelamin, handle_departemen) " +
                "values (?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,tfNama.getText());
            statement.setString(2,tfUsername.getText());
            statement.setString(3,pfPassword.getText());
            statement.setInt(4,cbJabatan.getSelectionModel().getSelectedIndex());
            statement.setBoolean(5,false);
            statement.setString(6, LocalDateTime.now().toString());
            statement.setString(7,cbDepartemen.getSelectionModel().getSelectedItem().toString());
            statement.setString(8,cbJK.getSelectionModel().getSelectedItem().toString());
            statement.setString(9,null);
            statement.executeUpdate();
            alert.setText("User berhasil ditambahkan");
            alert.setTextFill(Paint.valueOf("#00FF80"));

            tfNama.setText("");
            tfUsername.setText("");
            cbJK.getSelectionModel().clearSelection();
            cbJabatan.getSelectionModel().clearSelection();
            cbDepartemen.getSelectionModel().clearSelection();
            pfPassword.setText("");

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
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        String query = "select * from users where role=0";
        try {
            Statement statement =  connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            int i=0;
            while (output.next()){
                i++;
                String myjabatan = "";
                if(output.getInt("role") == 2){
                    myjabatan = "Admin";
                }else if (output.getInt("role") == 1){
                    myjabatan = "Supervisor";
                }else {
                    myjabatan = "Staff";
                }
                mylist.add(new Staff(output.getInt("id"),output.getString("fullname"),output.getString("username"),output.getString("jenis_kelamin"),output.getString("departemen"),myjabatan));
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
            colNama.setCellValueFactory(new PropertyValueFactory<>("fullname"));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colJK.setCellValueFactory(new  PropertyValueFactory<>("jenisKelamin"));
            colDepartemen.setCellValueFactory(new PropertyValueFactory<>("departemen"));
            colJabatan.setCellValueFactory(new PropertyValueFactory<>("jabatan"));
            colAction.setCellFactory(col -> {
                Button button = new Button("hapus");
                button.setStyle("-fx-background-color: #ff6666;");
                button.setTextFill(Color.WHITE);
                VBox vBox = new VBox(button);
                TableCell<Staff, Void> cell = new TableCell<Staff, Void>() {
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
                    Staff item = cell.getTableView().getItems().get(cell.getIndex());
                    delete(item.getID());
                });
                return cell;
            });
            staffTableView.setItems(mylist);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean delete(int id) {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        String query = "delete from users where id="+id;
        try {
            Statement statement = connection.createStatement();
            int res = statement.executeUpdate(query);
            if(res > 0){
                alert.setText("Data berhasil dihapus");
                read();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
