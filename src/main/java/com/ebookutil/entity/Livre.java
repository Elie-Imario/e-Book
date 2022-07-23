package com.ebookutil.entity;

import java.util.Date;

public class Livre {
    private int Id_Ouvrage;
    private String Titre_Ouvrage;
    private String Nom_Auteur;
    private Date Date_Edition;
    private boolean Disponible;
    private int NbFoisPret;


    public Livre(int id_Ouvrage, String titre_Ouvrage, String nom_Auteur, Date date_Edition, boolean disponible, int nbFoisPret) {
        this.Id_Ouvrage = id_Ouvrage;
        this.Titre_Ouvrage = titre_Ouvrage;
        this.Nom_Auteur = nom_Auteur;
        this.Date_Edition = date_Edition;
        this.Disponible = disponible;
        this.NbFoisPret = nbFoisPret;
    }

    public int getId_Ouvrage() {
        return Id_Ouvrage;
    }

    public void setId_Ouvrage(int id_Ouvrage) {
        this.Id_Ouvrage = id_Ouvrage;
    }



    public String getTitre_Ouvrage() {
        return Titre_Ouvrage;
    }

    public void setTitre_Ouvrage(String titre_Ouvrage) {
        this.Titre_Ouvrage = titre_Ouvrage;
    }

    public String getNom_Auteur() {
        return Nom_Auteur;
    }

    public void setNom_Auteur(String nom_Auteur) {
        this.Nom_Auteur = nom_Auteur;
    }

    public Date getDate_Edition() {
        return Date_Edition;
    }

    public void setDate_Edition(Date date_Edition) {
        this.Date_Edition = date_Edition;
    }


    public boolean getDisponible() {
        return Disponible;
    }

    public void setDisponible(boolean disponible) {
        Disponible = disponible;
    }

    public int getNbFoisPret() {
        return NbFoisPret;
    }

    public void setNbFoisPret(int nbFoisPret) {
        NbFoisPret = nbFoisPret;
    }

}
