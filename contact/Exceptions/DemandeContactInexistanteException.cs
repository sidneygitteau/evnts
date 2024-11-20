using System.Runtime.Serialization;

namespace contact.Services
{
    [Serializable]
    public class DemandeContactInexistanteException : Exception
    {
        public DemandeContactInexistanteException()
        {
        }

        public DemandeContactInexistanteException(string? message) : base(message)
        {
        }

        public DemandeContactInexistanteException(string? message, Exception? innerException) : base(message, innerException)
        {
        }
    }
}