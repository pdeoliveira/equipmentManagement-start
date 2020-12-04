import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStoreEquipment } from 'app/shared/model/store-equipment.model';
import { StoreEquipmentService } from './store-equipment.service';

@Component({
  templateUrl: './store-equipment-delete-dialog.component.html',
})
export class StoreEquipmentDeleteDialogComponent {
  storeEquipment?: IStoreEquipment;

  constructor(
    protected storeEquipmentService: StoreEquipmentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.storeEquipmentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('storeEquipmentListModification');
      this.activeModal.close();
    });
  }
}
