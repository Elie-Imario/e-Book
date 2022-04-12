package com.ebookutil.util;
import java.sql.*;

public class connectionToDatabase {
    private static String Url = "jdbc:mysql://localhost:3306/e-ndrana";
    private static String User = "root";
    private static String Password = "";
    private static Connection connection;

    public static Connection getInstance() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(Url, User, Password);
            } catch (Exception e) {
                System.out.println("Impossible de se connecter à la base de donnée");
            }
        }
        return connection;
    }
}
