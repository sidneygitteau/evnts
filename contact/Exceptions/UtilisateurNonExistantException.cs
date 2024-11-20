using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    public class UtilisateurNonExistantException : Exception
    {
        public UtilisateurNonExistantException()
        {
        }

        public UtilisateurNonExistantException(string? message) : base(message)
        {
        }

        public UtilisateurNonExistantException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}