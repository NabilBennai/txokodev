import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProjectIdea, NewProjectIdea } from '../project-idea.model';

export type PartialUpdateProjectIdea = Partial<IProjectIdea> & Pick<IProjectIdea, 'id'>;

type RestOf<T extends IProjectIdea | NewProjectIdea> = Omit<T, 'deadline'> & {
  deadline?: string | null;
};

export type RestProjectIdea = RestOf<IProjectIdea>;

export type NewRestProjectIdea = RestOf<NewProjectIdea>;

export type PartialUpdateRestProjectIdea = RestOf<PartialUpdateProjectIdea>;

export type EntityResponseType = HttpResponse<IProjectIdea>;
export type EntityArrayResponseType = HttpResponse<IProjectIdea[]>;

@Injectable({ providedIn: 'root' })
export class ProjectIdeaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/project-ideas');

  create(projectIdea: NewProjectIdea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projectIdea);
    return this.http
      .post<RestProjectIdea>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(projectIdea: IProjectIdea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projectIdea);
    return this.http
      .put<RestProjectIdea>(`${this.resourceUrl}/${this.getProjectIdeaIdentifier(projectIdea)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(projectIdea: PartialUpdateProjectIdea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(projectIdea);
    return this.http
      .patch<RestProjectIdea>(`${this.resourceUrl}/${this.getProjectIdeaIdentifier(projectIdea)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProjectIdea>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProjectIdea[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProjectIdeaIdentifier(projectIdea: Pick<IProjectIdea, 'id'>): number {
    return projectIdea.id;
  }

  compareProjectIdea(o1: Pick<IProjectIdea, 'id'> | null, o2: Pick<IProjectIdea, 'id'> | null): boolean {
    return o1 && o2 ? this.getProjectIdeaIdentifier(o1) === this.getProjectIdeaIdentifier(o2) : o1 === o2;
  }

  addProjectIdeaToCollectionIfMissing<Type extends Pick<IProjectIdea, 'id'>>(
    projectIdeaCollection: Type[],
    ...projectIdeasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const projectIdeas: Type[] = projectIdeasToCheck.filter(isPresent);
    if (projectIdeas.length > 0) {
      const projectIdeaCollectionIdentifiers = projectIdeaCollection.map(projectIdeaItem => this.getProjectIdeaIdentifier(projectIdeaItem));
      const projectIdeasToAdd = projectIdeas.filter(projectIdeaItem => {
        const projectIdeaIdentifier = this.getProjectIdeaIdentifier(projectIdeaItem);
        if (projectIdeaCollectionIdentifiers.includes(projectIdeaIdentifier)) {
          return false;
        }
        projectIdeaCollectionIdentifiers.push(projectIdeaIdentifier);
        return true;
      });
      return [...projectIdeasToAdd, ...projectIdeaCollection];
    }
    return projectIdeaCollection;
  }

  protected convertDateFromClient<T extends IProjectIdea | NewProjectIdea | PartialUpdateProjectIdea>(projectIdea: T): RestOf<T> {
    return {
      ...projectIdea,
      deadline: projectIdea.deadline?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProjectIdea: RestProjectIdea): IProjectIdea {
    return {
      ...restProjectIdea,
      deadline: restProjectIdea.deadline ? dayjs(restProjectIdea.deadline) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProjectIdea>): HttpResponse<IProjectIdea> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProjectIdea[]>): HttpResponse<IProjectIdea[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
