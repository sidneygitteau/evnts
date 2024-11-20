import gestSal.dto.EvenementDTO;
import gestSal.dto.SalonDTO;
import gestSal.dto.UtilisateurDTO;
import gestSal.facade.FacadeSalonImpl;
import gestSal.facade.erreurs.*;
import gestSal.modele.Evenement;
import gestSal.modele.Message;
import gestSal.modele.Salon;
import gestSal.modele.Utilisateur;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacadeSalonImplTest {

    private MockedStatic<SalonDTO> mockedSalonDTO;

    private MockedStatic<UtilisateurDTO> mockedUtilisateurDTO;

    private MockedStatic<EvenementDTO> mockedEvenementDTO;
    private FacadeSalonImpl facadeSalon;
    @BeforeEach
    void init(){
        mockedSalonDTO = Mockito.mockStatic(SalonDTO.class);
        mockedUtilisateurDTO = Mockito.mockStatic(UtilisateurDTO.class);
        mockedEvenementDTO = Mockito.mockStatic(EvenementDTO.class);
        facadeSalon = new FacadeSalonImpl();
    }

    @AfterEach
    void finish() {
        mockedSalonDTO.close();
        mockedUtilisateurDTO.close();
        mockedEvenementDTO.close();
    }
    @Test
    void testCreerSalonAvecNomValideOK() {
        try {
            FacadeSalonImpl service = new FacadeSalonImpl();
            String nomCreateur = "Vincent";
            String nomSalon = "Salon de One piece";

            Salon result = service.creerSalon(nomCreateur, nomSalon);

            // verif du salon
            assertNotNull(result);
            assertEquals(nomSalon, result.getNomSalon());
            assertEquals(nomCreateur, result.getNomCreateur());

            mockedSalonDTO.verify(() -> SalonDTO.creerSalonSQL(
                    nomSalon,
                    nomCreateur,
                    "https://e7.pngegg.com/pngimages/872/540/png-clipart-computer-icons-event-management-event-miscellaneous-angle-thumbnail.png"
            ));
        } catch (NomSalonVideException | NomTropCourtException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreerSalonAvecNomVideKO() {
        FacadeSalonImpl service = new FacadeSalonImpl();
        String nomCreateur = "Vincent";
        String nomSalon = "";  // Nom vide

        assertThrows(NomSalonVideException.class, () -> service.creerSalon(nomCreateur, nomSalon));
    }

    @Test
    void testCreerSalonAvecNomTropCourt() {
        FacadeSalonImpl service = new FacadeSalonImpl();
        String nomCreateur = "Vincent";
        String nomSalon = "HI";  // Nom trop court

        assertThrows(NomTropCourtException.class, () -> service.creerSalon(nomCreateur, nomSalon));
    }

    @Test
    void testModifierSalonNomValide() throws SQLException {
        // Arrange
        Salon salon = new Salon();
        salon.setNomSalon("AncienNom");
        salon.setIdSalon(1); // Supposons que l'ID du salon est 1

        when(SalonDTO.getSalonByName("AncienNom")).thenReturn(salon);
        when(SalonDTO.modifierSalonSQL(any(Salon.class), anyString(), anyString(), anyInt()))
                .thenAnswer(invocation -> {
                    Salon s = invocation.getArgument(0);
                    s.setNomSalon(invocation.getArgument(2));
                    return s;
                });

        FacadeSalonImpl facadeSalon = new FacadeSalonImpl();

        // Act
        Salon updatedSalon = facadeSalon.modifierSalon(salon, "nom", "NouveauNom");

        // Assert
        assertNotNull(updatedSalon);
        assertEquals("NouveauNom", updatedSalon.getNomSalon());
        mockedSalonDTO.verify(() -> SalonDTO.getSalonByName("AncienNom"));
        mockedSalonDTO.verify(() -> SalonDTO.modifierSalonSQL(salon, "nom", "NouveauNom", 1));
    }

    @Test
    void testModifierSalonThrowsRuntimeExceptionOnGetSalonByName() {
        String nomSalon = "Salon de One Piece";
        mockedSalonDTO.when(() -> SalonDTO.getSalonByName(anyString()))
                .thenThrow(new SQLException("Database error on fetching salon"));

        FacadeSalonImpl service = new FacadeSalonImpl();
        assertThrows(RuntimeException.class, () -> service.modifierSalon(new Salon(), "nom", "Nouveau Nom"),
                "Erreur lors de la récupération du salon par nom : " + nomSalon);
    }

    @Test
    void testModifierSalonThrowsRuntimeExceptionOnModifierSalonSQL() {
        Salon salon = new Salon();
        salon.setNomSalon("Salon de One Piece");
        mockedSalonDTO.when(() -> SalonDTO.getSalonByName(anyString())).thenReturn(salon);
        mockedSalonDTO.when(() -> SalonDTO.modifierSalonSQL(eq(salon), anyString(), anyString(), anyInt()))
                .thenThrow(new SQLException("Database error on modifying salon"));

        FacadeSalonImpl service = new FacadeSalonImpl();
        assertThrows(RuntimeException.class, () -> service.modifierSalon(salon, "nom", "Nouveau Nom"),
                "Erreur lors de la modification du salon : " + salon.getNomSalon());
    }

    @Test
    void testRejoindreSalonSuccess() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(1);
        Salon salon = new Salon();
        salon.setIdSalon(2);

        // Act
        FacadeSalonImpl service = new FacadeSalonImpl();
        assertDoesNotThrow(() -> service.rejoindreSalon(utilisateur, salon));

        // Assert
        mockedSalonDTO.verify(() -> SalonDTO.rejoindreSalon(utilisateur, salon));
    }

    @Test
    void testRejoindreSalonThrowsRuntimeExceptionOnSQLException() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(1);
        Salon salon = new Salon();
        salon.setIdSalon(2);
        mockedSalonDTO.when(() -> SalonDTO.rejoindreSalon(any(Utilisateur.class), any(Salon.class)))
                .thenThrow(new SQLException("Erreur en rejoignant"));

        FacadeSalonImpl service = new FacadeSalonImpl();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.rejoindreSalon(utilisateur, salon),
                "SQL exception renvoie Runtime");
    }

    @Test
    void testInviterUtilisateurReturnsValidUrl() {
        // Arrange
        FacadeSalonImpl service = new FacadeSalonImpl();
        Salon salon = new Salon();
        Utilisateur utilisateur = new Utilisateur();

        // Act
        String invitationUrl = service.inviterUtilisateur(salon, utilisateur);

        // Assert
        assertNotNull(invitationUrl, "L'URL d'invitation ne devrait pas être nulle.");
        assertTrue(invitationUrl.startsWith("https://evnt.com/invitation?code="),
                "L'URL d'invitation doit commencer par le préfixe spécifié.");
    }

    @Test
    void testInviterUtilisateurCodeLength() {
        // Arrange
        FacadeSalonImpl service = new FacadeSalonImpl();
        Salon salon = new Salon();
        Utilisateur utilisateur = new Utilisateur();

        // Act
        String invitationUrl = service.inviterUtilisateur(salon, utilisateur);
        String codePart = invitationUrl.substring("https://evnt.com/invitation?code=".length());

        // Assert
        assertNotNull(codePart, "Le code d'invitation ne devrait pas être nul.");
        assertFalse(codePart.isEmpty(), "Le code d'invitation devrait avoir une longueur positive.");
    }

    @Test
    void getUtilisateurByPseudoReturnsUser() throws NomUtilisateurVideException {
        String pseudo = "user123";
        Utilisateur expectedUser = new Utilisateur();
        expectedUser.setPseudo(pseudo);
        expectedUser.setIdUtilisateur(1);
        expectedUser.setEmail("user123@example.com");

        mockedUtilisateurDTO.when(() -> UtilisateurDTO.getUtilisateurByPseudo(pseudo))
                .thenReturn(expectedUser);

        Utilisateur result = facadeSalon.getUtilisateurByPseudo(pseudo);

        assertNotNull(result);
        assertEquals(pseudo, result.getPseudo());
        assertEquals(1, result.getIdUtilisateur());
        assertEquals("user123@example.com", result.getEmail());
    }

    @Test
    void getUtilisateurByPseudoThrowsRuntimeExceptionOnSQLException()  {
        String pseudo = "userNotFound";
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.getUtilisateurByPseudo(pseudo))
                .thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> facadeSalon.getUtilisateurByPseudo(pseudo));
    }

    @Test
    void getUtilisateurByPseudoThrowsNomUtilisateurVideExceptionForEmptyPseudo() {
        String pseudo = "";
        assertThrows(NomUtilisateurVideException.class, () -> facadeSalon.getUtilisateurByPseudo(pseudo));
    }

    @Test
    void testGetSalonByNumReturnsSalon(){
        int salonId = 1;
        Salon expectedSalon = new Salon();
        expectedSalon.setIdSalon(salonId);
        expectedSalon.setNomSalon("Test Salon");
        expectedSalon.setNomCreateur("Romain Camachalo");
        expectedSalon.setLogo("https://carronlebon.com/asterion.png");

        mockedSalonDTO.when(() -> SalonDTO.getSalonById(salonId)).thenReturn(expectedSalon);

        Salon result = facadeSalon.getSalonByNum(salonId);

        assertNotNull(result);
        assertEquals(salonId, result.getIdSalon());
        assertEquals("Test Salon", result.getNomSalon());
        assertEquals("Romain Camachalo", result.getNomCreateur());
        assertEquals("https://carronlebon.com/asterion.png", result.getLogo());
    }

    @Test
    void testGetSalonByNumThrowsRuntimeExceptionOnSQLException() {
        int salonId = 1;
        mockedSalonDTO.when(() -> SalonDTO.getSalonById(salonId))
                .thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> facadeSalon.getSalonByNum(salonId));
    }

    @Test
    void testGetSalonByNumReturnsNullSalon() {
        int salonId = 1;
        mockedSalonDTO.when(() -> SalonDTO.getSalonById(salonId)).thenReturn(null);

        Salon result = facadeSalon.getSalonByNum(salonId);

        assertNull(result, "");
    }

    @Test
    void testGetSalonByNomReturnsSalon(){
        String nomSalon = "Test Salon";
        Salon expectedSalon = new Salon();
        expectedSalon.setIdSalon(1);
        expectedSalon.setNomSalon(nomSalon);
        expectedSalon.setNomCreateur("Vincent AUBER");
        expectedSalon.setLogo("https://sitequitue.com/psglesgoat.png");

        mockedSalonDTO.when(() -> SalonDTO.getSalonByName(nomSalon)).thenReturn(expectedSalon);

        Salon result = facadeSalon.getSalonByNom(nomSalon);

        assertNotNull(result);
        assertEquals(nomSalon, result.getNomSalon());
        assertEquals("Vincent AUBER", result.getNomCreateur());
        assertEquals("https://sitequitue.com/psglesgoat.png", result.getLogo());
    }

    @Test
    void testGetSalonByNomThrowsRuntimeExceptionOnSQLException() {
        String nomSalon = "Salon qui existe pas";
        mockedSalonDTO.when(() -> SalonDTO.getSalonByName(nomSalon))
                .thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> facadeSalon.getSalonByNom(nomSalon));
    }

    @Test
    void testGetSalonByNomReturnsNullWhenNotFound() {
        String nomSalon = "Salon Vide";
        mockedSalonDTO.when(() -> SalonDTO.getSalonByName(nomSalon)).thenReturn(null);

        Salon result = facadeSalon.getSalonByNom(nomSalon);

        assertNull(result, "Null quand aucun salon n'y correspond");
    }

    @Test
    void testRetirerModerateurDuSalonSuccess() {
        Salon salon = new Salon();
        salon.setIdSalon(1);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(2);

        // Configurer le mock pour simuler la méthode statique void
        mockedSalonDTO.when(() -> SalonDTO.retirerModerateurDuSalon(salon, utilisateur)).then(invocation -> null);

        // Vérifier que l'appel ne lance pas d'exception
        assertDoesNotThrow(() -> facadeSalon.retirerModerateurDuSalon(salon, utilisateur));
    }

    @Test
    void testRetirerModerateurDuSalonThrowsRuntimeExceptionOnSQLException(){
        Salon salon = new Salon();
        salon.setIdSalon(1);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(2);

        mockedSalonDTO.when(() -> SalonDTO.retirerModerateurDuSalon(salon, utilisateur))
                .thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> facadeSalon.retirerModerateurDuSalon(salon, utilisateur));
    }

    @Test
    void testAjouterModerateurAuSalonSuccess() {
        Utilisateur nouveauModo = new Utilisateur();
        nouveauModo.setIdUtilisateur(1);
        Salon salon = new Salon();
        salon.setIdSalon(2);

        mockedSalonDTO.when(() -> SalonDTO.ajouterModerateurAuSalon(nouveauModo, salon)).then(invocation -> null);

        assertDoesNotThrow(() -> facadeSalon.ajouterModerateurAuSalon(nouveauModo, salon));
    }

    @Test
    void testAjouterModerateurAuSalonThrowsRuntimeExceptionOnSQLException() {
        Utilisateur nouveauModo = new Utilisateur();
        nouveauModo.setIdUtilisateur(1);
        Salon salon = new Salon();
        salon.setIdSalon(2);

        mockedSalonDTO.when(() -> SalonDTO.ajouterModerateurAuSalon(nouveauModo, salon))
                .thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> facadeSalon.ajouterModerateurAuSalon(nouveauModo, salon));
    }

    @Test
    void testAjouterModerateurAuSalonThrowsUtilisateurDejaModoException(){
        Utilisateur nouveauModo = new Utilisateur();
        nouveauModo.setIdUtilisateur(1);
        Salon salon = new Salon();
        salon.setIdSalon(2);

        mockedSalonDTO.when(() -> SalonDTO.ajouterModerateurAuSalon(nouveauModo, salon))
                .thenThrow(new UtilisateurDejaModoException());

        assertThrows(UtilisateurDejaModoException.class, () -> facadeSalon.ajouterModerateurAuSalon(nouveauModo, salon));
    }

    @Test
    void testSeDefiniCommePresentAUnEvenementSuccess() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(1);
        Salon salon = new Salon();
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);

        List<Utilisateur> expectedParticipants = List.of(utilisateur);
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement))
                .thenReturn(expectedParticipants);

        List<Utilisateur> result = facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedParticipants.size(), result.size());
        assertEquals(utilisateur.getIdUtilisateur(), result.get(0).getIdUtilisateur());
    }

    @Test
    void testSeDefiniCommePresentAUnEvenementThrowsRuntimeExceptionOnSQLException() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(1);
        Salon salon = new Salon();
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);

        mockedUtilisateurDTO.when(() -> UtilisateurDTO.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement))
                .thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> facadeSalon.seDefiniCommePresentAUnEvenement(utilisateur, salon, evenement));
    }

    @Test
    void testSeDefiniCommeAbsentAUnEvenementSuccess() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(1);
        Salon salon = new Salon();
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);

        List<Utilisateur> expectedParticipants = List.of();
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement))
                .thenReturn(expectedParticipants);

        List<Utilisateur> result = facadeSalon.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Liste vide car user absent");
    }

    @Test
    void testSeDefiniCommeAbsentAUnEvenementThrowsRuntimeExceptionOnSQLException() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(1);
        Salon salon = new Salon();
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);

        mockedUtilisateurDTO.when(() -> UtilisateurDTO.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement))
                .thenThrow(new SQLException("Erreur BDD"));

        assertThrows(RuntimeException.class, () -> facadeSalon.seDefiniCommeAbsentAUnEvenement(utilisateur, salon, evenement));
    }

    @Test
    void testGetEvenementByNomEtNumSalonSuccess() {
        int numSalon = 1;
        String nomEvenement = "Concert de rock";
        Evenement expectedEvenement = new Evenement();
        expectedEvenement.setIdEvenement(1);
        expectedEvenement.setNomEvenement(nomEvenement);

        mockedEvenementDTO.when(() -> EvenementDTO.getEvenementByNomEtNumSalon(numSalon, nomEvenement))
                .thenReturn(expectedEvenement);

        Evenement result = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement);

        assertNotNull(result);
        assertEquals(nomEvenement, result.getNomEvenement());
        assertEquals(numSalon, result.getIdEvenement());
    }

    @Test
    void testGetEvenementByNomEtNumSalonThrowsRuntimeExceptionOnSQLException() {
        int numSalon = 1;
        String nomEvenement = "Concert de rock";

        mockedEvenementDTO.when(() -> EvenementDTO.getEvenementByNomEtNumSalon(numSalon, nomEvenement))
                .thenThrow(new SQLException("Erreur de la base de données"));

        assertThrows(RuntimeException.class, () -> facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement));
    }

    @Test
    void testGetEvenementByNomEtNumSalonReturnsNullForNonexistentEvent(){
        int numSalon = 1;
        String nomEvenement = "Concert de rock";

        mockedEvenementDTO.when(() -> EvenementDTO.getEvenementByNomEtNumSalon(numSalon, nomEvenement))
                .thenReturn(null);

        Evenement result = facadeSalon.getEvenementByNomEtNumSalon(numSalon, nomEvenement);

        assertNull(result, "Devrait retourner null si aucun événement n'est trouvé.");
    }

    @Test
    void testCreerEvenementSuccess() {
        Salon salon = new Salon();
        salon.setIdSalon(1);
        String nomEvenement = "Concert de SKUT SKUT";
        int nombrePersonneMax = 100;
        String detailsEvenement = "OMG LA DINGZ";
        String lieu = "Paris";
        Utilisateur createur = new Utilisateur();
        createur.setPseudo("Momo on t'aime");
        String date = "2024-05-20";

        Evenement expectedEvenement = new Evenement();
        expectedEvenement.setNomEvenement(nomEvenement);

        mockedSalonDTO.when(() -> SalonDTO.creerEvenement(salon, nomEvenement, nombrePersonneMax, detailsEvenement, lieu, createur, date))
                .thenReturn(expectedEvenement);

        Evenement result = facadeSalon.creerEvenement(salon, nomEvenement, nombrePersonneMax, detailsEvenement, lieu, createur, date);

        assertNotNull(result);
        assertEquals(nomEvenement, result.getNomEvenement());
    }

    @Test
    void testCreerEvenementThrowsRuntimeExceptionOnSQLException(){
        Salon salon = new Salon();
        salon.setIdSalon(1);
        String nomEvenement = "Grosse fete chez aubert";
        int nombrePersonneMax = 100;
        String detailsEvenement = "Projet X pour feter le projet";
        String lieu = "Paris";
        Utilisateur createur = new Utilisateur();
        createur.setPseudo("Chloe Cheval");
        String date = "2024-05-20";

        mockedSalonDTO.when(() -> SalonDTO.creerEvenement(salon, nomEvenement, nombrePersonneMax, detailsEvenement, lieu, createur, date))
                .thenThrow(new SQLException("Erreur de la base de données"));

        assertThrows(RuntimeException.class, () -> facadeSalon.creerEvenement(salon, nomEvenement, nombrePersonneMax, detailsEvenement, lieu, createur, date));
    }

    @Test
    void testCreerEvenementThrowsConflictOnDuplicateEvent() {
        Salon salon = new Salon();
        salon.setIdSalon(1);
        String nomEvenement = "Concert de CS2";
        int nombrePersonneMax = 100;
        String detailsEvenement = "Une tuerie";
        String lieu = "Paris";
        Utilisateur createur = new Utilisateur();
        createur.setPseudo("Baptie");
        String date = "2024-05-20";

        mockedSalonDTO.when(() -> SalonDTO.creerEvenement(salon, nomEvenement, nombrePersonneMax, detailsEvenement, lieu, createur, date))
                .thenThrow(new SQLException("L'évènement existe déjà", "409"));

        Exception exception = assertThrows(RuntimeException.class, () -> facadeSalon.creerEvenement(salon, nomEvenement, nombrePersonneMax, detailsEvenement, lieu, createur, date));
        assertInstanceOf(SQLException.class, exception.getCause());
        assertEquals("L'évènement existe déjà", exception.getCause().getMessage());
    }


    @Test
    void testModifierEvenementThrowsRuntimeExceptionOnSQLException(){
        int idSalon = 1;
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        evenement.setNomEvenement("Concert Classique");

        mockedEvenementDTO.when(() -> EvenementDTO.modifierEvenement(idSalon, evenement, "nom", "Concert Rock"))
                .thenThrow(new SQLException("Erreur lors de la mise à jour"));

        assertThrows(RuntimeException.class, () -> facadeSalon.modifierEvenement(idSalon, evenement, "nom", "Concert Rock"));
    }
    /*
    @Test
    void testModifierEvenementSuccess() throws SQLException {
        int idSalon = 1;
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        evenement.setNomEvenement("Concert Classique");

        // Utilisez Mockito.when() pour ajuster la méthode simulée
        Mockito.when(() -> EvenementDTO.modifierEvenement(idSalon, evenement, "nom", "Concert Rock")).then(invocation -> null);

        Evenement modifiedEvent = facadeSalon.modifierEvenement(idSalon, evenement, "nom", "Concert Rock");

        assertNotNull(modifiedEvent);
        assertEquals("Concert Rock", modifiedEvent.getNomEvenement());
    }
    @Test
    void testModifierEvenementDescription() throws SQLException {
        int idSalon = 1;
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        evenement.setDetailsEvenement("Old Details");

        // Configurer le mock pour simuler la modification sans erreur
        mockedEvenementDTO.when(() -> EvenementDTO.modifierEvenement(idSalon, evenement, "description", "New Details")).then(invocation -> null);

        Evenement result = facadeSalon.modifierEvenement(idSalon, evenement, "description", "New Details");

        assertNotNull(result);
        assertEquals("New Details", result.getDetailsEvenement());
    }

    @Test
    void testModifierEvenementDate() throws SQLException {
        int idSalon = 1;
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        evenement.setDate("2024-01-01");

        mockedEvenementDTO.when(() -> EvenementDTO.modifierEvenement(idSalon, evenement, "date", "2024-12-31")).then(invocation -> null);

        Evenement result = facadeSalon.modifierEvenement(idSalon, evenement, "date", "2024-12-31");

        assertNotNull(result);
        assertEquals("2024-12-31", result.getDate());
    }

    @Test
    void testModifierEvenementLieu() throws SQLException {
        int idSalon = 1;
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        evenement.setLieu("Old Venue");

        mockedEvenementDTO.when(() -> EvenementDTO.modifierEvenement(idSalon, evenement, "lieu", "New Venue")).then(invocation -> null);

        Evenement result = facadeSalon.modifierEvenement(idSalon, evenement, "lieu", "New Venue");

        assertNotNull(result);
        assertEquals("New Venue", result.getLieu());
    }

    @Test
    void testModifierEvenementNombrePersonneMax() throws SQLException {
        int idSalon = 1;
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        evenement.setNombrePersonneMax(50);

        mockedEvenementDTO.when(() -> EvenementDTO.modifierEvenement(idSalon, evenement, "nombre", "100")).then(invocation -> null);

        Evenement result = facadeSalon.modifierEvenement(idSalon, evenement, "nombre", "100");

        assertNotNull(result);
        assertEquals(100, result.getNombrePersonneMax());
    }*/

    @Test
    void testValiderEvenementSuccess() {
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        evenement.setNombrePersonneMax(100);

        mockedEvenementDTO.when(() -> EvenementDTO.validerEvenement(evenement))
                .thenReturn(true);

        boolean result = facadeSalon.validerEvenement(evenement);

        assertTrue(result);
    }

    @Test
    void testValiderEvenementThrowsEvenementIncompletException() {
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        evenement.setNombrePersonneMax(100);

        mockedEvenementDTO.when(() -> EvenementDTO.validerEvenement(evenement))
                .thenThrow(new EvenementIncompletException());

        assertThrows(RuntimeException.class, () -> facadeSalon.validerEvenement(evenement));
    }

    @Test
    void testValiderEvenementThrowsRuntimeExceptionOnSQLException() {
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        evenement.setNombrePersonneMax(100);

        mockedEvenementDTO.when(() -> EvenementDTO.validerEvenement(evenement))
                .thenThrow(new SQLException("Erreur de base de données"));

        assertThrows(RuntimeException.class, () -> facadeSalon.validerEvenement(evenement));
    }

    /*
    @Test
    void testEnvoyerMessageSalon(){
        Salon salon = new Salon();
        salon.setIdSalon(1);
        salon.setConversation(new ArrayList<>());
        String pseudo = "sid";

        mockedSalonDTO.when(()->SalonDTO.envoyerMessageSalon(1,pseudo,"ptit message","21/04/2024"))
                .then(salon.getConversation().add(new Message(2,pseudo,"ptit message","21/04/2024")))
    }*/

    @Test
    void testEnvoyerMessageSalonSuccessfully() {
        // Prepare
        Salon salon = new Salon();
        salon.setIdSalon(1);
        String pseudo = "User1";
        String contenu = "Hello, World!";

        // Act & Assert within static mock
        mockedSalonDTO.when(() -> SalonDTO.envoyerMessageSalon(eq(salon.getIdSalon()), eq(pseudo), eq(contenu), anyString()))
                .thenAnswer(invocation -> null);

        facadeSalon.envoyerMessageSalon(salon, pseudo, contenu);

        // Verify the interaction with the static method
        mockedSalonDTO.verify(() -> SalonDTO.envoyerMessageSalon(eq(salon.getIdSalon()), eq(pseudo), eq(contenu), anyString()), times(1));
    }

    @Test
    void testEnvoyerMessageSalonWithSQLException() {
        // Prepare
        Salon salon = new Salon();
        salon.setIdSalon(1);
        String pseudo = "User1";
        String contenu = "Hello, World!";

        // Setup mock to throw an exception
        mockedSalonDTO.when(() -> SalonDTO.envoyerMessageSalon(anyInt(), anyString(), anyString(), anyString()))
                .thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facadeSalon.envoyerMessageSalon(salon, pseudo, contenu));
    }

    @Test
    void testEnvoyerMessageEvenementSuccessfully() {
        // Prepare
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        String pseudo = "User1";
        String contenu = "Hello, Event!";

        // Act & Assert within static mock
        mockedSalonDTO.when(() -> SalonDTO.envoyerMessageEvenement(anyInt(), anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> null);

        facadeSalon.envoyerMessageEvenement(evenement, pseudo, contenu);

        // Verify the interaction with the static method
        mockedSalonDTO.verify(() -> SalonDTO.envoyerMessageEvenement(eq(evenement.getIdEvenement()), eq(pseudo), eq(contenu), anyString()), times(1));
    }

    @Test
    void testEnvoyerMessageEvenementWithSQLException() {
        // Prepare
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        String pseudo = "User1";
        String contenu = "Hello, Event!";

        // Setup mock to throw an exception
        mockedSalonDTO.when(() -> SalonDTO.envoyerMessageEvenement(anyInt(), anyString(), anyString(), anyString()))
                .thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facadeSalon.envoyerMessageEvenement(evenement, pseudo, contenu));
    }

    @Test
    void testGetMessagesSalonSuccessfully() {
        // Prepare
        Salon salon = new Salon();
        salon.setIdSalon(1);
        List<Message> expectedMessages = new ArrayList<>();
        expectedMessages.add(new Message(1, "User1", "Hello, World!", "2024-01-01T12:00:00"));

        // Mocking the static method to return expected messages
        mockedSalonDTO.when(() -> SalonDTO.getMessagesSalon(salon)).thenReturn(expectedMessages);

        // Act
        List<Message> actualMessages = facadeSalon.getMessagesSalon(salon);

        // Assert
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    void testGetMessagesSalonWithSQLException() {
        // Prepare
        Salon salon = new Salon();
        salon.setIdSalon(1);

        // Mocking the static method to throw an SQLException
        mockedSalonDTO.when(() -> SalonDTO.getMessagesSalon(salon)).thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facadeSalon.getMessagesSalon(salon));
    }

    @Test
    void testGetMessagesEvenementSuccessfully() {
        // Prepare
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);
        List<Message> expectedMessages = new ArrayList<>();
        expectedMessages.add(new Message(1, "User2", "Event message", "2024-01-01T13:00:00"));

        // Mocking the static method to return expected messages
        mockedEvenementDTO.when(() -> EvenementDTO.getMessagesEvenement(evenement)).thenReturn(expectedMessages);

        // Act
        List<Message> actualMessages = facadeSalon.getMessagesEvenement(evenement);

        // Assert
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    void testGetMessagesEvenementWithSQLException() {
        // Prepare
        Evenement evenement = new Evenement();
        evenement.setIdEvenement(1);

        // Mocking the static method to throw an SQLException
        mockedEvenementDTO.when(() -> EvenementDTO.getMessagesEvenement(evenement)).thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facadeSalon.getMessagesEvenement(evenement));
    }

    @Test
    void testGetUtilisateurByEmailSuccessfully() {
        // Prepare
        String email = "user@example.com";
        Utilisateur expectedUser = new Utilisateur();
        expectedUser.setIdUtilisateur(1);
        expectedUser.setPseudo("user1");
        expectedUser.setEmail(email);

        // Mocking the static method to return the expected user
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.getUtilisateurByEmail(email)).thenReturn(expectedUser);

        // Act
        Utilisateur actualUser = facadeSalon.getUtilisateurByEmail(email);

        // Assert
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testGetUtilisateurByEmailWithSQLException() {
        // Prepare
        String email = "user@example.com";

        // Mocking the static method to throw an SQLException
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.getUtilisateurByEmail(email)).thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facadeSalon.getUtilisateurByEmail(email));
    }

    @Test
    void testSupprimerUtilisateurSuccessfully() {
        // Prepare
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(1);
        utilisateur.setPseudo("user1");
        utilisateur.setEmail("user1@example.com");

        // Mocking the static method to do nothing (simulate successful deletion)
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.supprimerUtilisateur(utilisateur))
                .thenAnswer(invocation -> null);

        // Act
        facadeSalon.supprimerUtilisateur(utilisateur);

        // Assert
        mockedUtilisateurDTO.verify(() -> UtilisateurDTO.supprimerUtilisateur(utilisateur), times(1));
    }

    @Test
    void testSupprimerUtilisateurWithSQLException() {
        // Prepare
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(1);
        utilisateur.setPseudo("user1");
        utilisateur.setEmail("user1@example.com");

        // Mocking the static method to throw an SQLException
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.supprimerUtilisateur(utilisateur))
                .thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facadeSalon.supprimerUtilisateur(utilisateur));
    }

    @Test
    void testGetNomEvenementByIdSuccessfully() {
        // Prepare
        int idEvent = 1;
        String expectedEventName = "Annual Gala";

        mockedEvenementDTO.when(() -> EvenementDTO.getEvenementById(idEvent))
                .thenReturn(expectedEventName);

        // Act
        String actualEventName = facadeSalon.getNomEvenementById(idEvent);

        // Assert
        assertEquals(expectedEventName, actualEventName);
    }

    @Test
    void testGetNomEvenementByIdWithSQLException() {
        // Prepare
        int idEvent = 1;

        // Mocking the static method to throw an SQLException
        mockedEvenementDTO.when(() -> EvenementDTO.getEvenementById(idEvent))
                .thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facadeSalon.getNomEvenementById(idEvent));
    }

    @Test
    void testGetSalonByUserSuccessfully() throws SQLException, NomUtilisateurVideException {
        String nom = "validUser";
        Utilisateur user = new Utilisateur();
        user.setIdUtilisateur(1);
        List<Integer> salonIds = List.of(101, 102);
        List<Salon> expectedSalons = new ArrayList<>();
        expectedSalons.add(new Salon());
        expectedSalons.add(new Salon());

        when(facadeSalon.getUtilisateurByPseudo(nom)).thenReturn(user);
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.getSalonByUser(user.getIdUtilisateur())).thenReturn(salonIds);
        when(facadeSalon.getSalonByNum(101)).thenReturn(expectedSalons.get(0));
        when(facadeSalon.getSalonByNum(102)).thenReturn(expectedSalons.get(1));

        List<Salon> actualSalons = facadeSalon.getSalonByUser(nom);

        assertEquals(expectedSalons, actualSalons);

    }

    @Test
    void testGetSalonByUserWithEmptyUsername() {
        String nom = "";

        assertThrows(NomUtilisateurVideException.class, () -> facadeSalon.getSalonByUser(nom));
    }

    @Test
    void testGetSalonByUserSQLExceptionOnUserFetch() throws  NomUtilisateurVideException {
        String nom = "validUser";
        when(facadeSalon.getUtilisateurByPseudo(nom)).thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> facadeSalon.getSalonByUser(nom));
    }

    @Test
    void testGetEvenementsUserSuccessfully() throws SQLException, NomUtilisateurVideException {
        String nom = "validUser";
        Utilisateur user = new Utilisateur();
        user.setIdUtilisateur(1);
        List<Integer> eventIds = List.of(101, 102);
        List<Evenement> expectedEvents = new ArrayList<>();
        expectedEvents.add(new Evenement());
        expectedEvents.add(new Evenement());

        when(facadeSalon.getUtilisateurByPseudo(nom)).thenReturn(user);
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.getEventByUser(user.getIdUtilisateur())).thenReturn(eventIds);
        when(facadeSalon.getEventById(101)).thenReturn(expectedEvents.get(0));
        when(facadeSalon.getEventById(102)).thenReturn(expectedEvents.get(1));

        List<Evenement> actualEvents = facadeSalon.getEvenementsUser(nom);

        assertEquals(expectedEvents, actualEvents);
    }

    @Test
    void testGetEvenementsUserWithEmptyUsername() {
        String nom = "";

        assertThrows(NomUtilisateurVideException.class, () -> facadeSalon.getEvenementsUser(nom));
    }

    @Test
    void testGetEvenementsUserSQLExceptionOnUserFetch() throws  NomUtilisateurVideException {
        String nom = "validUser";
        when(facadeSalon.getUtilisateurByPseudo(nom)).thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> facadeSalon.getEvenementsUser(nom));
    }

    @Test
    void testGetEventByIdSuccessfully() {
        // Setup
        int idEvenement = 100;
        Evenement expectedEvent = new Evenement();
        expectedEvent.setIdEvenement(idEvenement);
        expectedEvent.setNomEvenement("Un Barbecue");
        expectedEvent.setNombrePersonneMax(50);
        expectedEvent.setNomCreateur("Vincent");
        expectedEvent.setLieu("Chez moi");
        expectedEvent.setDetailsEvenement("Ça va être trop bien");
        expectedEvent.setDate("2024-07-04");
        expectedEvent.setEstValide(true);

        // Mocking the static method to return the expected event
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.getEventById(idEvenement))
                .thenReturn(expectedEvent);

        // Act
        Evenement actualEvent = facadeSalon.getEventById(idEvenement);

        // Assert
        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    void testGetEventByIdWithSQLException() {
        // Setup
        int idEvenement = 100;

        // Mocking the static method to throw an SQLException
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.getEventById(idEvenement))
                .thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> facadeSalon.getEventById(idEvenement));
    }

    @Test
    void testAjouterMembreSuccessfully() {
        // Prepare
        String nomMembre = "Vincent";
        String email = "vincent@example.com";

        // Act
        // Since no value is returned, we're primarily checking for absence of exceptions here
        assertDoesNotThrow(() -> facadeSalon.ajouterMembre(nomMembre, email));

        // Assert
        mockedUtilisateurDTO.verify(() -> UtilisateurDTO.ajouterMembre(nomMembre, email), times(1));
    }

    @Test
    void testAjouterMembreWithSQLException() {
        // Prepare
        String nomMembre = "Vincent";
        String email = "vincent@example.com";

        // Mocking the static method to throw an SQLException
        mockedUtilisateurDTO.when(() -> UtilisateurDTO.ajouterMembre(nomMembre, email))
                .thenThrow(new SQLException("Le membre est déjà présent", "409"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> facadeSalon.ajouterMembre(nomMembre, email));
        assertInstanceOf(SQLException.class, exception.getCause());
        assertEquals("Le membre est déjà présent", exception.getCause().getMessage());
    }

}