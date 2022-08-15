package com.ebookutil.ebook;

import com.ebookutil.entity.Livre;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BoDetailOuvrageController implements Initializable {
    @FXML
    private Label titledetail;
    @FXML private Label detailTitreOuvrage;
    @FXML private Label detailNomAuteur;
    @FXML private Label detailDateEdition;
    @FXML private Label detailDisponibilite;
    @FXML private Label detailNbfoisEnPret;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setDetail(Livre selectedlivre){
        titledetail.setText("FICHE DE RENSEIGNEMENT POUR L'OUVRAGE N°"+ selectedlivre.getId_Ouvrage() + " :");
        detailTitreOuvrage.setText(selectedlivre.getTitre_Ouvrage());
        detailNomAuteur.setText(selectedlivre.getNom_Auteur());
        detailDateEdition.setText(selectedlivre.getDate_Edition().toString());
        detailDisponibilite.setText((selectedlivre.getDisponible())? "Disponible" : "En prêt");
        detailNbfoisEnPret.setText(Integer.valueOf(selectedlivre.getNbFoisPret()).toString());
    }
}
