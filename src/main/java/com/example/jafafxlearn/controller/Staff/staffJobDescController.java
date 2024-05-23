package com.example.jafafxlearn.controller.Staff;

import com.example.jafafxlearn.controller.Login;
import com.example.jafafxlearn.database.DatabaseConnection;
import com.example.jafafxlearn.functionalityInterface.BasicIntegrationDB;
import com.example.jafafxlearn.model.Jobdesc;
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
import java.util.ResourceBundle;

public class staffJobDescController implements Initializable, BasicIntegrationDB  {
    @FXML
    TableView<Jobdesc> jobdescTableView;
    @FXML
    TableColumn<Jobdesc,Integer> colNo;
    @FXML
    TableColumn<Jobdesc,String> colJobdesc;

    ObservableList<Jobdesc> mList = FXCollections.observableArrayList();

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
        mList.clear();
        String selectedDepartemen = Login.getLoggedInUser().getDepartemen();
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        String query = "SELECT * FROM mst_jobdesc WHERE departemen = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedDepartemen);
            ResultSet res = statement.executeQuery();
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
        jobdescTableView.setItems(mList);
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
