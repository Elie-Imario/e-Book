package com.ebookutil.ebook;

import com.ebookutil.entity.Livre;
import com.ebookutil.entity.Pret;
import com.ebookutil.exception.BookException.BookException;
import com.ebookutil.util.AlertMessage;
import com.ebookutil.util.BookAction;
import com.ebookutil.util.BookValidateForm;
import com.ebookutil.util.PretAction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BoEditLivreController implements Initializable {

    @FXML private Label TitleBoEditLivre;
    @FXML private TextField currentNumBook;
    @FXML private TextField titleBook;
    @FXML private TextField AuthorName;
    @FXML private DatePicker dateEdtion;
    @FXML private TextField Designation;

    @FXML private Button btnupdateBook;
    @FXML private Button btnCancel;
    @FXML private Label errorMsgOuvrage;

    @FXML private TextField currenttitleBook;
    @FXML private TextField currentAuthorName;
    @FXML private DatePicker currentdateEdtion;
    @FXML private TextField currentDesignation;

    private TableView<Livre> TabListLivre;
    private TableView<Pret> TabListPret;
    private VBox mainWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnCancel.setOnMouseClicked((ActionEvent)->{
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });

        btnupdateBook.setOnMouseClicked((ActionEvent)->{
            try {
                UPDATELivreAction();

                Stage stage = (Stage) btnupdateBook.getScene().getWindow();
                stage.close();

            }
            catch (BookException e){
                errorMsgOuvrage.setText(e.getMessage());
                errorMsgOuvrage.setContentDisplay(ContentDisplay.LEFT);
                new animatefx.animation.FadeIn(errorMsgOuvrage).play();
            }
        });
    }

    public void setDetailToUpdate(Livre selectedLivreForUPdate){
        TitleBoEditLivre.setText("MODIFICATION DE L'OUVRAGE N°"+selectedLivreForUPdate.getId_Ouvrage());
        titleBook.setText(selectedLivreForUPdate.getTitre_Ouvrage());
        AuthorName.setText(selectedLivreForUPdate.getNom_Auteur());
        LocalDate localDate = new Date(selectedLivreForUPdate.getDate_Edition().getTime()).toLocalDate();
        dateEdtion.setValue(localDate);
        Designation.setText(selectedLivreForUPdate.getDesignation());

        // current Lecteur Info Hide
        currentNumBook.setText(Integer.valueOf(selectedLivreForUPdate.getId_Ouvrage()).toString());
        currenttitleBook.setText(selectedLivreForUPdate.getTitre_Ouvrage());
        currentAuthorName.setText(selectedLivreForUPdate.getNom_Auteur());
        currentdateEdtion.setValue(localDate);
        currentDesignation.setText(selectedLivreForUPdate.getDesignation());
    }


    public void UPDATELivreAction()
    throws BookException{
        //getValue
        int _idOuvrage = Integer.parseInt(currentNumBook.getText());
        String _titleBook = titleBook.getText();
        String _AuthorName = AuthorName.getText();
        Date _dateEdition = Date.valueOf(dateEdtion.getValue());
        String _designation = Designation.getText();

        //setValue compare to Old Value
        BookValidateForm.compareNewFormValueToOld(currenttitleBook, currentAuthorName, currentdateEdtion, currentDesignation, titleBook, AuthorName, dateEdtion, Designation);

        //valide v n form
         BookValidateForm.validForm(titleBook, AuthorName, dateEdtion, Designation);
        //update
        BookAction.UpdateBook(_idOuvrage, _titleBook, _AuthorName, _dateEdition, _designation);
        //mise a jour table Livre
        BookAction.RefreshTableLivre(TabListLivre);

        //mise a jour table Pret
        PretAction.RefreshTablePret(TabListPret);


        //msg success
        AlertMessage.AlertSuccess(mainWindow, "L'Ouvrage a été mis à jour!");

    }

    public void setTable(TableView<Livre> table){
        this.TabListLivre = table;
    }
    public void setTablePret(TableView<Pret> tablePret) {this.TabListPret = tablePret;}
    public void setMainWindow(VBox vBox){
        this.mainWindow = vBox;
    }
}
