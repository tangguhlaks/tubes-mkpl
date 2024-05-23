package com.example.jafafxlearn.database;


import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaselink;
    public Connection getConnection(){
        String dbName = "sys_kpi";
        String dbUser = "root";
        String dbPassword = "";//myprivate
        String url = "jdbc:mysql://localhost:3308/"+dbName;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            databaselink = DriverManager.getConnection(url,dbUser,dbPassword);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return  databaselink;
    }
}
