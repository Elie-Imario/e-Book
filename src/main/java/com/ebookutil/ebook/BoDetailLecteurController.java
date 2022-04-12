package com.ebookutil.ebook;

import com.ebookutil.entity.Lecteur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BoDetailLecteurController implements Initializable {

    @FXML
    private Label titledetail;
    @FXML private Label detailNomLecteur;
    @FXML private Label detailFonctionLecteur;
    @FXML private Label detailEmailLecteur;
    @FXML private Label detailMobileLecteur;
    @FXML private Label detailNbLivreLecteur;
    @FXML private Label detailAmendeLecteur;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setDetail(Lecteur selectedlecteur){
        titledetail.setText("FICHE DE RENSEIGNEMENT POUR LE LECTEUR N°"+ selectedlecteur.getId() + " :");
        detailNomLecteur.setText(selectedlecteur.getNom_Lecteur());
        detailFonctionLecteur.setText(selectedlecteur.getFonction());
        detailEmailLecteur.setText(selectedlecteur.getEmail());
        detailMobileLecteur.setText(selectedlecteur.getMobile());
        detailNbLivreLecteur.setText(Integer.valueOf(selectedlecteur.getNbFoisPret()).toString());
        detailAmendeLecteur.setText(Integer.valueOf(selectedlecteur.getAmende_Lecteur()).toString()+" Fmg");
    }
}
