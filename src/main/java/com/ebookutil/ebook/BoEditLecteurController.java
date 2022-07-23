package com.ebookutil.ebook;

import com.ebookutil.entity.Lecteur;
import com.ebookutil.entity.Pret;
import com.ebookutil.exception.LecteurException.LecteurException;
import com.ebookutil.util.AlertMessage;
import com.ebookutil.util.LecteurAction;
import com.ebookutil.util.LecteurValidateForm;
import com.ebookutil.util.PretAction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BoEditLecteurController implements Initializable {

    @FXML private Label TitleBoEditLeteur;
    @FXML private TextField readerName;
    @FXML private TextField emailReader;
    @FXML private TextField fonctionReader;
    @FXML private TextField mobileReader;

    @FXML private TextField currentIdLecteur;
    @FXML private TextField currentNom_Lecteur;
    @FXML private TextField currentEmail_Lecteur;
    @FXML private TextField currentFonction_Lecteur;
    @FXML private TextField currentMobile_Lecteur;

    @FXML private Button btnUpdateReader;
    @FXML private Button btnCancel;
    @FXML private Label errorMsg;

    private TableView<Lecteur> TabListLecteur;
    private TableView<Pret> TabListPret;
    private VBox mainWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnCancel.setOnMouseClicked((ActionEvent)->{
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });
        btnUpdateReader.setOnMouseClicked((ActionEvent)->{
            try {
                UPDATELecteurAction();

                Stage stage = (Stage) btnUpdateReader.getScene().getWindow();
                stage.close();

            }
            catch (LecteurException e){
                errorMsg.setText(e.getMessage());
                errorMsg.setContentDisplay(ContentDisplay.LEFT);
                new animatefx.animation.FadeIn(errorMsg).play();
            }
        });
    }

    public void setDetailToUpdate(Lecteur selectedlecteur){
        TitleBoEditLeteur.setText("MODIFICATION DU LECTEUR N°"+ selectedlecteur.getId());
        readerName.setText(selectedlecteur.getNom_Lecteur());
        fonctionReader.setText(selectedlecteur.getFonction());
        emailReader.setText(selectedlecteur.getEmail());
        mobileReader.setText(selectedlecteur.getMobile());

        // current Lecteur Info Hide
        currentIdLecteur.setText(Integer.valueOf(selectedlecteur.getId()).toString());
        currentNom_Lecteur.setText(selectedlecteur.getNom_Lecteur());
        currentEmail_Lecteur.setText(selectedlecteur.getEmail());
        currentFonction_Lecteur.setText(selectedlecteur.getFonction());
        currentMobile_Lecteur.setText(selectedlecteur.getMobile());
    }

    public void UPDATELecteurAction()
    throws LecteurException {
        int _idReader = Integer.parseInt(currentIdLecteur.getText());
        String readerNameVal = readerName.getText();
        String emailReaderVal = emailReader.getText();
        String fonctionReaderVal = fonctionReader.getText();
        String mobileReaderVal = mobileReader.getText();

        LecteurValidateForm.compareNewFormValueToOld(currentNom_Lecteur, currentEmail_Lecteur, currentFonction_Lecteur, currentMobile_Lecteur, readerName, emailReader, fonctionReader, mobileReader);
        LecteurValidateForm.ValidForm( readerName,emailReader, fonctionReader, mobileReader);

        LecteurAction.updateLecteur(_idReader, readerNameVal, emailReaderVal, fonctionReaderVal, mobileReaderVal);

        LecteurAction.RefreshTableLecteur(TabListLecteur);
        PretAction.RefreshTablePret(TabListPret);

        AlertMessage.AlertSuccess(mainWindow, "Les info du lecteur ont été modifiées avec succès!");

    }


    public void setTable(TableView<Lecteur> table){
        this.TabListLecteur = table;
    }
    public void setTablePret(TableView<Pret> tablePret) {this.TabListPret = tablePret;}
    public void setMainWindow(VBox vBox){
        this.mainWindow = vBox;
    }
}
