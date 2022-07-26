package com.ebookutil.ebook;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class BoConfirmBoxAlertController implements Initializable {
    @FXML protected javafx.scene.control.Label alertMsg;
    @FXML protected Button btnConfirm_OK, btn_ANNULER;
    @FXML protected VBox BoAlert;
    @FXML protected ImageView icoAlert;
    protected Stage stage;
    protected String filename;
    protected String alertConfirmStyle = """
             -fx-background-color: #f5f5f5""";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_ANNULER.setOnMouseClicked((ActionEvent)->{
            Stage stage = (Stage) btn_ANNULER.getScene().getWindow();
            stage.close();
        });
        btnConfirm_OK.setOnMouseClicked((ActionEvent)->{
            try {
                OpenPDFFileGenerated(filename);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            Stage stage = (Stage) btnConfirm_OK.getScene().getWindow();
            stage.close();
        });
    }

    public void WarningAlert(String alertmsg, String icoalerturl){
        BoAlert.setStyle(alertConfirmStyle);
        alertMsg.setText(alertmsg);
        alertMsg.setStyle("-fx-text-fill:BLACK;"+
                "-fx-font-family: \"sans-serif\";");
        Image image = new Image(this.getClass().getResourceAsStream(icoalerturl));
        icoAlert.setImage(image);
    }

    public void OpenPDFFileGenerated(String FileName) throws IOException{
        File f;
        f = new File(System.getProperty("user.home") + "\\Downloads\\" + FileName + ".pdf");
        Desktop.getDesktop().open(f);
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
    public void setFileName(String fileName) {this.filename = fileName;}
}
