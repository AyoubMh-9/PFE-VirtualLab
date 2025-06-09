// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { RouterModule } from '@angular/router';
import { LoginComponent } from './shared/components/login/login.component';
//import { DashboardComponent } from './shared/components/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { AdminComponent } from './shared/components/admin/admin.component'
import { ClientComponent } from './shared/components/client/client.component'
import { TechnicienComponent } from './shared/components/technicien/technicien-dashboard.component'
import { ProfileComponent } from './modules/client/pages/profile/profile.component';
import { TestsComponent } from './modules/client/pages/tests/tests.component';
import { SupportComponent } from './modules/client/pages/support/support.component';
//import { ManageUsersComponent } from './admin/manage-users/manage-users.component';
//import { ReportsComponent } from './admin/reports/reports.component';
//import { SettingsComponent } from './admin/settings/settings.component';
import { UserListComponent } from '../app/modules/admin/pages/user-list/user-list.component'
//import { TasksComponent } from './technicien/tasks/tasks.component';
//import { TechnicienTestsComponent } from './technicien/tests/technicientests.component';
import { ProductsComponent } from '../app/modules/client/pages/products/products.component';
import { UserAddComponent } from '../app/modules/admin/pages/user-add/user-add.component';
import { ProjectListComponent } from '../app/modules/admin/pages/project-list/project-list.component'
import { ProjectAddComponent } from '../app/modules/admin/pages/project-add/project-add.component'
import { ChatComponent } from '../app/shared/components/chat/chat.component'
import { RepotsComponent } from './modules/admin/pages/repots/repots.component'; 
import { ProjectsComponent } from './modules/technicien/pages/projects/projects.component';


export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  //{ path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },

  {
  path: 'admin',
  component: AdminComponent,
  canActivate: [RoleGuard],
  data: { roles: ['ADMIN'] },
  children: [
    //{ path: 'users', component: ManageUsersComponent },
    { path: 'users', component: UserListComponent },
      { path: 'add', component: UserAddComponent },
      {path: 'projects', component: ProjectListComponent},
      {path: 'add-project', component: ProjectAddComponent},
      {path: 'reports', component: RepotsComponent},
    //  {path: 'settings', component: SettingsComponent},
      {
    path: 'admin/chat/:clientId/:productId',
    loadComponent: () =>
      import('../app/shared/components/chat/chat.component').then(m => m.ChatComponent)
  },


    //{ path: 'reports', component: ReportsComponent },
    //{ path: 'settings', component: SettingsComponent },
    { path: '', redirectTo: 'users', pathMatch: 'full' } // redirection par défaut
  ]
},
{
  path: 'client',
  component: ClientComponent,
  canActivate: [RoleGuard],
  data: { roles: ['CLIENT'] },
  children: [
    { path: 'tests', component: TestsComponent },
    { path: 'profile', component: ProfileComponent },
    { path: 'support', component: SupportComponent },
    { path: 'produits', component: ProductsComponent },
    { path: '', redirectTo: 'tests', pathMatch: 'full' }
  ]
}
,
  {
  path: 'technicien',
  component: TechnicienComponent,
  canActivate: [RoleGuard],
  data: { roles: ['TECHNICIEN'] },
  children: [
    { path: '', redirectTo: '../app/shared/components/technicien', pathMatch: 'full' },
   // { path: 'tasks', component: TasksComponent },
    //{ path: 'tests', component: TechnicienTestsComponent },
    { path: 'projects', component: ProjectsComponent }

    //{ path: 'profile', component: TechnicienComponent },
    //{ path: '', redirectTo: 'profile', pathMatch: 'full' }
  ]
}
,



  // ⚠️ TOUJOURS À LA FIN
  { path: '**', redirectTo: '/login' }
];
