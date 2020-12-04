import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStore } from 'app/shared/model/store.model';

type EntityResponseType = HttpResponse<IStore>;
type EntityArrayResponseType = HttpResponse<IStore[]>;

@Injectable({ providedIn: 'root' })
export class StoreService {
  public resourceUrl = SERVER_API_URL + 'api/stores';

  constructor(protected http: HttpClient) {}

  create(store: IStore): Observable<EntityResponseType> {
    return this.http.post<IStore>(this.resourceUrl, store, { observe: 'response' });
  }

  update(store: IStore): Observable<EntityResponseType> {
    return this.http.put<IStore>(this.resourceUrl, store, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStore>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStore[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
