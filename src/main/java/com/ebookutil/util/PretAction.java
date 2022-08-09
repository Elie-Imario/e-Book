package com.ebookutil.util;

import com.ebookutil.entity.Pret;
import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PretAction {
    private static ObservableList<Pret> ListPret;

    public static ObservableList<Pret> getListPret() {
        return ListPret;
    }

    public static void setListPret(ObservableList<Pret> listPret) {
        ListPret = listPret;
    }
    public static ArrayList set_readerNameSuggestion(){
        ArrayList _readerNameList = new ArrayList();
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT * FROM Lecteur";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                _readerNameList.add(resultSet.getString("Nom_Lecteur"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return _readerNameList;
    }
    public static ArrayList set_titleBookSuggestion(){
        ArrayList _titleBook = new ArrayList();
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT * FROM Livre WHERE Disponible = TRUE";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                _titleBook.add(resultSet.getString("Titre_Ouvrage"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return _titleBook;
    }

    //Nb jour Pret<7
    public static void EffectuerPretAction(int _IdLecteur, int _IdBook, Date _dateDebPret, Date _dateFinPret, int _nbJourPret){
        Connection connection = connectionToDatabase.getInstance();
        String query = "INSERT INTO Pret(Id_Lecteur, Id_Ouvrage, DateDebPret, DateFinPret, NbJourPret) VALUES(?,?,?,?,?)";
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, _IdLecteur);
            preparedStatement.setInt(2, _IdBook);
            preparedStatement.setDate(3, _dateDebPret);
            preparedStatement.setDate(4, _dateFinPret);
            preparedStatement.setInt(5, _nbJourPret);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        UpdateLecteurTable(_IdLecteur, 0);
        UpdateLivreTable(_IdBook);
    }

    //Nb jour Pret>7
    public static void EffectuerPretAction(int _IdLecteur, int _IdBook, Date _dateDebPret, Date _dateFinPret, int _nbJourPret, int _amende){
        Connection connection = connectionToDatabase.getInstance();
        String query = "INSERT INTO Pret(Id_Lecteur, Id_Ouvrage, DateDebPret, DateFinPret, NbJourPret, Amende) VALUES(?,?,?,?,?,?)";
        try{
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, _IdLecteur);
            preparedStatement.setInt(2, _IdBook);
            preparedStatement.setDate(3, _dateDebPret);
            preparedStatement.setDate(4, _dateFinPret);
            preparedStatement.setInt(5, _nbJourPret);
            preparedStatement.setInt(6, _amende);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            statement.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        UpdateLecteurTable(_IdLecteur, _amende);
        UpdateLivreTable(_IdBook);
    }

    public static void UpdatePret(int etatPret, int _idPret, int _IdLecteur, int _newIdOuvrage, Date _newDateDebPret, Date _newDateFinPret, int _newNbJourPret,
                                  int currentIdOuvrage, int currentNbJourPret, int currentAmende){
        Connection connection = connectionToDatabase.getInstance();
        String query = new String();
        int newAmendePret = 0;
        int amendeLecteur = 0;

        if (etatPret == 1){//Pret toujours en cours
            if(currentIdOuvrage == _newIdOuvrage){// Le Lecteur n'a pas pris un autre livre
                if(currentNbJourPret<=7){
                    if(_newNbJourPret<=7){
                        newAmendePret = currentAmende;
                        amendeLecteur = currentAmende;
                    }
                    else {
                        newAmendePret += 5000;
                        amendeLecteur += 5000;
                    }
                }
                else {
                    if(_newNbJourPret<=7){
                        newAmendePret = currentAmende - 5000;
                        amendeLecteur = currentAmende - 5000;
                    }
                    else {
                        newAmendePret = currentAmende;
                        amendeLecteur = currentAmende;
                    }
                }

                query = "UPDATE Pret SET DateDebPret = ?, DateFinPret = ?, NbJourPret = ?, Amende = ?";
                query += " WHERE Id_Pret ="+_idPret;

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setDate(1, _newDateDebPret);
                    preparedStatement.setDate(2, _newDateFinPret);
                    preparedStatement.setInt(3, _newNbJourPret);
                    preparedStatement.setInt(4, newAmendePret);

                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                if(newAmendePret != currentAmende){
                    UpdateLecteurTable_AFTER_EDITPret(_IdLecteur, amendeLecteur, false);
                }
            }
            else {// Le Lecteur a décidé de prendre un autre livre
                if(currentNbJourPret<=7){
                    if(_newNbJourPret<=7){
                        newAmendePret = currentAmende;
                        amendeLecteur = currentAmende;
                    }
                    else {
                        newAmendePret += 5000;
                        amendeLecteur += 5000;
                    }
                }
                else {
                    if(_newNbJourPret<=7){
                        newAmendePret = currentAmende - 5000;
                        amendeLecteur = currentAmende - 5000;
                    }
                    else {
                        newAmendePret = currentAmende;
                        amendeLecteur = currentAmende;
                    }
                }

                query = "UPDATE Pret SET Id_Ouvrage = ?, DateDebPret = ?, DateFinPret = ?, NbJourPret = ?, Amende = ?";
                query += " WHERE Id_Pret ="+_idPret;

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setInt(1, _newIdOuvrage);
                    preparedStatement.setDate(2, _newDateDebPret);
                    preparedStatement.setDate(3, _newDateFinPret);
                    preparedStatement.setInt(4, _newNbJourPret);
                    preparedStatement.setInt(5, newAmendePret);

                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                if(newAmendePret != currentAmende){
                    UpdateLecteurTable_AFTER_EDITPret(_IdLecteur, amendeLecteur, true);
                }
                else {
                    UpdateLecteurTable_AFTER_EDITPret(_IdLecteur, true);
                }

                UpdateLivreTable_AFTER_EDITPret(currentIdOuvrage, _newIdOuvrage);
            }
        }


        else{ //Pret Terminé
            query = "UPDATE Pret SET Etat = ? WHERE Id_Pret ="+_idPret;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setBoolean(1, false);

                preparedStatement.executeUpdate();

                preparedStatement.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            UpdateLecteurTable_AFTER_EDIT_ETAT_PRET(_IdLecteur, true);
            UpdateLivreTable_AFTER_EDIT_ETAT_PRET(currentIdOuvrage, true);

        }
    }

    public static void UpdateLecteurTable(int IdLecteur, int _amendelecteur){
        Connection connection = connectionToDatabase.getInstance();
        //Get Lecteur Data
        String query = "SELECT * FROM Lecteur WHERE Id="+IdLecteur+"";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                //UPDATE TABLE LECTEUR
                int nbpretencours = resultSet.getInt("NbPretEnCours");
                int nbEmprint = resultSet.getInt("NbFoisPret");
                int amendeLecteur = resultSet.getInt("Amende_Lecteur");

                query = "UPDATE Lecteur set NbPretEnCours = ?, NbFoisPret = ?, Amende_Lecteur = ?";
                query += " WHERE Id="+IdLecteur;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, nbpretencours + 1);
                preparedStatement.setInt(2, nbEmprint + 1);
                preparedStatement.setInt(3, amendeLecteur + _amendelecteur);

                preparedStatement.executeUpdate();

                preparedStatement.close();
            }
            else{
                System.out.println("Aucon resultat trouvé");
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void UpdateLecteurTable_AFTER_EDITPret(int IdLecteur, boolean isNewBook){
        Connection connection = connectionToDatabase.getInstance();
        //Get Lecteur Data
        String query = "SELECT * FROM Lecteur WHERE Id="+IdLecteur+"";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                //UPDATE TABLE LECTEUR
                if(isNewBook){
                    int nbFoisPret = resultSet.getInt("NbFoisPret");

                    query = "UPDATE Lecteur SET NbFoisPret = ?";
                    query += " WHERE Id="+IdLecteur;

                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setInt(1, nbFoisPret + 1);

                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                }
            }
            else{
                System.out.println("Aucon resultat trouvé");
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void UpdateLecteurTable_AFTER_EDIT_ETAT_PRET(int IdLecteur, boolean isFinish){
        Connection connection = connectionToDatabase.getInstance();
        //Get Lecteur Data
        String query = "SELECT * FROM Lecteur WHERE Id="+IdLecteur+"";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                //UPDATE TABLE LECTEUR
                if(isFinish){
                    int nbPretEnCours = resultSet.getInt("NbPretEnCours");

                    query = "UPDATE Lecteur SET NbPretEnCours = ?";
                    query += " WHERE Id="+IdLecteur;

                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setInt(1, nbPretEnCours - 1);

                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                }
            }
            else{
                System.out.println("Aucon resultat trouvé");
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void UpdateLecteurTable_AFTER_EDITPret(int IdLecteur, int _amendelecteur, boolean isNewBook){
        Connection connection = connectionToDatabase.getInstance();
        //Get Lecteur Data
        String query = "SELECT * FROM Lecteur WHERE Id="+IdLecteur+"";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                //UPDATE TABLE LECTEUR
                int amendeLecteur = resultSet.getInt("Amende_Lecteur");

                if(isNewBook){
                    int nbFoisPret = resultSet.getInt("NbFoisPret");

                    query = "UPDATE Lecteur SET NbFoisPret = ?, Amende_Lecteur = ?";
                    query += " WHERE Id="+IdLecteur;

                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setInt(1, nbFoisPret + 1);

                    if (_amendelecteur == 0){
                        preparedStatement.setInt(2, amendeLecteur - 5000);
                    }
                    else {
                        preparedStatement.setInt(2, amendeLecteur + _amendelecteur);
                    }

                    preparedStatement.executeUpdate();

                    preparedStatement.close();

                }
                else {

                    query = "UPDATE Lecteur SET Amende_Lecteur = ?";
                    query += " WHERE Id="+IdLecteur;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    if (_amendelecteur == 0){
                        preparedStatement.setInt(1, amendeLecteur - 5000);
                    }
                    else {
                        preparedStatement.setInt(1, amendeLecteur + _amendelecteur);
                    }

                    preparedStatement.executeUpdate();

                    preparedStatement.close();

                }
            }
            else{
                System.out.println("Aucon resultat trouvé");
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void UpdateLivreTable(int idOuvrage){
        Connection connection = connectionToDatabase.getInstance();
        //Get Livre Data
        String query = "SELECT * FROM Livre WHERE Id_Ouvrage="+idOuvrage;
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                int nbFoisPretLivre = resultSet.getInt("NbFoisPret");
                query = "UPDATE Livre set Disponible =?, NbFoisPret =? WHERE Id_Ouvrage="+idOuvrage;

                boolean disponible = resultSet.getBoolean("Disponible");
                if(disponible){
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setBoolean(1, false);
                    preparedStatement.setInt(2, nbFoisPretLivre+1);

                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                }
                else {
                    System.out.println("Livre Indisponible");
                }
            }
            else{
                System.out.println("Aucun resultat trouvé");
            }

            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void UpdateLivreTable_AFTER_EDITPret(int currentIdOuvrage, int newIdOuvrage){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT * FROM Livre WHERE Id_Ouvrage="+currentIdOuvrage;//Update OldBook
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                query = "UPDATE Livre set Disponible =? WHERE Id_Ouvrage="+currentIdOuvrage;

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setBoolean(1, true);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }

            else{
                System.out.println("Aucun resultat trouvé");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        query = "SELECT * FROM Livre WHERE Id_Ouvrage="+newIdOuvrage;//Update newBook

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                int nbFoisPretLivre = resultSet.getInt("NbFoisPret");
                query = "UPDATE Livre set Disponible =?, NbFoisPret =? WHERE Id_Ouvrage="+newIdOuvrage;

                boolean disponible = resultSet.getBoolean("Disponible");
                if(disponible){
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setBoolean(1, false);
                    preparedStatement.setInt(2, nbFoisPretLivre+1);

                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                }
                else {
                    System.out.println("Livre Indisponible");
                }
            }
            else{
                System.out.println("Aucun resultat trouvé");
            }

            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public static void UpdateLivreTable_AFTER_EDIT_ETAT_PRET(int IdOuvrage, boolean isFinish){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT * FROM Livre WHERE Id_Ouvrage="+IdOuvrage;
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                if (isFinish){
                    query = "UPDATE Livre SET Disponible =? WHERE Id_Ouvrage="+IdOuvrage;

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setBoolean(1, true);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                }
            }

            else{
                System.out.println("Aucun resultat trouvé");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public static ObservableList<Pret> GetAllPret(){
        ObservableList<Pret> AllPret = FXCollections.observableArrayList();
        Connection connection = connectionToDatabase.getInstance();

        String query = "SELECT * FROM Pret ORDER BY Id_Pret asc";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            Pret pret;
            while (resultSet.next()){
                pret = new Pret(resultSet.getInt("Id_Pret"), resultSet.getInt("Id_Lecteur"), resultSet.getInt("Id_Ouvrage"), resultSet.getDate("DateDebPret"), resultSet.getDate("DateFinPret"), resultSet.getInt("NbJourPret"), resultSet.getBoolean("Etat"), resultSet.getInt("Amende"));
                AllPret.add(pret);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return AllPret;
    }

    public static void ShowListPret(TableView tabListPret, TableColumn numPret, TableColumn titreOuvrage, TableColumn nomLecteur, TableColumn datedebPret, TableColumn datefinPret, TableColumn nbjourPret, TableColumn etatPret, TableColumn amendePret){
        setListPret(GetAllPret());
        ObservableList<Pret> ListPret = getListPret();


        numPret.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pret, Integer>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pret, Integer> param) {
                Pret pret = param.getValue();
                int IdPret = pret.getId_Pret();

                return new SimpleStringProperty("P/"+IdPret);
            }
        });
        titreOuvrage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pret, Integer>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pret, Integer> param) {
                Pret pret = param.getValue();
                String _titreOuvrage = GetTitreOuvrage(pret.getId_Ouvrage());
                return new SimpleStringProperty(_titreOuvrage);
            }
        });
        nomLecteur.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pret, Integer>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pret, Integer> param) {
               Pret pretValue = param.getValue();
               String _nomLecteur = GetNomLecteur(pretValue.getId_Lecteur());
               return new SimpleStringProperty(_nomLecteur);
            }
        });
        datedebPret.setCellValueFactory(new PropertyValueFactory<Pret, Date>("DateDebPret"));
        datefinPret.setCellValueFactory(new PropertyValueFactory<Pret, Date>("DateFinPret"));
        nbjourPret.setCellValueFactory(new PropertyValueFactory<Pret, Integer>("NbJourPret"));
        etatPret.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pret, Boolean>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pret, Boolean> param) {
                Pret pret = param.getValue();
                if(pret.isEtat()){
                    return new SimpleStringProperty("En cours");
                }
                else {
                    return new SimpleStringProperty("Terminé");
                }
            }
        });
        amendePret.setCellValueFactory(new PropertyValueFactory<Pret, Integer>("Amende"));

        if(ListPret.isEmpty()){
            tabListPret.setPrefHeight(100);
        }
        else {
            tabListPret.setPrefHeight(337);
            tabListPret.setItems(ListPret);
        }
    }

    public static ObservableList<Pret> SearchForPret(TextField numPret, TextField nomLecteur, DatePicker dateDeb, DatePicker dateFin, TextField titreOuvrage){
        ObservableList<Pret> Pret = FXCollections.observableArrayList();
        Connection connection = connectionToDatabase.getInstance();
        String query = "";
        if((numPret.getText().isBlank()) || (nomLecteur.getText().isBlank()) || (dateDeb.getValue() == null) || (dateFin.getValue() == null) || (titreOuvrage.getText().isBlank()) ){
            if(numPret.getText() != null && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() == null) && (dateFin.getValue() == null) && (titreOuvrage.getText().isBlank()) ){
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText() != null) && (dateDeb.getValue() == null) && (dateFin.getValue() == null) && (titreOuvrage.getText().isBlank())){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                query = "SELECT * FROM Pret WHERE Id_Lecteur="+ IdLecteur +"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue()!=null) && (dateFin.getValue() == null) && (titreOuvrage.getText().isBlank())){
                query = "SELECT * FROM Pret WHERE DateDebPret="+"'"+ Date.valueOf(dateDeb.getValue())+"'"+"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() == null) && (dateFin.getValue()!=null) && (titreOuvrage.getText().isBlank())){
                query = "SELECT * FROM Pret WHERE DateFinPret="+"'"+Date.valueOf(dateFin.getValue())+"'"+"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() == null) && (dateFin.getValue() == null) && (titreOuvrage.getText()!= null)){
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Ouvrage="+ IdOuvrage +"";
            }

            //2 params
            else if((numPret.getText() != null ) && (nomLecteur.getText() != null) && (dateDeb.getValue() == null) && (dateFin.getValue() == null) && (titreOuvrage.getText().isBlank())){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND Id_Lecteur="+ IdLecteur +"";
            }
            else if((numPret.getText() != null ) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() != null) && (dateFin.getValue() == null) && (titreOuvrage.getText().isBlank())){
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND DateDebPret="+"'"+ Date.valueOf(dateDeb.getValue())+"'"+"";
            }
            else if((numPret.getText() != null ) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() == null) && (dateFin.getValue() != null) && (titreOuvrage.getText().isBlank())){
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND DateFInPret="+"'"+ Date.valueOf(dateFin.getValue())+"'"+"";
            }
            else if((numPret.getText() != null ) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() == null) && (dateFin.getValue() == null) && (titreOuvrage.getText()!= null)){
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND Id_Ouvrage="+ IdOuvrage +"";
            }
            /**/
            else if((numPret.getText().isBlank()) && (nomLecteur.getText() != null) && (dateDeb.getValue() != null) && (dateFin.getValue() == null) && (titreOuvrage.getText().isBlank())){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                query = "SELECT * FROM Pret WHERE Id_Lecteur="+ IdLecteur +" AND DateDebPret="+"'"+ Date.valueOf(dateDeb.getValue())+"'"+"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText() != null) && (dateDeb.getValue() == null) && (dateFin.getValue() != null) && (titreOuvrage.getText().isBlank())){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                query = "SELECT * FROM Pret WHERE Id_Lecteur="+ IdLecteur +" AND DateFinPret="+"'"+ Date.valueOf(dateFin.getValue())+"'"+"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText() != null) && (dateDeb.getValue() == null) && (dateFin.getValue() == null) && (titreOuvrage.getText()!= null)){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Lecteur="+ IdLecteur +" AND Id_Ouvrage="+ IdOuvrage +"";
            }
            /**/
            else if((numPret.getText().isBlank()) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() != null) && (dateFin.getValue() != null) && (titreOuvrage.getText().isBlank())){
                query = "SELECT * FROM Pret WHERE DateDebPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" OR DateFinPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() != null) && (dateFin.getValue() == null) && (titreOuvrage.getText() != null)){
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE DateDebPret="+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
            }
            /**/
            else if((numPret.getText().isBlank()) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() == null) && (dateFin.getValue() != null) && (titreOuvrage.getText() != null)){
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE DateFinPret="+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
            }

            //3params
            else if((numPret.getText() != null ) && (nomLecteur.getText() != null) && (dateDeb.getValue() != null) && (dateFin.getValue() == null) && (titreOuvrage.getText().isBlank())){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND Id_Lecteur="+ IdLecteur +" AND DateDebPret="+"'"+ Date.valueOf(dateDeb.getValue())+"'"+"";
            }
            else if((numPret.getText() != null ) && (nomLecteur.getText() != null) && (dateDeb.getValue() == null) && (dateFin.getValue() != null) && (titreOuvrage.getText().isBlank())){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND Id_Lecteur="+ IdLecteur +" AND DateFinPret="+"'"+ Date.valueOf(dateFin.getValue())+"'"+"";
            }
            else if((numPret.getText() != null) && (nomLecteur.getText() != null) && (dateDeb.getValue() == null) && (dateFin.getValue()  == null) && (titreOuvrage.getText() != null)){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND Id_Lecteur="+ IdLecteur +" AND Id_Ouvrage="+ IdOuvrage +"";
            }
            else if((numPret.getText() != null) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() != null) && (dateFin.getValue() != null) && (titreOuvrage.getText().isBlank())){
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND DateDebPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND DateFinPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+"";
            }
            else if((numPret.getText() != null) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() != null) && (dateFin.getValue() == null) && (titreOuvrage.getText() != null)){
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND DateDebPret="+"'"+ Date.valueOf(dateDeb.getValue())+"'"+"AND Id_Ouvrage="+ IdOuvrage +"";
            }
            else if((numPret.getText() != null) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() == null) && (dateFin.getValue() != null) && (titreOuvrage.getText() != null)){
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND DateFinPret="+"'"+ Date.valueOf(dateFin.getValue())+"'"+"AND Id_Ouvrage="+ IdOuvrage +"";
            }
            /**/
            else if((numPret.getText().isBlank()) && (nomLecteur.getText() != null) && (dateDeb.getValue() != null) && (dateFin.getValue() != null) && (titreOuvrage.getText().isBlank())){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                query = "SELECT * FROM Pret WHERE Id_Lecteur="+ IdLecteur +" AND DateDebPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND DateFinPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText() != null) && (dateDeb.getValue() != null) && (dateFin.getValue() == null) && (titreOuvrage.getText() != null)){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Lecteur="+ IdLecteur +" AND DateDebPret="+"'"+ Date.valueOf(dateDeb.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText() != null) && (dateDeb.getValue() == null) && (dateFin.getValue() != null) && (titreOuvrage.getText() != null)){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Lecteur="+ IdLecteur +" AND DateFinPret="+"'"+ Date.valueOf(dateFin.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
            }
            /**/
            else if((numPret.getText().isBlank()) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() != null) && (dateFin.getValue() != null) && (titreOuvrage.getText() != null)){
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE DateDebPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND DateFinPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
            }

            //4params
            else if((numPret.getText() != null) && (nomLecteur.getText() != null) && (dateDeb.getValue() != null)  && (dateFin.getValue() != null) && (titreOuvrage.getText().isBlank())){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND Id_Lecteur="+ IdLecteur +" AND DateDebPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND DateFinPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+"";
            }
            else if((numPret.getText().isBlank()) && (nomLecteur.getText() != null) && (dateDeb.getValue() != null)  && (dateFin.getValue() != null) && (titreOuvrage.getText() != null)){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Lecteur="+ IdLecteur +" AND DateDebPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND DateFinPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
            }
            else if((numPret.getText() != null) && (nomLecteur.getText().isBlank()) && (dateDeb.getValue() != null)  && (dateFin.getValue() != null) && (titreOuvrage.getText() != null)){
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND DateDebPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND DateFinPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
            }
            else if((numPret.getText() != null) && (nomLecteur.getText() != null) && (dateDeb.getValue() == null)  && (dateFin.getValue() != null) && (titreOuvrage.getText() != null)){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND Id_Lecteur="+ IdLecteur +" AND DateFinPret="+"'"+ Date.valueOf(dateFin.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
            }
            else if((numPret.getText() != null) && (nomLecteur.getText() != null) && (dateDeb.getValue() != null)  && (dateFin.getValue() == null) && (titreOuvrage.getText() != null)){
                int IdLecteur = GetIdLecteur(nomLecteur.getText());
                int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
                query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND Id_Lecteur="+ IdLecteur +" AND DateDebPret="+"'"+ Date.valueOf(dateDeb.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
            }

        }
        else{
            int IdLecteur = GetIdLecteur(nomLecteur.getText());
            int IdOuvrage = GetIdOuvrage(titreOuvrage.getText());
            query = "SELECT * FROM Pret WHERE Id_Pret="+Integer.parseInt(numPret.getText())+" AND Id_Lecteur="+ IdLecteur +" AND DateDebPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND DateFinPret BETWEEN "+"'"+Date.valueOf(dateDeb.getValue())+"'"+" AND "+"'"+Date.valueOf(dateFin.getValue())+"'"+" AND Id_Ouvrage="+ IdOuvrage +"";
        }
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            Pret pret;
            while (resultSet.next()){
                pret = new Pret(resultSet.getInt("Id_Pret"), resultSet.getInt("Id_Lecteur"), resultSet.getInt("Id_Ouvrage"), resultSet.getDate("DateDebPret"), resultSet.getDate("DateFinPret"), resultSet.getInt("NbJourPret"), resultSet.getBoolean("Etat"), resultSet.getInt("Amende"));
                Pret.add(pret);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return Pret;
    }

    public static void resultPretSearch(TableView tabListPret, TableColumn idPret, TableColumn titreLivre, TableColumn nomLecteur, TableColumn dateDeb, TableColumn dateFin, TableColumn Duree, TableColumn Etat, TableColumn Amende,
                                        TextField _numPret, TextField _nomLecteur, DatePicker _dateDeb, DatePicker _dateFin, TextField _titreOuvrage){
        setListPret(SearchForPret(_numPret, _nomLecteur, _dateDeb, _dateFin, _titreOuvrage));
        ObservableList<Pret> resultSearch = getListPret();

        idPret.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pret, Integer>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pret, Integer> param) {
                Pret pret = param.getValue();
                int IdPret = pret.getId_Pret();

                return new SimpleStringProperty("P/"+IdPret);
            }
        });
        titreLivre.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pret, Integer>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pret, Integer> param) {
                Pret pret = param.getValue();
                String _titreOuvrage = GetTitreOuvrage(pret.getId_Ouvrage());
                return new SimpleStringProperty(_titreOuvrage);
            }
        });
        nomLecteur.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pret, Integer>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pret, Integer> param) {
                Pret pretValue = param.getValue();
                String _nomLecteur = GetNomLecteur(pretValue.getId_Lecteur());
                return new SimpleStringProperty(_nomLecteur);
            }
        });
        dateDeb.setCellValueFactory(new PropertyValueFactory<Pret, Date>("DateDebPret"));
        dateFin.setCellValueFactory(new PropertyValueFactory<Pret, Date>("DateFinPret"));
        Duree.setCellValueFactory(new PropertyValueFactory<Pret, Integer>("NbJourPret"));
        Etat.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pret, Boolean>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pret, Boolean> param) {
                Pret pret = param.getValue();
                if(pret.isEtat()){
                    return new SimpleStringProperty("En cours");
                }
                else {
                    return new SimpleStringProperty("Terminé");
                }
            }
        });
        Amende.setCellValueFactory(new PropertyValueFactory<Pret, Integer>("Amende"));


        if(resultSearch.isEmpty()){
            tabListPret.getItems().clear();
            tabListPret.setPlaceholder(new Label("Aucun Prêt ne correspond à vos critères de recherche"));
            tabListPret.setPrefHeight(100);
        }
        else {
            tabListPret.setPrefHeight(337);
            tabListPret.setItems(resultSearch);
        }
    }

    public static void DeletePret(int id){
        Connection connection = connectionToDatabase.getInstance();
        String query = "DELETE FROM Pret WHERE Id_Pret=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }


    public static int GetIdLecteur(String nomLecteur){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT Id FROM Lecteur WHERE Nom_Lecteur="+"'"+nomLecteur+"'"+"";

        int IdLecteur = 0;

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                IdLecteur = resultSet.getInt("Id");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return IdLecteur;
    }

    public static String GetNomLecteur(int idLecteur){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT Nom_Lecteur FROM Lecteur WHERE Id="+idLecteur;

        String nomLecteur = new String();

        try {
            Statement statement = connection    .createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                nomLecteur = resultSet.getString("Nom_Lecteur");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return nomLecteur;
    }

    public static int GetIdOuvrage(String titleBook){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT Id_Ouvrage FROM Livre WHERE Titre_Ouvrage="+"\""+titleBook+"\""+"";

        int IdOuvrage = 0;

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                IdOuvrage = resultSet.getInt("Id_Ouvrage");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return IdOuvrage;

    }

    public static String GetTitreOuvrage(int idOuvrage){
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT Titre_Ouvrage FROM Livre WHERE Id_Ouvrage="+idOuvrage;

        String titreOuvrage = new String();

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                titreOuvrage = resultSet.getString("Titre_Ouvrage");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return titreOuvrage;

    }

    public static void Export_ListPret_toPDF(VBox modalOwner) throws FileNotFoundException, DocumentException, IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHms");
        String formatedDate = now.format(dateTimeFormatter);
        generatePDF _pdf = new generatePDF("liste_prêts_"+formatedDate, 8);
        _pdf.addAppIco();
        _pdf.addTitle("LISTE DES PRETS");

        ObservableList<String> tableListPretItems = FXCollections.observableArrayList();
        tableListPretItems.add("N°Pret");
        tableListPretItems.add("Titre de l'ouvrage");
        tableListPretItems.add("Nom du lecteur");
        tableListPretItems.add("Date début");
        tableListPretItems.add("Date fin");
        tableListPretItems.add("Durée Prêt");
        tableListPretItems.add("Etat");
        tableListPretItems.add("Amende");

        _pdf.addHeaderRow(tableListPretItems);
        tableListPretItems.clear();

        ObservableList<Pret> ALLPret = getListPret();

        if(ALLPret.toArray().length < 1){
            AlertMessage.WarningAlert(modalOwner, "Aucune donnée à exporter!!!");
        }
        else{
            for(Pret pret : ALLPret){
                tableListPretItems.add("P/"+pret.getId_Pret());
                tableListPretItems.add(GetTitreOuvrage(pret.getId_Pret()));
                tableListPretItems.add(GetNomLecteur(pret.getId_Pret()));
                tableListPretItems.add(pret.getDateDebPret().toString());
                tableListPretItems.add(pret.getDateFinPret().toString());
                tableListPretItems.add(Integer.valueOf(pret.getNbJourPret()).toString());
                tableListPretItems.add((pret.isEtat())? "En cours" : "Terminé");
                tableListPretItems.add(Integer.valueOf(pret.getAmende()).toString());

                _pdf.addRow(tableListPretItems);
                tableListPretItems.clear();
            }
            _pdf.addTable();

            _pdf.addTableDescription("Affichage de "+ALLPret.toArray().length+" élément(s) sur "+ GetTotalPret());
            tableListPretItems.clear();

            _pdf.generate(modalOwner);
        }
    }

    public static int GetTotalPret(){

        String query = "SELECT COUNT(Id_Pret) as TotalPret FROM Pret";

        int Total = 0;
        Connection connection = connectionToDatabase.getInstance();

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()){
                Total = resultSet.getInt("TotalPret");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Total;
    }


    public static void RefreshTablePret(TableView tablePret){
        //REFRESH TABLE
        String query = "SELECT * FROM Pret ORDER BY Id_Pret asc";

        tablePret.getItems().clear();
        Connection connection = connectionToDatabase.getInstance();

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                Pret pret = new Pret(resultSet.getInt("Id_Pret"), resultSet.getInt("Id_Lecteur"), resultSet.getInt("Id_Ouvrage"), resultSet.getDate("DateDebPret"), resultSet.getDate("DateFinPret"), resultSet.getInt("NbJourPret"), resultSet.getBoolean("Etat"), resultSet.getInt("Amende"));
                if(tablePret != null){
                    tablePret.getItems().add(pret);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void resetToNormal(TableView tabListPret, TextField idPret, TextField nomLecteur, DatePicker dateDeb, DatePicker dateFin, TextField titreOuvrage){
        idPret.clear();
        nomLecteur.clear();
        dateDeb.setValue(null);
        dateFin.setValue(null);
        titreOuvrage.clear();
        tabListPret.setPrefHeight(337);
    }
}
