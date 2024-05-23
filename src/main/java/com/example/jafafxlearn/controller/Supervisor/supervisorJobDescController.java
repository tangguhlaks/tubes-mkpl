package com.example.jafafxlearn.controller.Supervisor;

import com.example.jafafxlearn.database.DatabaseConnection;
import com.example.jafafxlearn.functionalityInterface.BasicIntegrationDB;
import com.example.jafafxlearn.model.Jobdesc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class supervisorJobDescController  implements Initializable, BasicIntegrationDB {
    @FXML
    TextField tfJobdesc;
    @FXML
    ChoiceBox cbDepartemen;
    @FXML
    Button btnTambah;
    @FXML
    TableView<Jobdesc> jobdescTableView;
    @FXML
    TableColumn<Jobdesc,Integer> colNo;
    @FXML
    TableColumn<Jobdesc,String> colJobdesc;
    @FXML
    TableColumn<Jobdesc,String> colDepartemen;
    @FXML
    TableColumn<Jobdesc,Void> colAksi;

    ObservableList<Jobdesc> mList = FXCollections.observableArrayList();

    @Override
    public boolean add()
    {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        String query = "insert into mst_jobdesc (jobdesc,departemen) values (?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,tfJobdesc.getText());
            statement.setString(2,cbDepartemen.getSelectionModel().getSelectedItem().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        read();
        return true;
    }

    @Override
    public boolean update(int id) {
        return false;
    }

    @Override
    public boolean read() {
        mList.clear();
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        String query = "select * from mst_jobdesc";
        try {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(query);
            while (res.next()){
                mList.add(new Jobdesc(res.getInt("id"),res.getString("jobdesc"),res.getString("departemen")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
        colJobdesc.setCellValueFactory(new PropertyValueFactory<>("jobdesc"));
        colDepartemen.setCellValueFactory(new PropertyValueFactory<>("departemen"));
        colAksi.setCellFactory(col -> {
            Button button = new Button("hapus");
            button.setStyle("-fx-background-color: #ff6666;");
            button.setTextFill(Color.WHITE);
            VBox vBox = new VBox(button);
            TableCell<Jobdesc, Void> cell = new TableCell<Jobdesc, Void>() {
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
                Jobdesc item = cell.getTableView().getItems().get(cell.getIndex());
                delete(item.getID());
            });
            return cell;
        });
        jobdescTableView.setItems(mList);
        return true;
    }

    @Override
    public boolean delete(int id) {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        String query = "delete from mst_jobdesc where id="+id;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbDepartemen.setItems(FXCollections.observableArrayList(
                "SDM","KEUANGAN","PRODUKSI"
        ));
        read();
    }

    public void tambah(ActionEvent event) {
        if(tfJobdesc.getText().equals("") == false && cbDepartemen.getSelectionModel().getSelectedItem().toString().equals("") == false){
            add();
        }
    }

}
