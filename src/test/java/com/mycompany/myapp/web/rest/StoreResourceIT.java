package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.EquipmentManagementApp;
import com.mycompany.myapp.domain.Store;
import com.mycompany.myapp.repository.StoreRepository;
import com.mycompany.myapp.service.StoreService;
import com.mycompany.myapp.service.dto.StoreCriteria;
import com.mycompany.myapp.service.StoreQueryService;

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
 * Integration tests for the {@link StoreResource} REST controller.
 */
@SpringBootTest(classes = EquipmentManagementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StoreResourceIT {

    private static final String DEFAULT_STORE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STORE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "52";
    private static final String UPDATED_POSTAL_CODE = "327306";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreQueryService storeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoreMockMvc;

    private Store store;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Store createEntity(EntityManager em) {
        Store store = new Store()
            .storeName(DEFAULT_STORE_NAME)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .stateProvince(DEFAULT_STATE_PROVINCE);
        return store;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Store createUpdatedEntity(EntityManager em) {
        Store store = new Store()
            .storeName(UPDATED_STORE_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE);
        return store;
    }

    @BeforeEach
    public void initTest() {
        store = createEntity(em);
    }

    @Test
    @Transactional
    public void createStore() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();
        // Create the Store
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(store)))
            .andExpect(status().isCreated());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate + 1);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getStoreName()).isEqualTo(DEFAULT_STORE_NAME);
        assertThat(testStore.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testStore.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testStore.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testStore.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void createStoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();

        // Create the Store with an existing ID
        store.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(store)))
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStoreNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setStoreName(null);

        // Create the Store, which fails.


        restStoreMockMvc.perform(post("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(store)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStores() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList
        restStoreMockMvc.perform(get("/api/stores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().intValue())))
            .andExpect(jsonPath("$.[*].storeName").value(hasItem(DEFAULT_STORE_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)));
    }
    
    @Test
    @Transactional
    public void getStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", store.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(store.getId().intValue()))
            .andExpect(jsonPath("$.storeName").value(DEFAULT_STORE_NAME))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE));
    }


    @Test
    @Transactional
    public void getStoresByIdFiltering() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        Long id = store.getId();

        defaultStoreShouldBeFound("id.equals=" + id);
        defaultStoreShouldNotBeFound("id.notEquals=" + id);

        defaultStoreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStoreShouldNotBeFound("id.greaterThan=" + id);

        defaultStoreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStoreShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStoresByStoreNameIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where storeName equals to DEFAULT_STORE_NAME
        defaultStoreShouldBeFound("storeName.equals=" + DEFAULT_STORE_NAME);

        // Get all the storeList where storeName equals to UPDATED_STORE_NAME
        defaultStoreShouldNotBeFound("storeName.equals=" + UPDATED_STORE_NAME);
    }

    @Test
    @Transactional
    public void getAllStoresByStoreNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where storeName not equals to DEFAULT_STORE_NAME
        defaultStoreShouldNotBeFound("storeName.notEquals=" + DEFAULT_STORE_NAME);

        // Get all the storeList where storeName not equals to UPDATED_STORE_NAME
        defaultStoreShouldBeFound("storeName.notEquals=" + UPDATED_STORE_NAME);
    }

    @Test
    @Transactional
    public void getAllStoresByStoreNameIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where storeName in DEFAULT_STORE_NAME or UPDATED_STORE_NAME
        defaultStoreShouldBeFound("storeName.in=" + DEFAULT_STORE_NAME + "," + UPDATED_STORE_NAME);

        // Get all the storeList where storeName equals to UPDATED_STORE_NAME
        defaultStoreShouldNotBeFound("storeName.in=" + UPDATED_STORE_NAME);
    }

    @Test
    @Transactional
    public void getAllStoresByStoreNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where storeName is not null
        defaultStoreShouldBeFound("storeName.specified=true");

        // Get all the storeList where storeName is null
        defaultStoreShouldNotBeFound("storeName.specified=false");
    }
                @Test
    @Transactional
    public void getAllStoresByStoreNameContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where storeName contains DEFAULT_STORE_NAME
        defaultStoreShouldBeFound("storeName.contains=" + DEFAULT_STORE_NAME);

        // Get all the storeList where storeName contains UPDATED_STORE_NAME
        defaultStoreShouldNotBeFound("storeName.contains=" + UPDATED_STORE_NAME);
    }

    @Test
    @Transactional
    public void getAllStoresByStoreNameNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where storeName does not contain DEFAULT_STORE_NAME
        defaultStoreShouldNotBeFound("storeName.doesNotContain=" + DEFAULT_STORE_NAME);

        // Get all the storeList where storeName does not contain UPDATED_STORE_NAME
        defaultStoreShouldBeFound("storeName.doesNotContain=" + UPDATED_STORE_NAME);
    }


    @Test
    @Transactional
    public void getAllStoresByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultStoreShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the storeList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultStoreShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllStoresByStreetAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where streetAddress not equals to DEFAULT_STREET_ADDRESS
        defaultStoreShouldNotBeFound("streetAddress.notEquals=" + DEFAULT_STREET_ADDRESS);

        // Get all the storeList where streetAddress not equals to UPDATED_STREET_ADDRESS
        defaultStoreShouldBeFound("streetAddress.notEquals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllStoresByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultStoreShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the storeList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultStoreShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllStoresByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where streetAddress is not null
        defaultStoreShouldBeFound("streetAddress.specified=true");

        // Get all the storeList where streetAddress is null
        defaultStoreShouldNotBeFound("streetAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllStoresByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where streetAddress contains DEFAULT_STREET_ADDRESS
        defaultStoreShouldBeFound("streetAddress.contains=" + DEFAULT_STREET_ADDRESS);

        // Get all the storeList where streetAddress contains UPDATED_STREET_ADDRESS
        defaultStoreShouldNotBeFound("streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllStoresByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where streetAddress does not contain DEFAULT_STREET_ADDRESS
        defaultStoreShouldNotBeFound("streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS);

        // Get all the storeList where streetAddress does not contain UPDATED_STREET_ADDRESS
        defaultStoreShouldBeFound("streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllStoresByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultStoreShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the storeList where postalCode equals to UPDATED_POSTAL_CODE
        defaultStoreShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllStoresByPostalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where postalCode not equals to DEFAULT_POSTAL_CODE
        defaultStoreShouldNotBeFound("postalCode.notEquals=" + DEFAULT_POSTAL_CODE);

        // Get all the storeList where postalCode not equals to UPDATED_POSTAL_CODE
        defaultStoreShouldBeFound("postalCode.notEquals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllStoresByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultStoreShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the storeList where postalCode equals to UPDATED_POSTAL_CODE
        defaultStoreShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllStoresByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where postalCode is not null
        defaultStoreShouldBeFound("postalCode.specified=true");

        // Get all the storeList where postalCode is null
        defaultStoreShouldNotBeFound("postalCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllStoresByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where postalCode contains DEFAULT_POSTAL_CODE
        defaultStoreShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the storeList where postalCode contains UPDATED_POSTAL_CODE
        defaultStoreShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllStoresByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultStoreShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the storeList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultStoreShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }


    @Test
    @Transactional
    public void getAllStoresByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where city equals to DEFAULT_CITY
        defaultStoreShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the storeList where city equals to UPDATED_CITY
        defaultStoreShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllStoresByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where city not equals to DEFAULT_CITY
        defaultStoreShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the storeList where city not equals to UPDATED_CITY
        defaultStoreShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllStoresByCityIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where city in DEFAULT_CITY or UPDATED_CITY
        defaultStoreShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the storeList where city equals to UPDATED_CITY
        defaultStoreShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllStoresByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where city is not null
        defaultStoreShouldBeFound("city.specified=true");

        // Get all the storeList where city is null
        defaultStoreShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllStoresByCityContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where city contains DEFAULT_CITY
        defaultStoreShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the storeList where city contains UPDATED_CITY
        defaultStoreShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllStoresByCityNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where city does not contain DEFAULT_CITY
        defaultStoreShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the storeList where city does not contain UPDATED_CITY
        defaultStoreShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllStoresByStateProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where stateProvince equals to DEFAULT_STATE_PROVINCE
        defaultStoreShouldBeFound("stateProvince.equals=" + DEFAULT_STATE_PROVINCE);

        // Get all the storeList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultStoreShouldNotBeFound("stateProvince.equals=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void getAllStoresByStateProvinceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where stateProvince not equals to DEFAULT_STATE_PROVINCE
        defaultStoreShouldNotBeFound("stateProvince.notEquals=" + DEFAULT_STATE_PROVINCE);

        // Get all the storeList where stateProvince not equals to UPDATED_STATE_PROVINCE
        defaultStoreShouldBeFound("stateProvince.notEquals=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void getAllStoresByStateProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where stateProvince in DEFAULT_STATE_PROVINCE or UPDATED_STATE_PROVINCE
        defaultStoreShouldBeFound("stateProvince.in=" + DEFAULT_STATE_PROVINCE + "," + UPDATED_STATE_PROVINCE);

        // Get all the storeList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultStoreShouldNotBeFound("stateProvince.in=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void getAllStoresByStateProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where stateProvince is not null
        defaultStoreShouldBeFound("stateProvince.specified=true");

        // Get all the storeList where stateProvince is null
        defaultStoreShouldNotBeFound("stateProvince.specified=false");
    }
                @Test
    @Transactional
    public void getAllStoresByStateProvinceContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where stateProvince contains DEFAULT_STATE_PROVINCE
        defaultStoreShouldBeFound("stateProvince.contains=" + DEFAULT_STATE_PROVINCE);

        // Get all the storeList where stateProvince contains UPDATED_STATE_PROVINCE
        defaultStoreShouldNotBeFound("stateProvince.contains=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void getAllStoresByStateProvinceNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where stateProvince does not contain DEFAULT_STATE_PROVINCE
        defaultStoreShouldNotBeFound("stateProvince.doesNotContain=" + DEFAULT_STATE_PROVINCE);

        // Get all the storeList where stateProvince does not contain UPDATED_STATE_PROVINCE
        defaultStoreShouldBeFound("stateProvince.doesNotContain=" + UPDATED_STATE_PROVINCE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStoreShouldBeFound(String filter) throws Exception {
        restStoreMockMvc.perform(get("/api/stores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().intValue())))
            .andExpect(jsonPath("$.[*].storeName").value(hasItem(DEFAULT_STORE_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)));

        // Check, that the count call also returns 1
        restStoreMockMvc.perform(get("/api/stores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStoreShouldNotBeFound(String filter) throws Exception {
        restStoreMockMvc.perform(get("/api/stores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStoreMockMvc.perform(get("/api/stores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingStore() throws Exception {
        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStore() throws Exception {
        // Initialize the database
        storeService.save(store);

        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Update the store
        Store updatedStore = storeRepository.findById(store.getId()).get();
        // Disconnect from session so that the updates on updatedStore are not directly saved in db
        em.detach(updatedStore);
        updatedStore
            .storeName(UPDATED_STORE_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE);

        restStoreMockMvc.perform(put("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStore)))
            .andExpect(status().isOk());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getStoreName()).isEqualTo(UPDATED_STORE_NAME);
        assertThat(testStore.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testStore.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testStore.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStore.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void updateNonExistingStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreMockMvc.perform(put("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(store)))
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStore() throws Exception {
        // Initialize the database
        storeService.save(store);

        int databaseSizeBeforeDelete = storeRepository.findAll().size();

        // Delete the store
        restStoreMockMvc.perform(delete("/api/stores/{id}", store.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
