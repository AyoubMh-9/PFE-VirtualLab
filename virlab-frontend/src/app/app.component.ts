// src/app/app.component.ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RouterModule } from '@angular/router';
import { routes } from './app.routes';

import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../app/core/services/auth.service';
import { DragDropModule } from '@angular/cdk/drag-drop';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';





@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, DragDropModule],
  template: `
    <main>
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    html, body, main {
      height: 100%;
      width: 100%;
      margin: 0;
      padding: 0;
    }

    main {
      display: flex;
      flex-direction: column;
    }
  `]
})
export class AppComponent {}