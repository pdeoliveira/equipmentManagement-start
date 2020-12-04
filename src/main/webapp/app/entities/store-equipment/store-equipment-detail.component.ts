import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStoreEquipment } from 'app/shared/model/store-equipment.model';

@Component({
  selector: 'jhi-store-equipment-detail',
  templateUrl: './store-equipment-detail.component.html',
})
export class StoreEquipmentDetailComponent implements OnInit {
  storeEquipment: IStoreEquipment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ storeEquipment }) => (this.storeEquipment = storeEquipment));
  }

  previousState(): void {
    window.history.back();
  }
}
