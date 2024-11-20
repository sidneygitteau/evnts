package com.example.gestiongoogle.facade;

import bdd.InteractionBDDGoogle;
import com.example.gestiongoogle.exceptions.AucunCalendrierTrouveException;
import com.example.gestiongoogle.exceptions.DateFinEvenementInvalideException;
import com.example.gestiongoogle.exceptions.EmailDejaPritException;
import com.example.gestiongoogle.exceptions.ProblemeEnvoiMailException;
import com.example.gestiongoogle.modele.Utilisateur;
import com.example.gestiongoogle.service.EmailService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import dto.UtilisateurDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("facadeGoogle")
public class FacadeGoogleImpl implements IFacadeGoogle {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @Autowired
    private EmailService emailService;

    private InteractionBDDGoogle interactionBDDGoogle;

    private Map<String, Utilisateur> utilisateurs;

    private static final String SUFFIXE=":00.000Z";

    public FacadeGoogleImpl(){
        this.utilisateurs = new HashMap<>();
        this.interactionBDDGoogle = new InteractionBDDGoogle();
    }


    public List<Event> listerEvenements(Authentication authentication, String dateDebut, String dateFin) throws IOException, GeneralSecurityException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        Calendar service = new com.google.api.services.calendar.Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Evnts")
                .build();

        //on convertit le string en formulaire
        DateTime startDateTime = new DateTime(dateDebut+SUFFIXE);
        DateTime endDateTime = new DateTime(dateFin+SUFFIXE);

        Events events = service.events().list("primary")
                .setTimeMin(startDateTime)
                .setTimeMax(endDateTime)
                .execute();

        return events.getItems();
    }

    public String ajouterEvenementCalendrier(Authentication authentication, String nom, String location, String debut, String fin, String description) throws IOException, GeneralSecurityException, AucunCalendrierTrouveException, DateFinEvenementInvalideException {

        Event event = new Event()
                .setSummary(nom)
                .setLocation(location)
                .setDescription(description);

        //on convertit le string en formulaire
        DateTime startDateTime = new DateTime(debut+SUFFIXE); // Ajout de secondes et fuseau horaire UTC
        DateTime endDateTime = new DateTime(fin+SUFFIXE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        // Conversion des chaînes en LocalDateTime
        LocalDateTime startDateTimeVerif = LocalDateTime.parse(debut, formatter);
        LocalDateTime endDateTimeVerif = LocalDateTime.parse(fin, formatter);

        // Vérification que endDateTime n'est pas avant startDateTime
        if (endDateTimeVerif.isBefore(startDateTimeVerif)) {
            throw new DateFinEvenementInvalideException();
        }


        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Paris");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Paris");
        event.setEnd(end);
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        //String email = oidcUser.getEmail();

        // Récupérer le token d'accès OAuth2
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();

        // Configurer le client de l'API Google Calendar
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        Calendar service = new com.google.api.services.calendar.Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Evnts") // Nom de votre application
                .build();
        // Récupérer la liste des calendriers de l'utilisateur
        String calendarId = null;
        CalendarList calendarList = service.calendarList().list().execute();
        for (CalendarListEntry calendarListEntry : calendarList.getItems()) {
            //on tente de recuperer le calendrier principal
            if (calendarListEntry.getPrimary() != null && calendarListEntry.getPrimary()) {
                calendarId = calendarListEntry.getId();
                break;
            }
        }
        if (calendarId == null) {
            // Gérer l'absence d'un calendrier correspondant
            throw new AucunCalendrierTrouveException();
        }
        // Ajouter l'événement au calendrier choisi
        event = service.events().insert(calendarId, event).execute();
        return "Événement ajouté : " + event.getHtmlLink();

    }

    public void envoyerMailValidationEvenement(Authentication authentication) throws ProblemeEnvoiMailException {
        try{
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

            String email = oidcUser.getEmail();

            emailService.sendSimpleMessage(email, "Evenement bien créé", "Bonjour, \n Nous vous confirmons la bonne création de votre evenement.\nA bientot!");
        }catch(Exception e){
            throw new ProblemeEnvoiMailException();
        }
    }

    public void envoyerMailParContenu(String email, String sujet,String contenu) throws ProblemeEnvoiMailException {
        try{
            emailService.sendSimpleMessage(email,sujet,contenu);
        }catch(Exception e){
            throw new ProblemeEnvoiMailException();
        }
    }

    public boolean verifierUtilisateurExistant(String email) {
        return utilisateurs.containsKey(email);
    }

    public void newUtilisateur(String email) throws SQLException, EmailDejaPritException {
        if (utilisateurs.containsKey(email)) {
            throw new EmailDejaPritException();
        }
        UtilisateurDTO.ajouterUtilisateur(email);

    }



    public String ajouterEvenementCalendrierLink(String nom, String location, String debut, String fin, String description) throws DateFinEvenementInvalideException {
        String rendu = "https://www.google.com/calendar/render?action=TEMPLATE";
        rendu+="&text="+nom;

        // Conversion des chaînes en LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startDateTimeVerif = LocalDateTime.parse(debut, formatter);
        LocalDateTime endDateTimeVerif = LocalDateTime.parse(fin, formatter);
        // Vérification que endDateTime n'est pas avant startDateTime
        if (endDateTimeVerif.isBefore(startDateTimeVerif)) {
            throw new DateFinEvenementInvalideException();
        }
        String dateDebutFormatLink = debut.replace("-","").replace(":","")+"00Z";
        String dateFinFormatLink = fin.replace("-","").replace(":","")+"00Z";
        rendu+="&dates="+dateDebutFormatLink+"/"+dateFinFormatLink;
        rendu+="&ctz=Europe/Paris";
        rendu+="&details="+description;
        rendu+="&location="+location;
        rendu=rendu.replace(" ","%20");
        return rendu;
    }

    public void deleteUtilisateur(String email) {
        try {
            UtilisateurDTO.deleteUtilisateur(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
