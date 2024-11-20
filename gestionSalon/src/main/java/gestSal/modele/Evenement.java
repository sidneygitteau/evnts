package gestSal.modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evenement {
    private int idEvenement,nombrePersonneMax;
    private String nomEvenement,detailsEvenement,lieu,nomCreateur;
    private List<Utilisateur> listeParticipants;
    private String date;
    private boolean estValide, estTermine;
    private List<Message> conversation;

    public Evenement(int idEvenement, int nombrePersonneMax, String nomEvenement, String detailsEvenement, String lieu, String nomCreateur, List<Utilisateur> listeParticipants, String date, boolean estValide, boolean estTermine) {
        this.idEvenement = idEvenement;
        this.nombrePersonneMax = nombrePersonneMax;
        this.nomEvenement = nomEvenement;
        this.detailsEvenement = detailsEvenement;
        this.lieu = lieu;
        this.nomCreateur = nomCreateur;
        this.listeParticipants = listeParticipants;
        this.date = date;
        this.estValide = estValide;
        this.estTermine = estTermine;
        this.conversation=new ArrayList<>();
    }

    public Evenement() {
    }


    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public boolean isEstValide() {
        return estValide;
    }

    public boolean isEstTermine() {
        return estTermine;
    }

    public void setEstTermine(boolean estTermine) {
        this.estTermine = estTermine;
    }

    public int getNombrePersonneMax() {
        return nombrePersonneMax;
    }

    public void setNombrePersonneMax(int nombrePersonneMax) {
        this.nombrePersonneMax = nombrePersonneMax;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }

    public String getDetailsEvenement() {
        return detailsEvenement;
    }

    public void setDetailsEvenement(String detailsEvenement) {
        this.detailsEvenement = detailsEvenement;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getNomCreateur() {
        return nomCreateur;
    }

    public void setNomCreateur(String nomCreateur) {
        this.nomCreateur = nomCreateur;
    }

    public List<Utilisateur> getListeParticipants() {
        return listeParticipants;
    }

    public void setListeParticipants(List<Utilisateur> listeParticipants) {
        this.listeParticipants = listeParticipants;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEstValide(boolean estValide) {
        this.estValide = estValide;
    }

    public List<Message> getConversation() {
        return conversation;
    }

    public void setConversation(List<Message> conversation) {
        this.conversation = conversation;
    }
}
