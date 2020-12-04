import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStoreEquipment } from 'app/shared/model/store-equipment.model';

type EntityResponseType = HttpResponse<IStoreEquipment>;
type EntityArrayResponseType = HttpResponse<IStoreEquipment[]>;

@Injectable({ providedIn: 'root' })
export class StoreEquipmentService {
  public resourceUrl = SERVER_API_URL + 'api/store-equipments';

  constructor(protected http: HttpClient) {}

  create(storeEquipment: IStoreEquipment): Observable<EntityResponseType> {
    return this.http.post<IStoreEquipment>(this.resourceUrl, storeEquipment, { observe: 'response' });
  }

  update(storeEquipment: IStoreEquipment): Observable<EntityResponseType> {
    return this.http.put<IStoreEquipment>(this.resourceUrl, storeEquipment, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStoreEquipment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStoreEquipment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
