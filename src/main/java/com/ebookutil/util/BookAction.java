package com.ebookutil.util;

import com.ebookutil.entity.Livre;
import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BookAction {
    public static void ADDBOOK(String title, String nameAuthor, Date dateEdition){
        Connection connection = connectionToDatabase.getInstance();
        String query = "INSERT INTO Livre(Titre_Ouvrage, Nom_Auteur, Date_Edition) VALUES(?,?,?)";

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, nameAuthor);
            preparedStatement.setDate(4, dateEdition);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static ObservableList<Livre> GetALLBook(){
        ObservableList<Livre> allBook = FXCollections.observableArrayList();
        Connection connection = connectionToDatabase.getInstance();
        //String query = "SELECT * from Livre";

        String query = "SELECT * from Livre ORDER BY Id_Ouvrage asc"; //En cas de problème affichage

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            Livre livre;
            while (resultSet.next()){
                livre = new Livre(resultSet.getInt("Id_Ouvrage"), resultSet.getString("Titre_Ouvrage"), resultSet.getString("Nom_Auteur"), resultSet.getDate("Date_Edition"), resultSet.getBoolean("Disponible"), resultSet.getInt("NbFoisPret"));
                allBook.add(livre);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return allBook;
    }

    public static void showListBook(TableView tabListBook, TableColumn idLivre, TableColumn titreLivre, TableColumn auteurLivre, TableColumn dateEdition, TableColumn disponible){
        ObservableList<Livre> ListBook = GetALLBook();

        idLivre.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Livre, Integer>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Livre, Integer> param) {
                Livre livre = param.getValue();
                int id = livre.getId_Ouvrage();
                String id_Ouvrage = "L-"+id;
                return new SimpleStringProperty(id_Ouvrage);
            }
        });
        titreLivre.setCellValueFactory(new PropertyValueFactory<Livre, String>("Titre_Ouvrage"));
        auteurLivre.setCellValueFactory(new PropertyValueFactory<Livre, String>("Nom_Auteur"));
        dateEdition.setCellValueFactory(new PropertyValueFactory<Livre, Date>("Date_Edition"));
        disponible.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Livre, Boolean>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures <Livre, Boolean> param) {
                Livre livre = param.getValue();
                boolean disponible = livre.getDisponible();
                if(disponible){
                    return new SimpleStringProperty("Oui");
                }
                else {
                    return new  SimpleStringProperty("Non");
                }
            }
        });
        if(ListBook.isEmpty()){
            tabListBook.setPrefHeight(100);
        }
        else {
            tabListBook.setPrefHeight(337);
            tabListBook.setItems(ListBook);
        }
    }


    public static void UpdateBook(int IdBook, String TitleBook, String AuthorName, Date DateEdtion){
        Connection connection = connectionToDatabase.getInstance();
        String query = "UPDATE Livre SET Titre_Ouvrage = ?, Nom_Auteur = ?, Date_Edition = ?";
        query += " WHERE Id_Ouvrage="+IdBook+"";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, TitleBook);
            preparedStatement.setString(2, AuthorName);
            preparedStatement.setDate(4, DateEdtion);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void DeleteLivre(int IdBook){
        Connection connection = connectionToDatabase.getInstance();
        String query = "DELETE FROM Livre WHERE Id_Ouvrage=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1 , IdBook);

            preparedStatement.executeUpdate();

            preparedStatement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void RefreshTableLivre(TableView TabListLivre){
        String query = "SELECT * FROM Livre";
        TabListLivre.getItems().clear();

        Connection connection = connectionToDatabase.getInstance();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                Livre livre = new Livre(resultSet.getInt("Id_Ouvrage"), resultSet.getString("Titre_Ouvrage"), resultSet.getString("Nom_Auteur"), resultSet.getDate("Date_Edition"), resultSet.getBoolean("Disponible"), resultSet.getInt("NbFoisPret"));
                TabListLivre.getItems().add(livre);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ObservableList<Livre> searchForLivre(TextField _titreLivre, TextField _auteurLivre)
    {
        ObservableList<Livre> Livre = FXCollections.observableArrayList();
        Connection connection = connectionToDatabase.getInstance();
        String query="";

        if((_titreLivre.getText().isBlank()) || ((_auteurLivre.getText().isBlank()))){
            if((_titreLivre.getText() != null) && (_auteurLivre.getText().isBlank())){
                query = "SELECT * FROM Livre WHERE Titre_Ouvrage LIKE "+"\"%"+_titreLivre.getText()+"%\""+"";
            }
            else if((_titreLivre.getText().isBlank()) && (_auteurLivre.getText() != null)){
                query = "SELECT * FROM Livre WHERE Nom_Auteur LIKE "+"'%"+_auteurLivre.getText()+"%'"+"";
            }
        }
        else {
            query = "SELECT * FROM Livre WHERE Titre_Ouvrage ="+"\""+_titreLivre.getText()+"\""+" AND Nom_Auteur ="+"'"+_auteurLivre.getText()+"'"+"";
        }
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            Livre livre;
            while (resultSet.next()){
                livre = new Livre(resultSet.getInt("Id_Ouvrage"), resultSet.getString("Titre_Ouvrage"), resultSet.getString("Nom_Auteur"), resultSet.getDate("Date_Edition"), resultSet.getBoolean("Disponible"), resultSet.getInt("NbFoisPret"));
                Livre.add(livre);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return Livre;
    }


    public static void resultOuvrageSearch(TableView tabListBook, TableColumn idLivre, TableColumn titreLivre, TableColumn auteurLivre, TableColumn dateEdition, TableColumn disponible,
                                           TextField titre, TextField auteur){
        ObservableList<Livre> resultSearch = searchForLivre(titre, auteur);
        idLivre.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Livre, Integer>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Livre, Integer> param) {
                Livre livre = param.getValue();
                int id = livre.getId_Ouvrage();
                String id_Ouvrage = "L-"+id;
                return new SimpleStringProperty(id_Ouvrage);
            }
        });
        titreLivre.setCellValueFactory(new PropertyValueFactory<Livre, String>("Titre_Ouvrage"));
        auteurLivre.setCellValueFactory(new PropertyValueFactory<Livre, String>("Nom_Auteur"));
        dateEdition.setCellValueFactory(new PropertyValueFactory<Livre, Date>("Date_Edition"));
        disponible.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Livre, Boolean>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures <Livre, Boolean> param) {
                Livre livre = param.getValue();
                boolean disponible = livre.getDisponible();
                if(disponible){
                    return new SimpleStringProperty("Oui");
                }
                else {
                    return new  SimpleStringProperty("Non");
                }
            }
        });
        if(resultSearch.isEmpty()){
            tabListBook.getItems().clear();
            tabListBook.setPlaceholder(new Label("Aucun Livre ne correspond à vos critères de recherche"));
            tabListBook.setPrefHeight(100);
        }
        else {
            tabListBook.setPrefHeight(337);
            tabListBook.setItems(resultSearch);
        }
    }

    public static ArrayList GetTitreBook(){
        ArrayList titreBook = new ArrayList<>();
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT Titre_Ouvrage FROM Livre WHERE NbFoisPret > 0 ORDER BY Id_Ouvrage asc";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                titreBook.add(resultSet.getString("Titre_Ouvrage"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return titreBook;
    }
    public static ArrayList GetNbPretLivre(){
        ArrayList NbFoisPretBook = new ArrayList<>();
        Connection connection = connectionToDatabase.getInstance();
        String query = "SELECT NbFoisPret FROM Livre WHERE NbFoisPret > 0 ORDER BY Id_Ouvrage asc";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                NbFoisPretBook.add(resultSet.getInt("NbFoisPret"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return NbFoisPretBook;
    }

    public static void Export_ListBook_toPDF() throws FileNotFoundException, DocumentException, IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHms");
        String formatedDate = now.format(dateTimeFormatter);
        generatePDF _pdf = new generatePDF("liste_des_Ouvrages_"+formatedDate, 5);
        _pdf.addAppIco();
        _pdf.addTitle("LISTE DES OUVRAGES");

        ObservableList<String> tableListOuvrageItems = FXCollections.observableArrayList();
        tableListOuvrageItems.add("N°Ouvrage");
        tableListOuvrageItems.add("Titre");
        tableListOuvrageItems.add("Auteur");
        tableListOuvrageItems.add("Date édition");
        tableListOuvrageItems.add("Disponible");

        _pdf.addHeaderRow(tableListOuvrageItems);
        tableListOuvrageItems.clear();

        ObservableList<Livre> AllBook = GetALLBook();
        for(Livre livre : AllBook){
            tableListOuvrageItems.add("L-"+livre.getId_Ouvrage());
            tableListOuvrageItems.add(livre.getTitre_Ouvrage());
            tableListOuvrageItems.add(livre.getNom_Auteur());
            tableListOuvrageItems.add(livre.getDate_Edition().toString());
            tableListOuvrageItems.add((livre.getDisponible()) ?  "Oui" :  "Non");
            _pdf.addRow(tableListOuvrageItems);
            tableListOuvrageItems.clear();
        }
        _pdf.addTable();

        _pdf.addTableDescription("Affichage de l'élément 0 à 0 sur 0 élément");
        tableListOuvrageItems.clear();

        _pdf.generate();
    }


    public static int GetTotalLivrePret(){

        String query = "SELECT SUM(NbFoisPret) as TotalLivrePret FROM Livre";

        int Total = 0;
        Connection connection = connectionToDatabase.getInstance();

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()){
                Total = resultSet.getInt("TotalLivrePret");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Total;
    }

    public static void resetToNormal(TableView tabListBook, TextField titre, TextField auteur){
        titre.clear();
        auteur.clear();
        tabListBook.setPrefHeight(337);
    }

}
