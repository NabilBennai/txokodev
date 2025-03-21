import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProjectIdea } from '../project-idea.model';
import { ProjectIdeaService } from '../service/project-idea.service';

const projectIdeaResolve = (route: ActivatedRouteSnapshot): Observable<null | IProjectIdea> => {
  const id = route.params.id;
  if (id) {
    return inject(ProjectIdeaService)
      .find(id)
      .pipe(
        mergeMap((projectIdea: HttpResponse<IProjectIdea>) => {
          if (projectIdea.body) {
            return of(projectIdea.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default projectIdeaResolve;
