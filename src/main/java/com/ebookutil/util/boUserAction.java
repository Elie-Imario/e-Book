package com.ebookutil.util;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class boUserAction {
    public static String generateHashPassWord(String pswrd){
        try {
            MessageDigest messageDigest  =MessageDigest.getInstance("MD5");
            byte[] md = messageDigest.digest(pswrd.getBytes());
            BigInteger bigInteger = new BigInteger(1, md);
            String hashedPassword = bigInteger.toString(16);
            while (hashedPassword.length() < 32){
                hashedPassword = "0" + hashedPassword;
            }
            return hashedPassword;

        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static void UpdateUserPassword(String password){
        Connection connection = connectionToDatabase.getInstance();
        String query = "UPDATE bo_User SET Password = ? WHERE Id = 1";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, password);

            preparedStatement.executeUpdate();

            preparedStatement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void resetFormToNormal(PasswordField currentpswrd, PasswordField newpswrd, PasswordField confirmpswrd, Label errorMsg){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        currentpswrd.setText("");
        currentpswrd.setStyle(successStyle);
        newpswrd.setText("");
        newpswrd.setStyle(successStyle);
        confirmpswrd.setText("");
        confirmpswrd.setStyle(successStyle);

        errorMsg.setText("");
        errorMsg.setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
}
