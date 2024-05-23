package com.example.jafafxlearn.controller;

import com.example.jafafxlearn.RuntimeConfiguration;
import com.example.jafafxlearn.database.DatabaseConnection;
import com.example.jafafxlearn.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import  javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private Button btnLogin;
    @FXML
    private Label labelNotif;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;

    private Window mywindow;
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public void btnLoginOnClick(ActionEvent event){
        if (tf_username.getText().equals("")){
            labelNotif.setText("Username is required");
        }else if(tf_password.getText().equals("")){
            labelNotif.setText("Password is required");
        }else{
            validateLogin(event);
        }
    }

    private void validateLogin(ActionEvent event) {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        String query = "SELECT * FROM users WHERE username = '" + tf_username.getText() + "' AND password = '" + tf_password.getText() + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            if (queryResult.next()) {
                RuntimeConfiguration.saveLoginId(queryResult.getString("id"));
                int role = queryResult.getInt("role");
                switch (role) {
                    case 0: // Staff
                        loggedInUser = new User(queryResult.getInt("ID"), queryResult.getString("fullname"), queryResult.getString("username"),
                                queryResult.getString("jenis_kelamin"), queryResult.getString("departemen"), queryResult.getString("role"));
                        openStaffPage();
                        break;
                    case 1: // Supervisor
                        loggedInUser = new User(queryResult.getInt("ID"), queryResult.getString("fullname"), queryResult.getString("username"),
                                queryResult.getString("jenis_kelamin"), queryResult.getString("departemen"), queryResult.getString("role"));
                        openSupervisorPage();
                        break;
                    case 2: // Admin
                       // loggedInUser = loggedInUser = new User(queryResult.getInt("ID"), queryResult.getString("nama_lengkap"), queryResult.getString("username"),
                            //    queryResult.getString("jenisKelamin"), queryResult.getString("departemen"), queryResult.getString("jabatan"));
                        openAdminPage();
                        break;
                    default:
                        labelNotif.setText("Invalid role");
                }
            } else {
                labelNotif.setText("Invalid login, please try again");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAdminPage() {
        try {
            mywindow = labelNotif.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/admin/side-nav.fxml"));
            Stage stage = (Stage) mywindow;
            stage.setTitle("Admin Page");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    private void openStaffPage() {
        try {
            mywindow = labelNotif.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/staff/side-nav.fxml"));
            Stage stage = (Stage) mywindow;
            stage.setTitle("Staff Page");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    private void openSupervisorPage() {
        try {
            mywindow = labelNotif.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/supervisor/side-nav.fxml"));
            Stage stage = (Stage) mywindow;
            stage.setTitle("Supervisor Page");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            labelNotif.setText("");

    }
}
