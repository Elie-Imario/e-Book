package com.ebookutil.util;

import com.ebookutil.exception.boUserException.boUserException;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginValidateForm {
    public static void validForm(TextField login, PasswordField passwordField)
    throws boUserException {
        if((login.getText()).isBlank() && (passwordField.getText()).isBlank()){
            setErrorStyle(login, passwordField);
            throw new boUserException("Ces informations doivent être fournies pour accéder à E-Ndrana!");
        }
        else if((login.getText())!=null && (passwordField.getText()).isBlank()){
            setErrorStyle(passwordField);
            setSuccessStyle(login);
            throw new boUserException("Veuillez renseigner le mot de passe!");
        }
        else if((login.getText()).isBlank() && (passwordField.getText())!=null){
            setErrorStyle(login);
            setSuccessStyle(passwordField);
            throw new boUserException("Veuillez renseigner le login du compte!");
        }
        else {
            if (isLoginValid(login.getText()) && isPassWordCorrect(passwordField.getText())){
                System.out.println("login & password valid");
            }
            else {
                if(isLoginValid(login.getText())==false){
                    setErrorStyle(login);
                    setSuccessStyle(passwordField);
                    throw new boUserException("Ce Login de compte est incorrect!");
                }
                else if (isPassWordCorrect(passwordField.getText())==false){
                    setErrorStyle(passwordField);
                    setSuccessStyle(login);
                    throw new boUserException("Ce mot de passe est incorrect!");
                }
            }
        }
    }

    public static void setErrorStyle(TextField textField){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 3;";

        textField.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField).play();

    }
    public static void setErrorStyle(TextField textField1,TextField textField2){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 3;";

        textField1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField1).play();
        textField2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField2).play();
    }

    public static void setSuccessStyle(TextField textField){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField.setStyle(successStyle);
    }

    public static boolean isLoginValid(String _login){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT * FROM bo_user WHERE Username ="+"'"+_login+"'"+"" ;
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()){
                statement.close();
                return true;
            }
            else{
                statement.close();
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isPassWordCorrect(String _password){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT * FROM bo_user WHERE Password="+"\""+_password+"\""+"";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()){
                statement.close();
                return true;
            }
            else{
                statement.close();
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
