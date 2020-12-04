import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EquipmentManagementTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { StoreEquipmentDeleteDialogComponent } from 'app/entities/store-equipment/store-equipment-delete-dialog.component';
import { StoreEquipmentService } from 'app/entities/store-equipment/store-equipment.service';

describe('Component Tests', () => {
  describe('StoreEquipment Management Delete Component', () => {
    let comp: StoreEquipmentDeleteDialogComponent;
    let fixture: ComponentFixture<StoreEquipmentDeleteDialogComponent>;
    let service: StoreEquipmentService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EquipmentManagementTestModule],
        declarations: [StoreEquipmentDeleteDialogComponent],
      })
        .overrideTemplate(StoreEquipmentDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StoreEquipmentDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StoreEquipmentService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
