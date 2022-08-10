package com.ebookutil.ebook;

import com.ebookutil.entity.Lecteur;
import com.ebookutil.entity.Livre;
import com.ebookutil.entity.Pret;
import com.ebookutil.exception.PretException.PretException;
import com.ebookutil.util.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoEditPretController implements Initializable {

    @FXML private Label TitleBoEditPret, warningMsg, errorMsgPret;
    @FXML private TextField _readerName, _titleBook, NbjourPret;
    @FXML private DatePicker dateDebPret, dateFinPret;
    @FXML private ComboBox etatPret;

    //Current Pret Controls Value
    @FXML private TextField currentNumPret, currentTitleBook, currentNbJourPret, currentAmende;
    @FXML private DatePicker currentDateDeb, currentDateFin;
    @FXML private ComboBox currentEtatPret;

    @FXML private Button btnConfirmEditPret, btnCancel;


    @FXML private TableView TablePret;
    @FXML private TableView TableLecteur;
    @FXML private TableView TableLivre;
    @FXML private Label totalPret, totalBookenPret;

    @FXML private VBox mainWindow;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] tab = {"Terminé", "En cours"};
        etatPret.getItems().addAll(tab);
        currentEtatPret.getItems().addAll(tab);

        //AutoCompletion TextField ReaderName
        ArrayList readerNameList = PretAction.set_readerNameSuggestion();
        TextFields.bindAutoCompletion(_readerName, readerNameList);

        //AutoCompletion TextField TitleBook
        ArrayList titleBookList = PretAction.set_titleBookSuggestion();
        TextFields.bindAutoCompletion(_titleBook, titleBookList);

        dateDebPret.setOnAction((ActionEvent)->{
            dateFinPret.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate localDate, boolean empty) {
                    super.updateItem(localDate, empty);
                    //Disable dates before current date
                    setDisable(empty || localDate.compareTo(dateDebPret.getValue()) < 0);
                }
            });

            try {
                Date datedeb = Date.valueOf(dateDebPret.getValue());

                if(dateFinPret.getValue()==null){
                    Date datefin = null;
                }
                else {
                    Date datefin = Date.valueOf(dateFinPret.getValue());
                    //Calcul Nb de jour pret
                    int nbJourPret = (int)((datefin.getTime()-datedeb.getTime())/(24*60*60*1000));
                    NbjourPret.setText(Integer.valueOf(nbJourPret).toString());
                    if(nbJourPret>7){
                        warningMsg.setContentDisplay(ContentDisplay.LEFT);
                        warningMsg.setText("Le nombre de jour de prêt est plus de 7jours, le Lecteur sera facturé de 5000 Fmg d'amende!");
                        new animatefx.animation.FadeIn(warningMsg).play();
                    }
                    else{
                        warningMsg.setContentDisplay(ContentDisplay.LEFT);
                        warningMsg.setText("Ce prêt n'est soumise à aucune amende actuellement.");
                        new animatefx.animation.FadeIn(warningMsg).play();
                    }
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }


        });

        dateFinPret.setOnAction((ActionEvent)->{
            dateDebPret.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate localDate, boolean empty) {
                    super.updateItem(localDate, empty);
                    //Disable dates after future date
                    setDisable(empty || localDate.compareTo(dateFinPret.getValue()) > 0);
                }
            });


            try {
                Date datefin = Date.valueOf(dateFinPret.getValue());

                if(dateDebPret.getValue()==null){
                    Date datedeb = null;
                }
                else{
                    Date datedeb = Date.valueOf(dateDebPret.getValue());

                    //Calcul Nb de jour pret
                    int nbJourPret = (int)((datefin.getTime()-datedeb.getTime())/(24*60*60*1000));
                    NbjourPret.setText(Integer.valueOf(nbJourPret).toString());
                    if(nbJourPret>7){
                        warningMsg.setContentDisplay(ContentDisplay.LEFT);
                        warningMsg.setText("Le nombre de jour de prêt est plus de 7jours, le Lecteur sera facturé de 5000 Fmg d'amende!");
                        new animatefx.animation.FadeIn(warningMsg).play();
                    }
                    else{

                        warningMsg.setContentDisplay(ContentDisplay.LEFT);
                        warningMsg.setText("Ce prêt n'est soumise à aucune amende actuellement.");
                        new animatefx.animation.FadeIn(warningMsg).play();
                    }
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        });


        btnCancel.setOnMouseClicked((ActionEvent)->{
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });

        btnConfirmEditPret.setOnMouseClicked((ActionEvent)->{
            int indexSelectedEtatPret = etatPret.getSelectionModel().getSelectedIndex();
            try {
                UPDATEPretAction(indexSelectedEtatPret);
                Stage stage = (Stage) btnConfirmEditPret.getScene().getWindow();
                stage.close();
            }
            catch (Exception e){
                errorMsgPret.setContentDisplay(ContentDisplay.LEFT);
                errorMsgPret.setText(e.getMessage());
                new animatefx.animation.FadeIn(errorMsgPret).play();
            }
        });
    }




    public void setBoEditPretValue(Pret SelectedPretForUpdate){
        TitleBoEditPret.setText("MODIFICATION DU PRET N°"+SelectedPretForUpdate.getId_Pret());
        String nomLecteur = PretAction.GetNomLecteur(SelectedPretForUpdate.getId_Lecteur());
        _readerName.setText(nomLecteur);
        String titreOuvrage = PretAction.GetTitreOuvrage(SelectedPretForUpdate.getId_Ouvrage());
        _titleBook.setText(titreOuvrage);
        dateDebPret.setValue(new Date(SelectedPretForUpdate.getDateDebPret().getTime()).toLocalDate());
        dateFinPret.setValue(new Date(SelectedPretForUpdate.getDateFinPret().getTime()).toLocalDate());

        dateDebPret.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);
                //Disable dates after future date
                setDisable(empty || localDate.compareTo(dateFinPret.getValue()) > 0);
            }
        });
        dateFinPret.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);
                //Disable dates before current date
                setDisable(empty || localDate.compareTo(dateDebPret.getValue()) < 0);
            }
        });

        NbjourPret.setText(Integer.valueOf(SelectedPretForUpdate.getNbJourPret()).toString());

        if(SelectedPretForUpdate.getAmende()==0){
            warningMsg.setContentDisplay(ContentDisplay.LEFT);
            warningMsg.setText("Ce prêt n'est soumise à aucune amende actuellement.");
        }
        else {
            warningMsg.setContentDisplay(ContentDisplay.LEFT);
            warningMsg.setText("Ce prêt est actuellement facturé de 5000 fmg d'amende.");
        }

        if(SelectedPretForUpdate.isEtat())
        {
            etatPret.getSelectionModel().select(1);
            currentEtatPret.getSelectionModel().select(1);
        }



        //CURRENT INFO PRET

        currentNumPret.setText(Integer.valueOf(SelectedPretForUpdate.getId_Pret()).toString());
        currentTitleBook.setText(titreOuvrage);
        currentDateDeb.setValue(new Date(SelectedPretForUpdate.getDateDebPret().getTime()).toLocalDate());
        currentDateFin.setValue(new Date(SelectedPretForUpdate.getDateFinPret().getTime()).toLocalDate());
        currentNbJourPret.setText(Integer.valueOf(SelectedPretForUpdate.getNbJourPret()).toString());
        currentAmende.setText(Integer.valueOf(SelectedPretForUpdate.getAmende()).toString());
    }

    public void UPDATEPretAction(int idSelectedEtatPret)
    throws PretException {

        //getNewValue
        String _ReaderName = _readerName.getText();
        String _TitleBook = _titleBook.getText();
        Date _DateDebPret = Date.valueOf(dateDebPret.getValue());
        Date _DateFinPret = Date.valueOf(dateFinPret.getValue());
        int _NbJourPret = Integer.valueOf(NbjourPret.getText());

        //Get CurrentValue
        int _currentIdOuvrage = PretAction.GetIdOuvrage(currentTitleBook.getText());
        int _currentNbJourPret = Integer.valueOf(currentNbJourPret.getText());
        int _currentAmendePret = Integer.valueOf(currentAmende.getText());

        int IdPret = Integer.valueOf(currentNumPret.getText());
        int IdLecteur = PretAction.GetIdLecteur(_ReaderName);
        int IdOuvrage = PretAction.GetIdOuvrage(_TitleBook);



        //setValue compare to Old Value
        PretValidateForm.compareNewFormValueToOld(_titleBook, dateDebPret, dateFinPret, etatPret,
                                                  currentTitleBook, currentDateDeb, currentDateFin, currentEtatPret);


        //Valid Form
        PretValidateForm.validForm(_titleBook, dateDebPret, dateFinPret);

        PretAction.UpdatePret(idSelectedEtatPret, IdPret, IdLecteur, IdOuvrage, _DateDebPret, _DateFinPret, _NbJourPret,
                                _currentIdOuvrage, _currentNbJourPret, _currentAmendePret);

        AlertMessage.AlertSuccess(mainWindow, "Le prêt a été modifié avec succès!");
        LecteurAction.RefreshTableLecteur(TableLecteur);
        BookAction.RefreshTableLivre(TableLivre);
        PretAction.RefreshTablePret(TablePret);


        totalPret.setText(Integer.valueOf(PretAction.GetTotalPret()).toString());
        totalBookenPret.setText(Integer.valueOf(BookAction.GetTotalLivrePret()).toString());

    }


    public void setTable(TableView<Pret> table){
        this.TablePret = table;
    }
    public void setTableLecteur(TableView< Lecteur > table){
        this.TableLecteur = table;
    }
    public void setTableLivre(TableView< Livre > table){
        this.TableLivre = table;
    }
    public void setMainWindow(VBox vBox){
        this.mainWindow = vBox;
    }
    public void setTotalBookLabel(Label label) {this.totalBookenPret = label;}
    public void setTotalPretLabel(Label label){
        this.totalPret = label;
    }
}
