using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    public class DemandeDejaExistanteException : Exception
    {
        public DemandeDejaExistanteException()
        {
        }

        public DemandeDejaExistanteException(string? message) : base(message)
        {
        }

        public DemandeDejaExistanteException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}