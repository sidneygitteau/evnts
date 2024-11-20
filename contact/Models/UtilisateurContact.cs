using System.ComponentModel.DataAnnotations;

namespace contact.Models
{
    public class UtilisateurContact
    {
        [Key]
        public string EMail { get; set; }
        public List<string>? Contacts { get; set; }
        public List<string>? DemandeContact { get; set; }
        public List<Conversation>? Conversation { get; set; }

        public UtilisateurContact(string email)
        {
            EMail = email;
            Contacts = new List<string>();
            DemandeContact = new List<string>();
            Conversation = new List<Conversation>(); 
        }

    }

}
