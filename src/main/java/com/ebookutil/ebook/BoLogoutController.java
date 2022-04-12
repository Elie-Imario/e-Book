package com.ebookutil.ebook;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BoLogoutController implements Initializable {
    @FXML
    private Button btnConfirm;
    @FXML private Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnConfirm.setOnMouseClicked((ActionEvent)->{
            System.exit(0);
        });
        btnCancel.setOnMouseClicked((ActionEvent)->{
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });
    }
}
