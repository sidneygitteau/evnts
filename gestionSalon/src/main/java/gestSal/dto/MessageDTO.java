package gestSal.dto;

import gestSal.modele.Message;

import java.util.Date;

public class MessageDTO {
    private int idMessage;
    private String auteur,contenu;
    private String date;
    private boolean isSeen;

    public MessageDTO(int idMessage, String auteur, String contenu, String date, boolean isSeen) {
        this.idMessage = idMessage;
        this.auteur = auteur;
        this.contenu = contenu;
        this.date = date;
        this.isSeen = isSeen;
    }

    public MessageDTO() {
    }



    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
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

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
