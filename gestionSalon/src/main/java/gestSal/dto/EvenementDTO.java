package gestSal.dto;

import bdd.InteractionBDDSalon;
import gestSal.facade.erreurs.EvenementIncompletException;
import gestSal.modele.Evenement;
import gestSal.modele.Message;
import java.sql.SQLException;
import java.util.List;

public class EvenementDTO {
    private int nombrePersonneMax;
    private String nomEvenement,detailsEvenement,lieu,nomCreateur;
    private String date;
    private boolean estTermine,estValide;


    public EvenementDTO() {
    }

    public static Evenement getEvenementByNomEtNumSalon(int numSalon, String nomEvenement) throws SQLException {
        return InteractionBDDSalon.getEvenementByNomEtNumSalonSQL(numSalon,nomEvenement);
    }

    public static void modifierEvenement(int idSalon, Evenement evenement, String choix, String valeur) throws SQLException {
        InteractionBDDSalon.modifierEvenementSQL(idSalon,evenement,choix,valeur);
    }

    public static Boolean validerEvenement(Evenement evenement) throws SQLException, EvenementIncompletException {
        return InteractionBDDSalon.validerEvenementSQL(evenement);
    }

    public static List<Message> getMessagesEvenement(Evenement evenement) throws SQLException {
        return InteractionBDDSalon.getMessageEventSQL(evenement.getIdEvenement());
    }

    public static String getEvenementById(int idEvent) throws SQLException {
        return InteractionBDDSalon.getEvenementById(idEvent);
    }

    public int getNombrePersonneMax() {
        return nombrePersonneMax;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public String getDetailsEvenement() {
        return detailsEvenement;
    }


    public String getLieu() {
        return lieu;
    }

    public String getNomCreateur() {
        return nomCreateur;
    }

    public String getDate() {
        return date;
    }



}

