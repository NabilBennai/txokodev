import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProposalResolve from './route/proposal-routing-resolve.service';

const proposalRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/proposal.component').then(m => m.ProposalComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/proposal-detail.component').then(m => m.ProposalDetailComponent),
    resolve: {
      proposal: ProposalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/proposal-update.component').then(m => m.ProposalUpdateComponent),
    resolve: {
      proposal: ProposalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/proposal-update.component').then(m => m.ProposalUpdateComponent),
    resolve: {
      proposal: ProposalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default proposalRoute;
