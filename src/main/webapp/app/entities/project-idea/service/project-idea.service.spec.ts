import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProjectIdea } from '../project-idea.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../project-idea.test-samples';

import { ProjectIdeaService, RestProjectIdea } from './project-idea.service';

const requireRestSample: RestProjectIdea = {
  ...sampleWithRequiredData,
  deadline: sampleWithRequiredData.deadline?.format(DATE_FORMAT),
};

describe('ProjectIdea Service', () => {
  let service: ProjectIdeaService;
  let httpMock: HttpTestingController;
  let expectedResult: IProjectIdea | IProjectIdea[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProjectIdeaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ProjectIdea', () => {
      const projectIdea = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(projectIdea).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProjectIdea', () => {
      const projectIdea = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(projectIdea).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProjectIdea', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProjectIdea', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProjectIdea', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProjectIdeaToCollectionIfMissing', () => {
      it('should add a ProjectIdea to an empty array', () => {
        const projectIdea: IProjectIdea = sampleWithRequiredData;
        expectedResult = service.addProjectIdeaToCollectionIfMissing([], projectIdea);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(projectIdea);
      });

      it('should not add a ProjectIdea to an array that contains it', () => {
        const projectIdea: IProjectIdea = sampleWithRequiredData;
        const projectIdeaCollection: IProjectIdea[] = [
          {
            ...projectIdea,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProjectIdeaToCollectionIfMissing(projectIdeaCollection, projectIdea);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProjectIdea to an array that doesn't contain it", () => {
        const projectIdea: IProjectIdea = sampleWithRequiredData;
        const projectIdeaCollection: IProjectIdea[] = [sampleWithPartialData];
        expectedResult = service.addProjectIdeaToCollectionIfMissing(projectIdeaCollection, projectIdea);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(projectIdea);
      });

      it('should add only unique ProjectIdea to an array', () => {
        const projectIdeaArray: IProjectIdea[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const projectIdeaCollection: IProjectIdea[] = [sampleWithRequiredData];
        expectedResult = service.addProjectIdeaToCollectionIfMissing(projectIdeaCollection, ...projectIdeaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const projectIdea: IProjectIdea = sampleWithRequiredData;
        const projectIdea2: IProjectIdea = sampleWithPartialData;
        expectedResult = service.addProjectIdeaToCollectionIfMissing([], projectIdea, projectIdea2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(projectIdea);
        expect(expectedResult).toContain(projectIdea2);
      });

      it('should accept null and undefined values', () => {
        const projectIdea: IProjectIdea = sampleWithRequiredData;
        expectedResult = service.addProjectIdeaToCollectionIfMissing([], null, projectIdea, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(projectIdea);
      });

      it('should return initial array if no ProjectIdea is added', () => {
        const projectIdeaCollection: IProjectIdea[] = [sampleWithRequiredData];
        expectedResult = service.addProjectIdeaToCollectionIfMissing(projectIdeaCollection, undefined, null);
        expect(expectedResult).toEqual(projectIdeaCollection);
      });
    });

    describe('compareProjectIdea', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProjectIdea(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 27910 };
        const entity2 = null;

        const compareResult1 = service.compareProjectIdea(entity1, entity2);
        const compareResult2 = service.compareProjectIdea(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 27910 };
        const entity2 = { id: 16207 };

        const compareResult1 = service.compareProjectIdea(entity1, entity2);
        const compareResult2 = service.compareProjectIdea(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 27910 };
        const entity2 = { id: 27910 };

        const compareResult1 = service.compareProjectIdea(entity1, entity2);
        const compareResult2 = service.compareProjectIdea(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
