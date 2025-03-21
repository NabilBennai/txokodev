import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IProjectIdea } from 'app/entities/project-idea/project-idea.model';
import { ProjectIdeaService } from 'app/entities/project-idea/service/project-idea.service';
import { IProposal } from '../proposal.model';
import { ProposalService } from '../service/proposal.service';
import { ProposalFormService } from './proposal-form.service';

import { ProposalUpdateComponent } from './proposal-update.component';

describe('Proposal Management Update Component', () => {
  let comp: ProposalUpdateComponent;
  let fixture: ComponentFixture<ProposalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let proposalFormService: ProposalFormService;
  let proposalService: ProposalService;
  let appUserService: AppUserService;
  let projectIdeaService: ProjectIdeaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProposalUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProposalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProposalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    proposalFormService = TestBed.inject(ProposalFormService);
    proposalService = TestBed.inject(ProposalService);
    appUserService = TestBed.inject(AppUserService);
    projectIdeaService = TestBed.inject(ProjectIdeaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppUser query and add missing value', () => {
      const proposal: IProposal = { id: 6085 };
      const appUser: IAppUser = { id: 14418 };
      proposal.appUser = appUser;

      const appUserCollection: IAppUser[] = [{ id: 14418 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const additionalAppUsers = [appUser];
      const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ proposal });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(
        appUserCollection,
        ...additionalAppUsers.map(expect.objectContaining),
      );
      expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProjectIdea query and add missing value', () => {
      const proposal: IProposal = { id: 6085 };
      const projectIdea: IProjectIdea = { id: 27910 };
      proposal.projectIdea = projectIdea;

      const projectIdeaCollection: IProjectIdea[] = [{ id: 27910 }];
      jest.spyOn(projectIdeaService, 'query').mockReturnValue(of(new HttpResponse({ body: projectIdeaCollection })));
      const additionalProjectIdeas = [projectIdea];
      const expectedCollection: IProjectIdea[] = [...additionalProjectIdeas, ...projectIdeaCollection];
      jest.spyOn(projectIdeaService, 'addProjectIdeaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ proposal });
      comp.ngOnInit();

      expect(projectIdeaService.query).toHaveBeenCalled();
      expect(projectIdeaService.addProjectIdeaToCollectionIfMissing).toHaveBeenCalledWith(
        projectIdeaCollection,
        ...additionalProjectIdeas.map(expect.objectContaining),
      );
      expect(comp.projectIdeasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const proposal: IProposal = { id: 6085 };
      const appUser: IAppUser = { id: 14418 };
      proposal.appUser = appUser;
      const projectIdea: IProjectIdea = { id: 27910 };
      proposal.projectIdea = projectIdea;

      activatedRoute.data = of({ proposal });
      comp.ngOnInit();

      expect(comp.appUsersSharedCollection).toContainEqual(appUser);
      expect(comp.projectIdeasSharedCollection).toContainEqual(projectIdea);
      expect(comp.proposal).toEqual(proposal);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProposal>>();
      const proposal = { id: 25564 };
      jest.spyOn(proposalFormService, 'getProposal').mockReturnValue(proposal);
      jest.spyOn(proposalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proposal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proposal }));
      saveSubject.complete();

      // THEN
      expect(proposalFormService.getProposal).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(proposalService.update).toHaveBeenCalledWith(expect.objectContaining(proposal));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProposal>>();
      const proposal = { id: 25564 };
      jest.spyOn(proposalFormService, 'getProposal').mockReturnValue({ id: null });
      jest.spyOn(proposalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proposal: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proposal }));
      saveSubject.complete();

      // THEN
      expect(proposalFormService.getProposal).toHaveBeenCalled();
      expect(proposalService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProposal>>();
      const proposal = { id: 25564 };
      jest.spyOn(proposalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proposal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(proposalService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppUser', () => {
      it('Should forward to appUserService', () => {
        const entity = { id: 14418 };
        const entity2 = { id: 16679 };
        jest.spyOn(appUserService, 'compareAppUser');
        comp.compareAppUser(entity, entity2);
        expect(appUserService.compareAppUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProjectIdea', () => {
      it('Should forward to projectIdeaService', () => {
        const entity = { id: 27910 };
        const entity2 = { id: 16207 };
        jest.spyOn(projectIdeaService, 'compareProjectIdea');
        comp.compareProjectIdea(entity, entity2);
        expect(projectIdeaService.compareProjectIdea).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
