using Consul;
using System;
using System.Net;
using System.Threading.Tasks;

namespace contact.Config
{
    public class ConsulServiceRegistration
    {
        public async Task RegisterServiceAsync(string serviceName, int servicePort)
        {
            var consulClient = new ConsulClient();

            var registration = new AgentServiceRegistration()
            {
                ID = Guid.NewGuid().ToString(),
                Name = serviceName,
                Address = "localhost", 
                Port = servicePort,
                Check = new AgentServiceCheck
                {
                    HTTP = $"http://{Dns.GetHostName()}:{servicePort}/health", 
                    Interval = TimeSpan.FromSeconds(10),
                    Timeout = TimeSpan.FromSeconds(5) 
                }
            };

            // Enregistrement du service auprès de Consul
            await consulClient.Agent.ServiceRegister(registration);
        }
    }
}
