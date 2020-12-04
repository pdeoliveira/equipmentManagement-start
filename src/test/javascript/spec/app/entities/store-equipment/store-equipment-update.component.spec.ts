import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EquipmentManagementTestModule } from '../../../test.module';
import { StoreEquipmentUpdateComponent } from 'app/entities/store-equipment/store-equipment-update.component';
import { StoreEquipmentService } from 'app/entities/store-equipment/store-equipment.service';
import { StoreEquipment } from 'app/shared/model/store-equipment.model';

describe('Component Tests', () => {
  describe('StoreEquipment Management Update Component', () => {
    let comp: StoreEquipmentUpdateComponent;
    let fixture: ComponentFixture<StoreEquipmentUpdateComponent>;
    let service: StoreEquipmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EquipmentManagementTestModule],
        declarations: [StoreEquipmentUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(StoreEquipmentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StoreEquipmentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StoreEquipmentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StoreEquipment(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new StoreEquipment();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
