package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.StoreEquipment} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.StoreEquipmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /store-equipments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StoreEquipmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter sku;

    private StringFilter equipmentName;

    private StringFilter locationInStore;

    private LongFilter storeId;

    public StoreEquipmentCriteria() {
    }

    public StoreEquipmentCriteria(StoreEquipmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.equipmentName = other.equipmentName == null ? null : other.equipmentName.copy();
        this.locationInStore = other.locationInStore == null ? null : other.locationInStore.copy();
        this.storeId = other.storeId == null ? null : other.storeId.copy();
    }

    @Override
    public StoreEquipmentCriteria copy() {
        return new StoreEquipmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSku() {
        return sku;
    }

    public void setSku(LongFilter sku) {
        this.sku = sku;
    }

    public StringFilter getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(StringFilter equipmentName) {
        this.equipmentName = equipmentName;
    }

    public StringFilter getLocationInStore() {
        return locationInStore;
    }

    public void setLocationInStore(StringFilter locationInStore) {
        this.locationInStore = locationInStore;
    }

    public LongFilter getStoreId() {
        return storeId;
    }

    public void setStoreId(LongFilter storeId) {
        this.storeId = storeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StoreEquipmentCriteria that = (StoreEquipmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(equipmentName, that.equipmentName) &&
            Objects.equals(locationInStore, that.locationInStore) &&
            Objects.equals(storeId, that.storeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sku,
        equipmentName,
        locationInStore,
        storeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreEquipmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sku != null ? "sku=" + sku + ", " : "") +
                (equipmentName != null ? "equipmentName=" + equipmentName + ", " : "") +
                (locationInStore != null ? "locationInStore=" + locationInStore + ", " : "") +
                (storeId != null ? "storeId=" + storeId + ", " : "") +
            "}";
    }

}
