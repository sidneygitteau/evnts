using contact.Exceptions;
using contact.Models;
using System.Text.Json.Serialization;
using System.Text.Json;

namespace contact.Services
{
    public class ContactService : IContactService
    {
        private static List<UtilisateurContact> _utilisateurList = new List<UtilisateurContact>();

        public void AjoutContact(string Accepteur, string Envoyeur)
        {
            // Vérifier si les deux utilisateurs existent dans la liste des utilisateurs
            UtilisateurContact? utilisateur1 = _utilisateurList.SingleOrDefault(u => u.EMail == Accepteur);
            UtilisateurContact? utilisateur2 = _utilisateurList.SingleOrDefault(u => u.EMail == Envoyeur);

            if (utilisateur1 == null || utilisateur2 == null)
                throw new UtilisateurNonExistantException();

            if (utilisateur1.Contacts.Contains(Envoyeur) && utilisateur2.Contacts.Contains(Accepteur))
                throw new DejaEnContactException();

            if (!utilisateur1.DemandeContact.Contains(Envoyeur))
                throw new DemandeContactInexistanteException();

            utilisateur1.DemandeContact.Remove(Envoyeur);
            utilisateur2.Contacts.Add(Accepteur);
            utilisateur1.Contacts.Add(Envoyeur);
        }

        public void AjoutConversation(string Utilisateur1, string Utilisateur2)
        {
            //classe par ordre alphabétique pour éviter doublon de conversation
            if (String.Compare(Utilisateur1, Utilisateur2, StringComparison.Ordinal) > 0)
            {
                string temp = Utilisateur1;
                Utilisateur1 = Utilisateur2;
                Utilisateur2 = temp;
            }

            int id = GetIdConversation(Utilisateur1, Utilisateur2);

            // Vérifier si les deux utilisateurs existent dans la liste des utilisateurs
            UtilisateurContact? utilisateur1 = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur1);
            UtilisateurContact? utilisateur2 = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur2);

            if (utilisateur1 == null || utilisateur2 == null)
                throw new UtilisateurNonExistantException();

            if (utilisateur1.Conversation != null && utilisateur2.Conversation != null)
            {

                Conversation conversationExistante = utilisateur1.Conversation.FirstOrDefault(u => u.Id == id);

                if (conversationExistante != null)
                    throw new ConversationDejaExistanteException();
            }


            Conversation nouvelleConversation = new Conversation (id, utilisateur1, utilisateur2);
            

            utilisateur1.Conversation.Add(nouvelleConversation);
            utilisateur2.Conversation.Add(nouvelleConversation);

        }

        public void CreationUtilisateur(string EMail)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == EMail);

            if (utilisateur != null)
                throw new UtilisateurDejaExistantException();

            utilisateur = new UtilisateurContact(EMail);

            _utilisateurList.Add(utilisateur);
        }

        public void DemandeContact(string Demandeur, string Receveur)
        {

            UtilisateurContact? utilisateur1 = _utilisateurList.SingleOrDefault(u => u.EMail == Demandeur);
            UtilisateurContact? utilisateur2 = _utilisateurList.SingleOrDefault(u => u.EMail == Receveur);

            if (utilisateur1 == null || utilisateur2 == null)
                throw new UtilisateurNonExistantException();

            if (utilisateur2.DemandeContact.SingleOrDefault(u => u.Equals(Demandeur)) != null)
            {
                throw new DemandeDejaExistanteException();
            }

            utilisateur2.DemandeContact.Add(Demandeur);

        }

        public void EnvoieMessage(string Envoyeur, string Receveur, string Contenu)
        {
            Conversation conversation = GetConversation(Envoyeur, Receveur);
            List<Message>? messages = conversation.Messages;

            int id = messages == null ? 0 : messages.Count;

            var nouveauMessage = new Message(id, Contenu, Envoyeur);

            messages.Add(nouveauMessage);
        }

        public List<string>? GetContacts(string Utilisateur)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur);

            if (utilisateur == null)
                throw new UtilisateurNonExistantException();

            return utilisateur.Contacts;
        }



        public string GetMessagesConversation(string Utilisateur1, string Utilisateur2)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur1);

            if (utilisateur == null)
                throw new UtilisateurNonExistantException();

            Conversation conversation = GetConversation(Utilisateur1, Utilisateur2);

            if (conversation == null)
                throw new ConversationNonExistanteException();

            // Créez les options de sérialisation avec la gestion des références circulaires
            JsonSerializerOptions options = new JsonSerializerOptions
            {
                ReferenceHandler = ReferenceHandler.Preserve,
            };

            string serializedMessages = JsonSerializer.Serialize(conversation.Messages, options);

            return serializedMessages;
        }

        public List<string>? GetEmailConversations(string Utilisateur)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur);

            if (utilisateur == null)
                throw new UtilisateurNonExistantException();

            List<string> conversationsEmail = new List<string>();

            foreach (Conversation conv in utilisateur.Conversation)
            {
                string email = conv.UtilisateurContact1.EMail != Utilisateur ? conv.UtilisateurContact1.EMail : conv.UtilisateurContact2.EMail;
                conversationsEmail.Add(email);
            }

            return conversationsEmail;
        }

        public List<string>? GetDemandes(string Utilisateur)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur);

            if (utilisateur == null)
                throw new UtilisateurNonExistantException();

            return utilisateur.DemandeContact;
        }

        public void SuppressionContact(string Demandeur, string Cible)
        {
            UtilisateurContact? utilisateur1 = _utilisateurList.SingleOrDefault(u => u.EMail == Demandeur);
            UtilisateurContact? utilisateur2 = _utilisateurList.SingleOrDefault(u => u.EMail == Cible);

            if (utilisateur1 == null || utilisateur2 == null)
                throw new UtilisateurNonExistantException();

            if (!utilisateur1.Contacts.Contains(Cible))
                throw new ContactNonExistantException();

            utilisateur1.Contacts.Remove(Cible);
            utilisateur2.Contacts.Remove(Demandeur);
        }

        public void SuppressionUtilisateur(string EMail)
        {
            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == EMail);
            
            if (utilisateur == null)
                throw new UtilisateurNonExistantException();

            _utilisateurList.Remove(utilisateur);
        }

        public void VisionDuMessage(string Voyeur,string Envoyeur)
        {
            Conversation conversation = GetConversation(Voyeur, Envoyeur);

            foreach (Message message in conversation.Messages)
            {
                if (message.Auteur != Voyeur && message.EstVu == false)
                {
                    message.EstVu = true;
                }
            }
        }

        private int GetIdConversation(string Utilisateur1, string Utilisateur2)
        {
            int hash = (Utilisateur1 + Utilisateur2).GetHashCode();
            return Math.Abs(hash);
        }

        public Conversation GetConversation(string Utilisateur1, string Utilisateur2)
        {
            //classe par ordre alphabétique pour éviter doublon de conversation
            if (String.Compare(Utilisateur1, Utilisateur2, StringComparison.Ordinal) > 0)
            {
                string temp = Utilisateur1;
                Utilisateur1 = Utilisateur2;
                Utilisateur2 = temp;
            }

            int hash = (Utilisateur1 + Utilisateur2).GetHashCode(); 
            int id = Math.Abs(hash);

            UtilisateurContact? utilisateur = _utilisateurList.SingleOrDefault(u => u.EMail == Utilisateur1);

            if (utilisateur == null)
                throw new UtilisateurNonExistantException();

            Conversation conversation = utilisateur.Conversation.FirstOrDefault(c => c.Id == id);

            if (conversation == null)
                throw new ConversationNonExistanteException();

            return conversation; ;
        }
    }
}
