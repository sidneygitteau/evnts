using System.ComponentModel.DataAnnotations;

namespace contact.Models
{
    public class Conversation
    {

        [Key]
        public int Id { get; set; }

        public UtilisateurContact UtilisateurContact1 { get; set; }

        public UtilisateurContact UtilisateurContact2 { get; set; }

        public List<Message>? Messages { get; set; }

        public Conversation(int id, UtilisateurContact utilisateurContact1, UtilisateurContact utilisateurContact2) 
        {  
            Id = id; 
            UtilisateurContact1 = utilisateurContact1;
            UtilisateurContact2 = utilisateurContact2;
            Messages = new List<Message>();
        }
    }
}
