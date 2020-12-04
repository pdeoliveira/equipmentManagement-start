import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStoreEquipment, StoreEquipment } from 'app/shared/model/store-equipment.model';
import { StoreEquipmentService } from './store-equipment.service';
import { StoreEquipmentComponent } from './store-equipment.component';
import { StoreEquipmentDetailComponent } from './store-equipment-detail.component';
import { StoreEquipmentUpdateComponent } from './store-equipment-update.component';

@Injectable({ providedIn: 'root' })
export class StoreEquipmentResolve implements Resolve<IStoreEquipment> {
  constructor(private service: StoreEquipmentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStoreEquipment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((storeEquipment: HttpResponse<StoreEquipment>) => {
          if (storeEquipment.body) {
            return of(storeEquipment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StoreEquipment());
  }
}

export const storeEquipmentRoute: Routes = [
  {
    path: '',
    component: StoreEquipmentComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'equipmentManagementApp.storeEquipment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StoreEquipmentDetailComponent,
    resolve: {
      storeEquipment: StoreEquipmentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'equipmentManagementApp.storeEquipment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StoreEquipmentUpdateComponent,
    resolve: {
      storeEquipment: StoreEquipmentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'equipmentManagementApp.storeEquipment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StoreEquipmentUpdateComponent,
    resolve: {
      storeEquipment: StoreEquipmentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'equipmentManagementApp.storeEquipment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
