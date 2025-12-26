# 🏦 Al Baraka Digital - Plateforme Bancaire Sécurisée

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![Architecture](https://img.shields.io/badge/Architecture-Modulaire-purple.svg)](https://microservices.io/)

## 📋 Table des Matières

- [Vue d'ensemble](#-vue-densemble)
- [Architecture Modulaire](#-architecture-modulaire)
- [Structure du Projet](#-structure-du-projet)
- [Technologies](#-technologies)
- [Installation & Configuration](#-installation--configuration)
- [API Endpoints](#-api-endpoints)
- [Sécurité](#-sécurité)
- [Docker](#-docker)

---

## 🎯 Vue d'ensemble

Plateforme bancaire digitale sécurisée pour la gestion des opérations bancaires (dépôts, retraits, virements) avec validation automatique/manuelle selon les montants.

**Problématique résolue :**
- ❌ Traitement manuel → ✅ Automatisation intelligente
- ❌ Risques de fraude → ✅ Workflows de validation sécurisés
- ❌ Absence de traçabilité → ✅ Historique complet des transactions
- ❌ Code monolithique → ✅ Architecture modulaire maintenable

---

## 🏗️ Architecture Modulaire

```
al-baraka-digital/
├── common/         # DTOs, Exceptions, Enums, Utils, Mappers partagés
├── security/       # JWT, OAuth2, Spring Security Config
├── user/           # Gestion utilisateurs (Entity, DTO, Service, Controller)
├── account/        # Gestion comptes bancaires
├── operation/      # Opérations bancaires (DEPOSIT, WITHDRAWAL, TRANSFER)
└── app/            # Application Spring Boot (Point d'entrée)
```

### Principe d'Architecture

```
┌─────────────────────────────────────────────────────────┐
│                   al-baraka-digital                      │
│                     (Parent Module)                      │
└─────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
    ┌───▼────┐        ┌────▼─────┐       ┌────▼────┐
    │ common │        │ security │       │  user   │
    │(shared)│        │  (auth)  │       │(domain) │
    └────────┘        └──────────┘       └─────────┘
        │                   │                   │
        └───────────────────┼───────────────────┘
                      ┌─────▼──────┐
                      │  operation │
                      │  (domain)  │
                      └────────────┘
                      ┌─────▼──────┐
                      │   account  │
                      │  (domain)  │
                      └────────────┘
                      ┌─────▼──────┐
                      │     app    │
                      │(Spring Boot)│
                      └────────────┘
```

**Avantages :**
- ✅ Séparation des responsabilités
- ✅ Réutilisabilité du code
- ✅ Testabilité indépendante
- ✅ Scalabilité & maintenance facilitée
- ✅ Prêt pour migration microservices

---

## 📁 Structure du Projet

```
al-baraka-digital/
│
├── common/                              # Module Commun
│   └── src/main/java/com/albaraka/common/
│       ├── dto/                         # BaseDTO, ApiResponse, ErrorResponse, PageResponse
│       ├── enums/                       # Status, ResponseCode, FileType
│       ├── exception/                   # GlobalExceptionHandler, BusinessException, etc.
│       ├── mapper/                      # BaseMapper, MapperConfig
│       └── util/                        # DateUtil, StringUtil, ValidationUtil, FileUtil
│
├── security/                            # Module Sécurité
│   └── src/main/java/com/albaraka/security/
│       ├── config/                      # SecurityConfig, JwtConfig, OAuth2Config
│       ├── filter/                      # JwtAuthenticationFilter
│       ├── jwt/                         # JwtTokenProvider, JwtTokenValidator
│       ├── service/                     # CustomUserDetailsService, AuthenticationService
│       └── dto/                         # LoginRequest, LoginResponse
│
├── user/                                # Module Utilisateur
│   └── src/main/java/com/albaraka/user/
│       ├── domain/entity/               # User.java
│       ├── dto/                         # UserDTO, CreateUserRequest, UpdateUserRequest
│       ├── enums/                       # Role (CLIENT, AGENT_BANCAIRE, ADMIN), UserStatus
│       ├── mapper/                      # UserMapper (MapStruct)
│       ├── repository/                  # UserRepository (Spring Data JPA)
│       ├── service/                     # UserService, UserServiceImpl
│       └── controller/                  # UserController, AdminUserController
│
├── account/                             # Module Compte
│   └── src/main/java/com/albaraka/account/
│       ├── domain/entity/               # Account.java
│       ├── dto/                         # AccountDTO, CreateAccountRequest
│       ├── enums/                       # AccountStatus, AccountType
│       ├── mapper/                      # AccountMapper
│       ├── repository/                  # AccountRepository
│       ├── service/                     # AccountService, AccountServiceImpl
│       └── controller/                  # AccountController
│
├── operation/                           # Module Opération
│   └── src/main/java/com/albaraka/operation/
│       ├── domain/entity/               # Operation.java, Document.java
│       ├── dto/                         # OperationDTO, CreateOperationRequest, DocumentDTO
│       ├── enums/                       # OperationType, OperationStatus, DocumentType
│       ├── mapper/                      # OperationMapper, DocumentMapper
│       ├── repository/                  # OperationRepository, DocumentRepository
│       ├── service/                     # OperationService, ValidationService, DocumentService
│       └── controller/                  # ClientOperationController, AgentOperationController
│
└── app/                                 # Application Spring Boot
    ├── src/main/java/com/albaraka/app/
    │   ├── AlBarakaDigitalApplication.java  # Main Class
    │   └── config/                      # AppConfig, SwaggerConfig, CorsConfig
    ├── src/main/resources/
    │   ├── application.yml              # Configuration principale
    │   ├── application-dev.yml
    │   └── application-prod.yml
    ├── Dockerfile
    └── docker-compose.yml
```

---

## 🛠️ Technologies

| Catégorie | Technologies |
|-----------|-------------|
| **Backend** | Java 17, Spring Boot 3.2+, Spring Security 6, Spring Data JPA |
| **Sécurité** | JWT (io.jsonwebtoken), OAuth2 Resource Server, BCrypt |
| **Base de données** | PostgreSQL 15+ |
| **Mapping** | MapStruct, ModelMapper |
| **Validation** | Bean Validation (JSR-380) |
| **Documentation** | Swagger/OpenAPI 3 |
| **DevOps** | Docker, Docker Compose, Maven |

---

## 🚀 Installation & Configuration

### Prérequis
```bash
java -version    # Java 17+
mvn -version     # Maven 3.8+
docker --version # Docker
```

### Installation Rapide

```bash
# 1. Cloner le projet
git clone https://github.com/votre-username/al-baraka-digital.git
cd al-baraka-digital

# 2. Configuration (.env)
cat > .env << EOF
DB_URL=jdbc:postgresql://localhost:5432/albaraka_db
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your-super-secret-key-change-this-in-production-min-256-bits
JWT_EXPIRATION=86400000
EOF

# 3. Lancer avec Docker
docker-compose up -d

# OU sans Docker
mvn clean install
mvn spring-boot:run
```

**Application disponible sur :** `http://localhost:8080`  
**Swagger UI :** `http://localhost:8080/swagger-ui.html`

---

## 📚 API Endpoints

### Authentification (Public)
| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/auth/register` | POST | Créer compte client |
| `/auth/login` | POST | Login (retourne JWT) |

### Client (JWT requis - Role: CLIENT)
| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/client/profile` | GET | Profil utilisateur |
| `/api/client/account` | GET | Détails compte & solde |
| `/api/client/operations` | POST | Créer opération (dépôt/retrait/virement) |
| `/api/client/operations` | GET | Liste opérations |
| `/api/client/operations/{id}/document` | POST | Upload justificatif |

### Agent (JWT requis - Role: AGENT_BANCAIRE)
| Endpoint | Méthode | Sécurité | Description |
|----------|---------|----------|-------------|
| `/api/agent/operations/pending` | GET | **OAuth2** (scope: `operations.read`) | Liste opérations PENDING |
| `/api/agent/operations/{id}/approve` | PUT | JWT | Approuver opération |
| `/api/agent/operations/{id}/reject` | PUT | JWT | Rejeter opération |

### Admin (JWT requis - Role: ADMIN)
| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/admin/users` | POST | Créer utilisateur |
| `/api/admin/users` | GET | Liste utilisateurs |
| `/api/admin/users/{id}` | PUT/DELETE | Modifier/Supprimer |
| `/api/admin/users/{id}/status` | PATCH | Activer/Désactiver |

---

## 🔐 Sécurité

### JWT Authentication Flow
```
Client → POST /auth/login {email, password}
         ↓
Server → Valide credentials → Génère JWT (HMAC-SHA256)
         ↓
Client ← {token: "eyJhbGc...", expiresIn: 86400000}
         ↓
Client → GET /api/client/** 
         Header: Authorization: Bearer <JWT>
         ↓
Server → Valide JWT → Extrait user info → Vérifie permissions
         ↓
Client ← Réponse sécurisée
```

### Structure JWT Token
```json
{
  "sub": "client@example.com",
  "role": "CLIENT",
  "userId": 1,
  "accountNumber": "ACC123456789",
  "iat": 1703001600,
  "exp": 1703088000
}
```

### Sécurité Implémentée
- ✅ **JWT Stateless** : Authentification sans session
- ✅ **OAuth2** : Protection endpoints sensibles (ex: opérations PENDING)
- ✅ **BCrypt** : Encodage mots de passe (cost factor 12)
- ✅ **CORS** : Configuration restrictive
- ✅ **Validation** : Bean Validation sur tous les inputs
- ✅ **HTTPS** : Recommandé en production

---

## 🔄 Workflows Métier

### Règles de Validation

| Opération | Montant ≤ 10 000 DH | Montant > 10 000 DH |
|-----------|---------------------|---------------------|
| **Dépôt** | ✅ Auto-validé | ⏳ PENDING → Agent valide |
| **Retrait** | ✅ Auto-validé | ⏳ PENDING → Agent valide |
| **Virement** | ✅ Auto-validé | ⏳ PENDING → Agent valide |

### Workflow Simplifié
```
Opération créée → Montant > 10K ? 
                     │
                     ├─ NON → Auto-validée → Solde mis à jour
                     │
                     └─ OUI → Upload justificatif → PENDING 
                                                      ↓
                                            Agent valide/rejette
                                                      ↓
                                            Solde mis à jour si approuvé
```

---

## 🐳 Docker

### Dockerfile
```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/uploads
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: albaraka_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  backend:
    build: .
    depends_on:
      - postgres
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/albaraka_db
      DB_USERNAME: postgres
      DB_PASSWORD: postgres
      JWT_SECRET: change-this-in-production
    ports:
      - "8080:8080"
    volumes:
      - uploads_data:/app/uploads

volumes:
  postgres_data:
  uploads_data:
```

### Commandes Docker
```bash
docker-compose up -d          # Démarrer
docker-compose logs -f        # Logs en temps réel
docker-compose down           # Arrêter
docker-compose down -v        # Arrêter + supprimer volumes
```

---

## 🧪 Tests

```bash
mvn test                      # Tous les tests
mvn test -Dtest="*UnitTest"   # Tests unitaires
mvn test jacoco:report        # Avec couverture de code
```

---

## 📊 Schéma Base de Données

```sql
users (id, email, password, full_name, role, active, created_at)
  ↓ 1:1
accounts (id, account_number, balance, owner_id, created_at)
  ↓ 1:N
operations (id, type, amount, status, account_source_id, account_destination_id, created_at)
  ↓ 1:N
documents (id, file_name, file_type, storage_path, operation_id, uploaded_at)
```

---


---

## 👥 Auteur

**Charaf Eddine Tbibzat** - Développeur Backend & DevOps  
📧 Email : charafeddinetbibzat@gmail.com 
🐛 Issues : [GitHub Issues](https://github.com/votre-username/al-baraka-digital/issues)

---

<div align="center">

**⭐ N'oubliez pas de mettre une étoile si ce projet vous aide ! ⭐**

Made with ❤️ by Al Baraka Digital Team

</div>
