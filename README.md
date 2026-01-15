# Beskyttelsesrum - Eksamensopgave

Full-stack REST web-applikation til overblik over danske beskyttelsesrum.

**Programmering 2, 3. semester KEA, E24**

---

## Forudsætninger

- Java 17+
- Maven 3.6+
- MySQL 8.0+

---

## Installation

### 1. Klon repository

```bash
git clone <[repository-url](https://github.com/Gabel1998/beskyttelsesrum/tree/main)>
cd Beskyttelsesrum
```

### 2. Opret database i MySQL

```sql
CREATE DATABASE beskyttelsesrum;
```

### 3. Konfigurer database credentials

Applikationen bruger environment variables. Sæt disse før start:

**Linux/Mac:**
```bash
export DB_URL=jdbc:mysql://localhost:3306/beskyttelsesrum
export DB_USERNAME=root
export DB_PASSWORD=dit_password
```

**Windows (PowerShell):**
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/beskyttelsesrum"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="dit_password"
```

**Windows (CMD):**
```cmd
set DB_URL=jdbc:mysql://localhost:3306/beskyttelsesrum
set DB_USERNAME=root
set DB_PASSWORD=dit_password
```

**Alternativt:** Opret filen `src/main/resources/application-local.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/beskyttelsesrum
spring.datasource.username=root
spring.datasource.password=dit_password
```
Og kør med profil: `mvn spring-boot:run -Dspring-boot.run.profiles=local`

### 4. Start applikationen

```bash
mvn spring-boot:run
```

Ved første opstart hentes kommunedata automatisk fra Danmarks Adressers Web API (98 kommuner).

### 5. Indlæs testdata

Når applikationen kører, indlæs testdata i en ny terminal:

```bash
mysql -u root -p beskyttelsesrum < data_komplet.sql
```

Dette indsætter ca. 120 beskyttelsesrum og 55 vedligeholdelser fordelt på ~60 kommuner.

### 6. Åbn applikationen

Gå til: **http://localhost:8080**

---

## Funktionalitet

### Frontend (5 tabs)

| Tab | Beskrivelse |
|-----|-------------|
| **Kommuner** | Liste over kommuner med deres beskyttelsesrum |
| **Kapacitet** | Kommuner med samlet kapacitet |
| **Kort** | Interaktivt kort med alle beskyttelsesrum (Leaflet) |
| **Administrer** | CRUD for beskyttelsesrum |
| **Vedligehold** | CRUD for vedligeholdelsesopgaver |

### REST API Endpoints

**Beskyttelsesrum:**
- `GET /rooms` - Hent alle beskyttelsesrum
- `GET /rooms/{id}` - Hent enkelt beskyttelsesrum
- `POST /rooms` - Opret beskyttelsesrum
- `PUT /rooms/{id}` - Opdater beskyttelsesrum
- `DELETE /rooms/{id}` - Slet beskyttelsesrum

**Kommuner:**
- `GET /kommuner` - Hent alle kommuner
- `GET /kommuner/{id}` - Hent enkelt kommune
- `GET /kommuner/{kommuneId}/rooms` - Hent beskyttelsesrum i kommune

**Vedligeholdelse:**
- `GET /vedligehold` - Hent alle vedligeholdelser
- `GET /vedligehold/{id}` - Hent enkelt vedligeholdelse
- `POST /vedligehold` - Opret vedligeholdelse
- `PUT /vedligehold/{id}` - Opdater vedligeholdelse
- `DELETE /vedligehold/{id}` - Slet vedligeholdelse

---

## Projektstruktur

```
src/main/java/org/ek/beskyttelsesrum/
├── config/          # DataLoader (henter kommuner fra API)
├── controller/      # REST controllers
├── dto/             # Request/Response DTOs
├── entity/          # JPA entities (Kommune, Beskyttelsesrum, Vedligeholdelse)
├── mapper/          # Entity <-> DTO mappers
├── repository/      # JPA repositories
└── service/         # Business logic

src/main/resources/
├── static/          # Frontend (HTML, CSS, JavaScript)
└── application.properties
```

---

## Teknologier

- **Backend:** Spring Boot, Spring Web, Spring Data JPA
- **Database:** MySQL med JPA/Hibernate
- **Frontend:** HTML, CSS, JavaScript (vanilla)
- **Kort:** Leaflet.js
- **Build:** Maven
- **Lombok:** Til reducering af boilerplate kode

---

## Kør tests

```bash
mvn test
```

---

## Delopgaver

| # | Opgave | Status |
|---|--------|--------|
| 1 | Database med JPA | ✅ Implementeret |
| 2 | REST API | ✅ Implementeret |
| 3 | Frontend | ✅ Implementeret |
| 4 | Kort (valgfri) | ✅ Implementeret |

---

## Kendte begrænsninger

- Ingen brugerautentificering (alle kan oprette/redigere/slette)
- Koordinater er fiktive/approksimerede for testdata
