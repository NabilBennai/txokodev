import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAppUser } from '../app-user.model';

@Component({
  selector: 'jhi-app-user-detail',
  templateUrl: './app-user-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AppUserDetailComponent {
  appUser = input<IAppUser | null>(null);

  previousState(): void {
    window.history.back();
  }
}
