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

import com.mycompany.myapp.domain.Store;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.StoreRepository;
import com.mycompany.myapp.service.dto.StoreCriteria;

/**
 * Service for executing complex queries for {@link Store} entities in the database.
 * The main input is a {@link StoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Store} or a {@link Page} of {@link Store} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StoreQueryService extends QueryService<Store> {

    private final Logger log = LoggerFactory.getLogger(StoreQueryService.class);

    private final StoreRepository storeRepository;

    public StoreQueryService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * Return a {@link List} of {@link Store} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Store> findByCriteria(StoreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Store> specification = createSpecification(criteria);
        return storeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Store} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Store> findByCriteria(StoreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Store> specification = createSpecification(criteria);
        return storeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StoreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Store> specification = createSpecification(criteria);
        return storeRepository.count(specification);
    }

    /**
     * Function to convert {@link StoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Store> createSpecification(StoreCriteria criteria) {
        Specification<Store> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Store_.id));
            }
            if (criteria.getStoreName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStoreName(), Store_.storeName));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), Store_.streetAddress));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), Store_.postalCode));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Store_.city));
            }
            if (criteria.getStateProvince() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateProvince(), Store_.stateProvince));
            }
        }
        return specification;
    }
}
