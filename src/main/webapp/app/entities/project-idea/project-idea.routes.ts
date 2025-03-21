import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProjectIdeaResolve from './route/project-idea-routing-resolve.service';

const projectIdeaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/project-idea.component').then(m => m.ProjectIdeaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/project-idea-detail.component').then(m => m.ProjectIdeaDetailComponent),
    resolve: {
      projectIdea: ProjectIdeaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/project-idea-update.component').then(m => m.ProjectIdeaUpdateComponent),
    resolve: {
      projectIdea: ProjectIdeaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/project-idea-update.component').then(m => m.ProjectIdeaUpdateComponent),
    resolve: {
      projectIdea: ProjectIdeaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default projectIdeaRoute;
