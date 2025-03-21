import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProposal } from '../proposal.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../proposal.test-samples';

import { ProposalService } from './proposal.service';

const requireRestSample: IProposal = {
  ...sampleWithRequiredData,
};

describe('Proposal Service', () => {
  let service: ProposalService;
  let httpMock: HttpTestingController;
  let expectedResult: IProposal | IProposal[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProposalService);
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

    it('should create a Proposal', () => {
      const proposal = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(proposal).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Proposal', () => {
      const proposal = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(proposal).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Proposal', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Proposal', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Proposal', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProposalToCollectionIfMissing', () => {
      it('should add a Proposal to an empty array', () => {
        const proposal: IProposal = sampleWithRequiredData;
        expectedResult = service.addProposalToCollectionIfMissing([], proposal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proposal);
      });

      it('should not add a Proposal to an array that contains it', () => {
        const proposal: IProposal = sampleWithRequiredData;
        const proposalCollection: IProposal[] = [
          {
            ...proposal,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, proposal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Proposal to an array that doesn't contain it", () => {
        const proposal: IProposal = sampleWithRequiredData;
        const proposalCollection: IProposal[] = [sampleWithPartialData];
        expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, proposal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proposal);
      });

      it('should add only unique Proposal to an array', () => {
        const proposalArray: IProposal[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const proposalCollection: IProposal[] = [sampleWithRequiredData];
        expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, ...proposalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const proposal: IProposal = sampleWithRequiredData;
        const proposal2: IProposal = sampleWithPartialData;
        expectedResult = service.addProposalToCollectionIfMissing([], proposal, proposal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proposal);
        expect(expectedResult).toContain(proposal2);
      });

      it('should accept null and undefined values', () => {
        const proposal: IProposal = sampleWithRequiredData;
        expectedResult = service.addProposalToCollectionIfMissing([], null, proposal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proposal);
      });

      it('should return initial array if no Proposal is added', () => {
        const proposalCollection: IProposal[] = [sampleWithRequiredData];
        expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, undefined, null);
        expect(expectedResult).toEqual(proposalCollection);
      });
    });

    describe('compareProposal', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProposal(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 25564 };
        const entity2 = null;

        const compareResult1 = service.compareProposal(entity1, entity2);
        const compareResult2 = service.compareProposal(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 25564 };
        const entity2 = { id: 6085 };

        const compareResult1 = service.compareProposal(entity1, entity2);
        const compareResult2 = service.compareProposal(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 25564 };
        const entity2 = { id: 25564 };

        const compareResult1 = service.compareProposal(entity1, entity2);
        const compareResult2 = service.compareProposal(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
