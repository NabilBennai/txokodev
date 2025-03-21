import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { ProjectIdeaService } from '../service/project-idea.service';
import { IProjectIdea } from '../project-idea.model';
import { ProjectIdeaFormService } from './project-idea-form.service';

import { ProjectIdeaUpdateComponent } from './project-idea-update.component';

describe('ProjectIdea Management Update Component', () => {
  let comp: ProjectIdeaUpdateComponent;
  let fixture: ComponentFixture<ProjectIdeaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let projectIdeaFormService: ProjectIdeaFormService;
  let projectIdeaService: ProjectIdeaService;
  let appUserService: AppUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProjectIdeaUpdateComponent],
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
      .overrideTemplate(ProjectIdeaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjectIdeaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    projectIdeaFormService = TestBed.inject(ProjectIdeaFormService);
    projectIdeaService = TestBed.inject(ProjectIdeaService);
    appUserService = TestBed.inject(AppUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppUser query and add missing value', () => {
      const projectIdea: IProjectIdea = { id: 16207 };
      const appUser: IAppUser = { id: 14418 };
      projectIdea.appUser = appUser;

      const appUserCollection: IAppUser[] = [{ id: 14418 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const additionalAppUsers = [appUser];
      const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ projectIdea });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(
        appUserCollection,
        ...additionalAppUsers.map(expect.objectContaining),
      );
      expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const projectIdea: IProjectIdea = { id: 16207 };
      const appUser: IAppUser = { id: 14418 };
      projectIdea.appUser = appUser;

      activatedRoute.data = of({ projectIdea });
      comp.ngOnInit();

      expect(comp.appUsersSharedCollection).toContainEqual(appUser);
      expect(comp.projectIdea).toEqual(projectIdea);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjectIdea>>();
      const projectIdea = { id: 27910 };
      jest.spyOn(projectIdeaFormService, 'getProjectIdea').mockReturnValue(projectIdea);
      jest.spyOn(projectIdeaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectIdea });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projectIdea }));
      saveSubject.complete();

      // THEN
      expect(projectIdeaFormService.getProjectIdea).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(projectIdeaService.update).toHaveBeenCalledWith(expect.objectContaining(projectIdea));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjectIdea>>();
      const projectIdea = { id: 27910 };
      jest.spyOn(projectIdeaFormService, 'getProjectIdea').mockReturnValue({ id: null });
      jest.spyOn(projectIdeaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectIdea: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projectIdea }));
      saveSubject.complete();

      // THEN
      expect(projectIdeaFormService.getProjectIdea).toHaveBeenCalled();
      expect(projectIdeaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProjectIdea>>();
      const projectIdea = { id: 27910 };
      jest.spyOn(projectIdeaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectIdea });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(projectIdeaService.update).toHaveBeenCalled();
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
  });
});
