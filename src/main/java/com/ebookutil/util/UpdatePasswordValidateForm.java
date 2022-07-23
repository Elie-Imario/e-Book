package com.ebookutil.util;

import com.ebookutil.exception.boUserException.boUserException;
import javafx.scene.control.PasswordField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdatePasswordValidateForm{
    public static void validForm(PasswordField currentpswrd, PasswordField newpswrd, PasswordField confirmpswrd)
    throws boUserException {
        if((currentpswrd.getText().isBlank()) && (newpswrd.getText().isBlank()) && (confirmpswrd.getText().isBlank())){
            setErrorStyle(currentpswrd, newpswrd, confirmpswrd);
            throw new boUserException("Veuillez renseigner ces champs!");
        }
        else if((currentpswrd.getText().isBlank())){
            setErrorStyle(currentpswrd);
            setSuccessStyle(newpswrd, confirmpswrd);
            throw new boUserException("Veuillez rensigner le mot de passe actuel!");
        }
        else if((newpswrd.getText().isBlank())){
            setErrorStyle(newpswrd);
            setSuccessStyle(currentpswrd, confirmpswrd);
            throw new boUserException("Veuillez rensigner le nouveau mot de passe!");
        }
        else if((confirmpswrd.getText().isBlank())){
            setErrorStyle(confirmpswrd);
            setSuccessStyle(currentpswrd, newpswrd);
            throw new boUserException("Veuillez confirmer le nouveau mot de passe!");
        }

        else{
            if(((currentpswrd.getText())!=null) && ((newpswrd.getText())!=null) && (confirmpswrd.getText()!=null)){
                if(currentpswrd.getText()!=null){
                    if(isPasswordValid(currentpswrd.getText())){
                        System.out.println("Current Password Valid");
                    }
                    else{
                        setErrorStyle(currentpswrd);
                        setSuccessStyle(newpswrd, confirmpswrd);
                        throw new boUserException("Le mot de passe est incorrect");
                    }
                }

                if((newpswrd.getText()!=null) && (confirmpswrd.getText()!=null)) {
                    if (newpswrd.getText().length() < 4) {
                        setErrorStyle(newpswrd);
                        setSuccessStyle(currentpswrd, confirmpswrd);
                        throw new boUserException("Le nouveau de mot de passe doit contenir au moins 4 caractères");
                    } else if (confirmpswrd.getText().length() < 4) {
                        setErrorStyle(confirmpswrd);
                        setSuccessStyle(currentpswrd, newpswrd);
                        throw new boUserException("Le nouveau de mot de passe doit contenir au moins 4 caractères");
                    } else {
                        if ((newpswrd.getText()).equals((confirmpswrd.getText()))) {
                            System.out.println("newPassword & confirmPassword valid");
                        } else {
                            setErrorStyle(newpswrd, confirmpswrd);
                            setSuccessStyle(currentpswrd);
                            throw new boUserException("Le nouveau mot de passe différe de sa confirmation");
                        }
                    }
                }
            }

            else{

            }
        }
    }


    public static void setErrorStyle(PasswordField passwordField){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";

        passwordField.setStyle(errorStyle);
        new animatefx.animation.BounceIn(passwordField).play();

    }

    public static void setErrorStyle(PasswordField passwordField1, PasswordField passwordField2){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";

        passwordField1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(passwordField1).play();
        passwordField2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(passwordField2).play();

    }


    public static void setErrorStyle(PasswordField passwordField1, PasswordField passwordField2, PasswordField passwordField3){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 3;";

        passwordField1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(passwordField1).play();
        passwordField2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(passwordField2).play();
        passwordField3.setStyle(errorStyle);
        new animatefx.animation.BounceIn(passwordField3).play();
    }

    public static void setSuccessStyle(PasswordField passwordField){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        passwordField.setStyle(successStyle);
    }

    public static void setSuccessStyle(PasswordField passwordField1, PasswordField passwordField2){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        passwordField1.setStyle(successStyle);
        passwordField2.setStyle(successStyle);
    }

    public static boolean isPasswordValid(String password){
        Connection connection = connectionToDatabase.getInstance();
        String hashedPassword = boUserAction.generateHashPassWord(password);
        String query = "SELECT * FROM bo_User WHERE Password ="+"\""+hashedPassword+"\""+"";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
