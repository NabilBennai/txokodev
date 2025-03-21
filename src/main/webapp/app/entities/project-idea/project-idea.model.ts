import dayjs from 'dayjs/esm';
import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface IProjectIdea {
  id: number;
  title?: string | null;
  description?: string | null;
  budget?: number | null;
  deadline?: dayjs.Dayjs | null;
  location?: string | null;
  appUser?: Pick<IAppUser, 'id' | 'fullName'> | null;
}

export type NewProjectIdea = Omit<IProjectIdea, 'id'> & { id: null };
