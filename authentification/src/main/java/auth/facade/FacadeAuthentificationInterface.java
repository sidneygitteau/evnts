package auth.facade;

import auth.exception.*;
import auth.modele.Utilisateur;

public interface FacadeAuthentificationInterface {

        /**
         * Inscription d'un nouvel utilisateur sur le site
         *
         * @param pseudo
         * @param mdp
         * @param eMail
         * @throws PseudoDejaPrisException
         * @throws EMailDejaPrisException
         */
        void inscription(String pseudo, String mdp, String eMail) throws PseudoDejaPrisException, EMailDejaPrisException, EmailOuPseudoDejaPrisException;

        /**
         * Connexion de l'utilisateur sur le site
         *
         * @param pseudo
         * @param mdp
         * @return
         * @throws UtilisateurInexistantException
         * @throws MdpIncorrecteException
         */
        Utilisateur connexion(String pseudo, String mdp) throws UtilisateurInexistantException, MdpIncorrecteException;

        /**
         * Déconnexion de l'utilisateur sur le site
         *
         * @param pseudo
         * @throws UtilisateurInexistantException
         */
        void deconnexion(String pseudo) throws UtilisateurInexistantException, UtilisateurDejaDeconnecteException;

        boolean getStatus(String pseudo) throws UtilisateurInexistantException;

        void reSetPseudo(String ancienPseudo, String nouveauPseudo) throws UtilisateurInexistantException;

        void reSetMDP(String pseudo, String mdp, String nouveauMDP) throws UtilisateurInexistantException, MdpIncorrecteException, CombinaisonPseudoMdpIncorrect;

        void supprimerUtilisateur(String pseudo, String mdp) throws UtilisateurInexistantException, MdpIncorrecteException;

}
