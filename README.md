# web-service-library

 Inledning
Ni ska i denna uppgift skapa en web service med CRUD-funktioner för att hantera ett bibliotek. Klienten ska kunna göra följande.
- Som låntagare:
    söka efter böcker,
    låna böcker,
    återlämna böcker
 - Som bibliotekarie: spara nya böcker till bibliotekets databas, uppdatera metadata i bibliotekets databas, ta bort böcker från bibliotekets databas

Er web service är endast en backend. Denna uppgift ska alltså inte ha en frontend, utan alla anrop till endpoints görs via Postman.

Krav


- Kunna skapa ett Spring-projekt i IntelliJ
- Kunna skapa en controller, service, repository och entity
- Kunna konfigurera databas för Spring, SQL eller NoSql
- Kunna konfigurera logback för att skriva log till konsollen och fil(er) - Skriva REST-API enligt ”Best Practices" i möjligaste mån
- Alla REST-metoder skall returnera korrekt HTTP Statuskod
- Alla entities ska valideras innan dem sparas i databas
- Alla requests ska cache:as för att minimera hämtningar från DB
- Valfri databas (MongoDB, MySQL etc), tänk på att databasen ska vara tillgänglig vid inlämning
- Det ska finnas säkerhet i form av autentisering/inloggning och ACL
- Det ska gå att filtrera och sortera sökresultatet
- Det ska finnas minst 3 entiteter
- Hemsidan/startsidan till er service ska presentera API-dokumentationen
- Man ska kunna söka på fler entiteter än Book, t ex NewPaper, Movies, Games etc.
- Det ska gå att söka på en entitet med minst fyra parametrar
- Filuppladdning ska ingå på ett lämpligt sätt, t.ex. bild på omslaget
- Högre krav på att lämpliga HTTP-status koder returneras
- Högre krav på att loggning finns på lämpliga ställen i applikationen
- Högre krav på säkerhet, t.ex. endast admin får spara till och radera från DB
- Det ska finnas en distribuerad version (byggd jar-fil) som kan köras från terminalen (command line).

Genomförande
Säkerhet

Följande ska göras till ert API:

- GET requests för sökning av böcker skall vara tillåtet som anonym - Alla andra requests skall kräva autentisering
- Alla lösenord skall krypteras med BCrypt
- Lösenord skall aldrig skickas från er backend oavsett API-anrop
- Som inloggad admin skall man kunna köra alla REST-metoder
- RestController
- Ni skall skapa en Web-service (d.v.s ett REST-API) som erbjuder minst en av varje HTTP- metod: GET, GET (by id), POST, PUT, DELETE.
- Entiteter:
- Inloggning

