package com.example.jafafxlearn.controller.Admin;

import com.example.jafafxlearn.RuntimeConfiguration;
import com.example.jafafxlearn.database.DatabaseConnection;
import com.example.jafafxlearn.model.Staff;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class adminSideNav implements Initializable {
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;
    @FXML
    private Button countAdmin,countSPV,countStaff,countTask,countTaskDone,countJobdesc;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCountData();
        bp.setCenter(ap);
    }

    public void dashboardNav(ActionEvent event) {
        setCountData();
        bp.setCenter(ap);
    }

    public void dataUserNav(ActionEvent event) {
        loadPage("admin/data-user");
    }

    public void kpiNav(ActionEvent event) {
        loadPage("admin/kpi-page");
    }

    public void dataJobDescNav(ActionEvent event)
    {
        loadPage("admin/data-jobdesc");
    }

    public  void loadPage(String page){
        Parent root=null;
        try {
            root = FXMLLoader.load(getClass().getResource("/"+page+".fxml"));
        }catch (Exception e){
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
        }
        bp.setCenter(root);
    }

    private void setCountData() {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();

        //get admin count
        String query = "select count(id) as count from users where role = 2";
        try {
            Statement statement = connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()){
                String res = output.getString("count");
                countAdmin.setText("    "+res+"\nAdmin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //get spv count
        query = "select count(id) as count from users where role = 1";
        try {
            Statement statement = connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()){
                String res = output.getString("count");
                countSPV.setText("      "+res+"\nSupervisor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //get spv count
        query = "select count(id) as count from users where role = 0";
        try {
            Statement statement = connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()){
                String res = output.getString("count");
                countStaff.setText("  "+res+"\nStaff");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //get Jobdesc count
        query = "select count(id) as count from mst_jobdesc";
        try {
            Statement statement = connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()){
                String res = output.getString("count");
                countJobdesc.setText("    "+res+"\nJobdesc");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //get Task count
        query = "select count(id) as count from kpi";
        try {
            Statement statement = connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()){
                String res = output.getString("count");
                countTask.setText("     "+res+"\nTask KPI");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //get Task Done count
        query = "select count(id) as count from kpi  where valueActual != '-' or timeActual != '-'";
        try {
            Statement statement = connection.createStatement();
            ResultSet output = statement.executeQuery(query);
            while (output.next()){
                String res = output.getString("count");
                countTaskDone.setText("          "+res+"\nTask KPI Done");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public void logout(ActionEvent event) {
        try {
            Window mywindow = bp.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) mywindow;
            stage.setTitle("Login");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
            RuntimeConfiguration.saveLoginId("0");
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
}
