package com.ebookutil.ebook;

import com.ebookutil.exception.boUserException.boUserException;
import com.ebookutil.util.LoginValidateForm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    double mousePressedX, mousePressedY;

    @FXML private Button closeBtn;
    @FXML private Button signIn;
    @FXML private VBox Login;
    @FXML private TextField _username;
    @FXML private PasswordField _password;
    @FXML private Label errorMsg;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //close window
        closeBtn.setOnMouseClicked((ActionEvent) -> {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });

        //Get current window position
        Login.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            mousePressedX = mouseEvent.getX();
            mousePressedY = mouseEvent.getY();
        });

        //Drag window
        Login.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            double crrX = mouseEvent.getScreenX();
            double crrY = mouseEvent.getScreenY();
            Stage stage = (Stage) Login.getScene().getWindow();
            stage.setX(crrX - mousePressedX);
            stage.setY(crrY - mousePressedY);
        });

        //Login into app
        signIn.setOnMouseClicked((actionEvent)->{
            try {
                AccessToMainWIndow();
            }
            catch (boUserException e){
                errorMsg.setText(e.getMessage());
                errorMsg.setContentDisplay(ContentDisplay.LEFT);
                new animatefx.animation.FadeIn(errorMsg).play();
            }
        });
    }

    public void AccessToMainWIndow()
            throws boUserException {
        LoginValidateForm.validForm(_username, _password);
        showMainWindow();
    }

    public void showMainWindow(){
        Parent root;
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindowPanel.fxml"));
            root = loader.load();
            stage.setTitle("E-NDRANA|Dashboard");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("img/eBookIco.png")));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 1080, 676));
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();

            stage = (Stage) signIn.getScene().getWindow();
            stage.close();

        }
        catch (IOException e){e.printStackTrace();}
    }
}