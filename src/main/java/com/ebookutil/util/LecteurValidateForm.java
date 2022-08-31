package com.ebookutil.util;

import com.ebookutil.exception.LecteurException.LecteurException;
import javafx.scene.control.TextField;

public class LecteurValidateForm {

    public static void ValidForm(TextField nom, TextField email, TextField fonction, TextField mobile)
    throws LecteurException{
        if( (nom.getText()).isBlank() && (fonction.getText()).isBlank() && (mobile.getText()).isBlank() ){
            setErrorStyle(nom,fonction,email, mobile);
            throw new LecteurException("Ces champs sont obligatoires!");
        }
        else if((nom.getText()).isBlank()){
            setErrorStyle(nom);
            setSuccessStyle(fonction,email, mobile);
            throw new LecteurException("Veuillez renseigner le nom du Lecteur!");
        }
        else if((fonction.getText()).isBlank()){
            setErrorStyle(fonction);
            setSuccessStyle(nom,email, mobile);
            throw new LecteurException("Veuillez renseigner la fonction exercée par le Lecteur!");
        }
        else if((mobile.getText()).isBlank()){
            setErrorStyle(mobile);
            setSuccessStyle(nom,email, fonction);
            throw new LecteurException("Veuillez renseigner le mobile du Lecteur!");
        }
        else if((mobile.getText()).length()!=10){
            setErrorStyle(mobile);
            setSuccessStyle(nom,email, fonction);
            throw new LecteurException("Le numero du Lecteur doit être de 10 caractères!");
        }
        else{
            if(mobile.getText()!=null){
                try {
                    Integer.parseInt(mobile.getText());
                }
                catch (NumberFormatException e){
                    setErrorStyle(mobile);
                    setSuccessStyle(nom,email, fonction);

                    throw new LecteurException("Le numero du Lecteur doit être de type numérique!");
                }
            }
        }
    }


    public static void compareNewFormValueToOld(TextField nom, TextField email, TextField fonction, TextField mobile,TextField newNom, TextField newEmail, TextField newFonction, TextField newMobile)
    throws LecteurException {
        if((nom.getText().equals(newNom.getText())) && (email.getText().equals(newEmail.getText())) && (fonction.getText().equals(newFonction.getText())) && (mobile.getText().equals(newMobile.getText()))){
            setErrorStyle(newNom,newFonction,newEmail, newMobile);
            throw new LecteurException("Aucun champs n'a été modifié! Modifiez une valeur d'abord.");
        }
        else{

        }
    }


    public static void setErrorStyle(TextField textField){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";

        textField.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField).play();

    }

    public static void setSuccessStyle(TextField textField1,TextField textField2,TextField textField3){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField1.setStyle(successStyle);
        /*textField2.setStyle(successStyle);*/
        textField3.setStyle(successStyle);
    }
    public static void setSuccessStyle(TextField textField1,TextField textField2,TextField textField3, TextField textField4){
        String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 0; -fx-border-radius: 3;");

        textField1.setStyle(successStyle);
        /*textField2.setStyle(successStyle);*/
        textField3.setStyle(successStyle);
        textField4.setStyle(successStyle);
    }

    public static void setErrorStyle(TextField textField1,TextField textField2,TextField textField3,TextField textField4){
        String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 3;";

        textField1.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField1).play();
        textField2.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField2).play();
        /*textField3.setStyle(errorStyle);
        new animatefx.animation.Pulse(textField3).play();*/
        textField4.setStyle(errorStyle);
        new animatefx.animation.BounceIn(textField4).play();
    }
}
