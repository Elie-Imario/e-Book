package com.ebookutil.ebook;

import com.ebookutil.entity.Lecteur;
import com.ebookutil.entity.Livre;
import com.ebookutil.entity.Pret;
import com.ebookutil.exception.BookException.BookException;
import com.ebookutil.exception.LecteurException.LecteurException;
import com.ebookutil.exception.boUserException.boUserException;
import com.ebookutil.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class mainWindowController implements Initializable {

    @FXML
    private VBox mainWindow;

    //Menu Button
    @FXML private Button btnLogOut, btnPret, btnAddBook, btnListBook, annuaireSearch, monCompte, btnAddReaderView, btnListReaderVIew, chartBtn;

    //E-NDRANA Section View
    @FXML private VBox pretView, addOuvrageView, ListOuvrageView, annuaireView, monCompteView, createReaderView, ListReaderView, chartView;

    // ADD NEW READER
    @FXML private TextField readerName, emailReader, fonctionReader, mobileReader;
    @FXML private Button btnAddReader, btnCancelAddReader;
    @FXML private Label errorMsg;


    //SEARCH LECTEUR
    @FXML private TextField readerNameSearch, fonctionReaderSearch, mobileReaderSearch;
    @FXML private Button seachLecteurBtn, reinitialiserSearch;


    //TabListlecteur ContextMenu
    @FXML private MenuItem detailLecteur, updateLecteur, deleteLecteur;


    // LIST READER
    @FXML private TableView<Lecteur> TabListLecteur;
    @FXML private TableColumn<Lecteur, Integer> numLecteur;
    @FXML private TableColumn<Lecteur, String> nomLecteur;
    @FXML private TableColumn<Lecteur, String> fonctionLecteur;
    @FXML private TableColumn<Lecteur, String> mobileLecteur;
    @FXML private Label totalLecteur;


    // ADD NEW BOOK
    @FXML private TextField titleBook, AuthorName;
    @FXML private DatePicker dateEdtion;
    @FXML private Button btnADDBook, btnCancelAddBook;
    @FXML private Label errorMsgOuvrage;

    //LIST BOOK
    @FXML private TableView<Livre> TabListBook;
    @FXML private TableColumn<Livre, Integer> num_Ouvrage;
    @FXML private TableColumn<Livre, String> titre_Ouvrage;
    @FXML private TableColumn<Livre, String> auteur_Ouvrage;
    @FXML private TableColumn<Livre, Date> date_Edition;
    @FXML private TableColumn<Livre, Boolean> disponible;
    @FXML private Label totalLivrePret;

    //TabListLivre ContextMenu
    @FXML private MenuItem updateBook, deleteBook;

    //SEARCH OUVRAGE
    @FXML private TextField titreOuvrageSearch, nomAuteurSearch;
    @FXML private Button OuvragebtnSearch, reinitialiserOuvrageSearch;


    //Pret
    @FXML private Button BoEffectuerpretBtn;

    //TabListPret ContextMenu
    @FXML private MenuItem updatePret, deletePret;


    //LIST PRET
    @FXML private TableView<Pret> TabListPret;
    @FXML private TableColumn<Pret, Integer> numPret;
    @FXML private TableColumn<Pret, String> titreOuvrageEnPret;
    @FXML private TableColumn<Pret, String> nomLecteurPret;
    @FXML private TableColumn<Pret, Date> dateDebPret;
    @FXML private TableColumn<Pret, Date> dateFinPret;
    @FXML private TableColumn<Pret, Integer> nbJourPret;
    @FXML private TableColumn<Pret, Boolean> EtatPret;
    @FXML private TableColumn<Pret, Integer> AmendePret;
    @FXML private Label totalPret;
    //SEARCH Pret
    @FXML private Button btnSearchPret, reinitialiserSearchPret;


    //Chart Pret
    @FXML private PieChart PretPieChart;


    //CHANGE E-NDRANA Password
    @FXML private PasswordField _currentPswrd, _newPswrd, _confirmPswrd;
    @FXML private Button BtnupdatePswrd, btnCancelUpdatePswrd;
    @FXML private Label errorMsgChangePswrd;

    //BtnExport result to pdfFile
    @FXML private Button btnExportListUser, btnExportListBook, btnExportListPret;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GETListPretAction();
        GETListBookAction();
        GETListLecteurAction();
        GETStatPret();



        btnLogOut.setOnMouseClicked((ActionEvent)->{
            LogOUt();
        });

        btnPret.setOnMouseClicked((ActionEvent)->{

            BtnStyleCurrentView.pretViewBtnStyle(btnPret, btnAddBook, btnListBook, annuaireSearch, monCompte,
                    btnAddReaderView, btnListReaderVIew, chartBtn);

            pretView.toFront();
        });

        btnAddBook.setOnMouseClicked((ActionEvent)->{

            BtnStyleCurrentView.addBookViewBtnStyle(btnPret, btnAddBook, btnListBook, annuaireSearch, monCompte,
                    btnAddReaderView, btnListReaderVIew, chartBtn);

            addOuvrageView.toFront();
        });

        btnListBook.setOnMouseClicked((ActionEvent)->{

            BtnStyleCurrentView.ListBookViewBtnStyle(btnPret, btnAddBook, btnListBook, annuaireSearch, monCompte,
                    btnAddReaderView, btnListReaderVIew, chartBtn);

            ListOuvrageView.toFront();
        });

        annuaireSearch.setOnMouseClicked((ActionEvent)->{

            BtnStyleCurrentView.annuaireSearchViewBtnStyle(btnPret, btnAddBook, btnListBook, annuaireSearch, monCompte,
                    btnAddReaderView, btnListReaderVIew, chartBtn);

            annuaireView.toFront();
        });

        monCompte.setOnMouseClicked((ActionEvent)->{

            BtnStyleCurrentView.monCompteViewBtnStyle(btnPret, btnAddBook, btnListBook, annuaireSearch, monCompte,
                    btnAddReaderView, btnListReaderVIew, chartBtn);

            monCompteView.toFront();
        });

        btnAddReaderView.setOnMouseClicked((ActionEvent)->{
            BtnStyleCurrentView.addReaderViewBtnStyle(btnPret, btnAddBook, btnListBook, annuaireSearch, monCompte,
                    btnAddReaderView, btnListReaderVIew, chartBtn);

            createReaderView.toFront();
        });

        btnListReaderVIew.setOnMouseClicked((ActionEvent)->{

            BtnStyleCurrentView.listReaderViewBtnStyle(btnPret, btnAddBook, btnListBook, annuaireSearch, monCompte,
                    btnAddReaderView, btnListReaderVIew, chartBtn);

            ListReaderView.toFront();

        });

        chartBtn.setOnMouseClicked((ActionEvent)->{

            BtnStyleCurrentView.ChartViewBtnStyle(btnPret, btnAddBook, btnListBook, annuaireSearch, monCompte,
                    btnAddReaderView, btnListReaderVIew, chartBtn);

            chartView.toFront();
            GETStatPret();

        });


        /***************
         ********** C-R-U-D ******
         * ******** LECTEUR ******
         * ************
         */

        btnAddReader.setOnMouseClicked((ActionEvent)->{
            try {
                ADDNewLecteurAction();
            } catch (LecteurException e)
            {
                errorMsg.setText(e.getMessage());
                errorMsg.setContentDisplay(ContentDisplay.LEFT);
                new animatefx.animation.FadeIn(errorMsg).play();
            }
        });

        btnCancelAddReader.setOnMouseClicked((ActionEvent)->{
            FormAction.clearFormGroupLecteur(readerName, emailReader, fonctionReader, mobileReader, errorMsg);
            LecteurValidateForm.setSuccessStyle(readerName, emailReader, fonctionReader, mobileReader);
        });


        updateLecteur.setOnAction((ActionEvent)->{
            Lecteur selectedLecteurForUpdate = TabListLecteur.getSelectionModel().getSelectedItem();
            if(selectedLecteurForUpdate == null){
                AlertMessage.WarningAlert(mainWindow);
            }
            else{
                initUpdateLecteur(selectedLecteurForUpdate);
            }
        });



        deleteLecteur.setOnAction((ActionEvent)->{
            Lecteur selectedLecteurForDelete = TabListLecteur.getSelectionModel().getSelectedItem();
            if(selectedLecteurForDelete == null){
                AlertMessage.WarningAlert(mainWindow);
            }
            else{
                deleteLecteurAction(selectedLecteurForDelete);
            }

        });

        detailLecteur.setOnAction((ActionEvent)->{
            Lecteur selectedLecteur = TabListLecteur.getSelectionModel().getSelectedItem();
            if(selectedLecteur == null){
                AlertMessage.WarningAlert(mainWindow);
            }
            else{
                showDetailLecteur(selectedLecteur);
            }
        });

        seachLecteurBtn.setOnMouseClicked((ActionEvent)->{
            LecteurAction.resultLecteurSearch(TabListLecteur ,numLecteur, nomLecteur, fonctionLecteur, mobileLecteur,
                    readerNameSearch, fonctionReaderSearch, mobileReaderSearch);
        });

        reinitialiserSearch.setOnMouseClicked((ActionEvent)->{
            LecteurAction.resetToNormal(TabListLecteur, readerNameSearch, fonctionReaderSearch, mobileReaderSearch);
            GETListLecteurAction();
        });

        btnExportListUser.setOnMouseClicked((ActionEvent)->{
            try{
                LecteurAction.Export_ListLecteur_toPDF(mainWindow);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        });

        totalLecteur.setText(Integer.valueOf(LecteurAction.GetTotalLecteur()).toString());



        /***************
         ********** C-R-U-D ******
         * ******** OUVRAGE ******
         * ************
         */

        btnADDBook.setOnMouseClicked((ActionEvent)->{
            try {
                ADDNewBookAction();
            }
            catch (BookException e){
                errorMsgOuvrage.setText(e.getMessage());
                errorMsgOuvrage.setContentDisplay(ContentDisplay.LEFT);
                new animatefx.animation.FadeIn(errorMsgOuvrage).play();
            }
        });

        btnCancelAddBook.setOnMouseClicked((ActionEvent)->{
            FormAction.clearFormGroupBook(titleBook, AuthorName, dateEdtion, errorMsgOuvrage);
            BookValidateForm.setSuccessStyle(titleBook, AuthorName, dateEdtion);
        });

        updateBook.setOnAction((ActionEvent)->{
            Livre selectedLivreForUpdate = TabListBook.getSelectionModel().getSelectedItem();
            if(selectedLivreForUpdate == null){
                AlertMessage.WarningAlert(mainWindow);
            }
            else{
                initUpdateLivre(selectedLivreForUpdate);
            }
        });

        deleteBook.setOnAction((ActionEvent)->{
            Livre selectedLivreForDelete = TabListBook.getSelectionModel().getSelectedItem();
            if(selectedLivreForDelete == null){
                AlertMessage.WarningAlert(mainWindow);
            }
            else {
                deleteLivreAction(selectedLivreForDelete);
            }
        });
        OuvragebtnSearch.setOnMouseClicked((ActionEvent)->{
            BookAction.resultOuvrageSearch(TabListBook ,num_Ouvrage, titre_Ouvrage, auteur_Ouvrage, date_Edition, disponible,
                    titreOuvrageSearch, nomAuteurSearch);
        });

        reinitialiserOuvrageSearch.setOnMouseClicked((ActionEvent)->{
            BookAction.resetToNormal(TabListBook, titreOuvrageSearch, nomAuteurSearch);
            GETListBookAction();
        });

        btnExportListBook.setOnMouseClicked((ActionEvent)->{
            try{
                BookAction.Export_ListBook_toPDF(mainWindow);

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        });

        totalLivrePret.setText(Integer.valueOf(BookAction.GetTotalLivrePret()).toString());




            /***************
            ********** C-R-U-D ******
            ********* PRET ******
            ****
            **/

        BoEffectuerpretBtn.setOnMouseClicked((ActionEvent)->{
            showBoEffectuerPret();
        });

        updatePret.setOnAction((ActionEvent)->{
            Pret PretSelected = TabListPret.getSelectionModel().getSelectedItem();
            if(PretSelected == null){
                AlertMessage.WarningAlert(mainWindow);
            }
            else {
                if(PretSelected.isEtat()){
                    initUpdatePret(PretSelected);
                }
                else{
                    AlertMessage.ErrorAlert(mainWindow);
                }
            }
        });

        deletePret.setOnAction((ActionEvent)->{
            Pret PretSelected = TabListPret.getSelectionModel().getSelectedItem();
            if(PretSelected == null){
                AlertMessage.WarningAlert(mainWindow);
            }
            else{
                deletePretAction(PretSelected);
            }
        });


        reinitialiserSearchPret.setOnMouseClicked((ActionEvent)->{

            GETListPretAction();
        });

        btnExportListPret.setOnMouseClicked((ActionEvent)->{
            try{
                PretAction.Export_ListPret_toPDF(mainWindow);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        });

        totalPret.setText(Integer.valueOf(PretAction.GetTotalPret()).toString());


        /***************
         ********** UPDATE - PASSWORD ******
         ****************
         ****
         **/

        BtnupdatePswrd.setOnMouseClicked((ActionEvent)->{
            try {
                UpdatePassword(_currentPswrd, _newPswrd, _confirmPswrd);
            }
            catch (boUserException e){
                errorMsgChangePswrd.setContentDisplay(ContentDisplay.LEFT);
                errorMsgChangePswrd.setText(e.getMessage());

                new animatefx.animation.FadeIn(errorMsgChangePswrd).play();
            }
        });

        btnCancelUpdatePswrd.setOnMouseClicked((ActionEvent)->{
            boUserAction.resetFormToNormal(_currentPswrd, _newPswrd, _confirmPswrd, errorMsgChangePswrd);
        });

    }









    /**
     * LECTEUR
     */
    public void ADDNewLecteurAction()
    throws LecteurException {
        String readerNameVal = readerName.getText();
        String emailReaderVal = emailReader.getText();
        String fonctionReaderVal = fonctionReader.getText();
        String mobileReaderVal = mobileReader.getText();

        LecteurValidateForm.ValidForm(readerName,emailReader, fonctionReader, mobileReader);

        LecteurAction.AddLecteur(readerNameVal, emailReaderVal, fonctionReaderVal, mobileReaderVal);
        AlertMessage.AlertSuccess(mainWindow, "Le nouveau lecteur a été ajouté avec succès!");
        GETListLecteurAction();//refresh TabListLecteur
        FormAction.clearFormGroupLecteur(readerName, emailReader, fonctionReader, mobileReader, errorMsg);//clearFormulaireLecteur
        LecteurValidateForm.setSuccessStyle(readerName, emailReader, fonctionReader, mobileReader);
    }

    public void GETListLecteurAction(){
        LecteurAction.showListLecteur(TabListLecteur ,numLecteur, nomLecteur, fonctionLecteur, mobileLecteur);
    }

    public void initUpdateLecteur(Lecteur selectedLecteurForUpdate){
        try {
            Stage stage = new Stage();
            FXMLLoader BoEditLecteurFXML = new FXMLLoader(mainWindowController.class.getResource("BoEditLecteur.fxml"));
            Scene scene = new Scene(BoEditLecteurFXML.load(), 449, 443);

            BoEditLecteurController boEditLecteurController = BoEditLecteurFXML.getController();
            boEditLecteurController.setDetailToUpdate(selectedLecteurForUpdate);
            boEditLecteurController.setTable(TabListLecteur);
            boEditLecteurController.setMainWindow(mainWindow);
            boEditLecteurController.setTablePret(TabListPret);

            Stage Primary = new Stage();
            Primary = (Stage) mainWindow.getScene().getWindow();
            stage.initStyle(StageStyle.UNIFIED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("img/edit.png")));
            stage.setX(Primary.getX() + (Primary.getWidth()/2-150));
            stage.setY(Primary.getY() + 95);
            stage.initOwner(Primary);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteLecteurAction(Lecteur selectedLecteurForDelete){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Supprimer le Lecteur LEC-"+selectedLecteurForDelete.getId());
        confirmation.setHeaderText("");
        confirmation.setTitle("Alert-Confirmation");
        confirmation.initStyle(StageStyle.UTILITY);
        Stage Primary = new Stage();
        Primary = (Stage) mainWindow.getScene().getWindow();
        confirmation.setWidth( 350);confirmation.setHeight(100);
        confirmation.setX(Primary.getX() + (Primary.getWidth()/2-100));confirmation.setY(Primary.getY() + (Primary.getHeight()/2));
        confirmation.initOwner(Primary);


        Optional<ButtonType> buttonType = confirmation.showAndWait();

        if(buttonType.get() == ButtonType.OK){
            LecteurAction.deletelecteurItem(selectedLecteurForDelete.getId());

            TabListLecteur.getItems().remove(selectedLecteurForDelete);

            totalLecteur.setText(Integer.valueOf(LecteurAction.GetTotalLecteur()).toString());
        }
    }

    public void showDetailLecteur(Lecteur selectedLecteur){
        try {
            Stage stage = new Stage();
            FXMLLoader BoDetailLecteurFXML = new FXMLLoader(mainWindowController.class.getResource("BoDetailLecteur.fxml"));
            Scene scene = new Scene(BoDetailLecteurFXML.load(), 535, 464);

            BoDetailLecteurController boDetailLecteurController = BoDetailLecteurFXML.getController();
            boDetailLecteurController.setDetail(selectedLecteur);

            Stage Primary = new Stage();
            Primary = (Stage) mainWindow.getScene().getWindow();
            stage.initStyle(StageStyle.UNIFIED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("DETAILS DU LECTEUR");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("img/Inspector.png")));
            stage.setX(Primary.getX() + (Primary.getWidth()/2-150));
            stage.setY(Primary.getY() + 95);
            stage.initOwner(Primary);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }




    /**
     * OUVRAGE
     */

    public void ADDNewBookAction()
    throws BookException{
        String titleBookText = titleBook.getText();
        String authorNameText = AuthorName.getText();

        BookValidateForm.validForm(titleBook,AuthorName, dateEdtion);

        Date getDateEdition = Date.valueOf(dateEdtion.getValue());
        BookAction.ADDBOOK(titleBookText, authorNameText, getDateEdition);
        AlertMessage.AlertSuccess(mainWindow, "Un nouveau ouvrage a été ajouté sur E-NDRANA");
        GETListBookAction();
        FormAction.clearFormGroupBook(titleBook, AuthorName, dateEdtion, errorMsgOuvrage);
        BookValidateForm.setSuccessStyle(titleBook, AuthorName, dateEdtion);
    }

    public void GETListBookAction(){
        BookAction.showListBook(TabListBook, num_Ouvrage, titre_Ouvrage, auteur_Ouvrage, date_Edition, disponible);
    }

    public void initUpdateLivre(Livre selectedLivreForUpdate){
        try {
            Stage stage = new Stage();
            FXMLLoader BoEditLivreFXML = new FXMLLoader(mainWindowController.class.getResource("BoEditLivre.fxml"));
            Scene scene = new Scene(BoEditLivreFXML.load(), 449, 443);

            BoEditLivreController boEditLivreController = BoEditLivreFXML.getController();
            boEditLivreController.setDetailToUpdate(selectedLivreForUpdate);
            boEditLivreController.setTable(TabListBook);
            boEditLivreController.setMainWindow(mainWindow);
            boEditLivreController.setTablePret(TabListPret);

            Stage Primary = new Stage();
            Primary = (Stage) mainWindow.getScene().getWindow();
            stage.initStyle(StageStyle.UNIFIED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("img/edit.png")));

            stage.setX(Primary.getX() + (Primary.getWidth()/2-150));
            stage.setY(Primary.getY() + 95);
            stage.initOwner(Primary);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteLivreAction(Livre selectedLivreForDelete){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Supprimer l'Ouvarge L-"+selectedLivreForDelete.getId_Ouvrage());
        confirmation.setHeaderText("");
        confirmation.setTitle("Alert-Confirmation");
        confirmation.initStyle(StageStyle.UTILITY);
        Stage Primary = new Stage();
        Primary = (Stage) mainWindow.getScene().getWindow();
        confirmation.setWidth( 350);confirmation.setHeight(100);
        confirmation.setX(Primary.getX() + (Primary.getWidth()/2-100));confirmation.setY(Primary.getY() + (Primary.getHeight()/2));
        confirmation.initOwner(Primary);


        Optional<ButtonType> buttonType = confirmation.showAndWait();

        if(buttonType.get() == ButtonType.OK){
            BookAction.DeleteLivre(selectedLivreForDelete.getId_Ouvrage());

            TabListBook.getItems().remove(selectedLivreForDelete);

            totalLivrePret.setText(Integer.valueOf(BookAction.GetTotalLivrePret()).toString());

        }
    }


    /**
     * PRET
     */

    public void showBoEffectuerPret(){
        try {
            Stage stage = new Stage();
            FXMLLoader BoEffectuerPretFXML = new FXMLLoader(mainWindowController.class.getResource("BoEffectuerPret.fxml"));
            Scene scene = new Scene(BoEffectuerPretFXML.load(), 449, 443);


            BoEffectuerPretController boEffectuerPretController = BoEffectuerPretFXML.getController();
            boEffectuerPretController.setMainWindow(mainWindow);
            boEffectuerPretController.setTable(TabListPret);
            boEffectuerPretController.setTableLecteur(TabListLecteur);
            boEffectuerPretController.setTableLivre(TabListBook);
            boEffectuerPretController.setTotalPretLabel(totalPret);

            Stage Primary = new Stage();
            Primary = (Stage) mainWindow.getScene().getWindow();
            stage.initStyle(StageStyle.UNIFIED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("img/plus_icon.jpg")));

            stage.setX(Primary.getX() + (Primary.getWidth()/2-150));
            stage.setY(Primary.getY() + 95);
            stage.initOwner(Primary);
            stage.setTitle("Prêt|Effectuer un prêt");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void GETListPretAction(){
        PretAction.ShowListPret(TabListPret, numPret, titreOuvrageEnPret, nomLecteurPret, dateDebPret, dateFinPret, nbJourPret, EtatPret, AmendePret);
    }


    public void initUpdatePret(Pret PretSelectedForUpdate){
        try {
            Stage stage = new Stage();
            FXMLLoader BoEditPretFXML = new FXMLLoader(mainWindowController.class.getResource("BoEditPret.fxml"));
            Scene scene = new Scene(BoEditPretFXML.load(), 449, 500);

            BoEditPretController boEditPretController = BoEditPretFXML.getController();
            boEditPretController.setBoEditPretValue(PretSelectedForUpdate);
            boEditPretController.setMainWindow(mainWindow);
            boEditPretController.setTable(TabListPret);
            boEditPretController.setTableLivre(TabListBook);
            boEditPretController.setTableLecteur(TabListLecteur);
            boEditPretController.setTotalPretLabel(totalPret);

            Stage Primary = new Stage();
            Primary = (Stage) mainWindow.getScene().getWindow();
            stage.initStyle(StageStyle.UNIFIED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("img/edit.png")));
            stage.setX(Primary.getX() + (Primary.getWidth()/2-150));
            stage.setY(Primary.getY() + 95);
            stage.initOwner(Primary);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }




    public void deletePretAction(Pret selectedPretForDelete){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Supprimer le prêt P/"+selectedPretForDelete.getId_Pret());
        confirmation.setHeaderText("");
        confirmation.setTitle("Alert-Confirmation");
        confirmation.initStyle(StageStyle.UTILITY);
        Stage Primary = new Stage();
        Primary = (Stage) mainWindow.getScene().getWindow();
        confirmation.setWidth( 350);confirmation.setHeight(100);
        confirmation.setX(Primary.getX() + (Primary.getWidth()/2-100));confirmation.setY(Primary.getY() + (Primary.getHeight()/2));
        confirmation.initOwner(Primary);


        Optional<ButtonType> buttonType = confirmation.showAndWait();

        if(buttonType.get() == ButtonType.OK){
            PretAction.DeletePret(selectedPretForDelete.getId_Pret());

            TabListPret.getItems().remove(selectedPretForDelete);


            totalPret.setText(Integer.valueOf(PretAction.GetTotalPret()).toString());
        }
    }



    public void GETStatPret(){
        PretPieChart.getData().clear();
        ArrayList TitreOuvrage = BookAction.GetTitreBook();
        ArrayList NbFOisPretOuvrage = BookAction.GetNbPretLivre();
        for(int i=0;i<TitreOuvrage.toArray().length && i<NbFOisPretOuvrage.toArray().length; i++){

            PieChart.Data dataSeries = new PieChart.Data(TitreOuvrage.get(i).toString(), Double.valueOf(NbFOisPretOuvrage.get(i).toString()));

            PretPieChart.getData().add(dataSeries);
        }
    }


    public void UpdatePassword(PasswordField currentPswrd, PasswordField newPswrd, PasswordField confirmPswrd)
    throws boUserException {
        UpdatePasswordValidateForm.validForm(currentPswrd, newPswrd, confirmPswrd);
        String hashedPassword = boUserAction.generateHashPassWord(newPswrd.getText());

        boUserAction.UpdateUserPassword(hashedPassword);
        boUserAction.resetFormToNormal(currentPswrd, newPswrd, confirmPswrd, errorMsgChangePswrd);
        AlertMessage.AlertSuccess(mainWindow, "Votre mot de passe a été modifié avec succès!");
    }



    public void LogOUt(){
        try {
            Stage stage = new Stage();
            FXMLLoader BoLogOutPopUp = new FXMLLoader(mainWindowController.class.getResource("BoLogOutPopUp.fxml"));
            Scene scene = new Scene(BoLogOutPopUp.load(), 350, 100);
            Stage Primary = new Stage();
            Primary = (Stage) mainWindow.getScene().getWindow();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("DECONNEXION");
            stage.setX(Primary.getX() + (Primary.getWidth()/2-100));
            stage.setY(Primary.getY() + (Primary.getHeight()/2));
            stage.initOwner(Primary);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

