package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.StoreEquipment;
import com.mycompany.myapp.service.StoreEquipmentService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.StoreEquipmentCriteria;
import com.mycompany.myapp.service.StoreEquipmentQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.StoreEquipment}.
 */
@RestController
@RequestMapping("/api")
public class StoreEquipmentResource {

    private final Logger log = LoggerFactory.getLogger(StoreEquipmentResource.class);

    private static final String ENTITY_NAME = "storeEquipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoreEquipmentService storeEquipmentService;

    private final StoreEquipmentQueryService storeEquipmentQueryService;

    public StoreEquipmentResource(StoreEquipmentService storeEquipmentService, StoreEquipmentQueryService storeEquipmentQueryService) {
        this.storeEquipmentService = storeEquipmentService;
        this.storeEquipmentQueryService = storeEquipmentQueryService;
    }

    /**
     * {@code POST  /store-equipments} : Create a new storeEquipment.
     *
     * @param storeEquipment the storeEquipment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storeEquipment, or with status {@code 400 (Bad Request)} if the storeEquipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/store-equipments")
    public ResponseEntity<StoreEquipment> createStoreEquipment(@Valid @RequestBody StoreEquipment storeEquipment) throws URISyntaxException {
        log.debug("REST request to save StoreEquipment : {}", storeEquipment);
        if (storeEquipment.getId() != null) {
            throw new BadRequestAlertException("A new storeEquipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoreEquipment result = storeEquipmentService.save(storeEquipment);
        return ResponseEntity.created(new URI("/api/store-equipments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /store-equipments} : Updates an existing storeEquipment.
     *
     * @param storeEquipment the storeEquipment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storeEquipment,
     * or with status {@code 400 (Bad Request)} if the storeEquipment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storeEquipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/store-equipments")
    public ResponseEntity<StoreEquipment> updateStoreEquipment(@Valid @RequestBody StoreEquipment storeEquipment) throws URISyntaxException {
        log.debug("REST request to update StoreEquipment : {}", storeEquipment);
        if (storeEquipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StoreEquipment result = storeEquipmentService.save(storeEquipment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storeEquipment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /store-equipments} : get all the storeEquipments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storeEquipments in body.
     */
    @GetMapping("/store-equipments")
    public ResponseEntity<List<StoreEquipment>> getAllStoreEquipments(StoreEquipmentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StoreEquipments by criteria: {}", criteria);
        Page<StoreEquipment> page = storeEquipmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /store-equipments/count} : count all the storeEquipments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/store-equipments/count")
    public ResponseEntity<Long> countStoreEquipments(StoreEquipmentCriteria criteria) {
        log.debug("REST request to count StoreEquipments by criteria: {}", criteria);
        return ResponseEntity.ok().body(storeEquipmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /store-equipments/:id} : get the "id" storeEquipment.
     *
     * @param id the id of the storeEquipment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storeEquipment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/store-equipments/{id}")
    public ResponseEntity<StoreEquipment> getStoreEquipment(@PathVariable Long id) {
        log.debug("REST request to get StoreEquipment : {}", id);
        Optional<StoreEquipment> storeEquipment = storeEquipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storeEquipment);
    }

    /**
     * {@code DELETE  /store-equipments/:id} : delete the "id" storeEquipment.
     *
     * @param id the id of the storeEquipment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/store-equipments/{id}")
    public ResponseEntity<Void> deleteStoreEquipment(@PathVariable Long id) {
        log.debug("REST request to delete StoreEquipment : {}", id);
        storeEquipmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
