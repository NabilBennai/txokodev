import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProjectIdea, NewProjectIdea } from '../project-idea.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProjectIdea for edit and NewProjectIdeaFormGroupInput for create.
 */
type ProjectIdeaFormGroupInput = IProjectIdea | PartialWithRequiredKeyOf<NewProjectIdea>;

type ProjectIdeaFormDefaults = Pick<NewProjectIdea, 'id'>;

type ProjectIdeaFormGroupContent = {
  id: FormControl<IProjectIdea['id'] | NewProjectIdea['id']>;
  title: FormControl<IProjectIdea['title']>;
  description: FormControl<IProjectIdea['description']>;
  budget: FormControl<IProjectIdea['budget']>;
  deadline: FormControl<IProjectIdea['deadline']>;
  location: FormControl<IProjectIdea['location']>;
  appUser: FormControl<IProjectIdea['appUser']>;
};

export type ProjectIdeaFormGroup = FormGroup<ProjectIdeaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProjectIdeaFormService {
  createProjectIdeaFormGroup(projectIdea: ProjectIdeaFormGroupInput = { id: null }): ProjectIdeaFormGroup {
    const projectIdeaRawValue = {
      ...this.getFormDefaults(),
      ...projectIdea,
    };
    return new FormGroup<ProjectIdeaFormGroupContent>({
      id: new FormControl(
        { value: projectIdeaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(projectIdeaRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(projectIdeaRawValue.description, {
        validators: [Validators.required],
      }),
      budget: new FormControl(projectIdeaRawValue.budget, {
        validators: [Validators.required],
      }),
      deadline: new FormControl(projectIdeaRawValue.deadline),
      location: new FormControl(projectIdeaRawValue.location, {
        validators: [Validators.required],
      }),
      appUser: new FormControl(projectIdeaRawValue.appUser, {
        validators: [Validators.required],
      }),
    });
  }

  getProjectIdea(form: ProjectIdeaFormGroup): IProjectIdea | NewProjectIdea {
    return form.getRawValue() as IProjectIdea | NewProjectIdea;
  }

  resetForm(form: ProjectIdeaFormGroup, projectIdea: ProjectIdeaFormGroupInput): void {
    const projectIdeaRawValue = { ...this.getFormDefaults(), ...projectIdea };
    form.reset(
      {
        ...projectIdeaRawValue,
        id: { value: projectIdeaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProjectIdeaFormDefaults {
    return {
      id: null,
    };
  }
}
