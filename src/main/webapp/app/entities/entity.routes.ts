import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'txokoDevApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'app-user',
    data: { pageTitle: 'txokoDevApp.appUser.home.title' },
    loadChildren: () => import('./app-user/app-user.routes'),
  },
  {
    path: 'project-idea',
    data: { pageTitle: 'txokoDevApp.projectIdea.home.title' },
    loadChildren: () => import('./project-idea/project-idea.routes'),
  },
  {
    path: 'proposal',
    data: { pageTitle: 'txokoDevApp.proposal.home.title' },
    loadChildren: () => import('./proposal/proposal.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
