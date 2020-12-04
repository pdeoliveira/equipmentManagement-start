package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A StoreEquipment.
 */
@Entity
@Table(name = "store_equipment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StoreEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0L)
    @Max(value = 9999999L)
    @Column(name = "sku", nullable = false, unique = true)
    private Long sku;

    @NotNull
    @Column(name = "equipment_name", nullable = false)
    private String equipmentName;

    @Column(name = "location_in_store")
    private String locationInStore;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "storeEquipments", allowSetters = true)
    private Store store;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSku() {
        return sku;
    }

    public StoreEquipment sku(Long sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(Long sku) {
        this.sku = sku;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public StoreEquipment equipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
        return this;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getLocationInStore() {
        return locationInStore;
    }

    public StoreEquipment locationInStore(String locationInStore) {
        this.locationInStore = locationInStore;
        return this;
    }

    public void setLocationInStore(String locationInStore) {
        this.locationInStore = locationInStore;
    }

    public Store getStore() {
        return store;
    }

    public StoreEquipment store(Store store) {
        this.store = store;
        return this;
    }

    public void setStore(Store store) {
        this.store = store;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoreEquipment)) {
            return false;
        }
        return id != null && id.equals(((StoreEquipment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreEquipment{" +
            "id=" + getId() +
            ", sku=" + getSku() +
            ", equipmentName='" + getEquipmentName() + "'" +
            ", locationInStore='" + getLocationInStore() + "'" +
            "}";
    }
}
