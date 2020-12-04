import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EquipmentManagementTestModule } from '../../../test.module';
import { StoreEquipmentDetailComponent } from 'app/entities/store-equipment/store-equipment-detail.component';
import { StoreEquipment } from 'app/shared/model/store-equipment.model';

describe('Component Tests', () => {
  describe('StoreEquipment Management Detail Component', () => {
    let comp: StoreEquipmentDetailComponent;
    let fixture: ComponentFixture<StoreEquipmentDetailComponent>;
    const route = ({ data: of({ storeEquipment: new StoreEquipment(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EquipmentManagementTestModule],
        declarations: [StoreEquipmentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(StoreEquipmentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StoreEquipmentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load storeEquipment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.storeEquipment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
