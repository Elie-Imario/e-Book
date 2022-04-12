package com.ebookutil.util;

import com.ebookutil.entity.Lecteur;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LecteurAction {

    public static void AddLecteur(String nomLecteur, String emailLecteur, String fonctionLecteur, String mobileLecteur){
        Connection connection = connectionToDatabase.getInstance();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "INSERT INTO Lecteur(Nom_Lecteur, Fonction, Email, Mobile) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //set Value to Prestatement
            preparedStatement.setString(1, nomLecteur);
            preparedStatement.setString(2, fonctionLecteur);
            preparedStatement.setString(3, emailLecteur);
            preparedStatement.setString(4, mobileLecteur);

            //execute Insert

            preparedStatement.executeUpdate();

            preparedStatement.close();
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static ObservableList<Lecteur> getAllLecteur(){
        ObservableList<Lecteur> allLecteur = FXCollections.observableArrayList();
        Connection connection = connectionToDatabase.getInstance();
        //String query = "SELECT * FROM Lecteur";

        String query = "SELECT * from Lecteur ORDER BY Id asc"; //En cas de problème affichage

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            Lecteur lecteur;
            while (resultSet.next()){
                lecteur = new Lecteur(resultSet.getInt("Id"), resultSet.getString("Nom_Lecteur"), resultSet.getString("Fonction"), resultSet.getString("Email"), resultSet.getString("Mobile"), resultSet.getInt("NbPretEnCours"), resultSet.getInt("NbFoisPret"), resultSet.getInt("Amende_Lecteur"));
                allLecteur.add(lecteur);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return allLecteur;
    }

    public static void showListLecteur(TableView tableLecteur, TableColumn idLecteur, TableColumn nomLecteur, TableColumn fonctionLecteur, TableColumn mobileLecteur){
        ObservableList<Lecteur> ALLlecteur = getAllLecteur();
        idLecteur.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lecteur, Integer>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Lecteur, Integer> param) {
                Lecteur lecteur = param.getValue();
                int Id = lecteur.getId();
                String cellData = "LEC-"+Id;
                return new SimpleStringProperty(cellData);
            }
        });
        nomLecteur.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("Nom_Lecteur"));

        fonctionLecteur.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("Fonction"));
        mobileLecteur.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("Mobile"));

        if(ALLlecteur.isEmpty()){
            tableLecteur.setPrefHeight(100);
        }
        else {
            tableLecteur.setPrefHeight(337);
            tableLecteur.setItems(ALLlecteur);
        }

    }

    public static void updateLecteur(int idLecteur, String nomLecteur, String emailLecteur, String fonctionLecteur, String mobileLecteur){
        Connection connection = connectionToDatabase.getInstance();
        String query = "UPDATE Lecteur set Nom_Lecteur=?,Fonction=?, Email=?, Mobile=?";
        query += " WHERE Id="+idLecteur+"";
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, nomLecteur);
            preparedStatement.setString(2, fonctionLecteur);
            preparedStatement.setString(3, emailLecteur);
            preparedStatement.setString(4, mobileLecteur);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deletelecteurItem(int id){
        Connection connection = connectionToDatabase.getInstance();
        String query = "DELETE FROM Lecteur WHERE Id=?";
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

    public static void RefreshTableLecteur(TableView TabListLecteur){
        //REFRESH TABLE
        String query = "SELECT * FROM Lecteur";

        TabListLecteur.getItems().clear();
        Connection connection = connectionToDatabase.getInstance();

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                Lecteur lecteur = new Lecteur(resultSet.getInt("Id"), resultSet.getString("Nom_Lecteur"), resultSet.getString("Fonction"), resultSet.getString("Email"),resultSet.getString("Mobile") ,resultSet.getInt("NbPretEnCours"), resultSet.getInt("NbFoisPret"),resultSet.getInt("Amende_Lecteur"));
                if(TabListLecteur != null){
                    TabListLecteur.getItems().add(lecteur);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ObservableList<Lecteur> searchForLecteur(TextField nom, TextField fonction, TextField mobile){
        ObservableList<Lecteur> Lecteur = FXCollections.observableArrayList();
        Connection connection = connectionToDatabase.getInstance();
        String query = "";

        if((nom.getText().isBlank()) || (fonction.getText().isBlank()) || (mobile.getText().isBlank())){

            if(nom.getText() != null && (fonction.getText().isBlank()) && (mobile.getText().isBlank())){
                query = "SELECT * FROM Lecteur WHERE Nom_Lecteur LIKE "+"'%"+nom.getText()+"%'"+"";
            }

            else if((nom.getText().isBlank()) && (fonction.getText() != null) && (mobile.getText().isBlank())){
                query = "SELECT * FROM Lecteur WHERE Fonction="+"'"+fonction.getText()+"'"+"";
            }

            else if((nom.getText().isBlank()) && (fonction.getText().isBlank()) && (mobile.getText() != null)){
                query = "SELECT * FROM Lecteur WHERE Mobile="+"'"+mobile.getText()+"'"+"";
            }

            else if((nom.getText().isBlank()) && (fonction.getText() != null) && (mobile.getText() != null)){
                query = "SELECT * FROM Lecteur WHERE Mobile="+"'"+mobile.getText()+"'"+" AND Fonction="+"'"+fonction.getText()+"'"+"";
            }

            else if((fonction.getText().isBlank()) && (nom.getText() != null) && (mobile.getText() != null)){
                query = "SELECT * FROM Lecteur WHERE Nom_Lecteur LIKE "+"'%"+nom.getText()+"%'"+" AND Mobile="+"'"+mobile.getText()+"'"+"";
            }

            else if((mobile.getText().isBlank()) && (nom.getText() != null) && (fonction.getText() != null)){
                query = "SELECT * FROM Lecteur WHERE Nom_Lecteur LIKE "+"'%"+nom.getText()+"%'"+" AND Fonction="+"'"+fonction.getText()+"'"+"";
            }
        }

        else {
            query = "SELECT * FROM Lecteur WHERE Nom_Lecteur LIKE "+"'%"+nom.getText()+"%'"+" AND Fonction="+"'"+fonction.getText()+"'"+" AND Mobile="+"'"+mobile.getText()+"'"+"";
        }


        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            Lecteur lecteur;
            while (resultSet.next()){
                lecteur = new Lecteur(resultSet.getInt("Id"), resultSet.getString("Nom_Lecteur"), resultSet.getString("Fonction"), resultSet.getString("Email"), resultSet.getString("Mobile"), resultSet.getInt("NbPretEnCours"), resultSet.getInt("NbFoisPret"), resultSet.getInt("Amende_Lecteur"));
                Lecteur.add(lecteur);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Lecteur;
    }

    public static void resultLecteurSearch(TableView tableLecteur, TableColumn idLecteur, TableColumn nomLecteur, TableColumn fonctionLecteur, TableColumn mobileLecteur,
                                           TextField _nom, TextField _fonction, TextField _mobile){
        ObservableList<Lecteur> resultSearch = searchForLecteur(_nom, _fonction, _mobile);
        idLecteur.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lecteur, Integer>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Lecteur, Integer> param) {
                Lecteur lecteur = param.getValue();
                return new SimpleStringProperty("LEC-"+lecteur.getId());
            }
        });
        nomLecteur.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("Nom_Lecteur"));

        fonctionLecteur.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("Fonction"));
        mobileLecteur.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("Mobile"));

        if(resultSearch.isEmpty()){
            tableLecteur.getItems().clear();
            tableLecteur.setPlaceholder(new Label("Aucun Lecteur ne correspond à vos critères de recherche"));
            tableLecteur.setPrefHeight(100);
        }
        else {
            tableLecteur.setPrefHeight(337);
            tableLecteur.setItems(resultSearch);
        }

    }

    public static int GetTotalLecteur(){

        String query = "SELECT COUNT(Id) as TotalLecteur FROM Lecteur";

        int Total = 0;
        Connection connection = connectionToDatabase.getInstance();

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()){
                Total = resultSet.getInt("TotalLecteur");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Total;
    }


    public static void resetToNormal(TableView tabLecteur, TextField nom, TextField fonction, TextField mobile){
        nom.clear();
        fonction.clear();
        mobile.clear();
        tabLecteur.setPrefHeight(337);
    }
}
