package com.ebookutil.util;

import com.ebookutil.exception.BookException.BookException;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class BookValidateForm {

    public static void validForm(TextField textField1, TextField textField2, DatePicker datePicker)
    throws BookException {
        if((textField1.getText()).isBlank() && (textField2.getText()).isBlank() && datePicker.getValue()== null){
            setErrorStyle(textField1, textField2, datePicker);
            throw new BookException("Ces champs sont obligatoires!");
        }
        else if((textField1.getText()).isBlank()){
            setErrorStyle(textField1);
            setSuccessStyle(textField2, datePicker);
            throw new BookException("Veuillez renseigner le titre de l'Ouvrage!");
        }
        else if((textField2.getText()).isBlank()){
            setErrorStyle(textField2);
            setSuccessStyle(textField1, datePicker);
            throw new BookException("Veuillez renseigner le nom de l'auteur de l'Ouvrage!");
        }
        else if(datePicker.getValue()== null){
            setErrorStyle(datePicker);
            setSuccessStyle(textField1, textField2);
            throw new BookException("Veuillez renseigner la date d'edition de l'Ouvrage!");
        }
        else {

        }

    }


    public static void compareNewFormValueToOld(TextField title, TextField authorName, DatePicker dateedition,TextField newTitle, TextField newAuthorname, DatePicker newDateedition)
    throws BookException {
        if((title.getText().equals(newTitle.getText())) && (authorName.getText().equals(newAuthorname.getText())) && (dateedition.getValue().equals(newDateedition.getValue()))){
            setErrorStyle(newTitle,newAuthorname, newDateedition);
            throw new BookException("Aucun champs n'a été modifié! Modifiez une valeur d'abord.");
        }
        else{

        }
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

    public static void setSuccessStyle(TextField textField, DatePicker datePicker){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField.setStyle(successStyle);
        datePicker.setStyle(successStyle);
    }

    public static void setSuccessStyle(TextField textField1,TextField textField2){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField1.setStyle(successStyle);
        textField2.setStyle(successStyle);
    }

    public static void setErrorStyle(TextField textField1,TextField textField2,DatePicker datePicker){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 3;";

        textField1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField1).play();
        textField2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField2).play();
        datePicker.setStyle(errorStyle);
        new animatefx.animation.BounceIn(datePicker).play();
    }

    public static void setSuccessStyle(TextField textField1,TextField textField2,DatePicker datePicker){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField1.setStyle(successStyle);
        textField2.setStyle(successStyle);
        datePicker.setStyle(successStyle);
    }
}
