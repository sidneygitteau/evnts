using System.ComponentModel.DataAnnotations;

namespace contact.Models
{
    public class Message
    {
        [Key]
        public int Id { get; set; }

        public string Contenu { get; set; }

        public DateTimeOffset DateCreation { get; set; }

        public string Auteur { get; set; }

        public bool EstVu {  get; set; }

        public Message(int id, string contenu, string auteur) 
        { 
            Id=id; 
            Contenu=contenu;
            Auteur=auteur;
            DateCreation=DateTimeOffset.Now;
            EstVu=false;
        }
    }
}
