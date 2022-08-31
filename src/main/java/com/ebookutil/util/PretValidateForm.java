package com.ebookutil.util;

import com.ebookutil.exception.PretException.PretException;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PretValidateForm {

    public static void validForm(TextField nom, TextField title, DatePicker dateDeb, DatePicker dateFin)
    throws PretException {
        if ((nom.getText().isBlank()) && (title.getText().isBlank()) && (dateDeb.getValue() == null) && (dateFin.getValue() == null)) {
            setErrorStyle(nom, title, dateDeb, dateFin);
            throw new PretException("Ces champs sont obligatoires!");
        }
        else if ((nom.getText().isBlank())) {
            setErrorStyle(nom);
            setSuccessStyle(title, dateDeb, dateFin);
            throw new PretException("Veuillez renseigner le nom du Lecteur!");
        }
        else if ((title.getText().isBlank())) {
            setErrorStyle(title);
            setSuccessStyle(nom, dateDeb, dateFin);
            throw new PretException("Veuillez renseigner le titre de l'Ouvrage!");
        }
        else if ((dateDeb.getValue() == null) && (dateFin.getValue() == null)) {
            setErrorStyle(dateDeb, dateFin);
            setSuccessStyle(nom, title);
            throw new PretException("Veuillez rensigner la date de debut et de la fin du prêt!");
        }
        else if ((dateDeb.getValue() == null)) {
            setErrorStyle(dateDeb);
            setSuccessStyle(nom, title, dateFin);
            throw new PretException("Veuillez rensigner la date de debut du prêt!");
        }
        else if ((dateFin.getValue() == null)) {
            setErrorStyle(dateFin);
            setSuccessStyle(nom, title, dateDeb);
            throw new PretException("Veuillez rensigner la date de la fin du prêt!");
        }

        else if((title.getText()) != null || (nom.getText()) != null){
            if((title.getText()) != null){
                if (VerifyLivre(title.getText())){
                    if(isLivreDisponible(title.getText())){
                        System.out.println("Livre Valid");
                    }
                    else{
                        setErrorStyle(title);
                        setSuccessStyle(nom, dateDeb, dateFin);
                        throw new PretException("Cet ouvrage n'est pas diponible pour un prêt!");
                    }
                }
                else {
                    setErrorStyle(title);
                    setSuccessStyle(nom, dateDeb, dateFin);
                    throw new PretException("Cet ouvrage n'est pas encore enregistré sur E-NDRANA!");
                }
            }

            if ((nom.getText()) != null) {
                if (VerifyLecteur(nom.getText())) {
                    if (isNbPretEnCoursValid(nom.getText())) {
                        System.out.println("Lecteur Valid");
                    } else {
                        setErrorStyle(nom);
                        setSuccessStyle(title, dateDeb, dateFin);
                        throw new PretException("Ce Lecteur a atteint la limite de prêt autorisé (3 Livres)!");
                    }
                } else {
                    setErrorStyle(nom);
                    setSuccessStyle(title, dateDeb, dateFin);
                    throw new PretException("Ce Lecteur n'est pas encore enregistré sur E-NDRANA!");
                }
            }
        }


        else{

        }

    }


    public static void validForm(TextField title, DatePicker dateDeb, DatePicker dateFin)
    throws PretException {
        if ((title.getText().isBlank()) && (dateDeb.getValue() == null) && (dateFin.getValue() == null)) {
            setErrorStyle(title, dateDeb, dateFin);
            throw new PretException("Ces champs sont obligatoires!");
        }
        else if ((title.getText().isBlank())) {
            setErrorStyle(title);
            setSuccesStyle(dateDeb, dateFin);
            throw new PretException("Veuillez renseigner le titre de l'Ouvrage!");
        }
        else if ((dateDeb.getValue() == null) && (dateFin.getValue() == null)) {
            setErrorStyle(dateDeb, dateFin);
            setSuccesStyle(title);
            throw new PretException("Veuillez rensigner la date de debut et de la fin du prêt!");
        }
        else if ((dateDeb.getValue() == null)) {
            setErrorStyle(dateDeb);
            setSuccesStyle(title, dateFin);
            throw new PretException("Veuillez rensigner la date de debut du prêt!");
        }
        else if ((dateFin.getValue() == null)) {
            setErrorStyle(dateFin);
            setSuccesStyle(title, dateDeb);
            throw new PretException("Veuillez rensigner la date de la fin du prêt!");
        }

        else if((title.getText()) != null){
            if (VerifyLivre(title.getText())){
                if(isLivreDisponible(title.getText())){
                    System.out.println("Livre Valid");
                }
                /*else{
                    setErrorStyle(title);
                    setSuccesStyle(dateDeb, dateFin);
                    throw new PretException("Cet ouvrage n'est pas diponible pour un prêt!");
                }*/
            }
            else {
                setErrorStyle(title);
                setSuccesStyle(dateDeb, dateFin);
                throw new PretException("Cet ouvrage n'est pas encore enregistré sur E-NDRANA!");
            }
        }

        else{

        }

    }

    public static void compareNewFormValueToOld(TextField title, DatePicker dateDeb, DatePicker dateFin, ComboBox SelectedEtatPret,
                                                TextField currentTitle, DatePicker currentDateDeb, DatePicker currentDateFin, ComboBox currentSelectedEtatPret )
    throws PretException{
        if((title.getText().equals(currentTitle.getText())) && (dateDeb.getValue().equals(currentDateDeb.getValue())) && (dateFin.getValue().equals(currentDateFin.getValue())) && (SelectedEtatPret.getSelectionModel().getSelectedIndex()==currentSelectedEtatPret.getSelectionModel().getSelectedIndex())){
            setErrorStyle(SelectedEtatPret, title, dateDeb, dateFin);
            throw new PretException("Aucun champs n'a été modifié! Modifiez une valeur d'abord.");
        }
        else {

        }
    }
    public static void setSuccessStyle(TextField textField1,DatePicker datePicker1, DatePicker datePicker2){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField1.setStyle(successStyle);
        datePicker1.setStyle(successStyle);
        datePicker2.setStyle(successStyle);
    }

    public static void setSuccessStyle(TextField textField1, TextField textField2){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField1.setStyle(successStyle);
        textField2.setStyle(successStyle);
    }

    public static void setSuccessStyle(TextField textField1, TextField textField2, DatePicker datePicker){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField1.setStyle(successStyle);
        textField2.setStyle(successStyle);
        datePicker.setStyle(successStyle);
    }

    public static void setSuccesStyle(TextField textField){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField.setStyle(successStyle);

    }
    public static void setSuccesStyle(DatePicker datePicker1, DatePicker datePicker2){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        datePicker1.setStyle(successStyle);
        datePicker2.setStyle(successStyle);

    }
    public static void setSuccesStyle(TextField textField, DatePicker datePicker){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField.setStyle(successStyle);
        datePicker.setStyle(successStyle);

    }



    public static void setErrorStyle(TextField textField){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";

        textField.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField).play();

    }

    public static void setErrorStyle(DatePicker datePicker){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";

        datePicker.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker).play();

    }

    public static void setErrorStyle(TextField textField, DatePicker datePicker1, DatePicker datePicker2){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";

        textField.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField).play();
        datePicker1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker1).play();
        datePicker2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker2).play();

    }

    public static void setErrorStyle(DatePicker datePicker1, DatePicker datePicker2){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";

        datePicker1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker1).play();
        datePicker2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker2).play();

    }

    public static void setErrorStyle(ComboBox comboBox, TextField textField2,DatePicker datePicker1,DatePicker datePicker2){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 3;";

        comboBox.setStyle(errorStyle);
        new animatefx.animation.BounceIn(comboBox).play();
        textField2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField2).play();
        datePicker1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker1).play();
        datePicker2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker2).play();
    }

    public static void setErrorStyle(TextField textField1,TextField textField2,DatePicker datePicker1,DatePicker datePicker2){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 3;";

        textField1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField1).play();
        textField2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField2).play();
        datePicker1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker1).play();
        datePicker2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker2).play();
    }



    public static Boolean VerifyLecteur(String _nomLecteur){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT * FROM Lecteur WHERE Nom_Lecteur ="+"'"+_nomLecteur+"'"+"";
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
    public static boolean isNbPretEnCoursValid(String _nomLecteur){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT * FROM Lecteur WHERE Nom_Lecteur ="+"'"+_nomLecteur+"'"+"AND NbPretEnCours<3" ;
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

    public static Boolean VerifyLivre(String _titleBook){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT * FROM Livre WHERE Titre_Ouvrage="+"\""+_titleBook+"\""+"";
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
    public static boolean isLivreDisponible(String _titleBook){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT Disponible  FROM Livre WHERE Titre_Ouvrage ="+"\""+_titleBook+"\""+"";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()){
                if(resultSet.getBoolean("Disponible")){
                    return true;
                }
                else{
                    return false;
                }
            }
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
