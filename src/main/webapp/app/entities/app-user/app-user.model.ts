import { IUser } from 'app/entities/user/user.model';

export interface IAppUser {
  id: number;
  fullName?: string | null;
  bio?: string | null;
  city?: string | null;
  isDeveloper?: boolean | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewAppUser = Omit<IAppUser, 'id'> & { id: null };
