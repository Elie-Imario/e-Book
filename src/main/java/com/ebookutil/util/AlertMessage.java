package com.ebookutil.util;

import com.ebookutil.ebook.BoAlertController;
import com.ebookutil.ebook.mainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertMessage {

    public static void AlertSuccess(VBox ModalOwner, String msg){
        try{
            Stage stage =new Stage();
            FXMLLoader AlertFXML = new FXMLLoader(mainWindowController.class.getResource("BoAlert.fxml"));
            Scene scene = new Scene(AlertFXML.load(), 350, 100);

            BoAlertController boAlertController = AlertFXML.getController();
            boAlertController.SuccessAlert(msg, "img/success2.gif");
            new Stage();
            Stage Primary;
            Primary = (Stage) ModalOwner.getScene().getWindow();
            stage.initStyle(StageStyle.UNIFIED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setX(Primary.getX() + (Primary.getWidth()/2-100));
            stage.setY(Primary.getY() + 95);
            stage.setTitle("Alert-Sucess");
            stage.getIcons().add(new Image(mainWindowController.class.getResourceAsStream("img/IcoSuccess.jpg")));
            stage.initOwner(Primary);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void WarningAlert(VBox ModalOwner){
        try{
            Stage stage =new Stage();
            FXMLLoader AlertFXML = new FXMLLoader(mainWindowController.class.getResource("BoAlert.fxml"));
            Scene scene = new Scene(AlertFXML.load(), 350, 100);

            BoAlertController boAlertController = AlertFXML.getController();
            boAlertController.WarningAlert("Veuillez selectionner au moins une ligne!!!", "img/Warning.gif");
            new Stage();
            Stage Primary;
            Primary = (Stage) ModalOwner.getScene().getWindow();
            stage.initStyle(StageStyle.UNIFIED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setX(Primary.getX() + (Primary.getWidth()/2-100));
            stage.setY(Primary.getY() + 95);
            stage.setTitle("Alert-Warning");
            stage.getIcons().add(new Image(mainWindowController.class.getResourceAsStream("img/Warning.png")));
            stage.initOwner(Primary);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void ErrorAlert(VBox ModalOwner){
        try{
            Stage stage =new Stage();
            FXMLLoader AlertFXML = new FXMLLoader(mainWindowController.class.getResource("BoAlert.fxml"));
            Scene scene = new Scene(AlertFXML.load(), 350, 100);

            BoAlertController boAlertController = AlertFXML.getController();
            boAlertController.ErrorAlert("Ce prêt est déjà terminé, il est impossible de le modifier!", "img/error.gif");
            new Stage();
            Stage Primary;
            Primary = (Stage) ModalOwner.getScene().getWindow();
            stage.initStyle(StageStyle.UNIFIED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setX(Primary.getX() + (Primary.getWidth()/2-100));
            stage.setY(Primary.getY() + 95);
            stage.setTitle("Alert-Error");
            stage.getIcons().add(new Image(mainWindowController.class.getResourceAsStream("img/error-icon-transparent-5.png")));
            stage.initOwner(Primary);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
