import { IProposal, NewProposal } from './proposal.model';

export const sampleWithRequiredData: IProposal = {
  id: 1604,
  message: '../fake-data/blob/hipster.txt',
  price: 4749,
};

export const sampleWithPartialData: IProposal = {
  id: 32413,
  message: '../fake-data/blob/hipster.txt',
  price: 25231,
  status: 'REFUSE',
};

export const sampleWithFullData: IProposal = {
  id: 8698,
  message: '../fake-data/blob/hipster.txt',
  price: 3408,
  status: 'REFUSE',
};

export const sampleWithNewData: NewProposal = {
  message: '../fake-data/blob/hipster.txt',
  price: 12134,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
