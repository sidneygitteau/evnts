using Microsoft.AspNetCore.Mvc;
using contact.Exceptions;
using contact.Models;

namespace contact.Controllers
{
    [ApiController]
    [Route("/contact")]
    public class ContactController : ControllerBase
    {
        private readonly IContactService _contactService;

        public ContactController(IContactService contactService)
        {
            _contactService = contactService;
        }

        [HttpPost("ajout-contact")]
        public IActionResult AjoutContact([FromQuery] string accepteur, [FromQuery] string envoyeur)
        {
            try
            {
                _contactService.AjoutContact(accepteur, envoyeur);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("Un des utilisateurs n'existe pas.");
            }
            catch (DejaEnContactException)
            {
                return Conflict("Les utilisateurs sont déjà en contact.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("ajout-conversation")]
        public IActionResult AjoutConversation([FromQuery] string utilisateur1, [FromQuery] string utilisateur2)
        {
            try
            {
                _contactService.AjoutConversation(utilisateur1, utilisateur2);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("Un des utilisateurs n'existe pas.");
            }
            catch (ConversationDejaExistanteException)
            {
                return Conflict("La conversation existe déjà.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("creation-utilisateur")]
        public IActionResult CreationUtilisateur([FromQuery] string email)
        {
            try
            {
                _contactService.CreationUtilisateur(email);
                return Ok();
            }
            catch (UtilisateurDejaExistantException)
            {
                return Conflict("L'utilisateur existe déjà.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("demande-contact")]
        public IActionResult DemandeContact([FromQuery] string demandeur, [FromQuery] string receveur)
        {
            try
            {
                _contactService.DemandeContact(demandeur, receveur);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'un des utilisateurs n'existe pas.");
            }
            catch (DemandeDejaExistanteException)
            {
                return Conflict("La demande de contact existe déjà.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("envoie-message")]
        public IActionResult EnvoieMessage([FromQuery] string envoyeur, [FromQuery] string receveur, [FromQuery] string contenu)
        {
            try
            {
                _contactService.EnvoieMessage(envoyeur, receveur, contenu);
                return Ok();
            }
            catch (ConversationNonExistanteException)
            {
                return NotFound("La conversation n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("get-contacts/{utilisateur}")]
        public IActionResult GetContacts([FromRoute] string utilisateur)
        {
            try
            {
                List<string> contacts = _contactService.GetContacts(utilisateur);
                return Ok(contacts);
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'utilisateur n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("get-conversation")]
        public IActionResult GetConversation([FromQuery] string utilisateur1, [FromQuery] string utilisateur2)
        {
            try
            {
                string conversationMessages = _contactService.GetMessagesConversation(utilisateur1, utilisateur2);
                return Ok(conversationMessages);
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'un des utilisateurs n'existe pas.");
            }
            catch (ConversationNonExistanteException)
            {
                return NotFound("La conversation n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }


        [HttpGet("get-demandes/{utilisateur}")]
        public IActionResult GetDemandes([FromRoute] string utilisateur)
        {
            try
            {
                List<string> demandes = _contactService.GetDemandes(utilisateur);
                return Ok(demandes);
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'utilisateur n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpGet("get-conversations/{utilisateur}")]
        public IActionResult GetEmailConversations([FromRoute] string utilisateur)
        {
            try
            {
                List<string>? conversations = _contactService.GetEmailConversations(utilisateur);
                return Ok(conversations);
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'utilisateur n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }


        [HttpDelete("suppression-contact")]
        public IActionResult SuppressionContact([FromQuery] string demandeur, [FromQuery] string cible)
        {
            try
            {
                _contactService.SuppressionContact(demandeur, cible);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'un des utilisateurs n'existe pas.");
            }
            catch (ContactNonExistantException)
            {
                return NotFound("Le contact n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpDelete("suppression-utilisateur")]
        public IActionResult SuppressionUtilisateur([FromQuery] string email)
        {
            try
            {
                _contactService.SuppressionUtilisateur(email);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'utilisateur n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        [HttpPost("vision-message")]
        public IActionResult VisionDuMessage([FromQuery] string visionneur, [FromQuery] string envoyeur)
        {
            try
            {
                _contactService.VisionDuMessage(visionneur, envoyeur);
                return Ok();
            }
            catch (UtilisateurNonExistantException)
            {
                return NotFound("L'un des utilisateurs n'existe pas.");
            }
            catch (ConversationNonExistanteException)
            {
                return NotFound("La conversation n'existe pas.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

    }
}
