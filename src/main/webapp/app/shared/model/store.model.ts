export interface IStore {
  id?: number;
  storeName?: string;
  streetAddress?: string;
  postalCode?: string;
  city?: string;
  stateProvince?: string;
}

export class Store implements IStore {
  constructor(
    public id?: number,
    public storeName?: string,
    public streetAddress?: string,
    public postalCode?: string,
    public city?: string,
    public stateProvince?: string
  ) {}
}
