# TxokoDev – Le code au cœur du Pays Basque 🇫🇷🏔️

TxokoDev est une plateforme web locale de mise en relation entre **porteurs d'idées d'application** et **développeurs** de la région du **Pays Basque**. Inspirée de Fiverr, mais avec une forte **identité locale** et une volonté de favoriser le **commerce de proximité**, TxokoDev permet de créer des projets numériques en soutenant les talents de la région.

---

## 🚀 Fonctionnalités

- 🧑‍💻 Création de profils pour développeurs & porteurs d’idées (avec localisation obligatoire Pays Basque)
- 💡 Publication d’idées de projets (titre, budget, description, délai, localisation)
- 📩 Propositions par les développeurs avec messagerie et suivi des statuts
- 📊 Interface admin complète pour gérer utilisateurs, projets, propositions
- 🌐 Interface multilingue : Français 🇫🇷 / Espagnol 🇪🇸 / (Basque bientôt 🇧🇷)
- 🎨 Design localisé (couleurs basques, branding personnalisé)
- 🔐 Authentification sécurisée (JWT)
- 💾 Base de données MySQL
- ⚡ Backend Spring Boot + Frontend Angular

---

## 🛠️ Stack Technique

- **Frontend** : Angular + SCSS (Bootswatch Materia)
- **Backend** : Spring Boot (JHipster) avec API REST sécurisée JWT
- **BDD** : MySQL (développement et production)
- **ORM** : JPA / Hibernate
- **Cache** : Ehcache (local)
- **WebSockets** : activés (chat temps réel futur)
- **Docker** : prêt pour la prod (containers BDD/app)

---

## ⚙️ Lancement en développement

### 1. Backend (Spring Boot)

```bash
./mvnw
```

### 2. Frontend (Angular)

```bash
npm install
npm start
```

Accès : [http://localhost:4200](http://localhost:4200)

Login admin :

- **Username** : admin
- **Password** : admin

---

## 📍 Spécificité locale : Pays Basque

TxokoDev filtre et met en avant uniquement les utilisateurs **basés dans la région Pays Basque**. Une future version intégrera :

- 📍 Géolocalisation automatique
- ✅ Vérification via code postal ou GPS
- 🗺️ Carte interactive des projets/devs

---

## ✨ Prochaines évolutions

- 💳 Intégration Stripe/MangoPay pour paiements sécurisés
- 💬 Chat en temps réel (WebSockets)
- 🌍 Traduction complète en **basque**
- 📱 Version mobile optimisée
- 🏆 Mise en avant de projets locaux réussis

---

## 🤝 Contribution

TxokoDev est un projet open à la contribution, centré sur le **local**, le **proximité**, et le **digital durable**.

---

**Fait avec amour au Pays Basque ❤️**
