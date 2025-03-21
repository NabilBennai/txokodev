import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 19407,
  fullName: 'conseil d’administration ha adversaire',
  city: 'Tours',
  isDeveloper: false,
};

export const sampleWithPartialData: IAppUser = {
  id: 30630,
  fullName: 'svelte assurément',
  bio: "à l'instar de",
  city: 'Mérignac',
  isDeveloper: false,
};

export const sampleWithFullData: IAppUser = {
  id: 1902,
  fullName: 'suffisamment',
  bio: 'main-d’œuvre fêter',
  city: 'Rennes',
  isDeveloper: true,
};

export const sampleWithNewData: NewAppUser = {
  fullName: 'distraire juriste jusqu’à ce que',
  city: 'Limoges',
  isDeveloper: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
