import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProposal, NewProposal } from '../proposal.model';

export type PartialUpdateProposal = Partial<IProposal> & Pick<IProposal, 'id'>;

export type EntityResponseType = HttpResponse<IProposal>;
export type EntityArrayResponseType = HttpResponse<IProposal[]>;

@Injectable({ providedIn: 'root' })
export class ProposalService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/proposals');

  create(proposal: NewProposal): Observable<EntityResponseType> {
    return this.http.post<IProposal>(this.resourceUrl, proposal, { observe: 'response' });
  }

  update(proposal: IProposal): Observable<EntityResponseType> {
    return this.http.put<IProposal>(`${this.resourceUrl}/${this.getProposalIdentifier(proposal)}`, proposal, { observe: 'response' });
  }

  partialUpdate(proposal: PartialUpdateProposal): Observable<EntityResponseType> {
    return this.http.patch<IProposal>(`${this.resourceUrl}/${this.getProposalIdentifier(proposal)}`, proposal, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProposal>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProposal[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProposalIdentifier(proposal: Pick<IProposal, 'id'>): number {
    return proposal.id;
  }

  compareProposal(o1: Pick<IProposal, 'id'> | null, o2: Pick<IProposal, 'id'> | null): boolean {
    return o1 && o2 ? this.getProposalIdentifier(o1) === this.getProposalIdentifier(o2) : o1 === o2;
  }

  addProposalToCollectionIfMissing<Type extends Pick<IProposal, 'id'>>(
    proposalCollection: Type[],
    ...proposalsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const proposals: Type[] = proposalsToCheck.filter(isPresent);
    if (proposals.length > 0) {
      const proposalCollectionIdentifiers = proposalCollection.map(proposalItem => this.getProposalIdentifier(proposalItem));
      const proposalsToAdd = proposals.filter(proposalItem => {
        const proposalIdentifier = this.getProposalIdentifier(proposalItem);
        if (proposalCollectionIdentifiers.includes(proposalIdentifier)) {
          return false;
        }
        proposalCollectionIdentifiers.push(proposalIdentifier);
        return true;
      });
      return [...proposalsToAdd, ...proposalCollection];
    }
    return proposalCollection;
  }
}
