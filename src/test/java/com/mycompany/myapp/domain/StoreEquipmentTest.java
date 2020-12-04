package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class StoreEquipmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreEquipment.class);
        StoreEquipment storeEquipment1 = new StoreEquipment();
        storeEquipment1.setId(1L);
        StoreEquipment storeEquipment2 = new StoreEquipment();
        storeEquipment2.setId(storeEquipment1.getId());
        assertThat(storeEquipment1).isEqualTo(storeEquipment2);
        storeEquipment2.setId(2L);
        assertThat(storeEquipment1).isNotEqualTo(storeEquipment2);
        storeEquipment1.setId(null);
        assertThat(storeEquipment1).isNotEqualTo(storeEquipment2);
    }
}
