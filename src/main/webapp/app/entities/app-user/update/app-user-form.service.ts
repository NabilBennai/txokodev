import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAppUser, NewAppUser } from '../app-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppUser for edit and NewAppUserFormGroupInput for create.
 */
type AppUserFormGroupInput = IAppUser | PartialWithRequiredKeyOf<NewAppUser>;

type AppUserFormDefaults = Pick<NewAppUser, 'id' | 'isDeveloper'>;

type AppUserFormGroupContent = {
  id: FormControl<IAppUser['id'] | NewAppUser['id']>;
  fullName: FormControl<IAppUser['fullName']>;
  bio: FormControl<IAppUser['bio']>;
  city: FormControl<IAppUser['city']>;
  isDeveloper: FormControl<IAppUser['isDeveloper']>;
  user: FormControl<IAppUser['user']>;
};

export type AppUserFormGroup = FormGroup<AppUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppUserFormService {
  createAppUserFormGroup(appUser: AppUserFormGroupInput = { id: null }): AppUserFormGroup {
    const appUserRawValue = {
      ...this.getFormDefaults(),
      ...appUser,
    };
    return new FormGroup<AppUserFormGroupContent>({
      id: new FormControl(
        { value: appUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fullName: new FormControl(appUserRawValue.fullName, {
        validators: [Validators.required],
      }),
      bio: new FormControl(appUserRawValue.bio),
      city: new FormControl(appUserRawValue.city, {
        validators: [Validators.required],
      }),
      isDeveloper: new FormControl(appUserRawValue.isDeveloper, {
        validators: [Validators.required],
      }),
      user: new FormControl(appUserRawValue.user),
    });
  }

  getAppUser(form: AppUserFormGroup): IAppUser | NewAppUser {
    return form.getRawValue() as IAppUser | NewAppUser;
  }

  resetForm(form: AppUserFormGroup, appUser: AppUserFormGroupInput): void {
    const appUserRawValue = { ...this.getFormDefaults(), ...appUser };
    form.reset(
      {
        ...appUserRawValue,
        id: { value: appUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppUserFormDefaults {
    return {
      id: null,
      isDeveloper: false,
    };
  }
}
