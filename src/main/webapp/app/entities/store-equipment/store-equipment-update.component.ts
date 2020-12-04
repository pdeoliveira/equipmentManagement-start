import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStoreEquipment, StoreEquipment } from 'app/shared/model/store-equipment.model';
import { StoreEquipmentService } from './store-equipment.service';
import { IStore } from 'app/shared/model/store.model';
import { StoreService } from 'app/entities/store/store.service';

@Component({
  selector: 'jhi-store-equipment-update',
  templateUrl: './store-equipment-update.component.html',
})
export class StoreEquipmentUpdateComponent implements OnInit {
  isSaving = false;
  stores: IStore[] = [];

  editForm = this.fb.group({
    id: [],
    sku: [null, [Validators.required, Validators.min(0), Validators.max(9999999)]],
    equipmentName: [null, [Validators.required]],
    locationInStore: [],
    store: [null, Validators.required],
  });

  constructor(
    protected storeEquipmentService: StoreEquipmentService,
    protected storeService: StoreService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ storeEquipment }) => {
      this.updateForm(storeEquipment);

      this.storeService.query().subscribe((res: HttpResponse<IStore[]>) => (this.stores = res.body || []));
    });
  }

  updateForm(storeEquipment: IStoreEquipment): void {
    this.editForm.patchValue({
      id: storeEquipment.id,
      sku: storeEquipment.sku,
      equipmentName: storeEquipment.equipmentName,
      locationInStore: storeEquipment.locationInStore,
      store: storeEquipment.store,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const storeEquipment = this.createFromForm();
    if (storeEquipment.id !== undefined) {
      this.subscribeToSaveResponse(this.storeEquipmentService.update(storeEquipment));
    } else {
      this.subscribeToSaveResponse(this.storeEquipmentService.create(storeEquipment));
    }
  }

  private createFromForm(): IStoreEquipment {
    return {
      ...new StoreEquipment(),
      id: this.editForm.get(['id'])!.value,
      sku: this.editForm.get(['sku'])!.value,
      equipmentName: this.editForm.get(['equipmentName'])!.value,
      locationInStore: this.editForm.get(['locationInStore'])!.value,
      store: this.editForm.get(['store'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStoreEquipment>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IStore): any {
    return item.id;
  }
}
