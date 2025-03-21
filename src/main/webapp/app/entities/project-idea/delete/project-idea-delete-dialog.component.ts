import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProjectIdea } from '../project-idea.model';
import { ProjectIdeaService } from '../service/project-idea.service';

@Component({
  templateUrl: './project-idea-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProjectIdeaDeleteDialogComponent {
  projectIdea?: IProjectIdea;

  protected projectIdeaService = inject(ProjectIdeaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.projectIdeaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
