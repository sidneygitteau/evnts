using contact.Exceptions;
using contact.Models;
using contact.Services;

namespace contact_tests

{
    [TestFixture]
    public class Tests
    {

        ContactService service;

        [SetUp]
        public void Setup()
        {
            service = new ContactService();
        }



        //####AjoutContact####

        [Test]
        public void AjoutContact_UtilisateursExistantsEtPasDejaEnContact_AjouteContactAUtilisateurs()
        {

            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.DemandeContact("utilisateur2@example.com", "utilisateur1@example.com");
            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            List<string>? contactsUtilisateur1 = service.GetContacts("utilisateur1@example.com");
            List<string>? contactsUtilisateur2 = service.GetContacts("utilisateur2@example.com");

            CollectionAssert.Contains(contactsUtilisateur1, "utilisateur2@example.com");
            CollectionAssert.Contains(contactsUtilisateur2, "utilisateur1@example.com");
        }

        [Test]
        public void AjoutContact_UtilisateursExistantsEtDejaEnContact_LanceDejaEnContact()
        {

            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.DemandeContact("utilisateur2@example.com", "utilisateur1@example.com");
            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            Assert.Throws<DejaEnContactException>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [Test]
        public void AjoutContact_UtilisateurExistantEtReceveurInexistant_LanceUtilisateurNonExistant()
        {
            service.CreationUtilisateur("utilisateur1@example.com");

            Assert.Throws<UtilisateurNonExistantException>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [Test]
        public void AjoutContact_UtilisateursInexistants_LanceUtilisateurNonExistant()
        {
            Assert.Throws<UtilisateurNonExistantException>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [Test]
        public void AjoutContact_DemandeInexistante_ThrowDemandeContactInexistanteException()
        {
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            // Act & Assert
            Assert.Throws<DemandeContactInexistanteException>(() => service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####AjoutConversation####

        [Test]
        public void AjoutConversation_UtilisateursExistantsEtPasDejaEnConversation_AjouteConversationEntreUtilisateurs()
        {
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            List<string> conversationsUtilisateur1 = service.GetEmailConversations("utilisateur1@example.com");
            List<string> conversationsUtilisateur2 = service.GetEmailConversations("utilisateur2@example.com");

            Assert.AreEqual(1, conversationsUtilisateur1.Count);
            Assert.AreEqual(1, conversationsUtilisateur2.Count);
        }

        [Test]
        public void AjoutConversation_UtilisateursExistantsEtDejaEnConversation_LanceConversationDejaExistante()
        {
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            Assert.Throws<ConversationDejaExistanteException>(() => service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [Test]
        public void AjoutConversation_UtilisateurExistantEtReceveurInexistant_LanceUtilisateurNonExistant()
        {
            service.CreationUtilisateur("utilisateur1@example.com");

            Assert.Throws<UtilisateurNonExistantException>(() => service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [Test]
        public void AjoutConversation_UtilisateursInexistants_LanceUtilisateurNonExistant()
        {
            Assert.Throws<UtilisateurNonExistantException>(() => service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####CreationUtilisateur####

        [Test]
        public void CreationUtilisateur_UtilisateurInexistant_CreerNouvelUtilisateur()
        {
            service.CreationUtilisateur("utilisateur1@example.com");

            List<string>? contactsUtilisateur = service.GetContacts("utilisateur1@example.com");
            Assert.AreEqual(0, contactsUtilisateur.Count);
        }

        [Test]
        public void CreationUtilisateur_UtilisateurExistant_LanceUtilisateurDejaExistant()
        {
            service.CreationUtilisateur("utilisateur1@example.com");

            Assert.Throws<UtilisateurDejaExistantException>(() => service.CreationUtilisateur("utilisateur1@example.com"));
        }




        //####DemandeContact####

        [Test]
        public void DemandeContact_UtilisateursExistantsEtPasDejaEnDemande_AjouteDemandeEntreUtilisateurs()
        {

            service.CreationUtilisateur("demandeur@example.com");
            service.CreationUtilisateur("receveur@example.com");
            service.DemandeContact("demandeur@example.com", "receveur@example.com");
            List<string>? demandesReceveur = service.GetDemandes("receveur@example.com");

            CollectionAssert.Contains(demandesReceveur, "demandeur@example.com");
        }

        [Test]
        public void DemandeContact_UtilisateursExistantsEtDejaEnDemande_LanceDemandeDejaExistante()
        {


            service.CreationUtilisateur("demandeur@example.com");
            service.CreationUtilisateur("receveur@example.com");
            service.DemandeContact("demandeur@example.com", "receveur@example.com");
            service.DemandeContact("receveur@example.com", "demandeur@example.com");

            Assert.Throws<DemandeDejaExistanteException>(() => service.DemandeContact("demandeur@example.com", "receveur@example.com"));
        }

        [Test]
        public void DemandeContact_UtilisateurExistantEtReceveurInexistant_LanceUtilisateurNonExistant()
        {


            service.CreationUtilisateur("demandeur@example.com");


            Assert.Throws<UtilisateurNonExistantException>(() => service.DemandeContact("demandeur@example.com", "receveur@example.com"));
        }

        [Test]
        public void DemandeContact_UtilisateursInexistants_LanceUtilisateurNonExistant()
        {

            Assert.Throws<UtilisateurNonExistantException>(() => service.DemandeContact("demandeur@example.com", "receveur@example.com"));
        }




        //####EnvoieMessage####

        [Test]
        public void EnvoieMessage_ConversationExistant_AjouteMessageALaConversation()
        {

            service.CreationUtilisateur("envoyeur@example.com");
            service.CreationUtilisateur("receveur@example.com");
            service.AjoutConversation("envoyeur@example.com", "receveur@example.com");

            service.EnvoieMessage("envoyeur@example.com", "receveur@example.com", "Bonjour !");

            Conversation conversation = service.GetConversation("envoyeur@example.com", "receveur@example.com");
            Assert.AreEqual(1, conversation.Messages.Count);
            Assert.AreEqual("Bonjour !", conversation.Messages[0].Contenu);
            Assert.AreEqual("envoyeur@example.com", conversation.Messages[0].Auteur);
            Assert.AreEqual(conversation.Messages[0].EstVu, false);
        }

        [Test]
        public void EnvoieMessage_ConversationInexistante_LanceConversationNonExistante()
        {

            service.CreationUtilisateur("envoyeur@example.com");
            service.CreationUtilisateur("receveur@example.com");

            Assert.Throws<ConversationNonExistanteException>(() => service.EnvoieMessage("envoyeur@example.com", "receveur@example.com", "Bonjour !"));
        }




        //####GetContacts####

        [Test]
        public void GetContacts_UtilisateurExistant_RetourneListeContacts()
        {

            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.CreationUtilisateur("utilisateur3@example.com");
            service.DemandeContact("utilisateur3@example.com", "utilisateur1@example.com");
            service.DemandeContact("utilisateur2@example.com", "utilisateur1@example.com");
            service.AjoutContact("utilisateur1@example.com", "utilisateur3@example.com");
            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            List<string>? contacts = service.GetContacts("utilisateur1@example.com");

            Assert.AreEqual(2, contacts.Count);
            CollectionAssert.Contains(contacts, "utilisateur2@example.com");
            CollectionAssert.Contains(contacts, "utilisateur3@example.com");
        }

        [Test]
        public void GetContacts_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            Assert.Throws<UtilisateurNonExistantException>(() => service.GetContacts("utilisateur1@example.com"));
        }




        //####GetMessagesConversation####

        [Test]
        public void GetMessagesConversation_ConversationExistant_RetourneMessagesConversation()
        {
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            service.EnvoieMessage("utilisateur1@example.com", "utilisateur2@example.com", "Bonjour !");
            service.EnvoieMessage("utilisateur2@example.com", "utilisateur1@example.com", "Salut !");

            string messagesConversation = service.GetMessagesConversation("utilisateur1@example.com", "utilisateur2@example.com");

            Assert.IsTrue(messagesConversation.Contains("Bonjour !"));
            Assert.IsTrue(messagesConversation.Contains("Salut !"));
        }

        [Test]
        public void GetMessagesConversation_ConversationInexistante_LanceConversationNonExistante()
        {
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            Assert.Throws<ConversationNonExistanteException>(() => service.GetMessagesConversation("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        [Test]
        public void GetEmailConversations_UtilisateurExistant_RetourneListeConversations()
        {
            // Création des utilisateurs et ajout de la conversation
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.AjoutConversation("utilisateur1@example.com", "utilisateur2@example.com");

            // Récupération des conversations de l'utilisateur
            List<string> conversations = service.GetEmailConversations("utilisateur1@example.com");

            // Assertions
            Assert.AreEqual(1, conversations.Count);
            Assert.AreEqual("utilisateur2@example.com", conversations[0]);
        }

        [Test]
        public void GetEmailConversations_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            // Tentative de récupération des conversations pour un utilisateur inexistant
            Assert.Throws<UtilisateurNonExistantException>(() => service.GetEmailConversations("utilisateur1@example.com"));
        }




        //####GetDemandes####

        [Test]
        public void GetDemandes_UtilisateurExistant_RetourneListeDemandes()
        {

            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.DemandeContact("utilisateur1@example.com", "utilisateur2@example.com");

            // Act
            List<string>? demandes = service.GetDemandes("utilisateur2@example.com");

            Assert.AreEqual(1, demandes.Count);
            Assert.AreEqual("utilisateur1@example.com", demandes[0]);
        }

        [Test]
        public void GetDemandes_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            Assert.Throws<UtilisateurNonExistantException>(() => service.GetDemandes("utilisateur1@example.com"));
        }




        //####SuppressionContact####

        [Test]
        public void SuppressionContact_ContactExistant_SupprimeContact()
        {
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");
            service.DemandeContact("utilisateur2@example.com", "utilisateur1@example.com");
            service.AjoutContact("utilisateur1@example.com", "utilisateur2@example.com");

            service.SuppressionContact("utilisateur1@example.com", "utilisateur2@example.com");

            List<string>? contactsUtilisateur1 = service.GetContacts("utilisateur1@example.com");
            List<string>? contactsUtilisateur2 = service.GetContacts("utilisateur2@example.com");

            Assert.AreEqual(0, contactsUtilisateur1.Count);
            Assert.AreEqual(0, contactsUtilisateur2.Count);
        }

        [Test]
        public void SuppressionContact_ContactInexistant_LanceContactNonExistant()
        {
            service.CreationUtilisateur("utilisateur1@example.com");
            service.CreationUtilisateur("utilisateur2@example.com");

            Assert.Throws<ContactNonExistantException>(() => service.SuppressionContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }

        [Test]
        public void SuppressionContact_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            service.CreationUtilisateur("utilisateur1@example.com");

            Assert.Throws<UtilisateurNonExistantException>(() => service.SuppressionContact("utilisateur1@example.com", "utilisateur2@example.com"));
        }




        //####SuppressionUtilisateur####

        [Test]
        public void SuppressionUtilisateur_UtilisateurExistant_SupprimeUtilisateur()
        {

            service.CreationUtilisateur("utilisateur1@example.com");

            service.SuppressionUtilisateur("utilisateur1@example.com");

            Assert.Throws<UtilisateurNonExistantException>(() => service.GetContacts("utilisateur1@example.com"));
        }

        [Test]
        public void SuppressionUtilisateur_UtilisateurInexistant_LanceUtilisateurNonExistant()
        {
            Assert.Throws<UtilisateurNonExistantException>(() => service.SuppressionUtilisateur("utilisateur1@example.com"));
        }




        //####VisionDuMessage####

        [Test]
        public void VisionDuMessage_MessagesNonVus_ModifieEstVu()
        {

            service.CreationUtilisateur("voyeur@example.com");
            service.CreationUtilisateur("envoyeur@example.com");
            service.AjoutConversation("voyeur@example.com", "envoyeur@example.com");
            service.EnvoieMessage("envoyeur@example.com", "voyeur@example.com", "Bonjour!");
            service.VisionDuMessage("voyeur@example.com", "envoyeur@example.com");

            Conversation conversation = service.GetConversation("voyeur@example.com", "envoyeur@example.com");
            foreach (contact.Models.Message message in conversation.Messages)
            {
                if (message.Auteur != "voyeur@example.com")
                {
                    Assert.IsTrue(message.EstVu);
                }
            }
        }

        [Test]
        public void VisionDuMessage_AucunMessage_ModificationNonNecessaire()
        {
            service.CreationUtilisateur("voyeur@example.com");
            service.CreationUtilisateur("envoyeur@example.com");
            service.AjoutConversation("voyeur@example.com", "envoyeur@example.com");
            service.VisionDuMessage("voyeur@example.com", "envoyeur@example.com");

            Conversation conversation = service.GetConversation("voyeur@example.com", "envoyeur@example.com");
            foreach (contact.Models.Message message in conversation.Messages)
            {
                Assert.IsFalse(message.EstVu);
            }
        }
    }
}