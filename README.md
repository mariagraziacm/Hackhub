<h1>HackHub</h1>h1>
 
<h2>HackHub è una piattaforma web per la gestione di hackathon e competizioni di programmazione, sviluppata con Spring Boot e Java 21.</h2>h2>

La piattaforma supporta l’intero ciclo di vita di un hackathon, dalla creazione alla valutazione finale delle submission ed erogazione del premio.


<h3>Funzionalità principali</h3>

<h3>Organizzatori</h3>
Creazione e gestione hackathon
Definizione timeline e regole
Gestione iscrizioni team
Assegnazione mentor e giudici

<h3>Partecipanti</h3>
Creazione e gestione team
Invio submission
Richiesta supporto tecnico
Interazione con mentor

<h3>Giudici</h3>
Valutazione delle submission
Applicazione criteri di scoring
Determinazione risultati

<h3>Mentor</h3>
Supporto ai team
Gestione richieste di call
Segnalazioni e feedback


<h3>Stack Tecnologico</h3>
Framework: Spring Boot 4.x
Language: Java 21
Database: MySQL
ORM: Hibernate / JPA
Build Tool: Maven
Utilities: Lombok, Jakarta Validation

<h3>Design Patterns utilizzati</h3>
</h3>h3>Builder Pattern</h3>

Utilizzato per la creazione di oggetti complessi in modo fluido e sicuro.

<h3>State Pattern

Utilizzato per gestire il ciclo di vita degli hackathon e delle submission.

 
 Endpoint principali

| Endpoint | Metodo | Descrizione |
|----------|--------|-------------|
| `PUT /api/calls/{callId}/response` | PUT | Gestisce risposta alla chiamata |
| `POST /api/hackathons/hackathons` | POST | Crea un nuovo hackathon |
| `POST /api/hackathons/hackathons/mentors` | POST | Aggiunge mentore a hackathon |
| `POST /api/hackathons/teams` | POST | Iscrive team a hackathon |
| `GET /api/hackathons/{hackathonId}/results` | GET | Visualizza risultati dell'hackathon |
| `GET /api/hackathons/{hackathonId}/registrations` | GET | Visualizza iscrizioni dell'hackathon |
| `GET /api/hackathons/history/staff/{staffId}` | GET | Mostra storico dello staff |
| `PUT /api/hackathons/hackathons/winner` | PUT | Proclama vincitore dell'hackathon |
| `DELETE /api/hackathons/{hackathonId}/teams/{teamId}` | DELETE | Rimuove team da hackathon |
| `POST /api/invites/team` | POST | Invia invito al team |
| `PUT /api/invites/{inviteId}/accept` | PUT | Accetta invito |
| `PUT /api/invites/{inviteId}/decline` | PUT | Rifiuta invito |
| `POST /api/invites/mentor` | POST | Invita mentore |
| `POST /api/invites/judge` | POST | Invita giudice |
| `POST /api/participation-requests` | POST | Richiede partecipazione al team |
| `PUT /api/participation-requests/{requestId}/accept` | PUT | Accetta richiesta |
| `PUT /api/participation-requests/{requestId}/decline` | PUT | Rifiuta richiesta |
| `POST /api/ratings` | POST | Salva valutazione |
| `POST /api/submissions` | POST | Invia elaborato |
| `PUT /api/submissions/{submissionId}` | PUT | Modifica elaborato |
| `GET /api/submissions/{submissionId}` | GET | Ottiene elaborato |
| `POST /api/submissions/evaluate` | POST | Valuta elaborato |
| `GET /api/submissions/evaluation` | GET | Visualizza valutazione |
| `GET /api/submissions/hackathon/{hackathonId}` | GET | Lista elaborati hackathon |
| `POST /api/support-requests` | POST | Invia richiesta supporto |
| `GET /api/support-requests/mentor/{mentorId}` | GET | Richieste al mentore |
| `POST /api/support-requests/plan-call` | POST | Pianifica chiamata |
| `POST /api/teams` | POST | Crea team |
| `DELETE /api/teams/{teamId}/hackathons/{hackathonId}` | DELETE | Abbandona hackathon |
| `GET /api/violations` | GET | Lista violazioni |
| `PUT /api/violations/{violationId}/resolve` | PUT | Risolve violazione |
