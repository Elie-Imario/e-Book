package com.ebookutil.entity;

public class Lecteur {

    private int Id;
    private String Nom_Lecteur;
    private String Fonction;
    private String Email;
    private String Mobile;
    private int NbPretEnCours;
    private int NbFoisPret;
    private int Amende_Lecteur;

    public Lecteur(int id, String nom_Lecteur, String fonction, String email, String mobile, int nbPretEnCours, int nbFoisPret, int amende_Lecteur) {
        Id = id;
        Nom_Lecteur = nom_Lecteur;
        Fonction = fonction;
        Email = email;
        Mobile = mobile;
        NbPretEnCours = nbPretEnCours;
        NbFoisPret = nbFoisPret;
        Amende_Lecteur = amende_Lecteur;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getNom_Lecteur() {
        return Nom_Lecteur;
    }

    public void setNom_Lecteur(String nom_Lecteur) {
        this.Nom_Lecteur = nom_Lecteur;
    }

    public String getFonction() {
        return Fonction;
    }

    public void setFonction(String fonction) {
        this.Fonction = fonction;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }

    public int getAmende_Lecteur() {
        return Amende_Lecteur;
    }

    public void setAmende_Lecteur(int amende_Lecteur) {
        this.Amende_Lecteur = amende_Lecteur;
    }

    public int getNbPretEnCours() {
        return NbPretEnCours;
    }

    public void setNbPretEnCours(int nbPretEnCours) {
        NbPretEnCours = nbPretEnCours;
    }

    public int getNbFoisPret() {
        return NbFoisPret;
    }

    public void setNbFoisPret(int nbFoisPret) {
        NbFoisPret = nbFoisPret;
    }
}
