using System.Runtime.Serialization;

namespace contact.Exceptions
{
    [Serializable]
    public class ContactNonExistantException : Exception
    {
        public ContactNonExistantException()
        {
        }

        public ContactNonExistantException(string? message) : base(message)
        {
        }

        public ContactNonExistantException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

    }
}