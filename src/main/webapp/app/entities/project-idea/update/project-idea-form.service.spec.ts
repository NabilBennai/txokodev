import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../project-idea.test-samples';

import { ProjectIdeaFormService } from './project-idea-form.service';

describe('ProjectIdea Form Service', () => {
  let service: ProjectIdeaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectIdeaFormService);
  });

  describe('Service methods', () => {
    describe('createProjectIdeaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProjectIdeaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            budget: expect.any(Object),
            deadline: expect.any(Object),
            location: expect.any(Object),
            appUser: expect.any(Object),
          }),
        );
      });

      it('passing IProjectIdea should create a new form with FormGroup', () => {
        const formGroup = service.createProjectIdeaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            budget: expect.any(Object),
            deadline: expect.any(Object),
            location: expect.any(Object),
            appUser: expect.any(Object),
          }),
        );
      });
    });

    describe('getProjectIdea', () => {
      it('should return NewProjectIdea for default ProjectIdea initial value', () => {
        const formGroup = service.createProjectIdeaFormGroup(sampleWithNewData);

        const projectIdea = service.getProjectIdea(formGroup) as any;

        expect(projectIdea).toMatchObject(sampleWithNewData);
      });

      it('should return NewProjectIdea for empty ProjectIdea initial value', () => {
        const formGroup = service.createProjectIdeaFormGroup();

        const projectIdea = service.getProjectIdea(formGroup) as any;

        expect(projectIdea).toMatchObject({});
      });

      it('should return IProjectIdea', () => {
        const formGroup = service.createProjectIdeaFormGroup(sampleWithRequiredData);

        const projectIdea = service.getProjectIdea(formGroup) as any;

        expect(projectIdea).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProjectIdea should not enable id FormControl', () => {
        const formGroup = service.createProjectIdeaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProjectIdea should disable id FormControl', () => {
        const formGroup = service.createProjectIdeaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
