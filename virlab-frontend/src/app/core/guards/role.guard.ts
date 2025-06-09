import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    // Vérifier d'abord si l'utilisateur est authentifié
    if (!this.authService.isAuthenticated()) {
      // Rediriger vers la page de connexion
      this.router.navigate(['/login'], { 
        queryParams: { returnUrl: state.url },
        replaceUrl: true 
      });
      return false;
    }

    // Vérifier le rôle requis
    const expectedRoles = route.data['roles'] as string[];
    if (!expectedRoles || expectedRoles.length === 0) {
      // Pas de rôle requis, l'utilisateur est authentifié
      return true;
    }

    // Vérifier si l'utilisateur a le rôle requis
    const userRole = this.authService.getUserRole();
    if (expectedRoles.includes(userRole)) {
      return true;
    }

    // Rediriger vers la page appropriée selon le rôle actuel
    this.redirectBasedOnRole();
    return false;
  }

  // Rediriger l'utilisateur vers sa page de base selon son rôle
  private redirectBasedOnRole(): void {
    const role = this.authService.getUserRole();
    
    switch (role) {
      case 'ADMIN':
        this.router.navigateByUrl('/admin', { replaceUrl: true });
        break;
      case 'TECHNICIEN':
        this.router.navigateByUrl('/technicien', { replaceUrl: true });
        break;
      case 'CLIENT':
        this.router.navigateByUrl('/client', { replaceUrl: true });
        break;
      default:
        this.router.navigateByUrl('/login', { replaceUrl: true });
    }
  }
}