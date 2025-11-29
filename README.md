# WardenOdhran

**WardenOdhran** is a secure credential management web application built with Java and Thymeleaf.  
It allows users to safely store, organize, and manage login credentials for websites, focusing on **security**, **usability**, and **clean design**.

---

## Features

- **Secure Authentication**
  - Sign up, login, and password reset with hashed storage
  - Session tracking with last visited page restoration

- **Credential Management**
  - Add, edit, delete, and view credentials
  - Collapsible cards for site info: site name, username, password, and notes
  - Case-insensitive sorting for a smooth user experience

- **Security**
  - Hashed passwords; no plain-text sensitive data
  - Time-limited password reset tokens

- **User-Friendly UI**
  - Responsive design with modals for creating/editing credentials
  - Overlay prevents interaction during save confirmation

---

## Tech Stack

- **Backend:** Java, Spring Boot / Jetty WAR deployment
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript
- **Database:** MySQL
- **Build & Deployment:** Gradle, Git

---


## Quick Start

1. **Clone the repository**

```bash
git clone https://github.com/<your-username>/wardenodhran.git
cd wardenodhran
```

2. **Build and run application**

```bash
./gradlew build
java -jar build/libs/wardenodhran.war
```

3. **Open in your browser**

```bash
http://localhost:8080
```

## Production

WardenOdhran is also running on a production server: [http://wardenodhran.com](http://wardenodhran.com)

You can log in or sign up to try out the full functionality in a live environment.


## Security Highlights

- Passwords stored securely as hashes
- Sensitive data never saved in plain text
- Password reset tokens expire automatically
- Session management ensures smooth UX without compromising security

## Future Improvements

- Two-factor authentication (2FA)
- Categories/tags for credentials
- Dark mode and improved mobile responsiveness

## Contribution

Developed solely by **Odhran O’Hagan**.
