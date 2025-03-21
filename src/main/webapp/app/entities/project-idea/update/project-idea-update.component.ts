import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { ProjectIdeaService } from '../service/project-idea.service';
import { IProjectIdea } from '../project-idea.model';
import { ProjectIdeaFormGroup, ProjectIdeaFormService } from './project-idea-form.service';

@Component({
  selector: 'jhi-project-idea-update',
  templateUrl: './project-idea-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProjectIdeaUpdateComponent implements OnInit {
  isSaving = false;
  projectIdea: IProjectIdea | null = null;

  appUsersSharedCollection: IAppUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected projectIdeaService = inject(ProjectIdeaService);
  protected projectIdeaFormService = inject(ProjectIdeaFormService);
  protected appUserService = inject(AppUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProjectIdeaFormGroup = this.projectIdeaFormService.createProjectIdeaFormGroup();

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projectIdea }) => {
      this.projectIdea = projectIdea;
      if (projectIdea) {
        this.updateForm(projectIdea);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('txokoDevApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projectIdea = this.projectIdeaFormService.getProjectIdea(this.editForm);
    if (projectIdea.id !== null) {
      this.subscribeToSaveResponse(this.projectIdeaService.update(projectIdea));
    } else {
      this.subscribeToSaveResponse(this.projectIdeaService.create(projectIdea));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjectIdea>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(projectIdea: IProjectIdea): void {
    this.projectIdea = projectIdea;
    this.projectIdeaFormService.resetForm(this.editForm, projectIdea);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      projectIdea.appUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(
        map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.projectIdea?.appUser)),
      )
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));
  }
}
