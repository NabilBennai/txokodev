import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IProjectIdea } from 'app/entities/project-idea/project-idea.model';
import { ProposalStatus } from 'app/entities/enumerations/proposal-status.model';

export interface IProposal {
  id: number;
  message?: string | null;
  price?: number | null;
  status?: keyof typeof ProposalStatus | null;
  appUser?: Pick<IAppUser, 'id' | 'fullName'> | null;
  projectIdea?: Pick<IProjectIdea, 'id' | 'title'> | null;
}

export type NewProposal = Omit<IProposal, 'id'> & { id: null };
