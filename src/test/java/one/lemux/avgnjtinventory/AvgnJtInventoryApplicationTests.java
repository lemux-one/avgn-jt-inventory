package one.lemux.avgnjtinventory;

import one.lemux.avgnjtinventory.repositories.ContainerRepository;
import one.lemux.avgnjtinventory.repositories.ManagerRepository;
import one.lemux.avgnjtinventory.repositories.ProductRepository;
import one.lemux.avgnjtinventory.repositories.RoleRepository;
import one.lemux.avgnjtinventory.repositories.SectionRepository;
import one.lemux.avgnjtinventory.repositories.StoreRepository;
import one.lemux.avgnjtinventory.repositories.TypeRepository;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AvgnJtInventoryApplicationTests {

    @Autowired private ContainerRepository containers;
    @Autowired private ManagerRepository managers;
    @Autowired private ProductRepository products;
    @Autowired private RoleRepository roles;
    @Autowired private SectionRepository sections;
    @Autowired private StoreRepository stores;
    @Autowired private TypeRepository types;
    
    @Autowired private MockMvc mvc;
    
    @Test
    public void contextLoads() {
        assertThat(containers).isNotNull();
        assertThat(managers).isNotNull();
        assertThat(products).isNotNull();
        assertThat(roles).isNotNull();
        assertThat(sections).isNotNull();
        assertThat(stores).isNotNull();
        assertThat(types).isNotNull();
    }

    @Test
    public void healthStatusIsUp() throws Exception {
        mvc.perform(get(("/manage/health")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.spring-boot.actuator.v3+json"))
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
