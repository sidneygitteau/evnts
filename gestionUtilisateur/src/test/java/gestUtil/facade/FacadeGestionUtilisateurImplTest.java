package gestUtil.facade;//package gestUtil.facade;
//
//import gestUtil.dto.UtilisateurPublicDTO;
//import gestUtil.exceptions.EMailDejaPrisException;
//import gestUtil.exceptions.PseudoDejaPrisException;
//import gestUtil.exceptions.UtilisateurNonTrouveException;
//import gestUtil.modele.Utilisateur;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class FacadeGestionUtilisateurImplTest {
//
//    private FacadeGestionUtilisateurImpl facadeGestionUtilisateur;
//
//    @Before
//    public void setup() throws PseudoDejaPrisException, EMailDejaPrisException {
//        facadeGestionUtilisateur = new FacadeGestionUtilisateurImpl();
//        facadeGestionUtilisateur.creerCompte("existant","existant@gmail.com","salut j'existe deja","urlphotoquitue");
//        facadeGestionUtilisateur.creerCompte("deuxieme","deuxieme@gmail.com","salut j'existe deja aussi","urlphotoquituedelamort");
//        facadeGestionUtilisateur.creerCompte("troisieme","pastouche@gmail.com","salut c'est baptie ^^","urlphotoblitzcrank");
//
//    }
//    //#######################################################CREER COMPTE#######################################################//
//    /**
//     * Test creation d'un compte OK + verification de sa bio et son URL
//     */
//    @Test
//    public void creerCompteOk() throws UtilisateurNonTrouveException {
//        assertDoesNotThrow(()->facadeGestionUtilisateur.creerCompte("sid","sid@gmail.com","labioducreateurdestests","url"));
//        assertTrue(facadeGestionUtilisateur.getInformationsPubliques("sid").getBio().equals("labioducreateurdestests")
//        && facadeGestionUtilisateur.getInformationsPubliques("sid").getPhotoDeProfil().equals("url"));
//    }
//
//    /**
//     * Test creation d'un compte avec email déjà prit
//     */
//    @Test
//    public void creerCompteKoEMailDejaPrisException(){
//        assertThrows(EMailDejaPrisException.class,()->facadeGestionUtilisateur.creerCompte("romaincamachal","existant@gmail.com","salut je suis un doublon non ?","urlphotoromain"));
//    }
//
//    /**
//     * Test creation d'un compte avec un pseudo déjà utilisé
//     */
//    @Test
//    public void creerCompteKoPseudoDejaPrisException(){
//        assertThrows(PseudoDejaPrisException.class,()->facadeGestionUtilisateur.creerCompte("existant","vincentauber@gmail.com","salut je suis un doublon aussi non ?","urlphotovincent"));
//    }
//    //#######################################################CHANGER PSEUDO#######################################################//
//    /**
//     * Verification changement de pseudo ok
//     */
//    @Test
//    public void changerPseudoOk() throws Exception {
//        assertDoesNotThrow(()->facadeGestionUtilisateur.changerPseudo("existant@gmail.com", "nouveauPseudo"));
//        assertEquals("nouveauPseudo", facadeGestionUtilisateur.getInformationsPubliques("nouveauPseudo").getPseudo());
//    }
//
//    /**
//     * Changement de pseudo ko car utilisateur non trouvé
//     */
//    @Test
//    public void changerPseudoKoUtilisateurNonTrouve() {
//        assertThrows(UtilisateurNonTrouveException.class, () -> facadeGestionUtilisateur.changerPseudo("baptieleroi@gmail.com", "baptielenul"));
//    }
//
//    /**
//     * changement de pseudo ko car pseudo deja prit
//     */
//    @Test
//    public void changerPseudoKoPseudoDejaPris() {
//        assertThrows(PseudoDejaPrisException.class, () -> facadeGestionUtilisateur.changerPseudo("existant@gmail.com", "deuxieme"));
//    }
//
//    //#######################################################CHANGER PHOTO DE PROFIL#######################################################//
//
//    @Test
//    public void changerPhotoDeProfilSucces() throws Exception {
//        assertDoesNotThrow(()->facadeGestionUtilisateur.changerPhotoDeProfil("pastouche@gmail.com", "nouvellePhoto.jpg"));
//        assertEquals("nouvellePhoto.jpg", facadeGestionUtilisateur.getInformationsPubliques("troisieme").getPhotoDeProfil());
//    }
//
//    @Test
//    public void changerPhotoDeProfilUtilisateurNonTrouve() {
//        assertThrows(UtilisateurNonTrouveException.class, () -> facadeGestionUtilisateur.changerPhotoDeProfil("inexistant@gmail.com", "photo.jpg"));
//    }
//
//    //#######################################################SUPPRIMER COMPTE#######################################################//
//
//    @Test
//    public void supprimerCompteOk() throws Exception {
//        assertDoesNotThrow(()->facadeGestionUtilisateur.creerCompte("supprimemoi","asupprimer@gmail.com","supprime ou j'te supprime","baptie"));
//        assertDoesNotThrow(()->facadeGestionUtilisateur.supprimerCompte("asupprimer@gmail.com"));
//        assertThrows(UtilisateurNonTrouveException.class, () -> facadeGestionUtilisateur.getInformationsPubliques("supprimemoi"));
//    }
//
//    @Test
//    public void supprimerCompteUtilisateurNonTrouve() {
//        assertThrows(UtilisateurNonTrouveException.class, () -> facadeGestionUtilisateur.supprimerCompte("inexistant@gmail.com"));
//    }
//    //#######################################################Informations Publiques #######################################################//
//
//    @Test
//    public void getInformationsPubliquesOk() throws Exception {
//        facadeGestionUtilisateur.creerCompte("consulte","aconsulter@gmail.com","bonjour","saucisson");
//        UtilisateurPublicDTO utilisateur = facadeGestionUtilisateur.getInformationsPubliques("consulte");
//        assertNotNull(utilisateur);
//        assertEquals("bonjour", utilisateur.getBio());
//        assertEquals("saucisson", utilisateur.getPhotoDeProfil());
//        assertEquals("consulte",utilisateur.getPseudo());
//        assertTrue(utilisateur.getListeContacts().isEmpty());
//
//    }
//
//    @Test
//    public void getInformationsPubliquesKoUtilisateurNonTrouve() {
//        assertThrows(UtilisateurNonTrouveException.class, () -> facadeGestionUtilisateur.getInformationsPubliques("inexistant"));
//    }
//
//    //#######################################################Ajout Contact #######################################################//
//
//    @Test
//    public void ajoutContactOk() throws Exception {
//        facadeGestionUtilisateur.creerCompte("user1", "user1@gmail.com", "bio1", "photo1.jpg");
//        facadeGestionUtilisateur.creerCompte("user2", "user2@gmail.com", "bio2", "photo2.jpg");
//        facadeGestionUtilisateur.ajoutContact("user1", "user2");
//        assertTrue(facadeGestionUtilisateur.getInformationsPubliques("user1").getListeContacts().contains("user2"));
//        assertTrue(facadeGestionUtilisateur.getInformationsPubliques("user2").getListeContacts().contains("user1"));
//    }
//
//    @Test
//    public void ajoutContactKoUtilisateurNonTrouve() {
//        assertThrows(UtilisateurNonTrouveException.class, () -> facadeGestionUtilisateur.ajoutContact("inexistant", "existant"));
//    }
//
//
//    //#######################################################CHANGER BIO#######################################################//
//    /**
//     * Test pour changer la bio avec succès.
//     */
//    @Test
//    public void changerBioSucces() throws Exception {
//        String email = "test@gmail.com";
//        facadeGestionUtilisateur.creerCompte("username", email, "Ancienne bio", "photo.jpg");
//        facadeGestionUtilisateur.changerBio(email, "Nouvelle bio");
//
//        assertTrue(facadeGestionUtilisateur.getInformationsPubliques("username").getBio().equals("Nouvelle bio"));
//    }
//
//    /**
//     * Test pour changer la bio échoue si l'utilisateur n'existe pas.
//     */
//    @Test
//    public void changerBioUtilisateurNonTrouve() {
//        String emailNonExistant = "nonexistant@example.com";
//
//        assertThrows(UtilisateurNonTrouveException.class, () -> facadeGestionUtilisateur.changerBio(emailNonExistant, "Nouvelle bio"));
//    }
//}





