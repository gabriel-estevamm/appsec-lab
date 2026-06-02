# User API – AppSec Study Project

## 📖 Introduction
This API was created **for educational purposes in Application Security (AppSec)**.  
It is not intended for production use, but rather as a **learning tool** to explore secure coding practices, common vulnerabilities, and defensive techniques in modern web applications.

The project simulates a **User Management API** with endpoints for registration, login, profile updates, deactivation, and queries.  
It is designed to demonstrate both **secure implementations** and **potential pitfalls** that can occur when building authentication and authorization flows.

---

## 🌿 Branches
To facilitate study, the repository separates vulnerable and secure code into different branches:

- 🌿 **main** → Contains corrected and hardened implementations with best practices.  
- 🌿 **app-vulnerabilities** → Contains intentionally insecure implementations for study and exploitation. 

⚠️ Use the **vulnerable branch** only in isolated lab environments.  
✅ Use the **secure branch** as a reference for best practices.

---

## 🚀 Endpoints Overview

| Endpoint | Method | Role Required | Description |
|----------|--------|---------------|-------------|
| **`/users/register`** | POST | ADMIN | Register a new user |
| **`/users/login`** | POST | Public | Authenticate user and return JWT |
| **`/users/{id}`** | PUT | USER or ADMIN | Update user profile |
| **`/users/{id}`** | DELETE | ADMIN | Deactivate user |
| **`/users`** | GET | USER or ADMIN | List users |
| **`/users/by-username/{username}`** | GET | USER or ADMIN | Get user by username |

---

## 🔐 Security Notes
- Endpoints are protected with **Spring Security** and role-based access control.  
- JWT tokens are used for authentication.  
- Vulnerable branch may lack proper logging, refresh token rotation, or secure secret handling — intentionally left open for study.  
- Secure branch includes **logging of invalid login attempts**, **CloudWatch integration** (in AWS example), and **refresh token rotation**.

---

## 🧪 Purpose
This API is part of the **AppSec Study Repository** and is meant to:

- Help developers understand **authentication & authorization flaws**.  
- Provide hands-on labs for **JWT exploitation** and **secure design**.  
- Serve as a foundation for exploring **OWASP Top 10 vulnerabilities** in real code.  

---

## ⚠️ Disclaimer
This project is for **study and experimentation only**.  
Do not deploy vulnerable code in production environments.  
Unauthorized testing or exploitation of applications is illegal.
