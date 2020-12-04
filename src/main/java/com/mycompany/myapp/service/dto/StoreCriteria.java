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
 * Criteria class for the {@link com.mycompany.myapp.domain.Store} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.StoreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StoreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter storeName;

    private StringFilter streetAddress;

    private StringFilter postalCode;

    private StringFilter city;

    private StringFilter stateProvince;

    public StoreCriteria() {
    }

    public StoreCriteria(StoreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.storeName = other.storeName == null ? null : other.storeName.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.stateProvince = other.stateProvince == null ? null : other.stateProvince.copy();
    }

    @Override
    public StoreCriteria copy() {
        return new StoreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStoreName() {
        return storeName;
    }

    public void setStoreName(StringFilter storeName) {
        this.storeName = storeName;
    }

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(StringFilter stateProvince) {
        this.stateProvince = stateProvince;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StoreCriteria that = (StoreCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(storeName, that.storeName) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(city, that.city) &&
            Objects.equals(stateProvince, that.stateProvince);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        storeName,
        streetAddress,
        postalCode,
        city,
        stateProvince
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (storeName != null ? "storeName=" + storeName + ", " : "") +
                (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
                (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (stateProvince != null ? "stateProvince=" + stateProvince + ", " : "") +
            "}";
    }

}
