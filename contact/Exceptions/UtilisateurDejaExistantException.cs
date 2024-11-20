using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    public class UtilisateurDejaExistantException : Exception
    {
        public UtilisateurDejaExistantException()
        {
        }

        public UtilisateurDejaExistantException(string? message) : base(message)
        {
        }

        public UtilisateurDejaExistantException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}