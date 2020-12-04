import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EquipmentManagementSharedModule } from 'app/shared/shared.module';
import { StoreEquipmentComponent } from './store-equipment.component';
import { StoreEquipmentDetailComponent } from './store-equipment-detail.component';
import { StoreEquipmentUpdateComponent } from './store-equipment-update.component';
import { StoreEquipmentDeleteDialogComponent } from './store-equipment-delete-dialog.component';
import { storeEquipmentRoute } from './store-equipment.route';

@NgModule({
  imports: [EquipmentManagementSharedModule, RouterModule.forChild(storeEquipmentRoute)],
  declarations: [
    StoreEquipmentComponent,
    StoreEquipmentDetailComponent,
    StoreEquipmentUpdateComponent,
    StoreEquipmentDeleteDialogComponent,
  ],
  entryComponents: [StoreEquipmentDeleteDialogComponent],
})
export class EquipmentManagementStoreEquipmentModule {}
