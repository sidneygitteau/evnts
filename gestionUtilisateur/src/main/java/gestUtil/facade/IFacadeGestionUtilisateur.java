package gestUtil.facade;


import gestUtil.dto.UtilisateurDTO;
import gestUtil.exceptions.EMailDejaPrisException;
import gestUtil.exceptions.PseudoDejaPrisException;
import gestUtil.exceptions.UtilisateurNonTrouveException;

public interface IFacadeGestionUtilisateur {
    /**
     * Permet de creer un compte complet, avec la bio et la photo de profil
     * @param pseudo
     * @param eMail
     * @param bio
     * @param photoDeProfil
     * @throws PseudoDejaPrisException
     * @throws EMailDejaPrisException
     */
    void creerCompte(String pseudo, String eMail, String bio, String photoDeProfil) throws PseudoDejaPrisException, EMailDejaPrisException;

    /**
     * Permet de modifier son pseudo
     * @param email
     * @param nouveauPseudo
     */
    void changerPseudo(String email, String nouveauPseudo) throws UtilisateurNonTrouveException, PseudoDejaPrisException;
    /*
    /**
     * Permet de changer de MDP, en mettant son ancien mdp, TODO : ATTENTION A L'ENCODAGE ?
     * @param pseudo
     * @param ancienMdp
     * @param nouveauMdp
     * @throws UtilisateurNonTrouveException
     * @throws MdpIncorrectException
     * @throws UtilisateurNonTrouveException
     * @throws MdpIncorrectException

    void changerMdp(String pseudo, String ancienMdp, String nouveauMdp) throws UtilisateurNonTrouveException, MdpIncorrectException, UtilisateurNonTrouveException, MdpIncorrectException;
    */
    /**
     * Permet de modifier la bio de son profil
     * @param pseudo
     * @param nouvelleBio
     * @throws UtilisateurNonTrouveException
     */
    void changerBio(String pseudo, String nouvelleBio) throws UtilisateurNonTrouveException;

    /**
     * Permet de modifier sa photo de profil
     * @param pseudo
     * @param nouvellePhotoDeProfil
     * @throws UtilisateurNonTrouveException
     */
    void changerPhotoDeProfil(String pseudo, String nouvellePhotoDeProfil) throws UtilisateurNonTrouveException;

    /**
     * Permet de supprimer son compte
     * @param pseudo
     * @throws UtilisateurNonTrouveException
     */
    void supprimerCompte(String pseudo) throws UtilisateurNonTrouveException;

    /**
     * Permet d'obtenir les informations publiques d'un utilisateur
     * @param pseudo
     */
    UtilisateurDTO getInformationsPubliques(String pseudo) throws UtilisateurNonTrouveException;


    /**
     * Permet d'ajouter le pseudo de l'autre membre à ses contacts
     * @param pseudo1
     * @param pseudo2
     * @throws UtilisateurNonTrouveException
     */
    void ajoutContact(String pseudo1, String pseudo2) throws UtilisateurNonTrouveException;
}
