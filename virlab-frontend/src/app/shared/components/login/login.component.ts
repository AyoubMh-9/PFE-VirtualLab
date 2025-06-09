import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service'; // Adjust path if necessary

// ... (rest of your LoginComponent TypeScript code remains the same)

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <div class="site-container">
      <nav class="main-navbar">
        <div class="navbar-content">
          <a routerLink="/home" class="navbar-brand">
            <img src="assets/images/nicomatic-logo-white.png" alt="Nicomatic Logo" class="brand-logo">
            <span>Nicomatic Virtual Lab</span>
          </a>
          <ul class="nav-links">
            <li><a routerLink="/home" routerLinkActive="active" [routerLinkActiveOptions]="{exact:true}">Accueil</a></li>
            <li><a href="https://www.nicomatic.com/company" routerLinkActive="active">À Propos</a></li>
            <li><a href="https://www.nicomatic.com/services" routerLinkActive="active">Services</a></li>
            <li><a  href="https://www.nicomatic.com/contact" routerLinkActive="active">Contact</a></li>
          </ul>
        </div>
      </nav>

      <main class="login-page-main">
        <div class="left-panel">
  <div class="company-info-card">
    <div class="company-logo-header">
      <img src="assets/images/nicomatic-logo-white.png" alt="Nicomatic Logo" class="company-logo-full-width">
    </div>
    <h1 class="company-title">Bienvenue sur le <span>Virtual Lab</span></h1>
    <p class="company-slogan">
      Optimisez la gestion de vos tests et projets avec notre plateforme intuitive.
    </p>
    <div class="info-features">
      <div class="feature-item">
        <svg width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" d="M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M12,4A8,8 0 0,1 20,12A8,8 0 0,1 12,20A8,8 0 0,1 4,12A8,8 0 0,1 12,4M11,16.5L6.5,12L7.91,10.59L11,13.67L16.09,8.59L17.5,10L11,16.5Z"/></svg>
        <span>Accès sécurisé et rapide</span>
      </div>
      <div class="feature-item">
        <svg width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" d="M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M12,4A8,8 0 0,1 20,12A8,8 0 0,1 12,20A8,8 0 0,1 4,12A8,8 0 0,1 12,4M16,10H8V12H16V10M16,14H8V16H16V14Z"/></svg>
        <span>Interface conviviale</span>
      </div>
      <div class="feature-item">
        <svg width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" d="M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M12,4A8,8 0 0,1 20,12A8,8 0 0,1 12,20A8,8 0 0,1 4,12A8,8 0 0,1 12,4M13,7H11V14H13V7M13,15H11V17H13V15Z"/></svg>
        <span>Assistance dédiée</span>
      </div>
    </div>
    <a href="https://www.nicomatic.com" target="_blank" class="visit-website-link">
        Visiter Nicomatic.com
        <svg width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M14,3V5H17.59L7.76,14.83L9.17,16.24L19,6.41V10H21V3M19,19H5V5H12V3H5C3.89,3 3,3.9 3,5V19A2,2 0 0,0 5,21H19A2,2 0 0,0 21,19V12H19V19Z"/></svg>
    </a>
  </div>
</div>

        <div class="right-panel">
          <div class="login-card">
            <div class="card-header">
              <h2>Connectez-vous</h2>
              <p class="subtitle">Utilisez vos identifiants pour accéder</p>
            </div>

            <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="login-form">
              <div class="form-group">
                <div class="input-wrapper">
                  <input
                    type="text"
                    id="username"
                    formControlName="username"
                    placeholder=" "
                    [class.invalid]="username?.invalid && (username?.dirty || username?.touched)"
                  >
                  <label for="username" class="placeholder-label">Nom d'utilisateur</label>
                  <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24">
                    <path fill="currentColor" d="M12,4A4,4 0 0,1 16,8A4,4 0 0,1 12,12A4,4 0 0,1 8,8A4,4 0 0,1 12,4M12,14C16.42,14 20,15.79 20,18V20H4V18C4,15.79 7.58,14 12,14Z"/>
                  </svg>
                </div>
                <div *ngIf="username?.invalid && (username?.dirty || username?.touched)" class="error-message">
                  <svg width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M13 14H11V9H13M13 18H11V16H13M12 2A10 10 0 0 0 2 12A10 10 0 0 0 12 22A10 10 0 0 0 22 12A10 10 0 0 0 12 2Z"/></svg>
                  <span>Nom d'utilisateur requis</span>
                </div>
              </div>

              <div class="form-group">
                <div class="input-wrapper">
                  <input
                    type="password"
                    id="password"
                    formControlName="password"
                    placeholder=" "
                    [class.invalid]="password?.invalid && (password?.dirty || password?.touched)"
                  >
                  <label for="password" class="placeholder-label">Mot de passe</label>
                  <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24">
                    <path fill="currentColor" d="M12,17C10.89,17 10,16.1 10,15C10,13.89 10.89,13 12,13A2,2 0 0,1 14,15A2,2 0 0,1 12,17M18,20V10H6V20H18M18,8A2,2 0 0,1 20,10V20A2,2 0 0,1 18,22H6C4.89,22 4,21.1 4,20V10C4,8.89 4.89,8 6,8H7V6A5,5 0 0,1 12,1A5,5 0 0,1 17,6V8H18M12,3A3,3 0 0,0 9,6V8H15V6A3,3 0 0,0 12,3Z"/>
                  </svg>
                </div>
                <div *ngIf="password?.invalid && (password?.dirty || password?.touched)" class="error-message">
                  <svg width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M13 14H11V9H13M13 18H11V16H13M12 2A10 10 0 0 0 2 12A10 10 0 0 0 12 22A10 10 0 0 0 22 12A10 10 0 0 0 12 2Z"/></svg>
                  <span>Mot de passe requis</span>
                </div>
              </div>

              <div class="form-options">
                <a routerLink="/forgot-password" class="forgot-password">Mot de passe oublié ?</a>
              </div>

              <button type="submit" class="login-button" [disabled]="loginForm.invalid || isLoading">
                <span *ngIf="!isLoading">Se connecter</span>
                <span *ngIf="isLoading" class="loading-spinner"></span>
              </button>

              <div *ngIf="error" class="error-message global-error">
                <svg width="20" height="20" viewBox="0 0 24 24">
                  <path fill="currentColor" d="M11,15H13V17H11V15M11,7H13V13H11V7M12,2C6.47,2 2,6.5 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M12,20A8,8 0 0,1 4,12A8,8 0 0,1 12,4A8,8 0 0,1 20,12A8,8 0 0,1 12,20Z"/>
                </svg>
                <span>{{ error }}</span>
              </div>
            </form>
          </div>
        </div>
      </main>

      <footer class="site-footer">
        <p>© {{ currentYear }} Nicomatic Virtual Lab. Tous droits réservés.</p>
        <div class="footer-links">
          <a routerLink="/privacy">Confidentialité</a>
          <a routerLink="/terms">Conditions d'utilisation</a>
          <a routerLink="/contact">Contact</a>
        </div>
      </footer>
    </div>
  `,
  styles: [`
    /* Variables CSS */
    :root {
      --primary-blue: #007bff; /* Vibrant Blue */
      --secondary-blue: #0056b3; /* Darker Blue */
      --white: #FFFFFF;
      --light-gray: #F8F9FA;
      --dark-gray: #343A40;
      --text-color: #495057;
      --border-color: #DEE2E6;
      --shadow-color: rgba(0, 0, 0, 0.1);
      --error-color: #DC3545;
      --gradient-bg: linear-gradient(135deg, #E0F2F7 0%, #B3E0F2 30%, #80BFFF 100%); /* Light blue to a slightly darker blue */
      --card-bg: rgba(255, 255, 255, 0.95); /* Slightly transparent white for cards */
      --card-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
    }

    /* Base Styles */
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    :host {
      display: block;
      min-height: 100vh;
      font-family: 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
      color: var(--text-color);
      line-height: 1.6;
      background: var(--gradient-bg);
    }

    .site-container {
      display: flex;
      flex-direction: column;
      min-height: 100vh;
      overflow: hidden;
      position: relative;
    }

    /* Top Navigation Bar */
    .main-navbar {
      background-color: var(--primary-blue);
      padding: 1rem 2rem;
      box-shadow: 0 2px 8px var(--shadow-color);
      position: sticky;
      top: 0;
      z-index: 1000;
      width: 100%;
      color: var(--white);
    }

    .navbar-content {
      max-width: 1400px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .navbar-brand {
      display: flex;
      align-items: center;
      text-decoration: none;
      color: var(--white);
      font-weight: 700;
      font-size: 1.3rem;
      transition: transform 0.2s ease;
    }

    .navbar-brand:hover {
      transform: translateX(5px);
    }

    /* Enhanced Navbar Logo */
    .brand-logo {
      height: 45px; /* Slightly larger */
      margin-right: 12px; /* More space */
      filter: drop-shadow(0 0 8px rgba(255,255,255,0.4)); /* Stronger, defined glow */
      transition: all 0.3s ease-out;
    }
    .navbar-brand:hover .brand-logo {
        transform: scale(1.05); /* Slight scale on hover */
        filter: drop-shadow(0 0 12px rgba(255,255,255,0.6)); /* More intense glow on hover */
    }


    .nav-links {
      list-style: none;
      display: flex;
      gap: 1.8rem;
    }

    .nav-links a {
      text-decoration: none;
      color: rgba(255, 255, 255, 0.8);
      font-weight: 500;
      padding: 0.6rem 0.2rem;
      position: relative;
      transition: color 0.3s ease, transform 0.2s ease;
    }

    .nav-links a:hover {
      color: var(--white);
      transform: translateY(-2px);
    }

    .nav-links a.active {
      color: var(--white);
      font-weight: 600;
    }

    .nav-links a.active::after {
      content: '';
      position: absolute;
      bottom: -8px;
      left: 0;
      width: 100%;
      height: 3px;
      background-color: var(--white);
      border-radius: 2px;
      animation: expandUnderline 0.3s forwards;
    }

    @keyframes expandUnderline {
      from { transform: scaleX(0); }
      to { transform: scaleX(1); }
    }

    /* Main Content Area */
    .login-page-main {
      flex: 1;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 3rem 2rem;
      gap: 4rem;
      max-width: 1400px;
      margin: 0 auto;
      animation: fadeIn 0.8s ease-out;
    }

    /* Left Panel - Company Info */
    .left-panel {
      flex: 1;
      max-width: 600px;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .company-info-card {
      padding: 0;
      background-color: var(--primary-blue);
      color: var(--white);
      border-radius: 12px;
      box-shadow: var(--card-shadow);
      padding: 3rem;
      text-align: center;
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      transform: translateY(0);
      transition: transform 0.3s ease-out, box-shadow 0.3s ease-out;
      position: relative; /* For the background pattern */
      overflow: hidden; /* Ensure pattern doesn't bleed out */
    }
    /* ... (existing styles) ... */



/* NEW: Style for the logo header area */
.company-logo-header {
  background-color: rgba(255, 255, 255, 0.15); /* Slightly lighter background for the header area */
  padding: 2rem; /* Padding around the logo inside this header */
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  margin-bottom: 2rem; /* Space between logo header and text content */
  display: flex;
  justify-content: center; /* Center the logo horizontally */
  align-items: center;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1); /* Subtle shadow at the bottom of the header */
}

/* Style for the logo image itself within the header */
.company-logo-full-width {
  max-width: 80%; /* Controls the width of the logo relative to its parent header */
  height: auto; /* Maintains aspect ratio */
  filter: drop-shadow(0 0 20px rgba(255,255,255,0.7)); /* Strong glow for visibility */
  transition: all 0.3s ease-out;
}

.company-logo-header:hover .company-logo-full-width {
    transform: scale(1.05); /* Slight scale on hover */
    filter: drop-shadow(0 0 25px rgba(255,255,255,0.9)); /* More intense glow on hover */
}

/* Adjust padding for the rest of the content within the card */
.company-title,
.company-slogan,
.info-features,
.visit-website-link {
    padding-left: 3rem; /* Add padding to align text content */
    padding-right: 3rem;
    text-align: center; /* Re-center text content */
}

/* ... (rest of your existing CSS) ... */
    /* Optional: Subtle background pattern for decoration */
    .company-info-card::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-image: url('data:image/svg+xml,%3Csvg width="6" height="6" viewBox="0 0 6 6" xmlns="http://www.w3.org/2000/svg"%3E%3Cg fill="%23ffffff" fill-opacity="0.05" fill-rule="evenodd"%3E%3Cpath d="M5 0h1L0 6V5zm0 6v-1L6 0h-1z"/%3E%3C/g%3E%3C/svg%3E'); /* Very subtle diagonal lines */
        opacity: 0.8;
        z-index: 0;
        pointer-events: none;
    }


    .company-info-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
    }

    /* New wrapper for large logo decoration */
    .logo-decoration-wrapper {
        position: relative;
        z-index: 1; /* Bring logo above pattern */
        margin-bottom: 2.5rem; /* Space below the logo */
        background-color: rgba(255, 255, 255, 0.1); /* Subtle white background */
        padding: 1.5rem; /* Padding around logo */
        border-radius: 50%; /* Make it circular */
        box-shadow: 0 0 0 10px rgba(255, 255, 255, 0.05), /* Outer subtle border */
                    0 0 30px rgba(255, 255, 255, 0.2); /* Stronger glow */
        transition: all 0.3s ease-out;
    }
    .company-info-card:hover .logo-decoration-wrapper {
        transform: scale(1.05) rotate(5deg); /* Slight rotate and scale on hover */
        box-shadow: 0 0 0 12px rgba(255, 255, 255, 0.1),
                    0 0 40px rgba(255, 255, 255, 0.3);
    }


    /* Enhanced Large Logo */
    .company-logo-large {
      height: 100px; /* Even larger logo */
      filter: drop-shadow(0 0 15px rgba(255,255,255,0.6)); /* Very strong glow */
      transition: all 0.3s ease-out;
    }
    .company-info-card:hover .company-logo-large {
        filter: drop-shadow(0 0 20px rgba(255,255,255,0.8)); /* Even more intense glow on hover */
    }


    .company-title {
      font-size: 3rem;
      font-weight: 800;
      margin-bottom: 1rem;
      line-height: 1.2;
      position: relative; /* Above pattern */
      z-index: 1;
    }

    .company-title span {
      color: var(--white);
      display: block;
      letter-spacing: 1px;
    }

    .company-slogan {
      font-size: 1.2rem;
      opacity: 0.9;
      margin-bottom: 2.5rem;
      max-width: 450px;
      position: relative;
      z-index: 1;
    }

    .info-features {
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
      margin-bottom: 3rem;
      width: 100%;
      max-width: 380px;
      text-align: left;
      position: relative;
      z-index: 1;
    }

    .info-features .feature-item {
      display: flex;
      align-items: center;
      gap: 1rem;
      font-size: 1rem;
      font-weight: 500;
      opacity: 0.95;
    }

    .info-features .feature-item svg {
      color: var(--white);
      min-width: 24px;
      min-height: 24px;
    }

    .visit-website-link {
      display: inline-flex;
      align-items: center;
      gap: 0.7rem;
      color: var(--white);
      font-weight: 600;
      text-decoration: none;
      font-size: 1.1rem;
      padding: 0.9rem 2rem;
      border: 2px solid var(--white);
      border-radius: 30px;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(255, 255, 255, 0.2);
      position: relative;
      z-index: 1;
    }

    .visit-website-link:hover {
      background-color: var(--white);
      color: var(--primary-blue);
      transform: translateY(-4px);
      box-shadow: 0 8px 20px rgba(255, 255, 255, 0.3);
    }
    .visit-website-link:hover svg {
        color: var(--primary-blue);
    }


    /* Right Panel - Login Form (remains largely the same as the previous "performance" style) */
    .right-panel {
      flex-shrink: 0;
      width: 450px;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .login-card {
      background-color: var(--card-bg);
      border-radius: 12px;
      box-shadow: var(--card-shadow);
      width: 100%;
      max-width: 450px;
      overflow: hidden;
      transform: translateY(0);
      transition: transform 0.3s ease-out, box-shadow 0.3s ease-out;
    }

    .login-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
    }

    .card-header {
      padding: 2.5rem 3rem 1.5rem;
      border-bottom: 1px solid var(--border-color);
      text-align: center;
      background-color: var(--white);
    }

    .login-card h2 {
      color: var(--primary-blue);
      font-size: 2.2rem;
      font-weight: 700;
      margin-bottom: 0.75rem;
    }

    .subtitle {
      color: var(--text-color);
      font-size: 1rem;
    }

    /* Form Styles */
    .login-form {
      padding: 2rem 3rem 2.5rem;
      background-color: var(--white);
    }

    .form-group {
      margin-bottom: 1.8rem;
    }

    .input-wrapper {
      position: relative;
    }

    .input-wrapper label {
      position: absolute;
      top: 15px;
      left: 1.25rem;
      font-size: 0.95rem;
      color: var(--text-color);
      transition: all 0.2s ease;
      pointer-events: none;
      background-color: var(--white);
      padding: 0 5px;
      z-index: 1;
    }

    .input-wrapper input {
      width: 100%;
      padding: 1.1rem 3rem 1.1rem 1.5rem;
      font-size: 1rem;
      border: 1px solid var(--border-color);
      border-radius: 8px;
      transition: all 0.3s ease;
      background-color: var(--light-gray);
      color: var(--dark-gray);
      outline: none;
    }

    .input-wrapper input:focus,
    .input-wrapper input:not(:placeholder-shown) {
      border-color: var(--primary-blue);
      box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.2);
      background-color: var(--white);
    }

    .input-wrapper input:not(:placeholder-shown) + label,
    .input-wrapper input:focus + label {
      top: -10px;
      left: 10px;
      font-size: 0.75rem;
      color: var(--primary-blue);
      background-color: var(--white);
      transform: translateY(-2px);
    }

    .input-wrapper input:focus ~ svg.input-icon {
      color: var(--primary-blue);
    }

    .input-wrapper svg.input-icon {
      position: absolute;
      right: 15px;
      top: 50%;
      transform: translateY(-50%);
      color: var(--text-color);
      transition: color 0.3s ease;
      z-index: 2;
    }

    input.invalid {
      border-color: var(--error-color) !important;
      box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.2) !important;
    }

    .form-options {
      display: flex;
      justify-content: flex-end;
      margin: -1rem 0 2rem;
    }

    .forgot-password {
      color: var(--primary-blue);
      font-size: 0.95rem;
      text-decoration: none;
      font-weight: 500;
      transition: color 0.2s ease, text-decoration 0.2s ease;
    }

    .forgot-password:hover {
      color: var(--secondary-blue);
      text-decoration: underline;
    }

    .login-button {
      width: 100%;
      padding: 1.2rem;
      background-color: var(--primary-blue);
      color: var(--white);
      border: none;
      border-radius: 8px;
      font-size: 1.1rem;
      font-weight: 600;
      cursor: pointer;
      transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 0.75rem;
      box-shadow: 0 6px 15px rgba(0, 123, 255, 0.3);
    }

    .login-button:hover:not(:disabled) {
      background-color: var(--secondary-blue);
      transform: translateY(-3px);
      box-shadow: 0 8px 20px rgba(0, 123, 255, 0.4);
    }

    .login-button:active:not(:disabled) {
      transform: translateY(0);
      box-shadow: 0 2px 8px rgba(0, 123, 255, 0.2);
    }

    .login-button:disabled {
      background-color: #B0D8F7;
      cursor: not-allowed;
      box-shadow: none;
    }

    .loading-spinner {
      width: 24px;
      height: 24px;
      border: 4px solid rgba(255, 255, 255, 0.3);
      border-radius: 50%;
      border-top-color: var(--white);
      animation: spin 0.8s cubic-bezier(0.68, -0.55, 0.27, 1.55) infinite;
    }

    @keyframes spin {
      to { transform: rotate(360deg); }
    }

    /* Error Messages */
    .error-message {
      color: var(--error-color);
      font-size: 0.85rem;
      margin-top: 0.5rem;
      display: flex;
      align-items: center;
      gap: 0.5rem;
      font-weight: 500;
      animation: fadeIn 0.3s ease-out;
    }

    .error-message svg {
        min-width: 16px;
        min-height: 16px;
    }

    .global-error {
      background-color: rgba(220, 53, 69, 0.1);
      padding: 1rem 1.25rem;
      border-radius: 8px;
      margin-top: 1.5rem;
      display: flex;
      align-items: center;
      gap: 0.75rem;
      font-size: 0.95rem;
      box-shadow: 0 2px 8px rgba(220, 53, 69, 0.08);
      border-left: 5px solid var(--error-color);
    }

    /* Site Footer */
    .site-footer {
      background-color: var(--dark-gray);
      color: rgba(255, 255, 255, 0.8);
      padding: 1.5rem 2rem;
      text-align: center;
      font-size: 0.9rem;
      width: 100%;
    }

    .site-footer p {
      margin-bottom: 0.8rem;
    }

    .site-footer .footer-links {
      display: flex;
      justify-content: center;
      gap: 1.8rem;
      flex-wrap: wrap;
    }

    .site-footer .footer-links a {
      color: rgba(255, 255, 255, 0.8);
      text-decoration: none;
      transition: color 0.2s ease;
    }

    .site-footer .footer-links a:hover {
      color: var(--white);
      text-decoration: underline;
    }

    /* General Animations */
    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(10px); }
      to { opacity: 1; transform: translateY(0); }
    }

    /* Responsive Design (remains largely the same, minor adjustments for new logo styles) */
    @media (max-width: 992px) {
      .navbar-content {
        flex-direction: column;
        gap: 1rem;
      }
      .nav-links {
        justify-content: center;
        flex-wrap: wrap;
        gap: 1rem;
      }
      .nav-links a::after {
        bottom: -5px;
      }

      .login-page-main {
        flex-direction: column;
        padding: 2rem 1rem;
        gap: 2.5rem;
      }

      .left-panel, .right-panel {
        width: 100%;
        max-width: 550px;
      }

      .company-info-card {
        padding: 2.5rem;
      }
      .company-title {
        font-size: 2.5rem;
      }
      .company-title span {
        font-size: 2.8rem;
      }
      .company-slogan {
        font-size: 1.1rem;
      }
    }

    @media (max-width: 768px) {
      .main-navbar {
        padding: 0.8rem 1rem;
      }
      .brand-logo {
        height: 35px;
        margin-right: 8px;
      }
      .navbar-brand {
        font-size: 1.1rem;
      }
      .nav-links {
        gap: 0.8rem;
      }
      .nav-links a {
        padding: 0.4rem 0.5rem;
        font-size: 0.9rem;
      }

      .login-page-main {
        padding: 1.5rem 0.5rem;
        gap: 2rem;
      }

      .company-info-card {
        padding: 2rem;
      }
      .logo-decoration-wrapper {
          padding: 1rem;
          margin-bottom: 2rem;
      }
      .company-logo-large {
        height: 80px; /* Adjust size for responsiveness */
      }
      .company-title {
        font-size: 2rem;
      }
      .company-title span {
        font-size: 2.3rem;
      }
      .company-slogan {
        font-size: 1rem;
        margin-bottom: 2rem;
      }
      .info-features {
        gap: 1rem;
        margin-bottom: 2rem;
      }
      .info-features .feature-item {
        font-size: 0.9rem;
      }
      .visit-website-link {
        font-size: 1rem;
        padding: 0.8rem 1.5rem;
      }

      .login-card h2 {
        font-size: 1.8rem;
      }
      .subtitle {
        font-size: 0.9rem;
      }
      .card-header, .login-form {
        padding-left: 1.5rem;
        padding-right: 1.5rem;
      }
      .login-button {
        padding: 1rem;
        font-size: 1rem;
      }
    }

    @media (max-width: 576px) {
        .navbar-brand span {
            display: none;
        }
        .navbar-brand {
            font-size: 1rem;
        }

        .login-page-main {
            padding: 1rem 0.5rem;
        }

        .left-panel, .right-panel {
            max-width: 95%;
        }

        .company-info-card, .login-card {
            padding: 1.5rem;
        }
        .logo-decoration-wrapper {
            padding: 0.8rem;
            margin-bottom: 1.5rem;
        }
        .company-logo-large {
            height: 60px;
        }
        .company-title {
            font-size: 1.6rem;
        }
        .company-title span {
            font-size: 1.9rem;
        }
        .company-slogan {
            font-size: 0.85rem;
        }
        .card-header, .login-form {
            padding-left: 1rem;
            padding-right: 1rem;
        }
        .login-card h2 {
            font-size: 1.5rem;
        }
        .subtitle {
            font-size: 0.85rem;
        }
        .input-wrapper input {
            padding: 0.9rem 2.5rem 0.9rem 1rem;
            font-size: 0.9rem;
        }
        .input-wrapper label {
            font-size: 0.75rem;
            top: 10px;
        }
    }
  `]
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isLoading = false;
  error: string | null = null;
  currentYear: number;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
    this.currentYear = new Date().getFullYear();
  }

  ngOnInit(): void {
    this.authService.logout(false);
    this.loginForm.reset();

    if (isPlatformBrowser(this.platformId)) {
      history.pushState(null, '', location.href);
      window.onpopstate = () => {
        history.pushState(null, '', location.href);
      };
    }
  }

  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.isLoading = true;
    this.error = null;

    const credentials = this.loginForm.value;
    this.authService.login(credentials).subscribe({
      next: (response) => {
        const { token, user } = response;
        if (token && user?.role) {
          this.authService.handleAuthentication(token, user.username, user.role);
          this.redirectByRole(user.role);
        } else {
          this.error = 'Informations d\'authentification invalides';
          this.isLoading = false;
        }
      },
      error: (error) => {
        this.error = error.error?.message || 'Échec de la connexion. Veuillez vérifier vos identifiants.';
        this.isLoading = false;
      }
    });
  }

  private redirectByRole(role: string): void {
    const routes: { [key: string]: string } = {
      'ADMIN': '/admin',
      'TECHNICIEN': '/technicien',
      'CLIENT': '/client'
    };
    this.router.navigateByUrl(routes[role] || '/', { replaceUrl: true });
  }
}