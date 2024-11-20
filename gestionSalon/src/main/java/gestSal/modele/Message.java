package gestSal.modele;

import java.util.Date;

public class Message {
    private int idMessage;
    private String auteur,contenu;
    private String date;

    public Message(int idMessage, String auteur , String contenu, String date ) {
        this.idMessage = idMessage;
        this.auteur = auteur;
        this.contenu = contenu;
        this.date = date;
    }

    public Message() {
    }

    public int getIdMessage() {
        return idMessage;
    }


    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
