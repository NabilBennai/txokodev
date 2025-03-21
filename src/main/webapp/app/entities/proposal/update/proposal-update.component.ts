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
import { IProjectIdea } from 'app/entities/project-idea/project-idea.model';
import { ProjectIdeaService } from 'app/entities/project-idea/service/project-idea.service';
import { ProposalStatus } from 'app/entities/enumerations/proposal-status.model';
import { ProposalService } from '../service/proposal.service';
import { IProposal } from '../proposal.model';
import { ProposalFormGroup, ProposalFormService } from './proposal-form.service';

@Component({
  selector: 'jhi-proposal-update',
  templateUrl: './proposal-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProposalUpdateComponent implements OnInit {
  isSaving = false;
  proposal: IProposal | null = null;
  proposalStatusValues = Object.keys(ProposalStatus);

  appUsersSharedCollection: IAppUser[] = [];
  projectIdeasSharedCollection: IProjectIdea[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected proposalService = inject(ProposalService);
  protected proposalFormService = inject(ProposalFormService);
  protected appUserService = inject(AppUserService);
  protected projectIdeaService = inject(ProjectIdeaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProposalFormGroup = this.proposalFormService.createProposalFormGroup();

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  compareProjectIdea = (o1: IProjectIdea | null, o2: IProjectIdea | null): boolean => this.projectIdeaService.compareProjectIdea(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposal }) => {
      this.proposal = proposal;
      if (proposal) {
        this.updateForm(proposal);
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
    const proposal = this.proposalFormService.getProposal(this.editForm);
    if (proposal.id !== null) {
      this.subscribeToSaveResponse(this.proposalService.update(proposal));
    } else {
      this.subscribeToSaveResponse(this.proposalService.create(proposal));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProposal>>): void {
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

  protected updateForm(proposal: IProposal): void {
    this.proposal = proposal;
    this.proposalFormService.resetForm(this.editForm, proposal);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      proposal.appUser,
    );
    this.projectIdeasSharedCollection = this.projectIdeaService.addProjectIdeaToCollectionIfMissing<IProjectIdea>(
      this.projectIdeasSharedCollection,
      proposal.projectIdea,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.proposal?.appUser)))
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));

    this.projectIdeaService
      .query()
      .pipe(map((res: HttpResponse<IProjectIdea[]>) => res.body ?? []))
      .pipe(
        map((projectIdeas: IProjectIdea[]) =>
          this.projectIdeaService.addProjectIdeaToCollectionIfMissing<IProjectIdea>(projectIdeas, this.proposal?.projectIdea),
        ),
      )
      .subscribe((projectIdeas: IProjectIdea[]) => (this.projectIdeasSharedCollection = projectIdeas));
  }
}
