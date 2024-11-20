using contact.Models;
using System;
using System.Collections.Generic;

public interface IContactService
{
    void AjoutContact(string Demandeur, string Receveur);
    void AjoutConversation(string Utilisateur1, string Utilisateur2);
    void CreationUtilisateur(string EMail);
    void DemandeContact(string Demandeur, string Receveur);
    List<string>? GetContacts(string Utilisateur);
    List<string>? GetEmailConversations(string Utilisateur);
    List<string>? GetDemandes(string Utilisateur);
    string GetMessagesConversation(string Utilisateur1, string Utilisateur2);
    void SuppressionContact(string Demandeur, string Cible);
    void SuppressionUtilisateur(string EMail);
    void EnvoieMessage(string Envoyeur, string Receveur, string Contenu);
    void VisionDuMessage(string Voyeur, string Envoyeur );
}
