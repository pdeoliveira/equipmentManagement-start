package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.StoreEquipment;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.StoreEquipmentRepository;
import com.mycompany.myapp.service.dto.StoreEquipmentCriteria;

/**
 * Service for executing complex queries for {@link StoreEquipment} entities in the database.
 * The main input is a {@link StoreEquipmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StoreEquipment} or a {@link Page} of {@link StoreEquipment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StoreEquipmentQueryService extends QueryService<StoreEquipment> {

    private final Logger log = LoggerFactory.getLogger(StoreEquipmentQueryService.class);

    private final StoreEquipmentRepository storeEquipmentRepository;

    public StoreEquipmentQueryService(StoreEquipmentRepository storeEquipmentRepository) {
        this.storeEquipmentRepository = storeEquipmentRepository;
    }

    /**
     * Return a {@link List} of {@link StoreEquipment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StoreEquipment> findByCriteria(StoreEquipmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StoreEquipment> specification = createSpecification(criteria);
        return storeEquipmentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link StoreEquipment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StoreEquipment> findByCriteria(StoreEquipmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StoreEquipment> specification = createSpecification(criteria);
        return storeEquipmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StoreEquipmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StoreEquipment> specification = createSpecification(criteria);
        return storeEquipmentRepository.count(specification);
    }

    /**
     * Function to convert {@link StoreEquipmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StoreEquipment> createSpecification(StoreEquipmentCriteria criteria) {
        Specification<StoreEquipment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StoreEquipment_.id));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSku(), StoreEquipment_.sku));
            }
            if (criteria.getEquipmentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEquipmentName(), StoreEquipment_.equipmentName));
            }
            if (criteria.getLocationInStore() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocationInStore(), StoreEquipment_.locationInStore));
            }
            if (criteria.getStoreId() != null) {
                specification = specification.and(buildSpecification(criteria.getStoreId(),
                    root -> root.join(StoreEquipment_.store, JoinType.LEFT).get(Store_.id)));
            }
        }
        return specification;
    }
}
