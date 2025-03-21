import dayjs from 'dayjs/esm';

import { IProjectIdea, NewProjectIdea } from './project-idea.model';

export const sampleWithRequiredData: IProjectIdea = {
  id: 14439,
  title: 'blême',
  description: '../fake-data/blob/hipster.txt',
  budget: 3521,
  location: 'de sorte que',
};

export const sampleWithPartialData: IProjectIdea = {
  id: 5018,
  title: 'hé plus vraisemblablement',
  description: '../fake-data/blob/hipster.txt',
  budget: 16970,
  deadline: dayjs('2025-03-20'),
  location: 'en guise de agir autour de',
};

export const sampleWithFullData: IProjectIdea = {
  id: 3123,
  title: 'communauté étudiante retirer',
  description: '../fake-data/blob/hipster.txt',
  budget: 14330,
  deadline: dayjs('2025-03-20'),
  location: 'taire toc police',
};

export const sampleWithNewData: NewProjectIdea = {
  title: 'coupable',
  description: '../fake-data/blob/hipster.txt',
  budget: 5568,
  location: 'afin que insipide',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
