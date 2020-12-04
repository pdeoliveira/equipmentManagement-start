import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'store',
        loadChildren: () => import('./store/store.module').then(m => m.EquipmentManagementStoreModule),
      },
      {
        path: 'store-equipment',
        loadChildren: () => import('./store-equipment/store-equipment.module').then(m => m.EquipmentManagementStoreEquipmentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EquipmentManagementEntityModule {}
