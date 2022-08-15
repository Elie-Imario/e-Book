package com.ebookutil.ebook;

import com.ebookutil.entity.Lecteur;
import com.ebookutil.entity.Livre;
import com.ebookutil.entity.Pret;
import com.ebookutil.exception.PretException.PretException;
import com.ebookutil.util.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoEffectuerPretController implements Initializable {

    @FXML private TextField _readerName, _titleBook, NbjourPret;
    @FXML private HBox alertAmende;
    @FXML private Label errorMsgPret;
    @FXML private DatePicker dateDebPret, dateFinPret;
    @FXML private Button btnConfirmPret, btnCancel;

    @FXML private TableView TablePret;
    @FXML private TableView TableLecteur;
    @FXML private TableView TableLivre;
    @FXML private Label totalPret, totalBookenPret;
    @FXML private VBox mainWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnCancel.setOnMouseClicked((ActionEvent)->{
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });

        //AutoCompletion TextField ReaderName
        ArrayList readerNameList = PretAction.set_readerNameSuggestion();
        TextFields.bindAutoCompletion(_readerName, readerNameList);

        //AutoCompletion TextField TitleBook
        ArrayList titleBookList = PretAction.set_titleBookSuggestion();
        TextFields.bindAutoCompletion(_titleBook, titleBookList);

        btnConfirmPret.setOnMouseClicked((ActionEvent)->{
            try {
                faireUnPret(_readerName, _titleBook, dateDebPret, dateFinPret, NbjourPret);
                Stage stage = (Stage) btnConfirmPret.getScene().getWindow();
                stage.close();
            }
            catch (PretException e){
                errorMsgPret.setText(e.getMessage());
                errorMsgPret.setContentDisplay(ContentDisplay.LEFT);
                new animatefx.animation.FadeIn(errorMsgPret).play();
            }

        });

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
                        alertAmende.setVisible(true);
                        new animatefx.animation.FadeIn(alertAmende).play();
                    }
                    else{
                        alertAmende.setVisible(false);
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
                        alertAmende.setVisible(true);
                        new animatefx.animation.FadeIn(alertAmende).play();
                    }
                    else{
                        alertAmende.setVisible(false);
                    }
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        });
    }







    public void faireUnPret(TextField _readerName, TextField _titleBook, DatePicker _dateDebPret, DatePicker _dateFinPret, TextField _nbJourPret )
    throws PretException {

        PretValidateForm.validForm(_readerName, _titleBook, _dateDebPret, _dateFinPret);

        String readerName = _readerName.getText();
        String titleBook = _titleBook.getText();
        Date dateDebPret = Date.valueOf(_dateDebPret.getValue());
        Date dateFinPret = Date.valueOf(_dateFinPret.getValue());
        int nbJourPret = Integer.parseInt(_nbJourPret.getText());

        int IdLecteur = PretAction.GetIdLecteur(readerName);
        int IdOuvrage = PretAction.GetIdOuvrage(titleBook);

        if (nbJourPret<=7){
            PretAction.EffectuerPretAction(IdLecteur, IdOuvrage, dateDebPret, dateFinPret, nbJourPret);
        }
        else{
            int Amende = 5000;
            PretAction.EffectuerPretAction(IdLecteur, IdOuvrage, dateDebPret, dateFinPret, nbJourPret, Amende);
        }


        AlertMessage.AlertSuccess(mainWindow, "Le prêt a été éffectué avec succès!");
        LecteurAction.RefreshTableLecteur(TableLecteur);
        BookAction.RefreshTableLivre(TableLivre);
        //INSERT LAST ADD IN TABLE PRET
        ADD_LAST_INSERT();


        totalPret.setText(Integer.valueOf(PretAction.GetTotalPret()).toString());
        totalBookenPret.setText(Integer.valueOf(BookAction.GetTotalLivrePret()).toString());
    }

    public void setTable(TableView<Pret> table){
        this.TablePret = table;
    }
    public void setTableLecteur(TableView<Lecteur> table){
        this.TableLecteur = table;
    }
    public void setTableLivre(TableView<Livre> table){
        this.TableLivre = table;
    }
    public void setMainWindow(VBox vBox){
        this.mainWindow = vBox;
    }
    public void setTotalBookLabel(Label label) {this.totalBookenPret = label;}
    public void setTotalPretLabel(Label label){
        this.totalPret = label;
    }


    public void ADD_LAST_INSERT(){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT LAST_INSERT_ID() as Id_Pret FROM Pret";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()){
                int newPretId = resultSet.getInt("Id_Pret");
                query = "SELECT * FROM Pret WHERE Id_Pret="+newPretId;
                resultSet = statement.executeQuery(query);

                while (resultSet.next()){
                    Pret pret = new Pret(resultSet.getInt("Id_Pret"), resultSet.getInt("Id_Lecteur"), resultSet.getInt("Id_Ouvrage"), resultSet.getDate("DateDebPret"), resultSet.getDate("DateFinPret"), resultSet.getInt("NbJourPret"), resultSet.getBoolean("Etat"), resultSet.getInt("Amende"));
                    if(TablePret!= null){
                        TablePret.getItems().add(pret);
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
