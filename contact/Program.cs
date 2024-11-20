using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using contact.Services;
using contact.Config;

namespace contact
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);
            builder.Services.AddSingleton<IContactService, ContactService>();
            builder.Services.AddControllers();
            builder.Services.AddSingleton<ConsulServiceRegistration>(); // Ajout de la dépendance

            var app = builder.Build();

            // Récupération de la dépendance ConsulServiceRegistration
            var consulRegistration = app.Services.GetRequiredService<ConsulServiceRegistration>();
            // Appel de la méthode pour enregistrer le service auprès de Consul
            consulRegistration.RegisterServiceAsync("contact", 8084).Wait();

            app.MapControllers();

            app.Run();
        }
    }
}
