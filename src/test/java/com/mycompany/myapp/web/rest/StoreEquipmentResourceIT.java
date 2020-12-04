package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.EquipmentManagementApp;
import com.mycompany.myapp.domain.StoreEquipment;
import com.mycompany.myapp.domain.Store;
import com.mycompany.myapp.repository.StoreEquipmentRepository;
import com.mycompany.myapp.service.StoreEquipmentService;
import com.mycompany.myapp.service.dto.StoreEquipmentCriteria;
import com.mycompany.myapp.service.StoreEquipmentQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StoreEquipmentResource} REST controller.
 */
@SpringBootTest(classes = EquipmentManagementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StoreEquipmentResourceIT {

    private static final Long DEFAULT_SKU = 0L;
    private static final Long UPDATED_SKU = 1L;
    private static final Long SMALLER_SKU = 0L - 1L;

    private static final String DEFAULT_EQUIPMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_IN_STORE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_IN_STORE = "BBBBBBBBBB";

    @Autowired
    private StoreEquipmentRepository storeEquipmentRepository;

    @Autowired
    private StoreEquipmentService storeEquipmentService;

    @Autowired
    private StoreEquipmentQueryService storeEquipmentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoreEquipmentMockMvc;

    private StoreEquipment storeEquipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoreEquipment createEntity(EntityManager em) {
        StoreEquipment storeEquipment = new StoreEquipment()
            .sku(DEFAULT_SKU)
            .equipmentName(DEFAULT_EQUIPMENT_NAME)
            .locationInStore(DEFAULT_LOCATION_IN_STORE);
        // Add required entity
        Store store;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            store = StoreResourceIT.createEntity(em);
            em.persist(store);
            em.flush();
        } else {
            store = TestUtil.findAll(em, Store.class).get(0);
        }
        storeEquipment.setStore(store);
        return storeEquipment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoreEquipment createUpdatedEntity(EntityManager em) {
        StoreEquipment storeEquipment = new StoreEquipment()
            .sku(UPDATED_SKU)
            .equipmentName(UPDATED_EQUIPMENT_NAME)
            .locationInStore(UPDATED_LOCATION_IN_STORE);
        // Add required entity
        Store store;
        if (TestUtil.findAll(em, Store.class).isEmpty()) {
            store = StoreResourceIT.createUpdatedEntity(em);
            em.persist(store);
            em.flush();
        } else {
            store = TestUtil.findAll(em, Store.class).get(0);
        }
        storeEquipment.setStore(store);
        return storeEquipment;
    }

    @BeforeEach
    public void initTest() {
        storeEquipment = createEntity(em);
    }

    @Test
    @Transactional
    public void createStoreEquipment() throws Exception {
        int databaseSizeBeforeCreate = storeEquipmentRepository.findAll().size();
        // Create the StoreEquipment
        restStoreEquipmentMockMvc.perform(post("/api/store-equipments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeEquipment)))
            .andExpect(status().isCreated());

        // Validate the StoreEquipment in the database
        List<StoreEquipment> storeEquipmentList = storeEquipmentRepository.findAll();
        assertThat(storeEquipmentList).hasSize(databaseSizeBeforeCreate + 1);
        StoreEquipment testStoreEquipment = storeEquipmentList.get(storeEquipmentList.size() - 1);
        assertThat(testStoreEquipment.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testStoreEquipment.getEquipmentName()).isEqualTo(DEFAULT_EQUIPMENT_NAME);
        assertThat(testStoreEquipment.getLocationInStore()).isEqualTo(DEFAULT_LOCATION_IN_STORE);
    }

    @Test
    @Transactional
    public void createStoreEquipmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeEquipmentRepository.findAll().size();

        // Create the StoreEquipment with an existing ID
        storeEquipment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreEquipmentMockMvc.perform(post("/api/store-equipments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeEquipment)))
            .andExpect(status().isBadRequest());

        // Validate the StoreEquipment in the database
        List<StoreEquipment> storeEquipmentList = storeEquipmentRepository.findAll();
        assertThat(storeEquipmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSkuIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeEquipmentRepository.findAll().size();
        // set the field null
        storeEquipment.setSku(null);

        // Create the StoreEquipment, which fails.


        restStoreEquipmentMockMvc.perform(post("/api/store-equipments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeEquipment)))
            .andExpect(status().isBadRequest());

        List<StoreEquipment> storeEquipmentList = storeEquipmentRepository.findAll();
        assertThat(storeEquipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipmentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeEquipmentRepository.findAll().size();
        // set the field null
        storeEquipment.setEquipmentName(null);

        // Create the StoreEquipment, which fails.


        restStoreEquipmentMockMvc.perform(post("/api/store-equipments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeEquipment)))
            .andExpect(status().isBadRequest());

        List<StoreEquipment> storeEquipmentList = storeEquipmentRepository.findAll();
        assertThat(storeEquipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStoreEquipments() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList
        restStoreEquipmentMockMvc.perform(get("/api/store-equipments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeEquipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU.intValue())))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME)))
            .andExpect(jsonPath("$.[*].locationInStore").value(hasItem(DEFAULT_LOCATION_IN_STORE)));
    }
    
    @Test
    @Transactional
    public void getStoreEquipment() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get the storeEquipment
        restStoreEquipmentMockMvc.perform(get("/api/store-equipments/{id}", storeEquipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storeEquipment.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU.intValue()))
            .andExpect(jsonPath("$.equipmentName").value(DEFAULT_EQUIPMENT_NAME))
            .andExpect(jsonPath("$.locationInStore").value(DEFAULT_LOCATION_IN_STORE));
    }


    @Test
    @Transactional
    public void getStoreEquipmentsByIdFiltering() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        Long id = storeEquipment.getId();

        defaultStoreEquipmentShouldBeFound("id.equals=" + id);
        defaultStoreEquipmentShouldNotBeFound("id.notEquals=" + id);

        defaultStoreEquipmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStoreEquipmentShouldNotBeFound("id.greaterThan=" + id);

        defaultStoreEquipmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStoreEquipmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStoreEquipmentsBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where sku equals to DEFAULT_SKU
        defaultStoreEquipmentShouldBeFound("sku.equals=" + DEFAULT_SKU);

        // Get all the storeEquipmentList where sku equals to UPDATED_SKU
        defaultStoreEquipmentShouldNotBeFound("sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsBySkuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where sku not equals to DEFAULT_SKU
        defaultStoreEquipmentShouldNotBeFound("sku.notEquals=" + DEFAULT_SKU);

        // Get all the storeEquipmentList where sku not equals to UPDATED_SKU
        defaultStoreEquipmentShouldBeFound("sku.notEquals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where sku in DEFAULT_SKU or UPDATED_SKU
        defaultStoreEquipmentShouldBeFound("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU);

        // Get all the storeEquipmentList where sku equals to UPDATED_SKU
        defaultStoreEquipmentShouldNotBeFound("sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where sku is not null
        defaultStoreEquipmentShouldBeFound("sku.specified=true");

        // Get all the storeEquipmentList where sku is null
        defaultStoreEquipmentShouldNotBeFound("sku.specified=false");
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsBySkuIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where sku is greater than or equal to DEFAULT_SKU
        defaultStoreEquipmentShouldBeFound("sku.greaterThanOrEqual=" + DEFAULT_SKU);

        // Get all the storeEquipmentList where sku is greater than or equal to (DEFAULT_SKU + 1)
        defaultStoreEquipmentShouldNotBeFound("sku.greaterThanOrEqual=" + (DEFAULT_SKU + 1));
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsBySkuIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where sku is less than or equal to DEFAULT_SKU
        defaultStoreEquipmentShouldBeFound("sku.lessThanOrEqual=" + DEFAULT_SKU);

        // Get all the storeEquipmentList where sku is less than or equal to SMALLER_SKU
        defaultStoreEquipmentShouldNotBeFound("sku.lessThanOrEqual=" + SMALLER_SKU);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsBySkuIsLessThanSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where sku is less than DEFAULT_SKU
        defaultStoreEquipmentShouldNotBeFound("sku.lessThan=" + DEFAULT_SKU);

        // Get all the storeEquipmentList where sku is less than (DEFAULT_SKU + 1)
        defaultStoreEquipmentShouldBeFound("sku.lessThan=" + (DEFAULT_SKU + 1));
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsBySkuIsGreaterThanSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where sku is greater than DEFAULT_SKU
        defaultStoreEquipmentShouldNotBeFound("sku.greaterThan=" + DEFAULT_SKU);

        // Get all the storeEquipmentList where sku is greater than SMALLER_SKU
        defaultStoreEquipmentShouldBeFound("sku.greaterThan=" + SMALLER_SKU);
    }


    @Test
    @Transactional
    public void getAllStoreEquipmentsByEquipmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where equipmentName equals to DEFAULT_EQUIPMENT_NAME
        defaultStoreEquipmentShouldBeFound("equipmentName.equals=" + DEFAULT_EQUIPMENT_NAME);

        // Get all the storeEquipmentList where equipmentName equals to UPDATED_EQUIPMENT_NAME
        defaultStoreEquipmentShouldNotBeFound("equipmentName.equals=" + UPDATED_EQUIPMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsByEquipmentNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where equipmentName not equals to DEFAULT_EQUIPMENT_NAME
        defaultStoreEquipmentShouldNotBeFound("equipmentName.notEquals=" + DEFAULT_EQUIPMENT_NAME);

        // Get all the storeEquipmentList where equipmentName not equals to UPDATED_EQUIPMENT_NAME
        defaultStoreEquipmentShouldBeFound("equipmentName.notEquals=" + UPDATED_EQUIPMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsByEquipmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where equipmentName in DEFAULT_EQUIPMENT_NAME or UPDATED_EQUIPMENT_NAME
        defaultStoreEquipmentShouldBeFound("equipmentName.in=" + DEFAULT_EQUIPMENT_NAME + "," + UPDATED_EQUIPMENT_NAME);

        // Get all the storeEquipmentList where equipmentName equals to UPDATED_EQUIPMENT_NAME
        defaultStoreEquipmentShouldNotBeFound("equipmentName.in=" + UPDATED_EQUIPMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsByEquipmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where equipmentName is not null
        defaultStoreEquipmentShouldBeFound("equipmentName.specified=true");

        // Get all the storeEquipmentList where equipmentName is null
        defaultStoreEquipmentShouldNotBeFound("equipmentName.specified=false");
    }
                @Test
    @Transactional
    public void getAllStoreEquipmentsByEquipmentNameContainsSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where equipmentName contains DEFAULT_EQUIPMENT_NAME
        defaultStoreEquipmentShouldBeFound("equipmentName.contains=" + DEFAULT_EQUIPMENT_NAME);

        // Get all the storeEquipmentList where equipmentName contains UPDATED_EQUIPMENT_NAME
        defaultStoreEquipmentShouldNotBeFound("equipmentName.contains=" + UPDATED_EQUIPMENT_NAME);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsByEquipmentNameNotContainsSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where equipmentName does not contain DEFAULT_EQUIPMENT_NAME
        defaultStoreEquipmentShouldNotBeFound("equipmentName.doesNotContain=" + DEFAULT_EQUIPMENT_NAME);

        // Get all the storeEquipmentList where equipmentName does not contain UPDATED_EQUIPMENT_NAME
        defaultStoreEquipmentShouldBeFound("equipmentName.doesNotContain=" + UPDATED_EQUIPMENT_NAME);
    }


    @Test
    @Transactional
    public void getAllStoreEquipmentsByLocationInStoreIsEqualToSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where locationInStore equals to DEFAULT_LOCATION_IN_STORE
        defaultStoreEquipmentShouldBeFound("locationInStore.equals=" + DEFAULT_LOCATION_IN_STORE);

        // Get all the storeEquipmentList where locationInStore equals to UPDATED_LOCATION_IN_STORE
        defaultStoreEquipmentShouldNotBeFound("locationInStore.equals=" + UPDATED_LOCATION_IN_STORE);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsByLocationInStoreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where locationInStore not equals to DEFAULT_LOCATION_IN_STORE
        defaultStoreEquipmentShouldNotBeFound("locationInStore.notEquals=" + DEFAULT_LOCATION_IN_STORE);

        // Get all the storeEquipmentList where locationInStore not equals to UPDATED_LOCATION_IN_STORE
        defaultStoreEquipmentShouldBeFound("locationInStore.notEquals=" + UPDATED_LOCATION_IN_STORE);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsByLocationInStoreIsInShouldWork() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where locationInStore in DEFAULT_LOCATION_IN_STORE or UPDATED_LOCATION_IN_STORE
        defaultStoreEquipmentShouldBeFound("locationInStore.in=" + DEFAULT_LOCATION_IN_STORE + "," + UPDATED_LOCATION_IN_STORE);

        // Get all the storeEquipmentList where locationInStore equals to UPDATED_LOCATION_IN_STORE
        defaultStoreEquipmentShouldNotBeFound("locationInStore.in=" + UPDATED_LOCATION_IN_STORE);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsByLocationInStoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where locationInStore is not null
        defaultStoreEquipmentShouldBeFound("locationInStore.specified=true");

        // Get all the storeEquipmentList where locationInStore is null
        defaultStoreEquipmentShouldNotBeFound("locationInStore.specified=false");
    }
                @Test
    @Transactional
    public void getAllStoreEquipmentsByLocationInStoreContainsSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where locationInStore contains DEFAULT_LOCATION_IN_STORE
        defaultStoreEquipmentShouldBeFound("locationInStore.contains=" + DEFAULT_LOCATION_IN_STORE);

        // Get all the storeEquipmentList where locationInStore contains UPDATED_LOCATION_IN_STORE
        defaultStoreEquipmentShouldNotBeFound("locationInStore.contains=" + UPDATED_LOCATION_IN_STORE);
    }

    @Test
    @Transactional
    public void getAllStoreEquipmentsByLocationInStoreNotContainsSomething() throws Exception {
        // Initialize the database
        storeEquipmentRepository.saveAndFlush(storeEquipment);

        // Get all the storeEquipmentList where locationInStore does not contain DEFAULT_LOCATION_IN_STORE
        defaultStoreEquipmentShouldNotBeFound("locationInStore.doesNotContain=" + DEFAULT_LOCATION_IN_STORE);

        // Get all the storeEquipmentList where locationInStore does not contain UPDATED_LOCATION_IN_STORE
        defaultStoreEquipmentShouldBeFound("locationInStore.doesNotContain=" + UPDATED_LOCATION_IN_STORE);
    }


    @Test
    @Transactional
    public void getAllStoreEquipmentsByStoreIsEqualToSomething() throws Exception {
        // Get already existing entity
        Store store = storeEquipment.getStore();
        storeEquipmentRepository.saveAndFlush(storeEquipment);
        Long storeId = store.getId();

        // Get all the storeEquipmentList where store equals to storeId
        defaultStoreEquipmentShouldBeFound("storeId.equals=" + storeId);

        // Get all the storeEquipmentList where store equals to storeId + 1
        defaultStoreEquipmentShouldNotBeFound("storeId.equals=" + (storeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStoreEquipmentShouldBeFound(String filter) throws Exception {
        restStoreEquipmentMockMvc.perform(get("/api/store-equipments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeEquipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU.intValue())))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME)))
            .andExpect(jsonPath("$.[*].locationInStore").value(hasItem(DEFAULT_LOCATION_IN_STORE)));

        // Check, that the count call also returns 1
        restStoreEquipmentMockMvc.perform(get("/api/store-equipments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStoreEquipmentShouldNotBeFound(String filter) throws Exception {
        restStoreEquipmentMockMvc.perform(get("/api/store-equipments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStoreEquipmentMockMvc.perform(get("/api/store-equipments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingStoreEquipment() throws Exception {
        // Get the storeEquipment
        restStoreEquipmentMockMvc.perform(get("/api/store-equipments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreEquipment() throws Exception {
        // Initialize the database
        storeEquipmentService.save(storeEquipment);

        int databaseSizeBeforeUpdate = storeEquipmentRepository.findAll().size();

        // Update the storeEquipment
        StoreEquipment updatedStoreEquipment = storeEquipmentRepository.findById(storeEquipment.getId()).get();
        // Disconnect from session so that the updates on updatedStoreEquipment are not directly saved in db
        em.detach(updatedStoreEquipment);
        updatedStoreEquipment
            .sku(UPDATED_SKU)
            .equipmentName(UPDATED_EQUIPMENT_NAME)
            .locationInStore(UPDATED_LOCATION_IN_STORE);

        restStoreEquipmentMockMvc.perform(put("/api/store-equipments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStoreEquipment)))
            .andExpect(status().isOk());

        // Validate the StoreEquipment in the database
        List<StoreEquipment> storeEquipmentList = storeEquipmentRepository.findAll();
        assertThat(storeEquipmentList).hasSize(databaseSizeBeforeUpdate);
        StoreEquipment testStoreEquipment = storeEquipmentList.get(storeEquipmentList.size() - 1);
        assertThat(testStoreEquipment.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testStoreEquipment.getEquipmentName()).isEqualTo(UPDATED_EQUIPMENT_NAME);
        assertThat(testStoreEquipment.getLocationInStore()).isEqualTo(UPDATED_LOCATION_IN_STORE);
    }

    @Test
    @Transactional
    public void updateNonExistingStoreEquipment() throws Exception {
        int databaseSizeBeforeUpdate = storeEquipmentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreEquipmentMockMvc.perform(put("/api/store-equipments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeEquipment)))
            .andExpect(status().isBadRequest());

        // Validate the StoreEquipment in the database
        List<StoreEquipment> storeEquipmentList = storeEquipmentRepository.findAll();
        assertThat(storeEquipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStoreEquipment() throws Exception {
        // Initialize the database
        storeEquipmentService.save(storeEquipment);

        int databaseSizeBeforeDelete = storeEquipmentRepository.findAll().size();

        // Delete the storeEquipment
        restStoreEquipmentMockMvc.perform(delete("/api/store-equipments/{id}", storeEquipment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StoreEquipment> storeEquipmentList = storeEquipmentRepository.findAll();
        assertThat(storeEquipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
