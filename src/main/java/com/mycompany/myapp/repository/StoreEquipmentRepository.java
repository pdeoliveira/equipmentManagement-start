package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StoreEquipment;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the StoreEquipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreEquipmentRepository extends JpaRepository<StoreEquipment, Long>, JpaSpecificationExecutor<StoreEquipment> {
}
