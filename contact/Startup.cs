using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.IdentityModel.Tokens;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Text;

public class Startup
{
    public void ConfigureServices(IServiceCollection services)
    {
        services.AddControllers();

        // Clé publique RSA sous forme de chaîne
        string publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwuZxXzEqgg7ksjGtDGsF\n" +
                "Y8m2vNEN1OAyWkVbKhcV3+5NNArXBdKLEg6iYZWe5UCBJ/JKSJU0K2sJFYzgTSp6\n" +
                "eda2u1BdsvYCvAyWj+8lg003GviWei7muIatWZT5lz8qdFFN29XU40yO8X1rbhPa\n" +
                "tlueua2WL3plDbne9B915pR+RCPu/6I9FaCuN3Tv8XbOGvPbhLMpYqubXjpSoOwr\n" +
                "vrqhDXrKuSsuGryyQBx809i2HtqhgxVNDWYsjTrojY+8/FA/8xRwfxpv2uFzwrmn\n" +
                "je/TO2mkavEpjef8aDI2CzWUEI/FV37wt4SVNp05IE3gPWntTbesw5OdM3hBQPVT\n" +
                "GwIDAQAB\n";

        // Convertir la clé publique RSA de chaîne en tableau d'octets
        byte[] publicKeyBytes = Encoding.ASCII.GetBytes(publicKeyString);

        // Créer un objet RSA à partir de la clé publique en bytes
        RSA publicKey;
        using (var rsa = RSA.Create())
        {
            rsa.ImportSubjectPublicKeyInfo(publicKeyBytes, out _);
            publicKey = rsa;
        }

        // Configuration de la validation JWT
        services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            .AddJwtBearer(options =>
            {
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = true,
                    ValidateAudience = true,
                    ValidateLifetime = true,
                    ValidateIssuerSigningKey = true,
                    ValidIssuer = "your_issuer",
                    ValidAudience = "your_audience",
                    IssuerSigningKey = new RsaSecurityKey(publicKey)
                };
            });

        services.AddAuthorization();
    }



    public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
    {
        if (env.IsDevelopment())
        {
            app.UseDeveloperExceptionPage();
        }

        app.UseHttpsRedirection();

        app.UseRouting();

        app.UseAuthentication(); 

        app.UseAuthorization();

        app.UseEndpoints(endpoints =>
        {
            endpoints.MapControllers();
        });
    }

}
