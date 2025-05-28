# Proiect Retele de Calculatoare – Grupa 3A

Acest proiect reprezintă o aplicație dezvoltată în cadrul cursului **Rețele de Calculatoare**, dedicată simulării unei aplicații de gestionare a mașinilor într-un context distribuit, folosind Java și o bază de date embedded (H2).

## 📚 Descriere generală

Scopul proiectului este să pună în aplicare concepte de rețelistică și programare orientată pe evenimente, integrând o aplicație client-server care gestionează informații despre mașini. 

Sistemul permite:

- Adăugarea, ștergerea și modificarea de înregistrări despre mașini.
- Comunicarea între componente folosind rețea (TCP/UDP).
- Persistența datelor într-o bază de date locală H2.

## 🛠️ Tehnologii folosite

- **Java 11+**
- **Java Swing** – pentru interfața grafică
- **Sockets TCP/UDP** – pentru comunicație în rețea
- **H2 Database** – bază de date embedded
- **IntelliJ IDEA** – ca mediu de dezvoltare

## 🗃️ Structura proiectului

- `.idea/` – Fișiere de configurare IntelliJ IDEA  
- `src/` – Codul sursă principal  
  - `client/` – Interfața clientului și conexiunea la server  
  - `server/` – Serverul și logica de gestionare a cererilor  
  - `database/` – Clase pentru conectarea și manipularea bazei de date H2  
- `masiniDB.mv.db` – Fișierul bazei de date H2 (persistență locală)  
- `proiect-retele.iml` – Fișierul proiectului IntelliJ  
- `.gitignore` – Listează fișierele ignorate de Git  
- `README.md` – Documentația proiectului (acest fișier)

## 🚀 Instrucțiuni de rulare

1. Clonează repository-ul:
   ```bash
   git clone https://github.com/aandreis20/ProiectReteleDeCalculatoare3A.git
   cd ProiectReteleDeCalculatoare3A
2. Deschide proiectul în **IntelliJ IDEA**.
3. Rulează clasa principală a serverului (`MainServer.java`) și apoi clientul (`MainClient.java`).
4. Asigură-te că baza de date H2 este accesibilă și configurată corect (implicit este stocată local, în fișierul `masiniDB.mv.db`).

## 💡 Funcționalități principale

- Interfață grafică intuitivă și prietenoasă.
- Adăugarea de noi înregistrări despre mașini.
- Vizualizarea și ștergerea înregistrărilor existente.
- Conectare client-server prin protocol TCP.
- Arhitectură modulară, ușor de extins.

## 🧩 Posibile îmbunătățiri

- Autentificare utilizatori și permisiuni.
- Suport pentru multiple conexiuni client simultane.
- Logare evenimente și jurnalizare activitate.
- Export/import date în format CSV sau JSON.

## 👨‍🎓 Autori

Proiect realizat de studenții grupei 3A în cadrul disciplinei **Rețele de Calculatoare**:

- **Andrei-Cristian Stanciu**
- **Adrian Selavardeanu**
- **Adrian-Petronel Spulber**
