package gestSal.facade;

import gestSal.dto.*;
import gestSal.facade.erreurs.*;
import gestSal.modele.*;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component("facadeSalon")
public class FacadeSalonImpl implements FacadeSalon {


    public FacadeSalonImpl(){
    }

    @Override
    public Salon creerSalon(String nomCreateur, String nomSalon) throws NomSalonVideException, NomTropCourtException {
        Salon salonCree;
        if(nomSalon.isBlank()){
            throw new NomSalonVideException();
        }else if(nomSalon.length()<=3){
            throw new NomTropCourtException();
        }else{
            int id = generateRandom4DigitNumber();
            salonCree = new Salon();
            salonCree.setNomSalon(nomSalon);
            salonCree.setNomCreateur(nomCreateur);
            salonCree.setNumSalon(id);
        }
        SalonDTO.creerSalonSQL(nomSalon,nomCreateur,"https://e7.pngegg.com/pngimages/872/540/png-clipart-computer-icons-event-management-event-miscellaneous-angle-thumbnail.png");
        return salonCree;
    }


    @Override
    public Salon modifierSalon(Salon salon, String choix, String valeur)  {
        int id = 0;
        try {
            id = SalonDTO.getSalonByName(salon.getNomSalon()).getIdSalon();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            salon = SalonDTO.modifierSalonSQL(salon,choix,valeur,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return salon;
    }

    @Override
    public void rejoindreSalon(Utilisateur utilisateur, Salon salonRejoint)  {
        try {
            SalonDTO.rejoindreSalon(utilisateur,salonRejoint);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String inviterUtilisateur(Salon salonInvite, Utilisateur utilisateurInvite) {

        String baseUrl = "https://evnt.com/invitation?code=";
        String invitationCode = generateRandomCode(); // Vous pouvez ajuster la longueur du code ici
        return baseUrl + invitationCode;

    }

    @Override
    public Utilisateur getUtilisateurByPseudo(String pseudoUtilisateur) throws NomUtilisateurVideException {
        Utilisateur utilisateur ;
        if(pseudoUtilisateur.isBlank()){
            throw new NomUtilisateurVideException();
        }
        try {
            utilisateur = UtilisateurDTO.getUtilisateurByPseudo(pseudoUtilisateur);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return utilisateur;

    }

    @Override
    public Salon getSalonByNum(int numSalon)   {
        Salon salon;
        try {
            salon = SalonDTO.getSalonById(numSalon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salon;
    }

    @Override
    public Salon getSalonByNom(String nomSalon)  {
        Salon salon;
        try {
            salon = SalonDTO.getSalonByName(nomSalon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salon;
    }

    @Override
    public void retirerModerateurDuSalon(Salon salon, Utilisateur utilisateurPlusModo) {
        try {
            SalonDTO.retirerModerateurDuSalon(salon,utilisateurPlusModo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void ajouterModerateurAuSalon(Utilisateur nouveauModo, Salon salonPourLeNouveauModo) throws UtilisateurDejaModoException {
        try {
            SalonDTO.ajouterModerateurAuSalon(nouveauModo,salonPourLeNouveauModo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Utilisateur> seDefiniCommePresentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement)   {
        List<Utilisateur> lesParticipants;
        try {
            lesParticipants = UtilisateurDTO.seDefiniCommePresentAUnEvenement(utilisateur,salon,evenement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lesParticipants;
    }

    @Override
    public List<Utilisateur> seDefiniCommeAbsentAUnEvenement(Utilisateur utilisateur, Salon salon, Evenement evenement)  {
        List<Utilisateur> lesParticipants;
        try {
            lesParticipants = UtilisateurDTO.seDefiniCommeAbsentAUnEvenement(utilisateur,salon,evenement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lesParticipants;
    }

    @Override
    public Evenement getEvenementByNomEtNumSalon(int numSalon, String nomEvenement)   {
        Evenement evenement ;
        try {
            evenement = EvenementDTO.getEvenementByNomEtNumSalon(numSalon,nomEvenement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return evenement;
    }

    @Override
    public Evenement creerEvenement(Salon salon, String nomEvenement, int nombrePersonneMax, String detailsEvenement, String lieu, Utilisateur createur, String date) {
        Evenement event;
        try {
            event =SalonDTO.creerEvenement(salon,nomEvenement,nombrePersonneMax,detailsEvenement,lieu,createur,date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return event;
    }



    @Override
    public Evenement modifierEvenement(int idSalon, Evenement evenement, String choix, String valeur)   {
        try {
            EvenementDTO.modifierEvenement(idSalon,evenement,choix,valeur);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return evenement;
    }

    @Override
    public boolean validerEvenement(Evenement evenement)   {
        boolean isValide;
        try {
            isValide = EvenementDTO.validerEvenement(evenement);
        } catch (SQLException | EvenementIncompletException e) {
            throw new RuntimeException(e);
        }

        return isValide;
    }

    @Override
    public void envoyerMessageSalon(Salon salon, String pseudoUtilisateur, String contenu)  {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        try {
            SalonDTO.envoyerMessageSalon(salon.getIdSalon(),pseudoUtilisateur,contenu,dateTime);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void envoyerMessageEvenement(Evenement evenement, String pseudoUtilisateur, String contenu) {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        try {
            SalonDTO.envoyerMessageEvenement(evenement.getIdEvenement(),pseudoUtilisateur,contenu,dateTime);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public List<Message> getMessagesSalon(Salon salon)  {
        List<Message> lesMessagesDuSalon;
        try {
            lesMessagesDuSalon = SalonDTO.getMessagesSalon(salon);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lesMessagesDuSalon;
    }
    @Override
    public List<Message> getMessagesEvenement(Evenement evenement) {
        List<Message> lesMessagesDEvent;

        try {
            lesMessagesDEvent = EvenementDTO.getMessagesEvenement(evenement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lesMessagesDEvent;
    }

    public Utilisateur getUtilisateurByEmail(String email){
        try {
            return UtilisateurDTO.getUtilisateurByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void supprimerUtilisateur(Utilisateur pseudoUtilisateur) {
        try {
            UtilisateurDTO.supprimerUtilisateur(pseudoUtilisateur);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getNomEvenementById(int idEvent) {
        try {
            return EvenementDTO.getEvenementById(idEvent);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Salon> getSalonByUser(String nom) throws NomUtilisateurVideException, SQLException {
        int idUser = getUtilisateurByPseudo(nom).getIdUtilisateur();
        List<Integer> lesSalonsDeLUser = UtilisateurDTO.getSalonByUser(idUser);
        List<Salon> lesSalons = new ArrayList<>();
        for(int idSalon : lesSalonsDeLUser) lesSalons.add(getSalonByNum(idSalon));
        return  lesSalons;
    }

    @Override
    public List<Evenement> getEvenementsUser(String nom) throws NomUtilisateurVideException, SQLException {
        int idUser = getUtilisateurByPseudo(nom).getIdUtilisateur();
        List<Integer> lesIdEvent = UtilisateurDTO.getEventByUser(idUser);
        List<Evenement> lesEvenements = new ArrayList<>();
        for(int idE : lesIdEvent) lesEvenements.add(getEventById(idE));
        return lesEvenements;
    }



    @Override
    public Evenement getEventById(int idEvenement) {
        try {
            return UtilisateurDTO.getEventById(idEvenement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ajouterMembre(String nomMembre, String email) {
        try {
            UtilisateurDTO.ajouterMembre(nomMembre,email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private String generateRandomCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder randomCode = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomValue = secureRandom.nextInt('Z' - 'A' + 1) + 'A';
            randomCode.append((char) randomValue);
        }

        return randomCode.toString();
    }
    private int generateRandom4DigitNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }
}
