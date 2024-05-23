package com.example.jafafxlearn.controller.Staff;

import com.example.jafafxlearn.database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class staffControllingModalController implements Initializable {
    @FXML
    private TextField tf;
    @FXML
    private DatePicker df;
    @FXML
    private Button btn;

    private int id;
    private String tipe;
    private DatabaseConnection db = new DatabaseConnection();
    private Connection connection = db.getConnection();


    public void setId(int id) {
        this.id = id;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
        if(tipe.equals("Target Value")){
            df.setVisible(false);
            tf.setVisible(true);
        }else{
            df.setVisible(true);
            tf.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addActual(ActionEvent event){
            if(tipe.equals("Target Value")) {
                String query = "UPDATE kpi SET  valueActual = ? WHERE id = ?";
                try {
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, tf.getText());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    Window w = df.getScene().getWindow();
                    Stage stage = (Stage) w;
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                String query = "UPDATE kpi SET timeActual = ? WHERE id = ?";
                try {
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, df.getValue().toString());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    Window w = df.getScene().getWindow();
                    Stage stage = (Stage) w;
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    }
}
