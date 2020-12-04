import { IStore } from 'app/shared/model/store.model';

export interface IStoreEquipment {
  id?: number;
  sku?: number;
  equipmentName?: string;
  locationInStore?: string;
  store?: IStore;
}

export class StoreEquipment implements IStoreEquipment {
  constructor(
    public id?: number,
    public sku?: number,
    public equipmentName?: string,
    public locationInStore?: string,
    public store?: IStore
  ) {}
}
