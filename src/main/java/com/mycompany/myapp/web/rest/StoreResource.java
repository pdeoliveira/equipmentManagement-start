package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Store;
import com.mycompany.myapp.service.StoreService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.StoreCriteria;
import com.mycompany.myapp.service.StoreQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Store}.
 */
@RestController
@RequestMapping("/api")
public class StoreResource {

    private final Logger log = LoggerFactory.getLogger(StoreResource.class);

    private static final String ENTITY_NAME = "store";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StoreService storeService;

    private final StoreQueryService storeQueryService;

    public StoreResource(StoreService storeService, StoreQueryService storeQueryService) {
        this.storeService = storeService;
        this.storeQueryService = storeQueryService;
    }

    /**
     * {@code POST  /stores} : Create a new store.
     *
     * @param store the store to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new store, or with status {@code 400 (Bad Request)} if the store has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stores")
    public ResponseEntity<Store> createStore(@Valid @RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to save Store : {}", store);
        if (store.getId() != null) {
            throw new BadRequestAlertException("A new store cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Store result = storeService.save(store);
        return ResponseEntity.created(new URI("/api/stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stores} : Updates an existing store.
     *
     * @param store the store to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated store,
     * or with status {@code 400 (Bad Request)} if the store is not valid,
     * or with status {@code 500 (Internal Server Error)} if the store couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stores")
    public ResponseEntity<Store> updateStore(@Valid @RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to update Store : {}", store);
        if (store.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Store result = storeService.save(store);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, store.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /stores} : get all the stores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stores in body.
     */
    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getAllStores(StoreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Stores by criteria: {}", criteria);
        Page<Store> page = storeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stores/count} : count all the stores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/stores/count")
    public ResponseEntity<Long> countStores(StoreCriteria criteria) {
        log.debug("REST request to count Stores by criteria: {}", criteria);
        return ResponseEntity.ok().body(storeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /stores/:id} : get the "id" store.
     *
     * @param id the id of the store to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the store, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stores/{id}")
    public ResponseEntity<Store> getStore(@PathVariable Long id) {
        log.debug("REST request to get Store : {}", id);
        Optional<Store> store = storeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(store);
    }

    /**
     * {@code DELETE  /stores/:id} : delete the "id" store.
     *
     * @param id the id of the store to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stores/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        log.debug("REST request to delete Store : {}", id);
        storeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
