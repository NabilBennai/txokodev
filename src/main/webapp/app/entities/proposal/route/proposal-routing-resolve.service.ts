import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProposal } from '../proposal.model';
import { ProposalService } from '../service/proposal.service';

const proposalResolve = (route: ActivatedRouteSnapshot): Observable<null | IProposal> => {
  const id = route.params.id;
  if (id) {
    return inject(ProposalService)
      .find(id)
      .pipe(
        mergeMap((proposal: HttpResponse<IProposal>) => {
          if (proposal.body) {
            return of(proposal.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default proposalResolve;
